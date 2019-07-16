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
            router.post('', this.createOrder.bind(this.createOrder));
            return router;
        }

        createOrder(req: express.Request, res: express.Response) {
            database.connection.query(`INSERT INTO \`order\` (idPersonReceiving, 
            idPaymentMethod, order_status, tracking_number, creation_date, modification_date, invoice)
             VALUES (NULL, NULL, '', '', NOW(), NOW(), NULL)`, (err, results) => {
                if (err) {
                    console.error(err);
                    res.status(400).send({});
                    return;
                }
                let counter = 0;
                for (let i = 0; i < req.body.products.length; i++) {
                    const product = req.body.products[i];
                    database.connection.query(`INSERT INTO contains_product (idOrder, idProduct, quantity) 
                    VALUES(?, ?, ?)`, [results.insertId, product.idProduct, product.quantity], (err1, results1) => {
                       if (err1) {
                           console.error(err1);
                           return;
                       }
                       counter++;
                       if (counter === req.body.products.length) {
                           res.send(200).send({});
                       }
                    });
                }
            });
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
