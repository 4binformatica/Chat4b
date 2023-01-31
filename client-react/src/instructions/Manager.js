import WebSocket from 'ws';

class Manager {
    constructor(onOpen, messageArrived, onClose) {
        this.socket = new WebSocket("ws://192.168.1.3:8887/");
        this.onOpen = onOpen;
        this.messageArrived = messageArrived;
        this.onClose = onClose;
        
        this.socket.onopen = function (event) {
            this.onOpen(event);
        }
    
        this.socket.onmessage = function (event) {
            this.messageArrived(event);
        }
    
        this.socket.onclose = function (event) {
            this.onClose(event);
        }
    }

    sendtoServer(message) {
        this.socket.send(message);
    }
}

export default Manager;
    


