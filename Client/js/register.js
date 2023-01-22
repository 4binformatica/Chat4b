socket.addEventListener('message', (event) => {
    console.log(event.data);
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    ip = JSON.parse(event.data).ip;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;

    switch(operation){
        case "register":
            if(data == "success"){
                window.location.pathname = 'Client/chat.html';
            }else{
                alert("Register failed");
            }
            break;
            default:
                console.log(data);
    }
})