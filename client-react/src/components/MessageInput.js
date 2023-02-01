import React from "react";
import "./css/MessageInput.css";
import Manager from "../instructions/Manager";

class MessageInput extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
    }
    render() {
        return (
        <>
            <input
            id = "message-input"
            className="message-input"
            type="text"
            placeholder="Type a message...."
            onChange={this.onMessageChange}
            />
            <button
            className="send-button"
            onClick={this.sendMessage}
            >
            Send
            </button>
        </>
        );
    }

    onMessageChange = () => {
        let message = {
            operation: "saveDraft",
            username: this.props.myUsername,
            receiver: this.props.selectedContact,
            data: document.getElementById("message-input").value,
            date: new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    getDraft = () => {
        let message = {
            operation: "getDraft",
            username: this.props.myUsername,
            receiver: this.props.selectedContact,
            data: "",
            date: new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    sendMessage = () => {
        let message = {
            operation: "message",
            username: this.props.myUsername,
            receiver: this.props.selectedContact,
            data: document.getElementById("message-input").value,
            date: new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
        document.getElementById("message-input").value = "";
    }

    onOpen = (event) => {
        console.log("Connection opened");
    }

    onClose = (event) => {
        console.log("Connection closed");
    }

    messageArrived = (event) => {
        let operation = JSON.parse(event.data).operation;
        let username = JSON.parse(event.data).username;
        let receiver = JSON.parse(event.data).receiver;
        let data = JSON.parse(event.data).data;

        switch (operation) {
            case "draft":
                document.getElementById("message-input").value = data;
                break;
            default:
                console.log("Unknown operation: " + operation);
                break;
        }
    }
}

export default MessageInput;