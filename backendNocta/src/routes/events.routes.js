import { Router } from "express";
import {
  getEvents,
  createEvent,
  updateEvent,
  deleteEvent,
  getEventById
} from "../controller/events.controller.js";

const router = Router();

// Obtener todos los eventos
router.get("/", getEvents);

// Crear un nuevo evento
router.post("/", createEvent);

// Obtener un evento por id
router.get("/:id", getEventById);

// Actualizar un evento existente
router.put("/:id", updateEvent);

// Eliminar un evento
router.delete("/:id", deleteEvent);

export default router;
