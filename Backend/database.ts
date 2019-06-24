"use strict";
import * as mysql from 'mysql';
import * as config from './config.json';

module Database {
    const conn = mysql.createConnection({
        host     : config.database.host,
        user     : config.database.user,
        password : config.database.password,
        database : config.database.name
    });
    conn.connect();
    conn.on('error', e => {
       console.error(e);
    });
    export const connection = conn;
}

export = Database;
