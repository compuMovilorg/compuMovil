import { Sequelize } from "sequelize";

export const sequelize = new Sequelize("nocta", "postgres", "password", {
  host: "localhost",
  port: 5432,
  dialect: "postgres",
});
