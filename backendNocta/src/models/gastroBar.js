import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const GastroBar = sequelize.define(
  "gastrobars",
  {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    imagePlace: {
      type: DataTypes.STRING, // URL o nombre de archivo de la imagen
      allowNull: true,
    },
    name: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    rating: {
      type: DataTypes.FLOAT,
      allowNull: false,
      defaultValue: 0.0,
    },
    reviewCount: {
      type: DataTypes.INTEGER,
      allowNull: false,
      defaultValue: 0,
    },
    address: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    hours: {
      type: DataTypes.STRING,
      allowNull: true, 
    },
    cuisine: {
      type: DataTypes.STRING,
      allowNull: true,
    },
    description: {
      type: DataTypes.TEXT,
      allowNull: true,
    },
    reviewId: {
      type: DataTypes.INTEGER,
      allowNull: true,
      references: {
        model: "reviews",  // nombre de la tabla de Review
        key: "id",
      },
      onDelete: "SET NULL",
      onUpdate: "CASCADE",
    },
  },
  {
    timestamps: false,
  }
);