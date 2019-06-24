"use strict";
import * as express from 'express';
import * as config from './config.json';
import * as userController from './controllers/user.controller';
import * as providerController from './controllers/provider.controller';
import * as http from 'http';
import * as https from 'https';
import {ProviderController} from "./controllers/provider.controller";
import * as fs from "fs";
import {startConnection} from "./sensors";
import ordersController = require('./controllers/orders.controller.js');
import {ProductsController} from "./controllers/products.controller";

const bodyParser = require('body-parser');

class Server {
    public app: express.Application;

    constructor() {
        //create expressjs application
        this.app = express();
        this.run();
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
        const privateKey = fs.readFileSync('keys/key.pem', 'utf8');
        const certificate = fs.readFileSync('keys/cert.pem', 'utf8');
        const credentials = {
            key: privateKey,
            cert: certificate,
            passphrase: 'socketio'
        };
        const server = new https.Server(credentials, this.app);
        startConnection(server);
        server.listen(Number(config.listenPort), config.listenAddress, -1, () => console.log('Escuchando'));
    }

    private routes() {
        const router = express.Router();
        const apiRouter = express.Router();
        router.use('/users', new userController.UserController().routes);
        router.use('/providers', new providerController.ProviderController().routes);
        router.use('/orders', new ordersController.OrdersController().routes);
        router.use('/products', new ProductsController().routes);
        apiRouter.use('/api', router);
        this.app.use(apiRouter);
    }
}

new Server();
