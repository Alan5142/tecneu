"use strict";
import * as express from 'express';
import * as config from './config.json';
import * as userController from './controllers/user.controller';

class Server {
    public app: express.Application;

    public static bootstrap(): Server {
      return new Server();
    }
  
    /**
     * Constructor.
     *
     * @class Server
     * @constructor
     */
    constructor() {
        //create expressjs application
        this.app = express();
        
        this.routes();

        this.app.get('/', (req, res) => res.send('Hello World!'));
        
    }

    public run(): void {
        this.app.listen(config.listenPort, () => console.log(`Example app listening on port ${config.listenPort}!`));
    }

    private routes() {
        const router = express.Router();
        this.app.use('/users', new userController.UserController().routes);
        this.app.use(router);
    }
}
new Server().run();