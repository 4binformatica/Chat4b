

class Manager {
    constructor(onOpen, messageArrived, onClose) {
        console.log("Manager created");
        this.socket = new WebSocket("ws://87.4.163.109:8887/");
        this.onOpen = onOpen;
        this.messageArrived = messageArrived;
        this.onClose = onClose;
        
        this.socket.onopen = function (event) {
            onOpen(event);
        }
    
        this.socket.onmessage = function (event) {
            messageArrived(event);
        }
    
        this.socket.onclose = function (event) {
            onClose(event);
        }
    }

    sendToServer(message) {
        this.socket.send(message);
    }

    storeValue(key, value) {
        if (localStorage) {
            localStorage.setItem(key, value);
        }
    }

    getStoredValue(key) {
        if (localStorage) {
            return localStorage.getItem(key);
        }
    }

    deleteStoredValue(key) {
        if (localStorage) {
            localStorage.removeItem(key);
        } 
    }
}

export default Manager;
    


