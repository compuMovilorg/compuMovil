// functions/index.js
const admin = require('firebase-admin');
const logger = require('firebase-functions/logger');
const { onDocumentUpdated, onDocumentCreated } = require('firebase-functions/v2/firestore');
const { defineSecret } = require('firebase-functions/params');
const nodemailer = require('nodemailer');

admin.initializeApp();
const db = admin.firestore();

/* === Secrets para SMTP (v2) ===
   Ejecuta:
   firebase functions:secrets:set SMTP_USER
   firebase functions:secrets:set SMTP_PASS
   (Opcionales si no usas Gmail)
   firebase functions:secrets:set SMTP_HOST
   firebase functions:secrets:set SMTP_PORT
   firebase functions:secrets:set SMTP_SECURE
*/
const SMTP_USER = defineSecret('SMTP_USER');
const SMTP_PASS = defineSecret('SMTP_PASS');
const SMTP_HOST = defineSecret('SMTP_HOST');
const SMTP_PORT = defineSecret('SMTP_PORT');
const SMTP_SECURE = defineSecret('SMTP_SECURE');

// ---------- 1) Mantienes tus otras funciones igual ----------
exports.updateUserInfoInReviews = onDocumentUpdated('users/{userId}', async (event) => {
  const userId = event.params.userId;
  const beforeData = event.data.before.data();
  const afterData = event.data.after.data();
  if (!beforeData || !afterData) return;
  if (beforeData.profileImage === afterData.profileImage) return;

  const snap = await db.collection('reviews').where('userId', '==', userId).get();
  const batch = db.batch();
  snap.forEach((doc) => batch.update(doc.ref, { 'user.profileImage': afterData.profileImage }));
  await batch.commit();
  logger.log('Reseñas actualizadas para', userId);
});

exports.sendLikeNotification = onDocumentCreated('reviews/{reviewId}/likes/{likeId}', async (event) => {
  try {
    const reviewId = event.params.reviewId;
    const likeData = event.data?.data();
    if (!likeData) return;

    const reviewSnap = await db.collection('reviews').doc(reviewId).get();
    if (!reviewSnap.exists) return;
    const review = reviewSnap.data();
    const authorId = review.userId;
    if (!authorId) return;

    const userSnap = await db.collection('users').doc(authorId).get();
    if (!userSnap.exists) return;
    const token = userSnap.data()?.FCMToken;
    if (!token) return;

    await admin.messaging().send({
      notification: { title: 'Nuevo Me gusta ❤️', body: 'A alguien le gustó tu reseña.' },
      token,
    });
  } catch (e) {
    logger.error('sendLikeNotification error', e);
  }
});

// ---------- 2) Email de bienvenida con Secrets (v2) ----------
exports.sendWelcomeEmailOnUserCreated = onDocumentCreated(
  { document: 'users/{userId}', secrets: [SMTP_USER, SMTP_PASS, SMTP_HOST, SMTP_PORT, SMTP_SECURE] },
  async (event) => {
    try {
      const userId = event.params.userId;
      const user = event.data?.data();
      if (!user) return;

      const to = user.email;
      const name = user.name || user.username || '';
      if (!to) {
        logger.warn(`users/${userId} sin email`);
        return;
      }

      // Lee secrets en runtime
      const userS = SMTP_USER.value();
      const passS = SMTP_PASS.value();
      const hostS = SMTP_HOST.value();       // puede ser ''
      const portS = SMTP_PORT.value();       // puede ser ''
      const secureS = SMTP_SECURE.value();   // puede ser ''

      if (!userS || !passS) {
        logger.warn('SMTP_USER/SMTP_PASS no definidos; se omite envío.');
        return;
      }

      // Crea transporter en cada ejecución (simple y seguro)
      let transporter;
      if (hostS) {
        // SMTP genérico (cPanel/SES/Office365…)
        const portNum = portS ? Number(portS) : 465;
        const secureBool = secureS ? /^(true|1)$/i.test(secureS) : true;
        transporter = nodemailer.createTransport({
          host: hostS,
          port: portNum,
          secure: secureBool, // true para 465; false para 587 (STARTTLS)
          auth: { user: userS, pass: passS },
        });
      } else {
        // Gmail “service” (App Password)
        transporter = nodemailer.createTransport({
          service: 'gmail',
          auth: { user: userS, pass: passS },
        });
      }

      await transporter.sendMail({
        from: `"Nocta" <${userS}>`,
        to,
        subject: '¡Bienvenido a Nocta! 🎉',
        text: `Hola ${name || ''}, gracias por registrarte en Nocta.`,
        html: `
          <div style="font-family:system-ui,Segoe UI,Roboto,Arial">
            <h2>¡Bienvenido a <i>Nocta</i>! 🎉</h2>
            <p>Hola <b>${name || 'Noctáfilo'}</b>, ¡gracias por registrarte!</p>
            <p>Desde hoy podrás descubrir gastrobares, dejar reseñas y mucho más.</p>
            <hr/>
            <p style="font-size:12px;color:#666">Si no fuiste tú, ignora este correo.</p>
          </div>
        `,
      });

      logger.info(`Bienvenida enviada a ${to} (uid=${userId})`);
    } catch (err) {
      logger.error('sendWelcomeEmailOnUserCreated: error enviando email', err);
    }
  }
);
