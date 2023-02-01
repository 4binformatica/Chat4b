import React from "react";
import Manager from "../instructions/Manager";

class ContactInfo extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
        this.contactPic = "";
        this.contactBio = "";
        this.lastContact = this.props.selectedContact;
    }
    render() {
        if(this.lastContact !== this.props.selectedContact){
            this.contactPic = "";
            this.contactBio = "";
            this.lastContact = this.props.selectedContact;
            this.getContactPic(this.props.selectedContact);
            this.getBio(this.props.selectedContact);
        }
        return (
        <>
            <div className="contact-info">
                <img src={this.contactPic } alt="Profile Picture" className="profile-picture" />
                <h2>{this.props.selectedContact}</h2>
                <p>{this.contactBio}</p>
            </div>
        </>
        );
    }

    onOpen = () => {
        console.log("onOpen");
        this.getContactPic(this.props.selectedContact);
        this.getBio(this.props.selectedContact);
    }

    onClose = () => {
        console.log("onClose");
    }

    getContactPic = (username) => {
        let message = {
            "operation": "getProfilePic",
            "username": this.props.selectedContact,
            "receiver": username,
            "data": "",
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    getBio = (username) => {
        let message = {
            "operation": "getBio",
            "username": "",
            "receiver": username,
            "data": this.props.selectedContact,
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    messageArrived = (event) => {
        let operation = JSON.parse(event.data).operation;
        let username = JSON.parse(event.data).username;
        let receiver = JSON.parse(event.data).receiver;
        let data = JSON.parse(event.data).data;

        switch(operation){
            case "profilePic":
                console.log("Profile pic received from: " + username);
                this.contactPic = data;
                this.forceUpdate();
                break;
            case "bio":
                console.log("Contact bio received from: " + data);
                this.contactBio = data;
                this.forceUpdate();
                break;
            default:
                console.log("Unknown operation: " + operation);
                break;
        }
    }
}

export default ContactInfo;