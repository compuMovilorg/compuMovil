import { User } from "../models/Users.js";

const initialUsers = [
  {
    username: "alicej",
    email: "alice@example.com",
    password: "123456",
    name: "Alice Johnson",
    birthdate: "1995-04-12",
    followersCount: 120,
    followingCount: 80,
  },
  {
    username: "carlosp",
    email: "carlos@example.com",
    password: "123456",
    name: "Carlos Pérez",
    birthdate: "1990-07-21",
    followersCount: 95,
    followingCount: 110,
  },
  {
    username: "mariag",
    email: "maria@example.com",
    password: "123456",
    name: "María Gómez",
    birthdate: "1992-11-05",
    followersCount: 200,
    followingCount: 150,
  },
  {
    username: "juanr",
    email: "juan@example.com",
    password: "123456",
    name: "Juan Rodríguez",
    birthdate: "1988-02-28",
    followersCount: 75,
    followingCount: 60,
  },
  {
    username: "lauram",
    email: "laura@example.com",
    password: "123456",
    name: "Laura Martínez",
    birthdate: "1997-09-15",
    followersCount: 180,
    followingCount: 95,
  },
];

export async function loadInitialUsers() {
  try {
    const count = await User.count();
    if (count === 0) {
      await User.bulkCreate(initialUsers);
      console.log("Usuarios iniciales cargados con followers y following.");
    } else {
      console.log("Los usuarios ya existen en la base de datos.");
    }
  } catch (error) {
    console.log(error);
  }
}
