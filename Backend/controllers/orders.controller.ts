import * as express from 'express';
import * as database from '../database';

module Route {
    export class OrdersController {
        get routes(): express.Router {
            const router = express.Router();
            return router;
        }

        getAllOrders(req: express.Request, res: express.Response) {
            database.connection.query('SELECT * from orders', (err, result) => {
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
