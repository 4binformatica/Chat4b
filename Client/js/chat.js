let cancelMsg = false;
let cancelContact = false;

socket.onopen = function (event) {
    keepAlive();
    setInterval(keepAlive, 10000);
    getContacts();
    isLogged();
};

let isLogged = () => {
    if (getStoredValue("username") == null) {
        window.location.pathname = 'Client/index.html';
    }
}

/* 
    for the msg
    in data we have an object with the following structure:
    {
        "text": "text of the message",
        "img": "url of the image"
    }
*/

/* 
let uploadImage = () => {
    let file = document.getElementById("file").files[0];
    let reader = new FileReader();
    reader.readAsDataURL(file);
      
 */
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

let imageInput = async () => {
    const imageInput= document.getElementById('image-input');

    const image = imageInput.files[0];

    const link = await uploadImage(image);

    let message = document.getElementById("message-input").value;
    let receiver = getSelectedContactName();
    let messageObject = {
        "operation": "message",
        "username": getStoredValue("username"),
        "receiver": receiver,
        "data": {
            "text": message,
            "img": link
        },
        "date": new Date().toISOString()
    }
    sendToServer(JSON.stringify(messageObject));
    document.getElementById("message-input").value = ""
    document.getElementById("messageList").scrollTop = document.getElementById("messageList").scrollHeight;
    reloadMessages();
}


async function uploadImage(image) {
    const formData = new FormData();
    formData.append('image', image);
  
    const response = await fetch("https://api.imgur.com/3/image", {
        method: 'POST',
        body: formData
      });
  
    const json = await response.json();
    return json.data.link;
  }



  

let sendMessage = async () => {   
    let message = document.getElementById("message-input").value;
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
                var message = document.createElement('div');
                if(username == getStoredValue("username")){
                    message.className = "message-container sent";
                }else{
                    message.className = "message-container received";
                }
                message.innerHTML = data; 
                message.onclick = function() {
                    message.date = date;
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
        case "contact":
            var contact = document.createElement('div');
            contact.innerHTML = data; 
            contact.className = "contact-name";
            //add button to remove contact
            var removeButton = document.createElement('button');
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
            }
            document.getElementById("contactList").appendChild(contact);
            break;
        default:
            console.log(data);
    }
});