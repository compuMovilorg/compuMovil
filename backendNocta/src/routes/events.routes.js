import { Router } from "express";
import {getEvents, createEvent, updateEvent, deleteEvent, getEventById} from "../controller/events.controller.js";

const router = Router();

// Obtener todos los eventos
router.get("/events", getEvents);

// Crear un nuevo evento
router.post("/events", createEvent);

// Actualizar un evento existente
router.put("/events/:id", updateEvent);

// Eliminar un evento
router.delete("/events/:id", deleteEvent);

// Obtener evento por id
router.get("/events/:id", getEventById);

export default router;
