let cancelMsg = false;
let cancelContact = false;

socket.onopen = function (event) {
    keepAlive();
    setInterval(keepAlive, 10000);
    getContacts();
    isLogged();
};

let draft = () => {
    let message = document.getElementById("message-input").value;
    let messageDraft = {
        "operation": "saveDraft",
        "username": getStoredValue("username"),
        "receiver": getSelectedContactName(),
        "data": message,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(messageDraft));
}

let isLogged = () => {
    let username = getStoredValue("username");
    let loginID = getStoredValue("loginID");
    let message = {
        "operation": "checkLoginID",
        "username": username,
        "receiver": "server",
        "data": loginID,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));

}

let keepAlive = function () {
    let message = {
        "operation": "keepAlive",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": "keepAlive",
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let getContacts = () => {
    let message = {
        "operation": "getContacts",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": "getContacts",
        "date": new Date().toISOString()

    }
    sendToServer(JSON.stringify(message));
}

let getMessages = (name) => {
    let message = {
        "operation": "getMessages",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": name,
        "date": new Date().toISOString()

    }
    sendToServer(JSON.stringify(message));
}


let addContact = () => {
    let person = prompt("Please enter the contact name", "Contact name");
    let message = {
        "operation": "addContact",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": person,
        "date": new Date().toISOString()
    
    }
    sendToServer(JSON.stringify(message));
    reloadContacts();
}

let reloadContacts = () => {
    document.getElementById("contactList").innerHTML = "";
    getContacts();
}

let getSelectedContactName = () => {
    let contactList = document.getElementById("contactList").children;
    for (let i = 0; i < contactList.length; i++) {
        if (contactList[i].id == "active") {
            return contactList[i].innerText;
        }
    }
}

let showProfilePic = (username1) => {
    let message = {
        "operation": "getProfilePic",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": username1,
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
}

let changeProfilePic = () => {
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
        showProfilePic(getStoredValue("username"));
    }
}

  let sendImage = async () => {
    let image = document.getElementById("image-input").files[0];
    let reader = new FileReader();
    reader.readAsDataURL(image);
    reader.onload = async () => {
        let receiver = getSelectedContactName();
        // convertire l'immagine in una stringa base64
        let base64Img = reader.result.split(',')[1];
        let messageObject = {
            "operation": "image",
            "username": getStoredValue("username"),
            "receiver":  getSelectedContactName(),
            "data": base64Img
        };
        sendToServer(JSON.stringify(messageObject));
        reloadMessages();
    }
}
  

let sendMessage = async () => {   
    let message = document.getElementById("message-input").value;
    if(message == ""){
        return;
    }
    let receiver = getSelectedContactName();
    let messageObject = {
        "operation": "message",
        "username": getStoredValue("username"),
        "receiver": receiver,
        "data": message,
        "date": new Date().toISOString()
    }

    sendToServer(JSON.stringify(messageObject));
    document.getElementById("message-input").value = ""
    document.getElementById("messageList").scrollTop = document.getElementById("messageList").scrollHeight;
    reloadMessages();
}

let reloadMessages = () => {
    document.getElementById("messageList").innerHTML = "";
    getMessages(getSelectedContactName());
}

let logout = () => {
    let message = {
        "operation": "logout",
        "username": getStoredValue("username"),
        "receiver": "server",
        "data": "logout",
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(message));
    deleteStoredValue("username");
    window.location.pathname = 'Client/index.html';
}

let deleteMsg = () => {
    if(cancelMsg){
        cancelMsg = false;
        document.getElementById("deleteMsg").style.backgroundColor = "#011936";
    }
    else{
        cancelMsg = true;
        document.getElementById("deleteMsg").style.backgroundColor = "red";
    }
}

let deteleContact = () => {
    if(cancelContact){
        cancelContact = false;
        document.getElementById("deleteContact").style.backgroundColor = "#011936";
    }
    else{
        cancelContact = true;
        document.getElementById("deleteContact").style.backgroundColor = "red";
    }
}



socket.addEventListener('message', (event) => {
    console.log(event.data);
    operation = JSON.parse(event.data).operation;
    username = JSON.parse(event.data).username;
    receiver = JSON.parse(event.data).receiver;
    data = JSON.parse(event.data).data;
    date = JSON.parse(event.data).date;

    switch(operation){
        case "message":
            if(receiver == "server"){
                alert("Message from server: " + data);

            }else{
                //if the message isn't from the current user and the current user is neither the sender nor the receiver
                if(username != getStoredValue("username") && getSelectedContactName() != username && getSelectedContactName() != receiver)
                     return;
                var message = document.createElement('div');
                if(username == getStoredValue("username")){
                    message.className = "message-container sent";
                }else{
                    message.className = "message-container received";
                }
                message.innerHTML = data; 
                message.date = date;
                message.onclick = function() {
                    if(cancelMsg){
                        let message1 = {
                            "operation": "removeMessage",
                            "username": getStoredValue("username"),
                            "receiver": getSelectedContactName(),
                            "data": message.date,
                            "date": new Date().toISOString()
                            
                        }
                        sendToServer(JSON.stringify(message1));
                        reloadMessages();
                    }
                }
                document.getElementById("messageList").appendChild(message);
                //scroll to bottom
                document.getElementById("messageList").scrollTop = document.getElementById("messageList").scrollHeight;
            }
            break;
        case "reload":
            reloadContacts();
            reloadMessages();
            break;
        case "image":
            var image = document.createElement('img');
            if(username == getStoredValue("username")){
                image.className = "image-sent";
            }else{
                image.className = "image-received";
            }
            image.src =  data;
            image.width = 200;
            image.height = 200;
            image.date = date;
            image.onclick = function() {
                if(cancelMsg){
                    let message1 = {
                        "operation": "removeMessage",
                        "username": getStoredValue("username"),
                        "receiver": getSelectedContactName(),
                        "data": image.date,
                        "date": new Date().toISOString()
                        
                    }
                    sendToServer(JSON.stringify(message1));
                    reloadMessages();
                }
            }
            document.getElementById("messageList").appendChild(image);
            //scroll to bottom
            document.getElementById("messageList").scrollTop = document.getElementById("messageList").scrollHeight;
            break;
        case "getProfilePic":
            var image = document.createElement('img');
            image.src =  data;
            image.width = 200;
            image.height = 200;
            
            
        case "contact":
            var contact = document.createElement('div');
            var name = document.createElement('div');
            var image = document.createElement('img');
            contact.className = "contact";
            name.className = "contact-name";
            image.className = "contact-image";
            image.width = 30;
            image.height = 30;
            contact.appendChild(image);
            contact.appendChild(name);
            name.innerText = username;
            image.src =  data;


            
            contact.className = "contact-name";
            contact.onclick = function() {
            document.getElementById("messageList").innerText = "";
            let contactList = document.getElementById("contactList").children;
            for (let i = 0; i < contactList.length; i++) {
                contactList[i].id = "";
                contactList[i].style.backgroundColor = "white";
                contactList[i].style.color = "black";
            }
            contact.id = "active";
            contact.style.backgroundColor = "#011936";
            contact.style.color = "white";
            contact.style.borderRadius = "5px";

            getMessages(getSelectedContactName());
            if(cancelContact){
                let message = {
                    "operation": "removeContact",
                    "username": getStoredValue("username"),
                    "receiver": "server",
                    "data": getSelectedContactName(),
                    "date": new Date().toISOString()
                    }
                sendToServer(JSON.stringify(message));
                reloadContacts();
                return;
                }
                message = {
                    "operation": "getDraft",
                    "username": getStoredValue("username"),
                    "receiver": getSelectedContactName(),
                    "data": "",
                    "date": new Date().toISOString()
                }
                sendToServer(JSON.stringify(message));
            }
            document.getElementById("contactList").appendChild(contact);
            break;
        case "checkLoginID":
            if(data == "true"){
                window.location.pathname = 'Client/chat.html';
            }else{
                window.location.pathname = 'Client/index.html';
            }
        case "draft":
            if(receiver == getSelectedContactName()){
                document.getElementById("message-input").value = data;
            }
            break;
        default:
            console.log(data);
    }
});