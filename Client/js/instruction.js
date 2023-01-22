

let sendToServer = (message) => {
    socket.send(message);
}

let getMessages = () => {
    let message = {
        "operation": "getMessages",
        "username": getStoredValue("username"),
        "ip": getStoredValue("ip"),
        "receiver": "server",
        "data": "getMessages"

    }
    sendToServer(JSON.stringify(message));
}

let sendMessage = (message, receiver) => {
    let messageObject = {
        "operation": "message",
        "username": getStoredValue("username"),
        "ip": getStoredValue("ip"),
        "receiver": receiver,
        "data": message
    }
    console.log(messageObject);
    sendToServer(JSON.stringify(messageObject));
}

let register = () => {
    let username = document.getElementById("username1").value;
    let password = document.getElementById("password1").value;
    let message = {
        "operation": "register",
        "username": username,
        "ip": getStoredValue("ip"),
        "receiver": "server",
        "data": password

    }
    sendToServer(JSON.stringify(message));
}

async function GetUserIP(){
    //get the ip of the user
    var ip = "xxx.xxx.xxx.xxx";
    await $.getJSON('https://api.ipify.org?format=jsonp&callback=?', function(data) {
        ip = data.ip;
    });
    return Promise.resolve(ip);
}

