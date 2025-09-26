import { Sequelize } from "sequelize";

export const sequelize = new Sequelize("nocta", "postgres", "12345678", {
    port: 5432,
    host: "localhost",
    dialect: "postgres",
});