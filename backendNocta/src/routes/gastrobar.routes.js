import { Router } from "express";
import { 
  getGastroBars, 
  createGastroBar, 
  updateGastroBar, 
  deleteGastroBar, 
  getGastroBarById 
} from "../controller/gastrobar.controller.js";

const router = Router();

// Obtener todos los gastrobares
router.get("/", getGastroBars);

// Crear un nuevo gastrobar
router.post("/", createGastroBar);

// Obtener gastrobar por id
router.get("/:id", getGastroBarById);

// Actualizar un gastrobar existente
router.put("/:id", updateGastroBar);

// Eliminar un gastrobar
router.delete("/:id", deleteGastroBar);

export default router;
