import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const Event = sequelize.define(
  "events",
  {
    id: {
      type: DataTypes.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    date: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    time: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    title: {
      type: DataTypes.STRING,
      allowNull: false,
    },
    eventImage: {
      type: DataTypes.STRING, 
      allowNull: true,
    },
  },
  {
    timestamps: false,
  }
);
