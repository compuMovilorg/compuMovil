import { Articulos } from "../models/Articulo.js";

const initialArticulos = [
  {
    gastroBarId: 1,
    titulo: "La Calle GastroBar",
    imagen: "images/la_calle_banner.jpg",
    descripcion: "Un espacio moderno con ambiente urbano y cocteles innovadores.",
    rating: 4.5,
  },
  {
    gastroBarId: 2,
    titulo: "Bohemia Discoteca & Bar",
    imagen: "images/bohemia_banner.jpg",
    descripcion: "Discoteca con música crossover y ambiente festivo para grupos grandes.",
    rating: 4.2,
  },
  {
    gastroBarId: 3,
    titulo: "El Refugio GastroPub",
    imagen: "images/el_refugio_banner.jpg",
    descripcion: "Especialistas en cervezas artesanales y hamburguesas gourmet.",
    rating: 4.7,
  },
  {
    gastroBarId: 4,
    titulo: "Nocturna Club",
    imagen: "images/nocturna_banner.jpg",
    descripcion: "Discoteca con DJ invitados y experiencias temáticas cada fin de semana.",
    rating: 4.0,
  },
  {
    gastroBarId: 5,
    titulo: "El Jardín Secreto",
    imagen: "images/el_jardin_banner.jpg",
    descripcion: "Un gastro bar acogedor rodeado de plantas y sabores frescos.",
    rating: 4.8,
  },
  {
    gastroBarId: 6,
    titulo: "Karma Discoteca",
    imagen: "images/karma_banner.jpg",
    descripcion: "Un lugar vibrante con pista de baile, luces y la mejor música electrónica.",
    rating: 4.3,
  },
];

export async function loadInitialArticulos() {
  try {
    const count = await Articulos.count();
    if (count === 0) {
      await Articulos.bulkCreate(initialArticulos);
      console.log("Artículos iniciales cargados correctamente.");
    } else {
      console.log("Los Artículos ya existen en la base de datos.");
    }
  } catch (error) {
    console.error("Error cargando Artículos iniciales:", error);
  }
}
