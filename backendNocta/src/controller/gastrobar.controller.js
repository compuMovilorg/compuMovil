import { Op } from "sequelize";
import { GastroBar } from "../models/gastroBar.js";
import { Review } from "../models/Review.js";
import { Articulos } from "../models/Articulo.js";

// GET /gastrobars?search=&limit=&offset=
export const getGastroBars = async (req, res) => {
  try {
    const limit = Number.parseInt(req.query.limit ?? "50", 10);
    const offset = Number.parseInt(req.query.offset ?? "0", 10);
    const search = (req.query.search ?? "").trim();

    const where = search
      ? { name: { [Op.iLike]: `%${search}%` } }
      : {};

    const rows = await GastroBar.findAll({
      where,
      limit,
      offset,
      order: [["id", "ASC"]],
      // Si quieres devolver también reviewCount real desde DB:
      // include: [{ model: Review, as: "reviews", attributes: [] }],
    });

    return res.json(rows);
  } catch (error) {
    console.error("getGastroBars error:", error);
    return res.status(500).json({ message: "Error al obtener gastrobares" });
  }
};

// POST /gastrobars
export const createGastroBar = async (req, res) => {
  try {
    const required = ["name", "address"];
    for (const f of required) {
      if (!req.body?.[f]) {
        return res.status(400).json({ message: `Campo requerido: ${f}` });
      }
    }

    const newGB = await GastroBar.create(req.body);
    return res.status(201).json(newGB);
  } catch (error) {
    console.error("createGastroBar error:", error);
    return res.status(500).json({ message: "Error al crear gastrobar" });
  }
};

// GET /gastrobars/:id
export const getGastroBarById = async (req, res) => {
  try {
    const id = Number.parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const gb = await GastroBar.findByPk(id, {
      include: [
        { model: Articulos, as: "articulo", attributes: ["id", "titulo", "descripcion"] },
        // Si quieres traer también reviews:
        // { model: Review, as: "reviews", attributes: ["id", "reviewText", "likes", "comments"] },
      ],
    });

    if (!gb) return res.status(404).json({ message: "GastroBar no encontrado" });
    return res.json(gb);
  } catch (error) {
    console.error("getGastroBarById error:", error);
    return res.status(500).json({ message: "Error al obtener gastrobar" });
  }
};

// PUT /gastrobars/:id
export const updateGastroBar = async (req, res) => {
  try {
    const id = Number.parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const gb = await GastroBar.findByPk(id);
    if (!gb) return res.status(404).json({ message: "GastroBar no encontrado" });

    await gb.update(req.body);
    return res.json(gb);
  } catch (error) {
    console.error("updateGastroBar error:", error);
    return res.status(500).json({ message: "Error al actualizar gastrobar" });
  }
};

// DELETE /gastrobars/:id
export const deleteGastroBar = async (req, res) => {
  try {
    const id = Number.parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const gb = await GastroBar.findByPk(id);
    if (!gb) return res.status(404).json({ message: "GastroBar no encontrado" });

    await gb.destroy();
    return res.sendStatus(204);
  } catch (error) {
    console.error("deleteGastroBar error:", error);
    return res.status(500).json({ message: "Error al eliminar gastrobar" });
  }
};
