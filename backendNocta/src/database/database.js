import { Sequelize } from "sequelize";

export const sequelize = new Sequelize("nocta", "postgres", "12345678", {
  host: "localhost",
  port: 5432,
  dialect: "postgres",
});
