
import { Articulos } from "../models/Articulo.js";
import { GastroBar } from "../models/gastroBar.js";
import { Op } from "sequelize";

// GET /articulos?search=&limit=&offset=
export const getArticulos = async (req, res) => {
  try {
    const limit = parseInt(req.query.limit ?? "50", 10);
    const offset = parseInt(req.query.offset ?? "0", 10);
    const search = (req.query.search ?? "").trim();

    const where = search
      ? { titulo: { [Op.iLike]: `%${search}%` } }
      : {};

    const articulos = await Articulos.findAll({
      where,
      limit,
      offset,
      include: [{ model: GastroBar, as: "gastroBar" }],
      order: [["id", "ASC"]],
    });
    return res.json(articulos);
  } catch (error) {
    console.error("Error al obtener artículos:", error);
    return res.status(500).json({ message: "Error al obtener artículos" });
  }
};

// POST /articulos
export const createArticulo = async (req, res) => {
  try {
    const { gastroBarId, titulo, descripcion } = req.body;
    if (!gastroBarId || !titulo || !descripcion) {
      return res.status(400).json({ message: "gastroBarId, titulo y descripcion son requeridos" });
    }

    const gb = await GastroBar.findByPk(gastroBarId);
    if (!gb) return res.status(400).json({ message: "gastroBarId no existe" });

    const newArticulo = await Articulos.create(req.body);
    return res.status(201).json(newArticulo);
  } catch (error) {
    console.error("Error al crear artículo:", error);
    if (error.name === "SequelizeUniqueConstraintError") {
      return res.status(409).json({ message: "Ese GastroBar ya tiene un artículo" });
    }
    return res.status(500).json({ message: "Error al crear artículo" });
  }
};

// GET /articulos/:id
export const getArticuloById = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const articulo = await Articulos.findByPk(id, {
      include: [{ model: GastroBar, as: "gastroBar" }],
    });
    if (!articulo) return res.status(404).json({ message: "Artículo no encontrado" });

    return res.json(articulo);
  } catch (error) {
    console.error("Error al obtener artículo:", error);
    return res.status(500).json({ message: "Error al obtener artículo" });
  }
};

// PUT /articulos/:id
export const updateArticulo = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const articulo = await Articulos.findByPk(id);
    if (!articulo) return res.status(404).json({ message: "Artículo no encontrado" });

    // opcional: si cambian gastroBarId, validar que exista
    if (req.body.gastroBarId) {
      const gb = await GastroBar.findByPk(req.body.gastroBarId);
      if (!gb) return res.status(400).json({ message: "gastroBarId no existe" });
    }

    await articulo.update(req.body);
    return res.json(articulo);
  } catch (error) {
    console.error("Error al actualizar artículo:", error);
    if (error.name === "SequelizeUniqueConstraintError") {
      return res.status(409).json({ message: "Ese GastroBar ya tiene un artículo" });
    }
    return res.status(500).json({ message: "Error al actualizar artículo" });
  }
};

// DELETE /articulos/:id
export const deleteArticulo = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const articulo = await Articulos.findByPk(id);
    if (!articulo) return res.status(404).json({ message: "Artículo no encontrado" });

    await articulo.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("Error al eliminar artículo:", error);
    return res.status(500).json({ message: "Error al eliminar artículo" });
  }
};
