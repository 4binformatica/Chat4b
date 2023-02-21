var activeContainer = "container1";

let login = function () {
    username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let message = {
        "operation": "login",
        "username": username,
        "receiver": "Server",
        "data": password,
        "date": new Date().toISOString()
    }
    deleteStoredValue("username")
    storeValue("username", username);
    sendToServer(JSON.stringify(message));

}

let register = function () {
    let username = document.getElementById("regUsername").value;
    let email = document.getElementById("regEmail").value;
    let confmail = document.getElementById("confEmail").value;
    let password = document.getElementById("regPassword").value;
    let confpassw = document.getElementById("confPassword").value;
    if (email != confmail) {
        alert("Emails do not match!");
        return;
    }
    if (password != confpassw) {
        alert("Passwords do not match!");
        return;
    }
    let message = {
        "operation": "register",
        "username": username,
        "receiver": email,
        "data": password,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let checkVerificationCode = () => {
    message = {
        "operation": "checkVerificationCode",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": document.getElementById("checkCodeInput").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let changeCointainer = (container) => {
    activeContainer = container;
    container = document.getElementById(container);
    scrollToElement(container);
}

$(document).keyup(function(event) {
    if (event.key == "Enter") {
        console.log("enter");
        // Get the id of the focused element
        console.log(activeContainer);
        switch (activeContainer) {
            case "container1":
                console.log("login");
                login();
                break;
            case "container2":
                register();
                break;
            case "container3":
                checkVerificationCode();
                break;
            default:
                console.log("default");
                break;
        }
    }
});

/* Listening for a message from the server. */
socket.onmessage = function(event) {
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation) {
        case "loginID":
            this.loginID = data;
            deleteStoredValue("loginID");
            storeValue("loginID", data);
            deleteStoredValue("username");
            storeValue(document.getElementById("username").value, username);
            break;
        case "needVerification":
            activeContainer = "container3";
            var verification = document.getElementById("container3");
            //scrool to container3
            verification.scrollIntoView();
            break;
        case "checkVerificationCode":
            console.log(data);
            if(data == "success"){
                alert("Verification successful");
                storeValue("username", username);
                window.location.pathname = 'Client/chat.html';
            }else{
                alert("Verification failed");
            }
            break;
        case "login":
            if(data === "success") {
                storeValue("username", username);
                window.location.pathname = 'Client/chat.html';
            } else {
                alert("Login failed");
            }
            break;
        case "checkLoginID":
            if(data == "true"){
                window.location.pathname = 'Client/chat.html';
            }else{
                if(!window.location.pathname == 'Client/index.html')
                    window.location.pathname = 'Client/index.html';
            }
            break;
        case "register":
            if(data == "success"){
                alert("Registration successful, please check your mail for the verification code");
                storeValue("username", username);
                let login = document.getElementById("container1");
                login.scrollIntoView();
            }else{
                alert(data);
            }
            break;
        case "changeForgotPassword":
            if(data == "success"){
                alert("Password changed!");
                location.reload();
            }else{
                alert(data);
            }
            break;

        default:
            console.log(event.data);
            
    }
}