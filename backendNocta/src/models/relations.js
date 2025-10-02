import { User } from "./Users.js";
import { Review } from "./Review.js";
import { Follower } from "./Follower.js";
import { GastroBar } from "./gastroBar.js";
import { Articulos } from "./Articulo.js";

export function setupRelations() {
    // Relación User - Review
    User.hasMany(Review, { 
        foreignKey: 'userId',
        as: 'reviews',
        onDelete: 'cascade',
        hooks: true
    });

    Review.belongsTo(User, { 
        foreignKey: 'userId',
        as: 'user',
    });

    // Followers
    User.belongsToMany(User, {
        through: Follower,
        as: 'Userfollowing',
        foreignKey: 'followerId',
        otherKey: 'followingId',
    });

    User.belongsToMany(User, {
        through: Follower,
        as: 'Userfollowers',
        foreignKey: 'followingId',
        otherKey: 'followerId',
    });

    // Self-replies en Review
    Review.hasMany(Review, {
        foreignKey: 'parentReviewId',
        as: 'replies',
        onDelete: 'cascade',
        hooks: true
    });

    Review.belongsTo(Review, {
        foreignKey: 'parentReviewId',   
        as: 'parentReview',
    });

    // Relación uno a muchos GastroBar - Review
    GastroBar.hasMany(Review, {
    foreignKey: "gastroBarId",
    as: "reviews"
    });

    // Una Review pertenece a un único GastroBar
    Review.belongsTo(GastroBar, {
    foreignKey: "gastroBarId",
    as: "gastroBar"
    });

    // Relación 1:1 → cada GastroBar tiene un Articulo
    GastroBar.hasOne(Articulos, {
    foreignKey: "gastroBarId",
    as: "articulo",
    onDelete: "CASCADE",
    });
    Articulos.belongsTo(GastroBar, {
    foreignKey: "gastroBarId",
    as: "gastroBar",
    });

}
