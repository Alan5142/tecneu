import * as express from 'express';
import * as database from '../database';

module Route {
    export class ProductsController {
        get routes(): express.Router {
            const router = express.Router();
            router.get('/', this.getallProducts.bind(this.getallProducts));
            return router;
        }

        getallProducts(req: express.Request, res: express.Response) {
        }
    }

}

export = Route;
