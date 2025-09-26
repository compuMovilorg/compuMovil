import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const Review = sequelize.define(
  "reviews",
  {
    userId: { 
        type: DataTypes.INTEGER, 
        allowNull: false,
        references: {
            model: 'users',
            key: 'id'
        },
    },
    id: { 
        type: DataTypes.INTEGER, 
        primaryKey: true, 
        autoIncrement: true 
    },
    placeName: { 
        type: DataTypes.STRING, 
        allowNull: false 
    },
    reviewText: { 
        type: DataTypes.TEXT, 
        allowNull: false 
    },
    likes: { 
        type: DataTypes.INTEGER, 
        allowNull: false, 
        defaultValue: 0 
    },
    comments: { 
        type: DataTypes.INTEGER, 
        allowNull: false, 
        defaultValue: 0 
    },
    parentReviewId: { 
        type: DataTypes.INTEGER, 
        allowNull: true,
        references: {
            model: 'reviews',
            key: 'id'
        },
    },
  },
  {
    timestamps: false
  }
);