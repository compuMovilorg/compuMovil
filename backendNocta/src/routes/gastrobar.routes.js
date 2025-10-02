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
router.get("/gastrobars", getGastroBars);

// Crear un nuevo gastrobar
router.post("/gastrobars", createGastroBar);

// Actualizar un gastrobar existente
router.put("/gastrobars/:id", updateGastroBar);

// Eliminar un gastrobar
router.delete("/gastrobars/:id", deleteGastroBar);

// Obtener gastrobar por id
router.get("/gastrobars/:id", getGastroBarById);

export default router;
