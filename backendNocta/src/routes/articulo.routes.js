import { Router } from "express";
import {
  getArticulos,
  createArticulo,
  updateArticulo,
  deleteArticulo,
  getArticuloById
} from "../controller/articulo.controller.js";

const router = Router();

// Obtener todos los artículos
router.get("/", getArticulos);

// Crear un nuevo artículo
router.post("/", createArticulo);

// Obtener artículo por id
router.get("/:id", getArticuloById);

// Actualizar un artículo existente
router.put("/:id", updateArticulo);

// Eliminar un artículo
router.delete("/:id", deleteArticulo);

export default router;
