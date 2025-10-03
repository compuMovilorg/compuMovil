// src/controller/users.controller.js
import { Op } from "sequelize";
import { User } from "../models/Users.js";
import { Review } from "../models/Review.js";

/**
 * GET /users?search=&limit=&offset=
 * Búsqueda por username/email (case-insensitive en Postgres con iLike)
 * Paginación con limit/offset
 */
export const getUsers = async (req, res) => {
  try {
    const limit = Number.isNaN(parseInt(req.query.limit, 10))
      ? 50
      : parseInt(req.query.limit, 10);
    const offset = Number.isNaN(parseInt(req.query.offset, 10))
      ? 0
      : parseInt(req.query.offset, 10);
    const search = (req.query.search || "").trim();

    const where = search
      ? {
          [Op.or]: [
            { username: { [Op.iLike]: `%${search}%` } },
            { email: { [Op.iLike]: `%${search}%` } },
          ],
        }
      : {};

    const users = await User.findAll({ where, limit, offset });
    return res.json(users);
  } catch (err) {
    console.error("getUsers error:", err);
    return res.status(500).json({ message: "Error al obtener usuarios" });
  }
};

/**
 * GET /users/:id
 */
export const getUserById = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const user = await User.findByPk(id);
    if (!user) return res.status(404).json({ message: "Usuario no encontrado" });

    return res.json(user);
  } catch (err) {
    console.error("getUserById error:", err);
    return res.status(500).json({ message: "Error al obtener usuario" });
  }
};

/**
 * POST /users
 */
export const createUser = async (req, res) => {
  try {
    const newUser = await User.create(req.body);
    return res.status(201).json(newUser);
  } catch (err) {
    console.error("createUser error:", err);
    if (err.name === "SequelizeUniqueConstraintError") {
      return res
        .status(409)
        .json({ message: "username o email ya están en uso" });
    }
    return res.status(500).json({ message: "Error al crear usuario" });
  }
};

/**
 * PUT /users/:id
 */
export const updateUser = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const user = await User.findByPk(id);
    if (!user) return res.status(404).json({ message: "Usuario no encontrado" });

    await user.update(req.body);
    return res.json(user);
  } catch (err) {
    console.error("updateUser error:", err);
    if (err.name === "SequelizeUniqueConstraintError") {
      return res
        .status(409)
        .json({ message: "username o email ya están en uso" });
    }
    return res.status(500).json({ message: "Error al actualizar usuario" });
  }
};

/**
 * DELETE /users/:id
 */
export const deleteUser = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const user = await User.findByPk(id);
    if (!user) return res.status(404).json({ message: "Usuario no encontrado" });

    await user.destroy();
    return res.sendStatus(204);
  } catch (err) {
    console.error("deleteUser error:", err);
    return res.status(500).json({ message: "Error al eliminar usuario" });
  }
};

/**
 * GET /users/:id/reviews
 * Requiere las asociaciones:
 *   User.hasMany(Review, { foreignKey: "userId", as: "reviews" });
 *   Review.belongsTo(User, { foreignKey: "userId", as: "user" });
 */
export const getUserReviews = async (req, res) => {
  try {
    const id = parseInt(req.params.id, 10);
    if (Number.isNaN(id)) return res.status(400).json({ message: "ID inválido" });

    const exists = await User.count({ where: { id } });
    if (!exists) return res.status(404).json({ message: "Usuario no encontrado" });

    const reviews = await Review.findAll({
      where: { userId: id },
      include: [
        {
          model: User,
          as: "user",
          attributes: ["id", "username", "email"],
        },
      ],
      order: [["createdAt", "DESC"]], // asegúrate que Review tenga timestamps: true
    });

    return res.json(reviews);
  } catch (err) {
    console.error("getUserReviews error:", err);
    return res
      .status(500)
      .json({ message: "Error al obtener reseñas del usuario" });
  }
};
