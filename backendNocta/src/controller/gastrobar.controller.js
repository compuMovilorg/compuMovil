import { GastroBar } from "../models/gastroBar.js";

// Obtener todos los gastrobares
export const getGastroBars = async (req, res) => {
  try {
    const gastroBars = await GastroBar.findAll();
    return res.json(gastroBars);
  } catch (error) {
    console.error("Error al obtener gastrobares:", error);
    return res.status(500).json({ message: "Error al obtener gastrobares" });
  }
};

// Crear un nuevo gastrobar
export const createGastroBar = async (req, res) => {
  try {
    const newGastroBar = await GastroBar.create(req.body);
    return res.status(201).json(newGastroBar);
  } catch (error) {
    console.error("Error al crear gastrobar:", error);
    return res.status(500).json({ message: "Error al crear gastrobar" });
  }
};

// Obtener un gastrobar por ID
export const getGastroBarById = async (req, res) => {
  try {
    const id = req.params.id;
    const gastroBar = await GastroBar.findByPk(id);

    if (!gastroBar) {
      return res.status(404).json({ message: "GastroBar no encontrado" });
    }

    return res.json(gastroBar);
  } catch (error) {
    console.error("Error al obtener gastrobar:", error);
    return res.status(500).json({ message: "Error al obtener gastrobar" });
  }
};

// Actualizar un gastrobar
export const updateGastroBar = async (req, res) => {
  try {
    const id = req.params.id;
    const gastroBar = await GastroBar.findByPk(id);

    if (!gastroBar) {
      return res.status(404).json({ message: "GastroBar no encontrado" });
    }

    await gastroBar.update(req.body);
    return res.json(gastroBar);
  } catch (error) {
    console.error("Error al actualizar gastrobar:", error);
    return res.status(500).json({ message: "Error al actualizar gastrobar" });
  }
};

// Eliminar un gastrobar
export const deleteGastroBar = async (req, res) => {
  try {
    const id = req.params.id;
    const gastroBar = await GastroBar.findByPk(id);

    if (!gastroBar) {
      return res.status(404).json({ message: "GastroBar no encontrado" });
    }

    await gastroBar.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("Error al eliminar gastrobar:", error);
    return res.status(500).json({ message: "Error al eliminar gastrobar" });
  }
};
