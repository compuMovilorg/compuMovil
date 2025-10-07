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
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/UserImg%2Fuser3.jpg?alt=media&token=1c372927-7482-4569-a009-26896a8f7a75",
  },
  {
    username: "carlosp",
    email: "carlos@example.com",
    password: "123456",
    name: "Carlos Pérez",
    birthdate: "1990-07-21",
    followersCount: 95,
    followingCount: 110,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/profileImages%2FzxSNUhe6yhUrw5HHCYTcudijZ2c2.jpg?alt=media&token=98046e1d-449c-497f-9f79-759b8479a0c8",
  },
  {
    username: "mariag",
    email: "maria@example.com",
    password: "123456",
    name: "María Gómez",
    birthdate: "1992-11-05",
    followersCount: 200,
    followingCount: 150,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/UserImg%2Fuser2.jpg?alt=media&token=0e75642f-1ae6-40a9-aa5a-b1452be2b908",
  },
  {
    username: "juanr",
    email: "juan@example.com",
    password: "123456",
    name: "Juan Rodríguez",
    birthdate: "1988-02-28",
    followersCount: 75,
    followingCount: 60,
    profileImage: "https://firebasestorage.googleapis.com/v0/b/nocta-95b8c.firebasestorage.app/o/profileImages%2FnDuAf0JyaScYIpZOtXULfaCwZoz1.jpg?alt=media&token=115b5b46-eca0-41ab-b7ee-81d8cccb7a28",
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