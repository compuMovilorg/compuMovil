// src/routes/users.routes.js
import { Router } from "express";
import {
  getUsers,
  getUserById,          // <-- agrégala
  createUser,
  updateUser,
  deleteUser,
  getUserReviews
} from "../controller/users.controller.js";

const router = Router();

router.get("/", getUsers);                 // GET /users
router.post("/", createUser);              // POST /users

router.get("/:id/reviews", getUserReviews);// <- más específica ANTES
router.get("/:id", getUserById);           // <- ahora sí GET /users/:id

router.put("/:id", updateUser);            // PUT /users/:id
router.delete("/:id", deleteUser);         // DELETE /users/:id

export default router;
