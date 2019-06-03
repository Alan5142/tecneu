import * as socketio from 'socket.io';
import {Server} from "https";
export let io: socketio.Server;

export const startConnection = (server: Server) => {
    io = socketio(server);

    io.on('connection', socket => {
        socket.on('dataMessage', data => {
           console.log(data);
           io.emit('receiveData', data);
        });
        socket.on('receiveFingerprint', data => {

        })
    });
};
