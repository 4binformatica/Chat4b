socket.onopen = function() {
    isAdministrator();
    userTable = document.getElementById("userTable");
    userTable.innerHTML = "";
    userTable.innerHTML += "<tr><th>Profile Pic</th><th>Username</th><th>Email</th><th>Delete</th></tr>";

}

let isAdministrator = () => {
    message = {
        "operation" : "isAdministrator",
        "username": getStoredValue("username"),
        "receiver": "Server",
        "data": null,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let sendAdminCode = () => {
    let code = document.getElementById("adminCode").value;
    message = {
        "operation" : "verifyAdminCode",
        "username": getStoredValue("username"),
        "receiver": "Server",
        "data": code,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let getAllUsers = () => {
    message = {
        "operation" : "getAllUsers",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": null,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let deleteUser = (username) => {
    message = {
        "operation" : "deleteUser",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": username,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let changeUsername = (username) => {
    let newUsername = prompt("Enter new username");
    message = {
        "operation" : "changeUsername",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": newUsername,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let changeEmail = (username) => {
    let newEmail = prompt("Enter new email");
    message = {
        "operation" : "changeEmail",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": newEmail,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}


let deleteAllUsers = () => {
    message = {
        "operation" : "deleteAllUsers",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": null,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let deleteAllMessages = () => {
    message = {
        "operation" : "deleteAllMessages",
        "username": getStoredValue("username"),
        "receiver": getStoredValue("adminCode"),
        "data": null,
        "date": new Date().toISOString()
    }
    socket.send(JSON.stringify(message));
}

let logout = () => {
    deleteStoredValue("username");
    deleteStoredValue("adminCode");
    window.location.pathname = 'Client/index.html';
}

let refresh = () => {
    getAllUsers();
}

socket.onmessage = function(event) {
    console.log(event.data);
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation) {
        case "isAdministrator":
            if(data == "true"){
                console.log("is admin");
                if(window.location.pathname != "/Client/admin.html")
                    window.location.pathname = 'Client/admin.html';

                document.getElementById("adminCode").style.display = "block";
                document.getElementById("adminCodeButton").style.display = "block";
            }else{
                window.location.pathname = 'Client/chat.html';
            }
        case "verifyAdminCode":
            if(data == "true"){
                storeValue("adminCode", document.getElementById("adminCode").value);
                document.getElementById("adminCode").value = "";
                document.getElementById("adminCode").placeholder = "Code accepted";
                //document.getElementById("adminCode").style.display = "none";
                //document.getElementById("adminCodeButton").style.display = "none";
                getAllUsers();
            }else{
                document.getElementById("adminCode").value = "";
                document.getElementById("adminCode").placeholder = "Code not accepted";
                deleteStoredValue("adminCode");
            }
            break;
        case "getAllUsers":
            //put users in table
            //username is the user
            //data is the profile picture
            //receiver is the email
            let table = document.getElementById("userTable");
            let row = table.insertRow(-1);
            let cell1 = row.insertCell(0);
            let cell2 = row.insertCell(1);
            let cell3 = row.insertCell(2);
            let cell4 = row.insertCell(3);
            cell1.innerHTML = "<img src='" + data + "' alt='Profile Picture' width='50' height='50'>";
            cell2.innerHTML = username;
            cell3.innerHTML = receiver;
            cell4.innerHTML = "<button onclick='deleteUser(\"" + username + "\")'>Delete</button> <button onclick='changeUsername(\"" + username + "\")'>Change Username</button> <button onclick='changeEmail(\"" + username + "\")'>Change Email</button>";
            break;
        case "changeUsername":
            if(data == "true"){
                alert("Username changed");
            }else{
                alert("Username not changed");
            }
            break;
        case "changeEmail":
            if(data == "true"){
                alert("Email changed");
            }else{
                alert("Email not changed");
            }
        default:
            console.log(data);
            
    }
}

