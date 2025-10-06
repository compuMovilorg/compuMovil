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
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
  },
  {
    username: "carlosp",
    email: "carlos@example.com",
    password: "123456",
    name: "Carlos Pérez",
    birthdate: "1990-07-21",
    followersCount: 95,
    followingCount: 110,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
  },
  {
    username: "mariag",
    email: "maria@example.com",
    password: "123456",
    name: "María Gómez",
    birthdate: "1992-11-05",
    followersCount: 200,
    followingCount: 150,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
  },
  {
    username: "juanr",
    email: "juan@example.com",
    password: "123456",
    name: "Juan Rodríguez",
    birthdate: "1988-02-28",
    followersCount: 75,
    followingCount: 60,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
  },
  {
    username: "lauram",
    email: "laura@example.com",
    password: "123456",
    name: "Laura Martínez",
    birthdate: "1997-09-15",
    followersCount: 180,
    followingCount: 95,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/usr1.png?alt=media&token=a28c0778-494b-4e4c-8378-64d33450fe9d",
  },
];

export async function loadInitialUsers() {
  try {
    const count = await User.count();
    if (count === 0) {
      await User.bulkCreate(initialUsers);
      console.log("Usuarios iniciales cargados con followers, following y profileImage.");
    } else {
      console.log("Los usuarios ya existen en la base de datos.");
    }
  } catch (error) {
    console.log(error);
  }
}
