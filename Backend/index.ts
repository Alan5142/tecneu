"use strict";
import * as express from 'express';
import * as config from './config.json';
import * as userController from './controllers/user.controller';
import * as providerController from './controllers/provider.controller';
import {ProviderController} from "./controllers/provider.controller";

const bodyParser = require('body-parser');

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

        this.app.use(bodyParser.urlencoded({extended: false}));
        // parse application/json
        this.app.use(bodyParser.json());

        this.routes();

        this.app.use(function (err, req, res, next) {
            if (err.name === 'UnauthorizedError') {
                res.status(401).send({message: 'Invalid token or not provided'});
            }
        });

        this.app.get('/', (req, res) => res.send(''));
    }

    public run(): void {
        this.app.listen(config.listenPort, () => console.log(`Example app listening on port ${config.listenPort}!`));
    }

    private routes() {
        const router = express.Router();
        const apiRouter = express.Router();
        router.use('/users', new userController.UserController().routes);
        router.use('/providers', new providerController.ProviderController().routes);
        apiRouter.use('/api', router);
        this.app.use(apiRouter);
    }
}

new Server().run();
