let socket = new WebSocket("ws://localhost:8887/");

socket.addEventListener('open', (event) => {
    
    
  });

socket.addEventListener('message', (event) => {
    console.log(event.data);
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    ip = JSON.parse(event.data).ip;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;

    switch(operation){
        case "login":
            if(data == "success"){
                window.location.pathname = 'Client/chat.html';
            }else{
                alert("Login failed");
            }
            break;
        case "register":
            if(data == "success"){
                window.location.pathname = 'Client/chat.html';
            }else{
                alert("Register failed");
            }
            break;
        case "message":
            if(receiver == "server"){
                alert("Message from server: " + data);
            }else{
                alert("Message from " + username + ": " + data);
            }
            break;
            default:
                alert("Unknown operation");
    }
})


let sendToServer = (message) => {
    socket.send(message);
}

let login = () => {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let message = {
        "operation": "login",
        "username": username,
        "ip": GetUserIP(),
        "receiver": "server",
        "data": password

    }
    sendToServer(JSON.stringify(message));
}

let register = () => {
    let username = document.getElementById("username1").value;
    let password = document.getElementById("password1").value;
    let message = {
        "operation": "register",
        "username": username,
        "ip": GetUserIP(),
        "receiver": "server",
        "data": password

    }
    sendToServer(JSON.stringify(message));
}

function GetUserIP(){
    var ret_ip;
    $.ajaxSetup({async: false});
    $.get('http://jsonip.com/', function(r){ 
      ret_ip = r.ip; 
    });
    if(ret_ip == "undefined" || ret_ip == null || ret_ip == ""){
        return "xxx.xxx.xxx.xxx"
    }
    return ret_ip;
  }

