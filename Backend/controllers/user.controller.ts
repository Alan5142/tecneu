import * as express from 'express';
import * as database from '../database';
import {compare, genSalt, hash} from 'bcryptjs';
import * as jwt from 'jsonwebtoken';
import * as config from '../config.json';
import * as expressJwt from 'express-jwt'
import {fromHeaderOrQuerystring} from "../jwt-utilty";

module Route {
    export class UserController {
        constructor() {
        }

        get routes(): express.Router {
            const router = express.Router();
            router.get('/:id', this.getUserWithId.bind(this.getUserWithId));
            router.get('', expressJwt({
                secret: config.jwtSecret,
                getToken: fromHeaderOrQuerystring
            }), this.getAllUsers.bind(this.getAllUsers));
            router.post('/', this.createNewUser.bind(this.createNewUser));
            // delete user
            router.delete('/:userId', this.deleteUser.bind(this.deleteUser));
            // modificar usuario
            router.put('/:userId', this.modifyUser.bind(this.modifyUser));

            // login
            router.post('/login', this.login.bind(this.login));
            return router;
        }

        private getAllUsers(req: express.Request | any, res: express.Response) {
            database.connection.query('select idUser            as id,\n' +
                '       name              as userType,\n' +
                '       username,\n' +
                '       names,\n' +
                '       surnames,\n' +
                '       rfid,\n' +
                '       birthdate,\n' +
                '       creation_date     as creationDate,\n' +
                '       modification_date as modificationDate,\n' +
                '       u.name as userType\n' +
                'from user\n' +
                '       inner join user_type u on user.idUserType = u.idUserType', (err, result: Array<any>) => {

                res.status(200).send(result);
            });
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
                            result[0].idUserType,
                            body.username,
                            encryptedPassword,
                            body.surnames !== undefined ? body.surnames : '',
                            body.rfid,
                            body.birthdate,
                            body.names !== undefined ? body.names : ''], (err, result) => {
                            if (err) {
                                res.status(400);
                                res.send({});
                                return;
                            }
                            res.status(200);
                            res.send({});
                            return;
                        });
                    });
                });
            });

        }

        private login(req: express.Request, res: express.Response) {
            database.connection.query('SELECT username, password, user_type.name as type ' +
                'from user, user_type where username = ? and user.idUserType = user_type.idUserType',
                [req.body.username],
                (err, result) => {
                    if (err || result.length !== 1) {
                        res.status(404);
                        res.send({});
                        return;
                    }
                    compare(req.body.password, result[0].password).then(matches => {
                        if (!matches) {
                            res.status(404);
                            res.send({});
                            return;
                        }
                        const token = jwt.sign({
                                username: req.body.username,
                                id: result[0].idUser,
                                type: result[0].type
                            },
                            config.jwtSecret,
                            {expiresIn: "1y"});
                        res.status(200);
                        res.send({token: token, userType: result[0].type});
                    });
                    console.log(result[0]);
                })
        }

        private deleteUser(req: express.Request, res: express.Response) {
            database.connection.query('DELETE from user where idUser = ?', [req.params.userId], (err, result) => {
                res.status(err ? 400 : 200);
                res.send({});
            })
        }

        private modifyUser(req: express.Request, res: express.Response) {
            database.connection.query('SELECT * from user where idUser = ?', [req.params.userId], (err, result) => {
                if (err) {
                    res.status(400);
                    res.send({});
                    return;
                }
                const body = req.body;
                const currentInfo = result[0];
                const username = body.username ? body.username : currentInfo.username;
                const names = body.names ? body.names : currentInfo.names;
                const surnames = body.surnames ? body.surnames : currentInfo.surnames;
                const birthdate = body.birthdate ? body.birthdate : currentInfo.birthdate;
                if (body.password) {
                    genSalt(10, (err, salt) => {
                        // Encripta la contraseña
                        hash(body.password, salt, (err, encryptedPassword) => {
                            const password = encryptedPassword;
                            database.connection.query('UPDATE user SET password = ? WHERE idUser = ?', [encryptedPassword, req.params.userId], (err, dbRes) => {
                            })
                        });
                    });
                }
                if (body.userType) {
                    database.connection.query(`SELECT * from user_type WHERE name = ?`, [body.userType], (err, result: Array<any>) => {
                        database.connection.query('UPDATE user SET idUserType = ? WHERE idUser = ?', [result[0].idUserType, Number(req.params.userId)], (e, r) => {
                            console.log(e);
                        });
                    });
                }

                database.connection.query('UPDATE user SET ' +
                    'username = ?, ' +
                    'names = ?, ' +
                    'surnames = ?, ' +
                    'birthdate = ? ' +
                    'WHERE idUser = ?', [username, names, surnames, birthdate, req.params.userId], (err, r) => {
                    res.status(200);
                    res.send({});
                });

            })
        }
    }

}

export = Route;
