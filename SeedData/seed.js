import admin from "firebase-admin";
import serviceAccount from "./serviceAccountKey.json" with {type: "json"}
import {fa, faker, tr} from "@faker-js/faker"

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

function buildUser(i){
    const firstName = faker.person.firstName()
    const lastName = faker.person.lastName()
    const username = `${firstName.toLowerCase()}_${lastName.toLowerCase()}`;
    const email = `${username}@example.com`;

    const birthDate = faker.date.birthdate({
        min: 1970, 
        max: 2007, 
        mode: 'year'

        });
        const nowIso = new Date().toISOString();

    return {
        id: i,
        username,
        email,
        name: `${firstName} ${lastName}`,
        birthdate: birthDate.toISOString().split("T")[0],
        followersCount: faker.number.int({ min: 0, max: 5000 }),
        followingCount: faker.number.int({ min: 0, max: 5000 }),
        profileImage: faker.image.urlPicsumPhotos({
        width: 150,
        height: 150,
        category: "people",
        }),
        followed: faker.datatype.boolean(),
  };
}

function buildReview(user, i){
   const nowIso = new Date().toISOString();
    const hasGastro = faker.datatype.boolean();
    
    return {
        userImage: user?.profileImage ?? faker.image.urlPicsumPhotos({
      width: 150,
      height: 150,
      category: "people",
    }),
    placeImage: faker.image.urlPicsumPhotos({
      width: 640,
      height: 360,
      category: "food",
    }),
    id: `${user.id}_${i}`,
    userId: String(user.id),
    // 'name' puede ser el nombre del autor o null (según tu modelo lo admite)
    name: faker.datatype.boolean() ? user.name : null,
    placeName: `${faker.company.name()} ${faker.location.city()}`,
    placeImage: faker.datatype.boolean() 
    ? faker.image.urlPicsumPhotos({
      width: 640,
      height: 360
    }): null,
    reviewText: faker.lorem.sentence({ min: 5, max: 100 }),
    likes: faker.number.int({ min: 0, max: 2000 }),
    comments: faker.number.int({ min: 0, max: 200 }),
    gastroBarId: hasGastro ? `gastro_${faker.number.int({ min: 1, max: 200 })}` : null,
    liked: false,
    createdAt: nowIso,
    updatedAt: nowIso,
    user: {
        id: String(user.id),
        name: user.name,
        username: user.username,
        profileImage: user.profileImage,
    },
  };
}

async function seedUsers(count = 50){
    let batch = db.batch();
    const users = [];
    for(let i=1; i<=count; i++){
        const user = buildUser(i);
        users.push(user);

        const userRef = db.collection("users").doc(String(user.id));
        batch.set(userRef, user);
    }

    await batch.commit();

    return users
}

async function seedReviews(users, reviewsPerUser = 5){
    let batch = db.batch();

    for (const user of users){
        for(let i=1; i<=reviewsPerUser; i++){
            const review = buildReview(user,i);
        
            const reviewRef = db.collection("reviews").doc(String(review.id));
            batch.set(reviewRef, review);
    }
}

    await batch.commit();

    return users
}

async function main(){
    try{
    console.log("Seeding users...");
    const users = await seedUsers(5);
    console.log(`Seeding Reviews...`);
    await seedReviews(users, 2);
    console.log("Seeding completed.");
    process.exit(0);
    } catch (error){
        console.error("Error seeding data:", error);
        process.exit(1);
    }
}

main();