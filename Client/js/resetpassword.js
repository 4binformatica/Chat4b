let canChangePassword = false;

let checkMail = () => {
    message = {
        "operation": "forgotPassword",
        "username": document.getElementById("mail").value,
        "receiver": "server",
        "data": document.getElementById("mail").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
    alert("Check your mail for the code");
}

let checkCode = () => {
    message = {
        "operation": "checkForgotCode",
        "username": document.getElementById("mail").value,
        "receiver": "server",
        "data": document.getElementById("code").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let changePassword = () => {
    message = {
        "operation": "changeForgotPassword",
        "username": document.getElementById("mail").value,
        "receiver": "server",
        "data": document.getElementById("password1").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let checkPassword = () => {
    if(document.getElementById("password1").value === document.getElementById("password2").value){
        document.getElementById("password2").style.backgroundColor = "green";
        document.getElementById("password1").style.backgroundColor = "green";
        changePassword();
    }else{
        document.getElementById("password2").style.backgroundColor = "red";
        document.getElementById("password1").style.backgroundColor = "red";
    }
}

socket.addEventListener('message', (event) => {
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;

    switch(operation){
        case "forgotPassword":
            if(data == "success"){
                console.log("success");
                document.getElementById("maildiv").style.display = "none";
                document.getElementById("codediv").style.display = "block";
                document.getElementById("code").focus();
                
            }else{
                alert(data);
            }
            break;
        case "checkForgotCode":
            if(data == "success"){
                document.getElementById("password1").focus();
                document.getElementById("password2").focus();
                document.getElementById("codediv").style.display = "none";
                document.getElementById("passdiv").style.display = "block";
            }else{
                alert(data);
            }
            break;
        case "changeForgotPassword":
            if(data == "success"){
                alert("Password changed!");
                window.location.pathname = 'Client/index.html';
            }else{
                alert(data);
            }
            break;
        default:
            console.log(data);
            break;
        }
    }
)
