import express from "express";
import userRoutes from "./routes/users.routes.js";
import reviewRoutes from "./routes/reviews.routes.js";
import gastroBarRoutes from "./routes/gastrobar.routes.js";
import eventsRoutes from "./routes/events.routes.js";
import articuloRoutes from "./routes/articulo.routes.js";

const app = express();
app.use(express.json());


app.use("/users", userRoutes);
app.use("/reviews", reviewRoutes);
app.use("/gastrobars", gastroBarRoutes);
app.use("/events", eventsRoutes);
app.use("/articulos", articuloRoutes);

export default app;
