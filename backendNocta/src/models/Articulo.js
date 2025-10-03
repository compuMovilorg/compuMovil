import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const Articulos = sequelize.define(
  "articulos",
  {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    gastroBarId: {
      type: DataTypes.INTEGER,
      allowNull: false,
      unique: true,                  
      references: { model: "gastrobars", key: "id" },
      onDelete: "CASCADE",
    },
    titulo: {
      type: DataTypes.STRING,
      allowNull: false, // será el mismo nombre del GastroBar
    },
    imagen: {
      type: DataTypes.STRING, // URL o path de la imagen principal
      allowNull: true,
    },
    descripcion: {
      type: DataTypes.TEXT,
      allowNull: false,
    },
    rating: {
      type: DataTypes.FLOAT,
      allowNull: false,
      defaultValue: 0,
    },
    fechaPublicacion: {
      type: DataTypes.DATE,
      defaultValue: DataTypes.NOW,
    },
  },
  {
    timestamps: false,
  }
);

