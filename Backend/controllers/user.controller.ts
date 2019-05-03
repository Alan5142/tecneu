import * as express from 'express';
import * as database from '../database';
import { genSalt, hash, compare } from 'bcryptjs';
import * as jwt from 'jsonwebtoken';

module Route {
    export class UserController {
        constructor() {
        }

        get routes(): express.Router {
            const router = express.Router();
            router.get('/:id', this.getUserWithId.bind(this.getUserWithId));
            router.post('/', this.createNewUser.bind(this.createNewUser));

            // login
            router.post('/login', this.login.bind(this.login));
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

        /**
         * Crea un nuevo usuario
         * @param req petición del servidor al cliente
         * @param res respuesta del servidor al cliente
         */
        private createNewUser(req: express.Request, res: express.Response) {
            const body = req.body;
            if (!body.password || !body.username || !body.userType) {
                res.status(400);
                res.send();
                return;
            }

            // buscamos el user type
            database.connection.query(`SELECT * from user_type WHERE name = ?`, [body.userType], (err, result: Array<any>) => {
                if (err || result.length !== 1) {
                    res.status(400);
                    res.send();
                    return;
                }

                genSalt(10, (err, salt) => {
                    // Encripta la contraseña
                    hash(body.password, salt, (err, encryptedPassword) => {
                        database.connection.query(`INSERT INTO user
                        (idUserType, Username, Password, surnames, rfid, birthdate, creation_date, modification_date, names) 
                        VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), ?)`, [
                        result[0].id,
                        body.username,
                        encryptedPassword,
                        body.surnames !== undefined ? body.surnames : '',
                        body.rfid,
                        body.birthdate, 
                        body.names !== undefined ? body.names : ''], (err, result) => {
                            if (err) {
                                res.status(400);
                                res.send();
                                return;
                            }
                            res.status(200);
                            res.send();
                            return;
                        });
                    });
                });
            });
            
        }

        private login(req: express.Request, res: express.Response) {
            database.connection.query('SELECT idUser, Username, Password from user where Username = ?', [req.body.username], (err, result: Array<any>) => {
                if (err || result.length !== 1) {
                    res.status(404);
                    res.send();
                    return;
                }
                compare(req.body.password, result[0].Password).then(matches => {
                    if (!matches) {
                        res.status(404);
                        res.send();
                        return;
                    }
                    const token = jwt.sign({username: req.body.username, id: result[0].idUser }, "secret", {expiresIn: "1y"});
                    res.status(200);
                    res.send({token: token});

                })
                console.log(result[0]);
            })
        }
    }
    
}

export = Route;
