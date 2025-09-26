import { where } from 'sequelize';
import { User } from '../models/Users.js';

export const getUsers = async (req, res) => {
    const users = await User.findAll();
    return res.json(users);
}

export const createUser = async (req, res) => {
    const newUser = await User.create(req.body);
    return res.json(newUser);
};

export const updateUser = async (req, res) => {
    const id = req.params.id;
    const user = await User.findByPk(id);
    try {
        await user.update(req.body);
    } catch (error) {
        console.log(error);
    }

    return res.json(user);
};

export const deleteUser = async (req, res) => {
    const id = req.params.id;
    const user = await User.findByPk(id);
    await user.destroy();
    return res.sendStatus(204);
};

export const getUserReviews = async (req, res) => {
    const id = req.params.id;
    
    try {
        const reviews = await Review.findAll({
            where: { userId: id },
            include: {
                model: User,
                as: 'user',
                attributes: ['id', 'username', 'email'],
            },
        });
        
        return res.json(reviews);
    } catch (error) {
        console.error("Error al obtener reseñas del usuario:", error);
        return res.status(500).json({ message: "Error al obtener reseñas del usuario" });
    }
};
