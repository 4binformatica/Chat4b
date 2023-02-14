let firstClick = false;
let firstClick2 = false;
let mail = "";
let code = "";
let username = "";
let loginID = "";
let motivationalPhrases = [];

socket.onopen = async function() {
    console.log("Connection established");
    getMotivationalPhrases();
    isLogged();
    //wait 2 second and writeArray(motivationalPhrases, document.getElementById("writer"), 600, 1000);
    //ma usare il setTimeout invece di await sleep(2000);
    
    await sleep(2000);
    writeArray(motivationalPhrases, document.getElementById("writer"), 600, 1000);
}



let getMotivationalPhrases = () => {
    let message = {
        "operation": "getMotivationalPhrases"
    }
    socket.send(JSON.stringify(message));
}

//run isLofged() when the page is loaded after 1 second
window.onload = function() {
    createInput("text", "username", "Your username...", "user", "input_cont");
    createInput("password", "password", "Your password...", "pass", "input_cont");
    createButton("login_button", "Login", login, "btn btn-white btn-animation", "button_cont");
    createButton("register_button", "Register", register, "btn btn-white btn-animation", "button_cont");
    createButton("password_reset_button", "Password reset", passwordReset, "btn btn-white btn-animation", "button_cont");
}





/**
 * It takes the username and password from the login form, creates a JSON object, and sends it to the
 * server.
 */
let login = function() {
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

let register = function() {
    if(firstClick == false){
        document.getElementById("username").remove();
        document.getElementById("password").remove();
        document.getElementById("login_button").remove();
        document.getElementById("register_button").remove();
        document.getElementById("password_reset_button").remove();
        createInput("text", "username", "Your username...", "user", "input_cont");
        createInput("text", "mail", "Your mail...", "user", "input_cont");
        createInput("password", "password", "Your password...", "pass", "input_cont");
        createInput("password", "passwordverify", "Verify your password...", "pass", "input_cont");
        createButton("register_button", "Register", register, "btn btn-white btn-animation", "button_cont");
        firstClick = true;
        return;
    }
    username = document.getElementById("username").value;
    let mail = document.getElementById("mail").value;
    let password = document.getElementById("password").value;
    let passwordverify = document.getElementById("passwordverify").value;
    if(!mail.includes("@") || !mail.includes(".")){
        alert("Please enter a valid mail address");
        return;
    }
    if(password != passwordverify){
        alert("The passwords do not match");
        return;
    }
    let message = {
        "operation": "register",
        "username": username,
        "receiver": mail,
        "data": password,
        "date": new Date().toISOString()   
    }
    sendToServer(JSON.stringify(message));
}

let passwordReset = function() {
    if(firstClick2 == false){
        document.getElementById("username").remove();
        document.getElementById("password").remove();
        document.getElementById("login_button").remove();
        document.getElementById("register_button").remove();
        document.getElementById("password_reset_button").remove();
        createInput("text", "mail", "Your mail...", "user", "input_cont");
        createButton("mail_check_button", "Check mail", checkMail, "btn btn-white btn-animation", "button_cont");
        firstClick2 = true;
        return;
    }
    let mail = document.getElementById("mail").value;
}

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
        "username": mail,
        "receiver": "server",
        "data": document.getElementById("code").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let changePassword = () => {
    //check if the passwords match
    if(document.getElementById("password").value != document.getElementById("passwordverify").value){
        alert("The passwords do not match");
        return;
    }
    message = {
        "operation": "changeForgotPassword",
        "username": mail,
        "receiver": code,
        "data": document.getElementById("password").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let checkVerificationCode = () => {
    message = {
        "operation": "checkVerificationCode",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": document.getElementById("code").value,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let isLogged = () => {
    let username = getStoredValue("username");
    let loginID = getStoredValue("loginID");
    console.log("Username: " + username + " LoginID: " + loginID)
    let message = {
        "operation": "checkLoginID",
        "username": username,
        "receiver": "server",
        "data": loginID,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));

}

let createInput = (type, id, placeholder, className, parent) => {
    let input = document.createElement("input");
    input.type = type;
    input.id = id;
    input.placeholder = placeholder;
    input.className = className;
    document.getElementById(parent).appendChild(input);
    return input;
}

let createButton = (id, text, onclick, className, parent) => {
    console.log("Creating button");
    let button = document.createElement("a");
    button.id = id;
    button.innerHTML = text;
    button.onclick = onclick;
    button.className = className;
    button.href = "#";
    document.getElementById(parent).appendChild(button);
    return button;
}



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
            document.getElementById("username").remove();
            document.getElementById("password").remove();
            document.getElementById("login_button").remove();
            document.getElementById("register_button").remove();
            document.getElementById("password_reset_button").remove();
            createInput("text", "code", "Verification code...", "user", "input_cont");
            createButton("code_check_button", "Check code", checkVerificationCode, "btn btn-white btn-animation", "button_cont");
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
                location.reload();
            }else{
                alert(data);
            }
            break;

        case "forgotPassword":
            if(data == "success"){
                mail = document.getElementById("mail").value;
                document.getElementById("mail_check_button").remove();
                document.getElementById("mail").remove();
                createInput("text", "code", "Verification code...", "user", "input_cont");
                createButton("code_check_button", "Check code", checkCode, "btn btn-white btn-animation", "button_cont");
            }else{
                alert(data);
            }
            break;
        case "checkForgotCode":
            if(data == "success"){
                code = document.getElementById("code").value;
                document.getElementById("code_check_button").remove();
                document.getElementById("code").remove();
                createInput("password", "password", "New password...", "user", "input_cont");
                createInput("password", "passwordverify", "Verify password...", "user", "input_cont");
                createButton("change_password_button", "Change password", changePassword, "btn btn-white btn-animation", "button_cont");
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
        case "getMotivationalPhrases":
            motivationalPhrases.push(data);
            //console.log(motivationalPhrases);
            //writeArray(motivationalPhrases, document.getElementById("writer"), 600, 1000);
            break;

        default:
            console.log(event.data);
            
    }
}

