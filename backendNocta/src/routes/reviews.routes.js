import { Router } from "express";
import { 
  getReviews,
  createReview,
  updateReview,
  deleteReview,
  getReviewById,
  getRepliesByReviewId,
  getReviewsByArticulo,
  getReviewsByUser
} from "../controller/reviews.controller.js";

const router = Router();

// Obtener todas las reviews
router.get("/reviews", getReviews);

// Crear una review normal
router.post("/reviews", createReview);

// Obtener todas las reviews de un artículo
router.get("/reviews/articulo/:articuloId", getReviewsByArticulo);

// Obtener todas las reviews de un usuario
router.get("/reviews/user/:userId", getReviewsByUser);

// Obtener una review por id
router.get("/reviews/:id", getReviewById);

// Obtener las respuestas de una review
router.get("/reviews/:id/replies", getRepliesByReviewId);

// Actualizar una review
router.put("/reviews/:id", updateReview);

// Eliminar una review
router.delete("/reviews/:id", deleteReview);

export default router;
