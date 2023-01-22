$(window).on("load", function () {
    keepAlive();
    setInterval(keepAlive, 10000);
}); // End of window load

let keepAlive = function () {
    let message = {
        "operation": "keepAlive",
        "username": getStoredValue("username"),
        "ip": getStoredValue("ip"),
        "receiver": "server",
        "data": "keepAlive"
    }
    sendToServer(JSON.stringify(message));
}

socket.addEventListener('message', (event) => {
    console.log(event.data);
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    ip = JSON.parse(event.data).ip;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;

    switch(operation){
        case "message":
            if(receiver == "server"){
                alert("Message from server: " + data);

            }else{
                alert("Message from " + username + ": " + data);
            }
            break;
            default:
                console.log(data);
    }
})