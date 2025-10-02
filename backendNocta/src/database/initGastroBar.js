import { GastroBar } from "../models/gastroBar.js";

const initialGastroBars = [
  {
    imagePlace: "images/la_calle.jpg",
    name: "La Calle GastroBar",
    rating: 4.5,
    reviewCount: 120,
    address: "Cra. 15 #93-60, Bogotá",
    hours: "Lun-Dom 12:00 - 02:00",
    cuisine: "Colombiana fusión",
    description: "Un espacio moderno con ambiente urbano y cocteles innovadores.",
  },
  {
    imagePlace: "images/bohemia.jpg",
    name: "Bohemia Discoteca & Bar",
    rating: 4.2,
    reviewCount: 85,
    address: "Cl. 85 #12-30, Bogotá",
    hours: "Jue-Sáb 20:00 - 04:00",
    cuisine: "Internacional",
    description: "Discoteca con música crossover y ambiente festivo para grupos grandes.",
  },
  {
    imagePlace: "images/el_refugio.jpg",
    name: "El Refugio GastroPub",
    rating: 4.7,
    reviewCount: 200,
    address: "Cl. 70 #10-25, Bogotá",
    hours: "Mar-Dom 15:00 - 01:00",
    cuisine: "Pub & Cervezas artesanales",
    description: "Especialistas en cervezas artesanales y hamburguesas gourmet.",
  },
  {
    imagePlace: "images/nocturna.jpg",
    name: "Nocturna Club",
    rating: 4.0,
    reviewCount: 150,
    address: "Av. Pepe Sierra #22-45, Bogotá",
    hours: "Vie-Sáb 21:00 - 05:00",
    cuisine: "Snacks & Cócteles",
    description: "Discoteca con DJ invitados y experiencias temáticas cada fin de semana.",
  },
  {
    imagePlace: "images/el_jardin.jpg",
    name: "El Jardín Secreto",
    rating: 4.8,
    reviewCount: 95,
    address: "Cl. 59 #7-15, Bogotá",
    hours: "Lun-Dom 12:00 - 23:00",
    cuisine: "Mediterránea",
    description: "Un gastro bar acogedor rodeado de plantas y un menú inspirado en sabores frescos.",
  },
  {
    imagePlace: "images/karma.jpg",
    name: "Karma Discoteca",
    rating: 4.3,
    reviewCount: 180,
    address: "Zona T, Bogotá",
    hours: "Jue-Sáb 21:00 - 04:00",
    cuisine: "Internacional",
    description: "Un lugar vibrante con pista de baile, luces y la mejor música electrónica.",
  },
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
