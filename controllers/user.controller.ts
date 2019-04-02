import * as express from 'express';
import * as database from '../database';

module Route {
    export class UserController {
        constructor() {
        }

        get routes(): express.Router {
            const router = express.Router();
            router.get('/:id', this.getUserWithId.bind(this.getUserWithId));
            router.post('/', this.createNewUser.bind(this.createNewUser));
            return router;
        }

        private getUserWithId(req: express.Request, res: express.Response) {
            database.connection.query('SELECT * from user where idUser = ?', [req.params.id], (err, result: Array<any>) => {
                if (err || result.length !== 1) {
                    res.status(404);
                    res.send();
                    return;
                }
                res.status(200);
                res.send(result[0]);
            });
            
        }

        private createNewUser(req: express.Request, res: express.Response) {

        }
    }
    
}

export = Route;
