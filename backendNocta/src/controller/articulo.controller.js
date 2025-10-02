import { Articulos } from "../models/Articulo.js";
import { GastroBar } from "../models/gastroBar.js";

// Obtener todos los artículos
export const getArticulos = async (req, res) => {
  try {
    const articulos = await Articulos.findAll({
      include: [{ model: GastroBar, as: "gastroBar" }],
    });
    return res.json(articulos);
  } catch (error) {
    console.error("Error al obtener artículos:", error);
    return res.status(500).json({ message: "Error al obtener artículos" });
  }
};

// Crear un nuevo artículo
export const createArticulo = async (req, res) => {
  try {
    const newArticulo = await Articulos.create(req.body);
    return res.status(201).json(newArticulo);
  } catch (error) {
    console.error("Error al crear artículo:", error);
    return res.status(500).json({ message: "Error al crear artículo" });
  }
};

// Obtener un artículo por ID
export const getArticuloById = async (req, res) => {
  try {
    const id = req.params.id;
    const articulo = await Articulos.findByPk(id, {
      include: [{ model: GastroBar, as: "gastroBar" }],
    });

    if (!articulo) {
      return res.status(404).json({ message: "Artículo no encontrado" });
    }

    return res.json(articulo);
  } catch (error) {
    console.error("Error al obtener artículo:", error);
    return res.status(500).json({ message: "Error al obtener artículo" });
  }
};

// Actualizar un artículo
export const updateArticulo = async (req, res) => {
  try {
    const id = req.params.id;
    const articulo = await Articulos.findByPk(id);

    if (!articulo) {
      return res.status(404).json({ message: "Artículo no encontrado" });
    }

    await articulo.update(req.body);
    return res.json(articulo);
  } catch (error) {
    console.error("Error al actualizar artículo:", error);
    return res.status(500).json({ message: "Error al actualizar artículo" });
  }
};

// Eliminar un artículo
export const deleteArticulo = async (req, res) => {
  try {
    const id = req.params.id;
    const articulo = await Articulos.findByPk(id);

    if (!articulo) {
      return res.status(404).json({ message: "Artículo no encontrado" });
    }

    await articulo.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("Error al eliminar artículo:", error);
    return res.status(500).json({ message: "Error al eliminar artículo" });
  }
};
