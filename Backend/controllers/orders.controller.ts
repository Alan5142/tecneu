import * as express from 'express';
import * as database from '../database';

module Route {
    export class OrdersController {
        get routes(): express.Router {
            const router = express.Router();
            router.delete('/products/:productId', this.deleteOrderProduct.bind(this.deleteOrderProduct));
            router.put('/:idOrder', this.finishOrder.bind(this.finishOrder));
            router.get('/:idOrder/products', this.getOrderProducts.bind(this.getOrderProducts));
            router.get('', this.getAllOrders.bind(this.getAllOrders));
            router.post('', this.createOrder.bind(this.createOrder));
            return router;
        }

        deleteOrderProduct(req: express.Request, res: express.Response) {
            database.connection.query('delete from contains_product where idHaveProduct = ?', [req.params.productId], (err, results) => {
                if (err) {
                    res.status(400).send({});
                    return;
                }
                res.status(200).send({});
            });
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

        finishOrder(req: express.Request, res: express.Response) {

            database.connection.query(`select cp.idProduct, p.stock, cp.quantity, \`order\`.order_status
from \`order\`
         inner join contains_product cp on \`order\`.idOrder = cp.idOrder
inner join products p on cp.idProduct = p.idProduct
where \`order\`.idOrder = ?`, [req.params.idOrder], (err, results: Array<any>) => {
                if (err || results[0].order_status === 'Finalizada') {
                    res.status(400).send({});
                    return;
                }

                database.connection.query(`update \`order\` set order_status = 'Finalizada' where idOrder = ?`, [req.params.idOrder], (err1, results1) => {
                    if (err1) {
                        res.status(400).send({});
                        return;
                    }
                    res.status(200).send({});
                    results.forEach(value => {
                        database.connection.query(`update products set stock = ? where idProduct = ?`,
                            [Number(value.stock) + Number(value.quantity), value.idProduct],
                            (err2, results2) => {
                            });
                    });
                });
            });
        }

        getOrderProducts(req: express.Request, res: express.Response) {
            database.connection.query(`select cp.idHaveProduct as idOrderProduct, quantity, p.idProduct, mercadolibre_id as meliId, stock, name, hp.price
from \`order\` inner join contains_product cp on \`order\`.idOrder = cp.idOrder
inner join products p on cp.idProduct = p.idProduct
inner join have_product hp on p.idProduct = hp.idProduct where cp.idOrder = ?`, [req.params.idOrder], (err, results) => {
                res.status(200).send(results);
            })
        }

        getAllOrders(req: express.Request, res: express.Response) {
            database.connection.query(`select idOrder, creation_date as creationDate, modification_date as modificationDate, order_status as orderStatus
from \`order\``, (err, results) => {
                if (err) {
                    res.status(400).send({});
                    return;
                }
                res.status(200).send(results);
            });
        }

        modifyOrder(req: express.Request, res: express.Response) {

        }

        deleteOrder(req: express.Request, res: express.Response) {
            database.connection.query('DELETE * FROM orders where orderId = ?', [req.params.id], (err, result) => {
                if (err) {
                    res.status(400).send({});
                    return;
                }
                res.status(200).send({});
            });
        }
    }

}

export = Route;
