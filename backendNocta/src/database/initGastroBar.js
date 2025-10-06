import { GastroBar } from "../models/gastroBar.js";

const initialGastroBars = [
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg1.png?alt=media&token=4a350408-b0cc-456b-91a6-e2a7c0713bcc",
    name: "Santa Juana Gastrobar",
    rating: 4.5,
    reviewCount: 30,
    address: "Calle 71 #11-51",
    hours: "Lunes a Sábado de 8:00 am a 11:00 pm\nDomingos y festivos de 7:30 am a 11:00 pm",
    cuisine: "Fusión Colombiana",
    description: "Santa Juana es un gastrobar en Quinta Camacho que fusiona arte y gastronomía. Ofrece cocina latina, coctelería artesanal y un ambiente lleno de diseño ancestral."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg2.jpg?alt=media&token=fabb803a-176d-4eac-bdca-0873e53dd58c",
    name: "Mono bandido",
    rating: 4.2,
    reviewCount: 18,
    address: "Carrera 12 #85-20",
    hours: "Martes a Domingo de 5:00 pm a 1:00 am",
    cuisine: "Internacional",
    description: "Un gastrobar que combina sabores del mundo con vinos y cocteles exclusivos en un ambiente moderno y acogedor."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg3.jpg?alt=media&token=76b61f04-1fbb-490f-aaf5-6ebe4336ca8c",
    name: "Matilde",
    rating: 4.7,
    reviewCount: 42,
    address: "Av. Chile #9-60",
    hours: "Todos los días de 12:00 m a 11:00 pm",
    cuisine: "Latinoamericana",
    description: "Platos inspirados en la riqueza de la cordillera de los Andes, con un enfoque en ingredientes locales frescos."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg4.jpg?alt=media&token=30449e71-b0c2-4eb3-8d4d-340bb5adfdc0",
    name: "Cabrera",
    rating: 4.3,
    reviewCount: 25,
    address: "Calle 59 #8-32",
    hours: "Miércoles a Domingo de 4:00 pm a 12:00 am",
    cuisine: "Contemporánea",
    description: "Un espacio donde el arte y la gastronomía conviven, ofreciendo cenas temáticas y cocteles artesanales."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg5.jpg?alt=media&token=f6f1d4d8-a25c-4a77-8008-5ca9f2c11a68",
    name: "Astorias",
    rating: 4.6,
    reviewCount: 35,
    address: "Carrera 7 #70-25",
    hours: "Lunes a Domingo de 11:00 am a 10:00 pm",
    cuisine: "Caribeña",
    description: "Experiencia gastronómica tropical con frutas exóticas, mariscos frescos y tragos inspirados en el Caribe."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg6.jpg?alt=media&token=7d3b50b1-7361-495c-8bab-e9020490b6c8",
    name: "Egua",
    rating: 4.8,
    reviewCount: 40,
    address: "Carrera 14 #93-12",
    hours: "Martes a Domingo de 12:00 m a 11:00 pm",
    cuisine: "Mediterránea",
    description: "Un rincón inspirado en la brisa marina, con platos frescos de mariscos y una selección especial de vinos blancos."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg7.jpg?alt=media&token=4dbb2e0d-7a98-494e-8cbc-70ffd299efaa",
    name: "Cantina",
    rating: 4.4,
    reviewCount: 27,
    address: "Calle 80 #15-45",
    hours: "Jueves a Sábado de 6:00 pm a 2:00 am",
    cuisine: "Coctelería de Autor",
    description: "Un espacio íntimo con luces tenues y música en vivo, especializado en cócteles creativos y tapas internacionales."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg8.jpg?alt=media&token=08ff7e08-8bbd-4737-a1a6-b872e7ac1fc2",
    name: "Radio Estrella",
    rating: 4.6,
    reviewCount: 33,
    address: "Carrera 10 #65-22",
    hours: "Lunes a Domingo de 1:00 pm a 10:00 pm",
    cuisine: "Cocina de Autor",
    description: "Propuesta culinaria que mezcla tradición y modernidad, con ingredientes locales reinventados en platos únicos."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg9.jpg?alt=media&token=a0afcbf3-3016-44f4-9d3a-4a3410916844",
    name: "Oceano",
    rating: 4.5,
    reviewCount: 29,
    address: "Calle 64 #9-14",
    hours: "Miércoles a Domingo de 12:00 m a 11:30 pm",
    cuisine: "Andina",
    description: "Un ambiente rústico que resalta lo mejor de la cultura andina con música folclórica en vivo y platos tradicionales."
  },
  {
    imagePlace: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/GastroBares%2Fgastrobarimg10.jpg?alt=media&token=623af3de-5fc7-4fac-b3a6-8a385290171d",
    name: "Santorini",
    rating: 4.7,
    reviewCount: 38,
    address: "Carrera 11 #84-09",
    hours: "Viernes y Sábado de 7:00 pm a 3:00 am",
    cuisine: "Internacional Moderna",
    description: "Un gastrobar elegante con DJs en vivo y cocina internacional de autor, ideal para noches largas en la ciudad."
  }
];

export async function loadInitialGastroBars() {
  try {
    const count = await GastroBar.count();
    if (count === 0) {
      await GastroBar.bulkCreate(initialGastroBars);
      console.log("GastroBars iniciales cargados correctamente.");
    } else {
      console.log("Los GastroBars ya existen en la base de datos.");
    }
  } catch (error) {
    console.error("Error cargando GastroBars iniciales:", error);
  }
}
