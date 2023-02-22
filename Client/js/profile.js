socket.onopen = function() {
    getProfilePic();
    getBio();
    document.getElementById("profileName").innerText = getStoredValue("username");
}

let profilePicClicked = function() {
    let file = document.getElementById("changeProfilePic-input").files[0];
    let reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = async () => {
        let base64Img = reader.result.split(',')[1];
        let message = {
            "operation": "changeProfilePic",
            "username": getStoredValue("username"),
            "receiver": "server",
            "data": base64Img,
            "date": new Date().toISOString()
        }
        sendToServer(JSON.stringify(message));
        reloadProfilePic();
    }
}


let editMailButton = function() {
    let saveButton = document.getElementById("saveMail");
    saveButton.style.display = "inline-block";
    let editButton = document.getElementById("editMail");
    editButton.style.display = "none";
}

let saveMailButton = function() {
    let saveButton = document.getElementById("saveMail");
    saveButton.style.display = "none";
    let editButton = document.getElementById("editMail");
    editButton.style.display = "inline-block";
    changeMail();
}


let editBioButton = function() {
    let maxLength = 200;

    document.getElementById("bioProfile").contentEditable = true;
    let saveButton = document.getElementById("saveBio");
    saveButton.style.display = "inline-block";
    let editButton = document.getElementById("editBio");
    editButton.style.display = "none";

    let bio = document.getElementById("bioProfile");
    //focus on the text
    bio.focus();
    //make the text selected
    let selection = window.getSelection();
    let range = document.createRange();
    range.selectNodeContents(bio);
    selection.removeAllRanges();
    selection.addRange(range);

    //if enter is pressed
    bio.addEventListener("keydown", function(e) {
        if(bio.innerText.length >= maxLength) {
            //if the text is longer than maxLength, prevent the user from typing
            if(e.keyCode !== 8 && e.keyCode !== 46) {
                if(selection.toString().length === 0) {
                    e.preventDefault();
                }
            }
        }
        if (e.keyCode === 13) {
            e.preventDefault();
            document.getElementById("saveBio").click();
        }
    });


}

let reloadProfilePic = function() {
    getProfilePic();
}

let saveBioButton = function() {  
    document.getElementById("bioProfile").contentEditable = false;
    let saveButton = document.getElementById("saveBio");
    saveButton.style.display = "none";
    let editButton = document.getElementById("editBio");
    editButton.style.display = "inline-block";
    editBio();
}

/**
 * "getProfilePic" is a function that sends a message to the server to get the profile picture of the
 * user.
 */
let getProfilePic = function() {
    message = {
        "operation": "getProfilePic",
        "username": getStoredValue("username"),
        "recevier": getStoredValue("loginID"),
        "data": "",
        "date": new Date().getTime()

    }
    socket.send(JSON.stringify(message));       
}

/**
 * GetBio() is a function that sends a message to the server to get the bio of the user.
 */
let getBio = function() {
    console.log("getBio" + getStoredValue("loginID"));
    message = {
        "operation": "getBio",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("loginID"),
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
        "receiver": getStoredValue("loginID"),
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
    let mail = document.getElementById("mail").innerHTML;
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
    let password = document.getElementById("pass").innerText;
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
    console.log(event);
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation) {
        case "profilePic":
            document.getElementById("profilePic").src = data;
            break;
        case "bio":
            document.getElementById("bioProfile").innerText = data;
            break;
        default:
            break;
    }

    
}

let fontChanged = () => {
    console.log("hello");
}