import { Review } from "../models/Review.js";

const initialReviews = [
  {
    userId: 1,
    placeName: "Gastrobar La Terraza",
    reviewText: "Excelente ambiente al aire libre y cócteles espectaculares.",
    likes: 25,
    comments: 6,
    parentReviewId: null,
  },
  {
    userId: 2,
    placeName: "Discoteca Eclipse",
    reviewText: "La música estuvo genial, aunque la pista estaba muy llena.",
    likes: 18,
    comments: 4,
    parentReviewId: null,
  },
  {
    userId: 3,
    placeName: "Gastrobar El Rincón Gourmet",
    reviewText: "Tapas deliciosas y buena atención. Ideal para cenar con amigos.",
    likes: 22,
    comments: 5,
    parentReviewId: null,
  },
  {
    userId: 4,
    placeName: "Discoteca Prisma",
    reviewText: "El DJ estuvo increíble, aunque la fila de entrada fue larga.",
    likes: 15,
    comments: 3,
    parentReviewId: null,
  },
  {
    userId: 5,
    placeName: "Gastrobar Sabor Urbano",
    reviewText: "Platos innovadores y un ambiente muy acogedor.",
    likes: 28,
    comments: 7,
    parentReviewId: null,
  },
  {
    userId: 1,
    placeName: "Discoteca Vértigo",
    reviewText: "Una de las mejores experiencias de fiesta que he tenido.",
    likes: 30,
    comments: 8,
    parentReviewId: null,
  },
  {
    userId: 2,
    placeName: "Gastrobar El Buen Paladar",
    reviewText: "Comida deliciosa y precios justos. Recomendado.",
    likes: 20,
    comments: 4,
    parentReviewId: null,
  },
  {
    userId: 3,
    placeName: "Discoteca Nocturna",
    reviewText: "Ambiente vibrante, buena música electrónica. Ideal para bailar.",
    likes: 26,
    comments: 6,
    parentReviewId: null,
  },
  {
    userId: 4,
    placeName: "Gastrobar La Esquina Sabrosa",
    reviewText: "Las hamburguesas y cocteles son una maravilla.",
    likes: 24,
    comments: 5,
    parentReviewId: null,
  },
  {
    userId: 5,
    placeName: "Discoteca Diamante",
    reviewText: "Muy buen show de luces, aunque las bebidas algo costosas.",
    likes: 19,
    comments: 3,
    parentReviewId: null,
  },
  {
    userId: 1,
    placeName: "Gastrobar Fusión Latina",
    reviewText: "Mezcla de sabores espectacular. El ceviche fue lo mejor.",
    likes: 27,
    comments: 7,
    parentReviewId: null,
  },
  {
    userId: 2,
    placeName: "Discoteca Lux",
    reviewText: "Un sitio elegante y moderno. La pista de baile es enorme.",
    likes: 21,
    comments: 4,
    parentReviewId: null,
  },
  {
    userId: 3,
    placeName: "Gastrobar El Encanto",
    reviewText: "Comida artesanal y tragos bien preparados. Me encantó.",
    likes: 23,
    comments: 5,
    parentReviewId: null,
  },
];

export async function loadInitialReviews() {
  try {
    const count = await Review.count();
    if (count === 0) {
      await Review.bulkCreate(initialReviews);
      console.log("Reviews iniciales de gastrobares y discotecas cargadas.");
    } else {
      console.log("Las reviews ya existen en la base de datos.");
    }
  } catch (error) {
    console.error("Error al cargar reviews:", error);
  }
}
