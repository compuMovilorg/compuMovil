
import { Review } from "../models/Review.js";
import { User } from "../models/Users.js";
import { Articulos } from "../models/Articulo.js";

/**
 * GET /reviews
 */
export const getReviews = async (_req, res) => {
  try {
    const reviews = await Review.findAll({
      include: [
        { model: User, as: "user", attributes: ["id", "username", "email"] },
        { model: Articulos, as: "articulo", attributes: ["id", "titulo"] },
      ],
      order: [["createdAt", "DESC"]], // requiere timestamps: true en Review
    });
    return res.json(reviews);
  } catch (error) {
    console.error("getReviews error:", error);
    return res.status(500).json({ message: "Error al obtener reseñas" });
  }
};

/**
 * POST /reviews
 * Body mínimo requerido: userId, articuloId, placeName, reviewText
 */
export const createReview = async (req, res) => {
  try {
    const { userId, articuloId, placeName, reviewText, parentReviewId } = req.body;

    if (!userId || !articuloId || !placeName || !reviewText) {
      return res
        .status(400)
        .json({ message: "userId, articuloId, placeName y reviewText son requeridos" });
    }

    const [user, articulo] = await Promise.all([
      User.findByPk(userId),
      Articulos.findByPk(articuloId),
    ]);
    if (!user) return res.status(400).json({ message: "userId no existe" });
    if (!articulo) return res.status(400).json({ message: "articuloId no existe" });

    if (parentReviewId != null) {
      const parent = await Review.findByPk(parentReviewId);
      if (!parent) return res.status(404).json({ message: "Reseña padre no encontrada" });
    }

    const newReview = await Review.create({
      userId,
      articuloId,
      placeName: String(placeName).trim(),
      reviewText,
      likes: req.body.likes ?? 0,
      comments: req.body.comments ?? 0,
      parentReviewId: parentReviewId ?? null,
    });

    return res.status(201).json(newReview);
  } catch (error) {
    console.error("createReview error:", error);
    return res.status(500).json({ message: "Error al crear reseña", detail: error.message });
  }
};

/**
 * PUT /reviews/:id
 */
export const updateReview = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    const review = await Review.findByPk(id);
    if (!review) return res.status(404).json({ message: "Reseña no encontrada" });

    // si envían parentReviewId, valida que exista
    if (req.body.parentReviewId != null) {
      const parent = await Review.findByPk(req.body.parentReviewId);
      if (!parent) return res.status(404).json({ message: "Reseña padre no encontrada" });
    }

    await review.update(req.body);
    return res.json(review);
  } catch (error) {
    console.error("updateReview error:", error);
    return res.status(500).json({ message: "Error al actualizar reseña" });
  }
};

/**
 * DELETE /reviews/:id
 */
export const deleteReview = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    const review = await Review.findByPk(id);
    if (!review) return res.status(404).json({ message: "Reseña no encontrada" });

    await review.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("deleteReview error:", error);
    return res.status(500).json({ message: "Error al eliminar reseña" });
  }
};

/**
 * GET /reviews/:id
 */
export const getReviewById = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    const review = await Review.findByPk(id, {
      include: [
        { model: User, as: "user", attributes: ["id", "username", "email"] },
        { model: Articulos, as: "articulo", attributes: ["id", "titulo"] },
      ],
    });
    if (!review) return res.status(404).json({ message: "Reseña no encontrada" });
    return res.json(review);
  } catch (error) {
    console.error("getReviewById error:", error);
    return res.status(500).json({ message: "Error al obtener reseña" });
  }
};

/**
 * GET /reviews/:id/replies
 */
export const getRepliesByReviewId = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    const replies = await Review.findAll({
      where: { parentReviewId: id },
      order: [["createdAt", "DESC"]], // requiere timestamps
    });
    return res.json(replies);
  } catch (error) {
    console.error("getRepliesByReviewId error:", error);
    return res.status(500).json({ message: "Error al obtener respuestas" });
  }
};

/**
 * GET /reviews/articulo/:articuloId
 */
export const getReviewsByArticulo = async (req, res) => {
  try {
    const articuloId = parseInt(req.params.articuloId, 10);
    const reviews = await Review.findAll({
      where: { articuloId },
      include: [{ model: User, as: "user", attributes: ["id", "username", "email"] }],
      order: [["createdAt", "DESC"]],
    });
    return res.json(reviews);
  } catch (error) {
    console.error("getReviewsByArticulo error:", error);
    return res.status(500).json({ message: "Error al obtener reseñas" });
  }
};

/**
 * GET /reviews/user/:userId
 */
export const getReviewsByUser = async (req, res) => {
  try {
    const userId = parseInt(req.params.userId, 10);
    const reviews = await Review.findAll({
      where: { userId },
      include: [{ model: Articulos, as: "articulo", attributes: ["id", "titulo", "descripcion"] }],
      order: [["createdAt", "DESC"]],
    });
    return res.json(reviews);
  } catch (error) {
    console.error("getReviewsByUser error:", error);
    return res.status(500).json({ message: "Error al obtener reseñas" });
  }
};
