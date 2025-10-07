import { User } from "./Users.js";
import { GastroBar } from "./gastroBar.js";
import { Articulos } from "./Articulo.js";
import { Review } from "./Review.js";
import { Follower } from "./Follower.js";


export function setupRelations() {
  // ========== Users <-> Reviews ==========
  User.hasMany(Review, {
    foreignKey: "userId",
    as: "userReviews",          // <- antes "reviews"
    onDelete: "CASCADE",
    hooks: true,
  });
  Review.belongsTo(User, {
    foreignKey: "userId",
    as: "user",
  });

  // ========== Followers (self many-to-many) ==========
  User.belongsToMany(User, {
    through: Follower,
    as: "Userfollowing",
    foreignKey: "followerId",
    otherKey: "followingId",
  });
  User.belongsToMany(User, {
    through: Follower,
    as: "Userfollowers",
    foreignKey: "followingId",
    otherKey: "followerId",
  });

  // ========== Reviews self-replies ==========
  Review.hasMany(Review, {
    foreignKey: "parentReviewId",
    as: "replies",
    onDelete: "CASCADE",
    hooks: true,
  });
  Review.belongsTo(Review, {
    foreignKey: "parentReviewId",
    as: "parentReview",
  });

  // ========== GastroBar <-> Reviews ==========
  GastroBar.hasMany(Review, {
    foreignKey: "gastroBarId",
    as: "gastroBarReviews",    
  });
  Review.belongsTo(GastroBar, {
    foreignKey: "gastroBarId",
    as: "gastroBar",
  });

  // ========== Articulos <-> Reviews ==========
  Articulos.hasMany(Review, {
    foreignKey: "articuloId",
    as: "articuloReviews",      // <- antes "reviews"
    onDelete: "CASCADE",
    hooks: true,
  });
  Review.belongsTo(Articulos, {
    foreignKey: "articuloId",
    as: "articulo",
  });

  // ========== GastroBar <-> Articulos (1:1) ==========
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
