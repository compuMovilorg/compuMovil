import app from './app.js';
import { sequelize } from './database/database.js';
import { loadInitialReviews } from './database/initReviews.js';
import { loadInitialUsers } from './database/initUsers.js';
import { loadInitialGastroBars } from './database/initGastroBar.js';
import { setupRelations } from './models/relations.js';
import  './models/Review.js';
import  './models/Users.js';
import  './models/Follower.js';
import { loadInitialEvents } from './database/initEvents.js';
import { loadInitialArticulos } from './database/initArticulos.js';

async function init() {

    try {
    await sequelize
    .authenticate()
    .then(() => {
        console.log('Connection has been established successfully.');
    })
    .catch(err => {
        console.error('Unable to connect to the database:', err);
    });

    setupRelations();
    
    await sequelize.sync({ force: true });

    await loadInitialUsers();
    await loadInitialGastroBars();
    await loadInitialReviews();
    await loadInitialEvents();
    await loadInitialArticulos();

  app.listen(3000, () => {
    console.log('Server is running on port 3000');
  });
    }catch (error) {
        console.log(error);
    }
}

init();