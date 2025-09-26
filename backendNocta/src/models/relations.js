import { User } from "./Users.js";
import { Review } from "./Review.js";
import { Follower } from "./Follower.js";

export function setupRelations() {
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
}