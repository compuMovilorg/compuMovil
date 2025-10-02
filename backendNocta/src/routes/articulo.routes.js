import { Router } from "express";
import { getArticulos, createArticulo, updateArticulo, deleteArticulo, getArticuloById } from "../controller/articulo.controller.js";

const router = Router();

// Obtener todos los artículos
router.get("/articulos", getArticulos);

// Crear un nuevo artículo
router.post("/articulos", createArticulo);

// Actualizar un artículo existente
router.put("/articulos/:id", updateArticulo);

// Eliminar un artículo
router.delete("/articulos/:id", deleteArticulo);

// Obtener artículo por id
router.get("/articulos/:id", getArticuloById);

export default router;
