
import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const Review = sequelize.define(
  "reviews",
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },

    userId: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: { model: "users", key: "id" },
      onDelete: "CASCADE",
    },

    // 🔹 FK obligatoria al artículo
    articuloId: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: { model: "articulos", key: "id" },
      onDelete: "CASCADE",
    },

    placeName: { type: DataTypes.STRING, allowNull: false },
    reviewText: { type: DataTypes.TEXT, allowNull: false },
    likes: { type: DataTypes.INTEGER, allowNull: false, defaultValue: 0 },
    comments: { type: DataTypes.INTEGER, allowNull: false, defaultValue: 0 },

    parentReviewId: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: { model: "reviews", key: "id" },
      onDelete: "SET NULL",
    },
  },
  { timestamps: true } // t
);
