import e from "express";
import { Review } from "../models/Review.js";
import { User } from "../models/Users.js";

// Obtener todas las reseñas
export const getReviews = async (req, res) => {
    try {
        const reviews = await Review.findAll({
            include : {
                model: User,
                as: 'user',
                attributes: ['id', 'username', 'email'],
             },
        });
        return res.json(reviews);
    } catch (error) {
        console.error("Error al obtener reseñas:", error);
        return res.status(500).json({ message: "Error al obtener reseñas" });
    }
};

// Crear una nueva reseña
export const createReview = async (req, res) => {
    try {
        
        const { parentReviewId } = req.body;

        if (parentReviewId != null && parentReviewId !== undefined) {
            const parentReview = await Review.findByPk(parentReviewId);
            if (!parentReview) {
                return res.status(404).json({ message: "Reseña padre no encontrada" });
            }
        }
        const newReview = await Review.create(req.body);
        return res.status(201).json(newReview);
    } catch (error) {
        console.error("Error al crear reseña:", error);
        return res.status(500).json({ message: "Error al crear reseña" });
    }
};

// Actualizar reseña
export const updateReview = async (req, res) => {
    try {
        const id = req.params.id;
        const review = await Review.findByPk(id);

        if (!review) {
            return res.status(404).json({ message: "Reseña no encontrada" });
        }

        await review.update(req.body);
        return res.json(review);
    } catch (error) {
        console.error("Error al actualizar reseña:", error);
        return res.status(500).json({ message: "Error al actualizar reseña" });
    }
};

// Eliminar reseña
export const deleteReview = async (req, res) => {
    try {
        const id = req.params.id;
        const review = await Review.findByPk(id);

        if (!review) {
            return res.status(404).json({ message: "Reseña no encontrada" });
        }

        await review.destroy();
        return res.sendStatus(204);
    } catch (error) {
        console.error("Error al eliminar reseña:", error);
        return res.status(500).json({ message: "Error al eliminar reseña" });
    }
};

export const getReviewById = async (req, res) => {
    try {
        const id = req.params.id;
        const review = await Review.findByPk(id);
        if (!review) {
            return res.status(404).json({ message: "Reseña no encontrada" });
        }
        return res.json(review);
    } catch (error) {
        console.error("Error al obtener reseña:", error);
        return res.status(500).json({ message: "Error al obtener reseña" });
    }
};

export const getRepliesByReviewId = async (req, res) => {
    const { id } = req.params

    try {
        const replies = await Review.findAll({
            where: { parentReviewId: id }, 
            order: [['createdAt', 'DESC']]
        });

        return res.json(replies);
    } catch (error) {
        console.error("Error al obtener respuestas:", error);
        return res.status(500).json({ message: "Error al obtener respuestas" });
    }
};
