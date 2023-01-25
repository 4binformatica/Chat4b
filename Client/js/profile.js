socket.onopen = function() {
    getProfilePic();
    getBio();
    document.getElementById("profileName").innerText = getStoredValue("username");
}

/**
 * "getProfilePic" is a function that sends a message to the server to get the profile picture of the
 * user.
 */
let getProfilePic = function() {
    message = {
        "operation": "getProfilePic",
        "username": getStoredValue("username"),
        "recevier": getStoredValue("username"),
        "data": "",
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));       
}

/**
 * GetBio() is a function that sends a message to the server to get the bio of the user.
 */
let getBio = function() {
    message = {
        "operation": "getBio",
        "username": getStoredValue("username"),
        "recevier": getStoredValue("username"),
        "data": getStoredValue("username"),
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));       
}

let editBio = function() {
    let bio = document.getElementById("bioProfile").innerText;
    let message = {
        "operation": "changeBio",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("username"),
        "data": bio,
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));
}

let changeUsername = function() {
    let username = document.getElementById("profileName").innerText;
    let message = {
        "operation": "changeUsername",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("username"),
        "data": username,
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));
}

let changeMail = function() {
    let mail = document.getElementById("mail").innerText;
    let message = {
        "operation": "changeMail",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("username"),
        "data": mail,
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));
}

let changePassword = function() {
    let password = document.getElementById("password").innerText;
    let message = {
        "operation": "changePassword",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("username"),
        "data": password,
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));
}

/* A function that is called when the client receives a message from the server. */
socket.onmessage = function(event) {
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation) {
        case "getProfilePic":
            document.getElementById("profilePic").src = data;
            break;
        case "getBio":
            document.getElementById("bioProfile").innerText = data;
            break;
        default:
            break;
    }
}