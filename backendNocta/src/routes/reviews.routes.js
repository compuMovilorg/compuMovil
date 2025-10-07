import { Router } from "express";
import { 
  getReviews,
  createReview,
  updateReview,
  deleteReview,
  getReviewById,
  getRepliesByReviewId,
  getReviewsByArticulo,
  getReviewsByUser,
  getReviewsByGastroBar
} from "../controller/reviews.controller.js";

const router = Router();

// Obtener todas las reviews
router.get("/", getReviews);

// Crear una review normal
router.post("/", createReview);

// Obtener todas las reviews de un artículo
router.get("/articulo/:articuloId", getReviewsByArticulo);

// Obtener todas las reviews de un usuario
router.get("/user/:userId", getReviewsByUser);

// Obtener las respuestas de una review
router.get("/:id/replies", getRepliesByReviewId);

// Obtener todas las reviews de un gastrobar
router.get("/gastrobar/:gastroBarId", getReviewsByGastroBar);

// Obtener una review por id
router.get("/:id", getReviewById);

// Actualizar una review
router.put("/:id", updateReview);

// Eliminar una review
router.delete("/:id", deleteReview);




export default router;
