import * as express from 'express';
import * as database from '../database';

module Route {
    export class ProductsController {
        get routes(): express.Router {
            const router = express.Router();
            router.post('/', this.modifyProducts.bind(this.modifyProducts));
            return router;
        }

        modifyProducts(req: express.Request, res: express.Response) {
            database.connection.query('UPDATE product SET stock = ? WHERE mercadolibre_id = ?',
                [req.body.stock, req.body.id],
                (err, result) => {
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
