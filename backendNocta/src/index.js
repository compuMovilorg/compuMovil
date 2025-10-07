import app from "./app.js";
import { sequelize } from "./database/database.js";
import { setupRelations } from "./models/relations.js";

// Importar modelos (asegura que Sequelize los registre)
import "./models/Users.js";
import "./models/Follower.js";
import "./models/Review.js";
import "./models/gastroBar.js";
import "./models/Articulo.js";

// Importar inicializadores
import { loadInitialUsers } from "./database/initUsers.js";
import { loadInitialGastroBars } from "./database/initGastroBar.js";
import { loadInitialArticulos } from "./database/initArticulos.js";
import { loadInitialReviews } from "./database/initReviews.js";
import { loadInitialEvents } from "./database/initEvents.js";

async function init() {
  try {
    await sequelize.authenticate();
    console.log("Conexion establecida correctamente.");

    // Configurar relaciones
    setupRelations();

    // Sincronizar base de datos
    await sequelize.sync({ force: true });
    console.log("Base de datos sincronizada.");

    // Cargar datos en orden correcto
    await loadInitialUsers();
    console.log("Usuarios cargados.");

    await loadInitialGastroBars();
    console.log("Gastrobares cargados.");

    await loadInitialArticulos();
    console.log("Articulos cargados.");

    await loadInitialReviews();
    console.log("Reviews cargadas.");

    await loadInitialEvents();
    console.log("Eventos cargados.");

    // Iniciar servidor
    app.listen(3000, () => {
      console.log("Servidor corriendo en el puerto 3000");
    });

  } catch (error) {
    console.error("Error al inicializar la base de datos:", error);
  }
}

init();
