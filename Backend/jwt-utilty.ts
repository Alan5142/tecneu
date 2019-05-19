import * as express from 'express';

export function fromHeaderOrQuerystring(req: express.Request) {
    if (req.headers.authorization && req.headers.authorization.split(' ')[0] === 'Bearer') {
        return req.headers.authorization.split(' ')[1];
    } else if (req.query && req.query.token) {
        return req.query.token;
    }
    console.log('dident find token');
    return null;
}
