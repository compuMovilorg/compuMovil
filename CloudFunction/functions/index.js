const admin = require('firebase-admin');
const logger = require('firebase-functions/logger');
const {
  onDocumentUpdated,
  onDocumentCreated,
} = require('firebase-functions/v2/firestore');

admin.initializeApp();

const db = admin.firestore();

// ✅ 1. ACTUALIZAR INFORMACION DE PERFIL EN REVIEWS
exports.updateUserInfoInReviews = onDocumentUpdated('users/{userId}', async (event) => {
  const userId = event.params.userId;
  const beforeData = event.data.before.data();
  const afterData = event.data.after.data();

  if (!beforeData || !afterData) {
    logger.warn('Datos del documento faltantes.');
    return;
  }

  if (beforeData.profileImage === afterData.profileImage) {
    logger.log('No hay cambios en la imagen de perfil. Terminando función.');
    return;
  }

  logger.log(`Imagen de perfil cambiada para ${userId}. Actualizando reseñas...`);

  const reviewSnapshot = await db.collection('reviews').where('userId', '==', userId).get();

  const batch = db.batch();
  reviewSnapshot.forEach((reviewDoc) => {
    batch.update(reviewDoc.ref, {
      'user.profileImage': afterData.profileImage,
    });
  });

  await batch.commit();
  logger.log('Reseñas actualizadas correctamente para el usuario:', userId);
});

// ✅ 2. NOTIFICACION CUANDO ALGUIEN DA LIKE
exports.sendLikeNotification = onDocumentCreated('reviews/{reviewId}/likes/{likeId}', async (event) => {
  logger.log('Función sendLikeNotification iniciada.');
  try {
    const reviewId = event.params.reviewId;
    const likeData = event.data?.data();

    if (!likeData) {
      logger.log('No se encontro data del like. Terminando...');
      return;
    }

    logger.log(`Nuevo like en la review ${reviewId}`, likeData);

    // Obtiene el review
    const reviewSnap = await db.collection('reviews').doc(reviewId).get();
    if (!reviewSnap.exists) {
      logger.log(`Review ${reviewId} no encontrado.`);
      return;
    }

    const review = reviewSnap.data();
    const authorId = review.userId;

    if (!authorId) {
      logger.log(`Review ${reviewId} no tiene userId.`);
      return;
    }

    // Obtiene el token FCM
    const userSnap = await db.collection('users').doc(authorId).get();
    if (!userSnap.exists) {
      logger.log(`Usuario ${authorId} no encontrado.`);
      return;
    }

    const userData = userSnap.data();
    const token = userData?.FCMToken;

    if (!token) {
      logger.log(`El usuario ${authorId} no tiene FCMToken.`);
      return;
    }

    // Envía la notificación
    const payload = {
      notification: {
        title: 'Nuevo Me gusta ❤️',
        body: 'A alguien le gustó tu reseña.',
      },
      token,
    };

    await admin.messaging().send(payload);
    logger.log(`Notificación enviada correctamente a ${authorId}`);
  } catch (error) {
    logger.error('Error en sendLikeNotification:', error);
  }
});
