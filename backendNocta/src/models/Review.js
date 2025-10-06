import { DataTypes } from "sequelize";
import { sequelize } from "../database/database.js";

export const Review = sequelize.define(
  "reviews",
  {
    id: { 
        type: DataTypes.INTEGER, 
        primaryKey: true, 
        autoIncrement: true 
    },
    userId: { 
        type: DataTypes.INTEGER, 
        allowNull: false,
        references: {
            model: 'users',
            key: 'id'
        },
    },
    gastroBarId: { 
        type: DataTypes.INTEGER, 
        allowNull: true,
        references: {
            model: 'gastrobars', // nombre de la tabla de GastroBar
            key: 'id'
        },
    },
    placeName: { 
        type: DataTypes.STRING, 
        allowNull: false 
    },
    imagePlace: { 
        type: DataTypes.STRING, 
        allowNull: true
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
    userImg: {
        type: DataTypes.VIRTUAL,
        get() {
            return this.user ? this.user.profileImage : null;
        },
    },
  },
  {
    timestamps: false
  }
);
