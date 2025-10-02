import express from 'express';
import userRoutes from './routes/users.routes.js';
import reviewRoutes from './routes/reviews.routes.js';
import gastroBatrRoutes from './routes/gastrobar.routes.js';
import eventsRoutes from './routes/events.routes.js';
import articuloRoutes from './routes/articulo.routes.js';

const app = express();
app.use(express.json());

app.use(userRoutes);
app.use(reviewRoutes);
app.use(gastroBatrRoutes);
app.use(eventsRoutes);
app.use(articuloRoutes);

export default app;