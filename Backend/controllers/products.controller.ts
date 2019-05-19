import * as express from 'express';
import * as database from '../database';

module Route {
    export class ProductsController {
        get routes(): express.Router {
            const router = express.Router();
            router.get('/', this.getallProducts.bind(this.getallProducts));
            router.post('/:id', this.getProductWithId.bind(this.getProductWithId));
            return router;
        }

        getallProducts(req: express.Request, res: express.Response) {
        }

        getProductWithId(req: express.Request, res: express.Response) {

        }
    }

}

export = Route;
