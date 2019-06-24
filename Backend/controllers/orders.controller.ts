import * as express from 'express';
import * as database from '../database';
import * as config from "../config.json";
import * as expressJwt from 'express-jwt'
import {fromHeaderOrQuerystring} from "../jwt-utilty";

module Route {
    export class OrdersController {
        get routes(): express.Router {
            const router = express.Router();
            router.get('', expressJwt({
                secret: config.jwtSecret,
                getToken: fromHeaderOrQuerystring
            }), this.getAllOrders.bind(this.getAllOrders));
            return router;
        }

        getAllOrders(req: express.Request, res: express.Response) {
            database.connection.query("SELECT tecneu.order.*, person_receiving.name as 'personR'," +
                " payment_method.name AS 'payment' FROM tecneu.order, tecneu.person_receiving, " +
                "tecneu.payment_method WHERE tecneu.person_receiving.idPersonReceiving = tecneu.order.idPersonReceiving" +
                " AND tecneu.payment_method.idPaymentMethod = tecneu.order.idPaymentMethod", (err, result) => {
                if (err) {
                    res.status(400);
                    res.send({});
                    return;
                }
                res.status(200);
                res.send(result);
            });
        }

        modifyOrder(req: express.Request, res: express.Response) {

        }

        deleteOrder(req: express.Request, res: express.Response) {
            database.connection.query('DELETE * FROM orders where orderId = ?', [req.params.id], (err, result) => {

            });
        }
    }

}

export = Route;
