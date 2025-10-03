import { Event } from "../models/Events.js";

// Obtener todos los eventos
export const getEvents = async (req, res) => {
  try {
    const events = await Event.findAll();
    return res.json(events);
  } catch (error) {
    console.error("Error al obtener eventos:", error);
    return res.status(500).json({ message: "Error al obtener eventos" });
  }
};

// Crear un nuevo evento
export const createEvent = async (req, res) => {
  try {
    const newEvent = await Event.create(req.body);
    return res.status(201).json(newEvent);
  } catch (error) {
    console.error("Error al crear evento:", error);
    return res.status(500).json({ message: "Error al crear evento" });
  }
};

// Obtener un evento por ID
export const getEventById = async (req, res) => {
  try {
    const id = req.params.id;
    const event = await Event.findByPk(id);

    if (!event) {
      return res.status(404).json({ message: "Evento no encontrado" });
    }

    return res.json(event);
  } catch (error) {
    console.error("Error al obtener evento:", error);
    return res.status(500).json({ message: "Error al obtener evento" });
  }
};

// Actualizar un evento
export const updateEvent = async (req, res) => {
  try {
    const id = req.params.id;
    const event = await Event.findByPk(id);

    if (!event) {
      return res.status(404).json({ message: "Evento no encontrado" });
    }

    await event.update(req.body);
    return res.json(event);
  } catch (error) {
    console.error("Error al actualizar evento:", error);
    return res.status(500).json({ message: "Error al actualizar evento" });
  }
};

// Eliminar un evento
export const deleteEvent = async (req, res) => {
  try {
    const id = req.params.id;
    const event = await Event.findByPk(id);

    if (!event) {
      return res.status(404).json({ message: "Evento no encontrado" });
    }

    await event.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("Error al eliminar evento:", error);
    return res.status(500).json({ message: "Error al eliminar evento" });
  }
};
