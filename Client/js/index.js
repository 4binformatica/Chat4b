socket.onopen = function() {
    isLogged();
}

//run isLofged() when the page is loaded after 1 second
window.onload = function() {
    setTimeout(isLogged, 1000);
}


/**
 * It takes the username and password from the login form, creates a JSON object, and sends it to the
 * server.
 */
let login = function() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;
    let message = {
        "operation": "login",
        "username": username,
        "receiver": "Server",
        "data": password,
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


socket.onopen = function() {
    console.log("Connection established!");
}

/* Listening for a message from the server. */
socket.onmessage = function(event) {
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation) {
        case "loginID":
            deleteStoredValue("loginID");
            storeValue("loginID", data);
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
        default:
            console.log(data);
            
    }
}

