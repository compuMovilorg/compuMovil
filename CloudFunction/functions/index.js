
const admin = require('firebase-admin');
const logger = require('firebase-functions/logger');
const {
    onDocumentUpdated,
    onDocumentCreated,
} = require('firebase-functions/firestore');
const { user } = require('firebase-functions/v1/auth');
admin.initializeApp();

const db = admin.firestore();

exports.updateUserInfoInReviews = onDocumentUpdated(
    'users/{userId}',
    async (event) => {
        const userId = event.params.userId;

        const beforeData = event.data.before.data();
        const afterData = event.data.after.data();
    
        if(beforeData.profileImage === afterData.profileImage ) {
            logger.log('No change in profile image. Exiting function.');
            return
        }
        logger.log('Profile image changed. Updating reviews...');
        
        const reviewSnapshot = await db
            .collection('reviews')
            .where('userId', '==', userId)
            .get();
    
        const batch = db.batch();
    
        reviewSnapshot.forEach((reviewDoc) => {
    
            batch.update(reviewDoc.ref, { 
                "user.profileImage": afterData.profileImage,
            });
        });
        await batch.commit();
        logger.log('Reviews updated successfully.',userId);
    }
);

exports.sendLikeNotification = onDocumentCreated(
  "reviews/{reviewId}/likes/{likeId}",
  async (event) => {
    const reviewId = event.params.reviewId;
    const likeData = event.data.data();

    if (!likeData) {
      logger.log("No like data found, skipping...");
      return;
    }

    logger.log(`Nuevo like en review ${reviewId}:`, likeData);

    // Busca el review para obtener el autor
    const reviewSnap = await admin.firestore()
      .collection("reviews")
      .doc(reviewId)
      .get();

    if (!reviewSnap.exists) {
      logger.log(`Review ${reviewId} no encontrado.`);
      return;
    }

    const review = reviewSnap.data();
    const authorId = review.userId;

    // Obtiene el token FCM del autor del review
    const userSnap = await admin.firestore()
      .collection("users")
      .doc(authorId)
      .get();

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

    // Envía notificación push
    const payload = {
      notification: {
        title: "Nuevo Me gusta ❤️",
        body: `A alguien le gusto tu reseña.`,
      },
      token: token,
    };

    try {
      await admin.messaging().send(payload);
      logger.log(`Notificacion enviada a ${authorId}`);
    } catch (error) {
      logger.error("Error enviando notificacion:", error);
    }
  }
);