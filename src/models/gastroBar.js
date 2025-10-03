import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const GastroBar = sequelize.define(
  "gastrobars",
  {
    id: { type: DataTypes.INTEGER, primaryKey: true, autoIncrement: true },

    imagePlace: { type: DataTypes.STRING, allowNull: true }, // URL o nombre de archivo
    name: { type: DataTypes.STRING, allowNull: false },
    rating: { type: DataTypes.FLOAT, allowNull: false, defaultValue: 0.0 },
    reviewCount: { type: DataTypes.INTEGER, allowNull: false, defaultValue: 0 },
    address: { type: DataTypes.STRING, allowNull: false },
    hours: { type: DataTypes.STRING, allowNull: true },
    cuisine: { type: DataTypes.STRING, allowNull: true },
    description: { type: DataTypes.TEXT, allowNull: true },

    
  },
  {
    timestamps: false, // cámbialo a true si vas a usar createdAt/updatedAt
  }
);
