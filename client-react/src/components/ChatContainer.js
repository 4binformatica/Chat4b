import React from "react";
import "./css/ChatContainer.css";
import Manager from "../instructions/Manager";
import Msg from "../components/Msg";


class ChatContainer extends React.Component {
    constructor(props){
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
        this.messages = [];
        this.selectedContactProfilePic = "";
        this.lastSelectedContact = this.props.selectedContact;
    }
    render() {
        if(this.lastSelectedContact !== this.props.selectedContact){
            this.messages = [];
            this.lastSelectedContact = this.props.selectedContact;
            this.getContactPic(this.props.selectedContact);
            this.getMessages();
        }

        return (
            <>
                <h1>ChatContainer</h1>
                <div className="chat-container">
                    {this.messages}
                </div>
            </>
        );
    }

    onOpen = (event) => {
        console.log('onOpen');
        this.getContactPic(this.props.selectedContact);
        this.getMessages();
    }

    onClose = (event) => {
        console.log('onClose');
    }

    getContactPic = (username) => {
        let pic = "";
        let message = {
            "operation": "getProfilePic",
            "username": this.props.selectedContact,
            "receiver": username,
            "data": "",
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    getMessages = () => {
        let message = {
            "operation": "getMessages",
            "username": this.props.myUsername,
            "receiver": "",
            "data": this.props.selectedContact,
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    addMessage = (username, text, time) => {
        console.log("Adding message: " + text);
        this.isOwn = (username === this.props.myUsername);
        let message = <Msg username={username} imgSrc={this.selectedContactProfilePic} text={text} time={time} isOwn={this.isOwn} />;
        this.messages.push(message);
        this.setState({ messages: this.messages });
    }


    messageArrived = (event) => {
        console.log('messageArrived');
        let operation = JSON.parse(event.data).operation;
        let username = JSON.parse(event.data).username;
        let receiver = JSON.parse(event.data).receiver;
        let data = JSON.parse(event.data).data;
        let date = JSON.parse(event.data).date;

        switch (operation) {
            case "message":
                console.log("Message received from: " + username);
                this.addMessage(username, data, date);
                break;
            case "profilePic":
                console.log("Profile pic received from: " + username);
                this.selectedContactProfilePic = data;
                break;
            default:
                console.log("Unknown operation");
                break;
        }
    }
}

export default ChatContainer;