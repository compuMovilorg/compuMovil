import { Review } from "../models/Review.js";

const initialReviews = [
  {
    userId: 1,
    gastroBarId: 1,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Santa Juana Gastrobar",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg1.png?alt=media&token=4a350408-b0cc-456b-91a6-e2a7c0713bcc",
    reviewText: "Excelente fusion entre arte y gastronomia. La cocteleria artesanal es imperdible.",
    likes: 25,
    comments: 6,
    parentReviewId: null,
  },
  {
    userId: 2,
    gastroBarId: 2,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Mono bandido",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg2.jpg?alt=media&token=fabb803a-176d-4eac-bdca-0873e53dd58c",
    reviewText: "Un ambiente moderno y acogedor, los cocteles exclusivos son lo mejor.",
    likes: 18,
    comments: 4,
    parentReviewId: null,
  },
  {
    userId: 3,
    gastroBarId: 3,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Matilde",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg3.jpg?alt=media&token=76b61f04-1fbb-490f-aaf5-6ebe4336ca8c",
    reviewText: "Platos inspirados en la cordillera de los Andes, sabores unicos y frescos.",
    likes: 22,
    comments: 5,
    parentReviewId: null,
  },
  {
    userId: 4,
    gastroBarId: 4,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Cabrera",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg4.jpg?alt=media&token=30449e71-b0c2-4eb3-8d4d-340bb5adfdc0",
    reviewText: "La mezcla de arte y gastronomia crea un ambiente unico, muy recomendado.",
    likes: 15,
    comments: 3,
    parentReviewId: null,
  },
  {
    userId: 5,
    gastroBarId: 5,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Astorias",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg5.jpg?alt=media&token=f6f1d4d8-a25c-4a77-8008-5ca9f2c11a68",
    reviewText: "Una propuesta tropical deliciosa, los mariscos frescos son espectaculares.",
    likes: 28,
    comments: 7,
    parentReviewId: null,
  },
  {
    userId: 1,
    gastroBarId: 6,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Egua",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg6.jpg?alt=media&token=7d3b50b1-7361-495c-8bab-e9020490b6c8",
    reviewText: "Un espacio mediterraneo increible, los vinos blancos son un exito.",
    likes: 30,
    comments: 8,
    parentReviewId: null,
  },
  {
    userId: 2,
    gastroBarId: 7,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Cantina",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg7.jpg?alt=media&token=4dbb2e0d-7a98-494e-8cbc-70ffd299efaa",
    reviewText: "Ambiente intimo y buena musica en vivo, los cocteles son lo maximo.",
    likes: 20,
    comments: 4,
    parentReviewId: null,
  },
  {
    userId: 3,
    gastroBarId: 8,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Radio Estrella",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg8.jpg?alt=media&token=08ff7e08-8bbd-4737-a1a6-b872e7ac1fc2",
    reviewText: "Una mezcla perfecta entre tradicion y modernidad, platos unicos.",
    likes: 26,
    comments: 6,
    parentReviewId: null,
  },
  {
    userId: 4,
    gastroBarId: 9,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Oceano",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg9.jpg?alt=media&token=a0afcbf3-3016-44f4-9d3a-4a3410916844",
    reviewText: "Ambiente rustico con musica folclorica en vivo, autentico y acogedor.",
    likes: 24,
    comments: 5,
    parentReviewId: null,
  },
  {
    userId: 5,
    gastroBarId: 10,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
    placeName: "Santorini",
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg10.jpg?alt=media&token=623af3de-5fc7-4fac-b3a6-8a385290171d",
    reviewText: "Un lugar elegante y moderno, la musica de DJ le da un toque unico.",
    likes: 19,
    comments: 3,
    parentReviewId: null,
  }
];

export async function loadInitialReviews() {
  try {
    const count = await Review.count();
    if (count === 0) {
      await Review.bulkCreate(initialReviews);
      console.log("Reviews iniciales de gastrobares cargadas con placeImage, userImg y gastroBarId.");
    } else {
      console.log("Las reviews ya existen en la base de datos.");
    }
  } catch (error) {
    console.error("Error al cargar reviews:", error);
  }
}
