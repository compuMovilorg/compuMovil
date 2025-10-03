import { Event } from "../models/Events.js";

const initialEvents = [
  {
    date: "2025-10-15",
    time: "20:00",
    title: "Noche de Salsa en La Calle",
    eventImage: "images/event_salsa.jpg",
  },
  {
    date: "2025-10-20",
    time: "18:00",
    title: "Cata de Cervezas Artesanales - El Refugio",
    eventImage: "images/event_cerveza.jpg",
  },
  {
    date: "2025-10-25",
    time: "22:00",
    title: "DJ Internacional en Karma Club",
    eventImage: "images/event_dj.jpg",
  },
  {
    date: "2025-11-01",
    time: "19:00",
    title: "Noche Bohemia - Música en Vivo",
    eventImage: "images/event_bohemia.jpg",
  },
  {
    date: "2025-11-05",
    time: "21:00",
    title: "Fiesta Electrónica en Nocturna",
    eventImage: "images/event_electronica.jpg",
  },
  {
    date: "2025-11-10",
    time: "17:00",
    title: "Tardes Mediterráneas en El Jardín Secreto",
    eventImage: "images/event_mediterranea.jpg",
  },
];

export async function loadInitialEvents() {
  try {
    const count = await Event.count();
    if (count === 0) {
      await Event.bulkCreate(initialEvents);
      console.log("Eventos iniciales cargados correctamente.");
    } else {
      console.log("Los eventos ya existen en la base de datos.");
    }
  } catch (error) {
    console.error("Error cargando eventos iniciales:", error);
  }
}
