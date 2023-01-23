let username;
let password;

let register = () => {
    this.username = document.getElementById("username1").value;
    this.password = document.getElementById("password1").value;
    let message = {
        "operation": "register",
        "username": this.username,
        "receiver": "server",
        "data": this.password

    }
    sendToServer(JSON.stringify(message));
}

socket.addEventListener('message', (event) => {
    console.log(event.data);
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;

    switch(operation){
        case "register":
            if(data == "success"){
                let message = {
                    "operation": "login",
                    "username": this.username,
                    "receiver": "server",
                    "data": this.password,
                    "date": new Date().toISOString()
                }
                sendToServer(JSON.stringify(message));
                window.location.pathname = 'Client/chat.html';
            }else{
                alert(data);
            }
            break;
            default:
                console.log(data);
    }
})