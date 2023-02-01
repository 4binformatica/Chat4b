import React from 'react';
import './css/Settings.css';
import Manager from '../instructions/Manager';

class Settings extends React.Component {
    constructor(props){
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
        this.myPic = "";
        this.myBio = "";
    }
    render() {
        return (
            <>
                <div className="settings-container">
                    <img src={this.myPic} alt="Profile Picture" className="profile-picture" />
                    <h2>{this.props.myUsername}</h2>
                    <p>{this.myBio}</p>
                </div>
            </>
        );
    }

    onOpen = (event) => {
        console.log('onOpen');
        this.getContactPic(this.props.myUsername);
        this.getBio(this.props.myUsername);
    }

    onClose = (event) => {
        console.log('onClose');
    }

    getContactPic = (username) => {
        let message = {
            "operation": "getProfilePic",
            "username": this.props.myUsername,
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
            "data": this.props.myUsername,
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    messageArrived = (event) => {
        let operation = JSON.parse(event.data).operation;
        let username = JSON.parse(event.data).username;
        let receiver = JSON.parse(event.data).receiver;
        let data = JSON.parse(event.data).data;

        switch (operation) {
            case "getProfilePic":
                this.myPic = data;
                this.forceUpdate();
                break;
            case "getBio":
                this.myBio = data;
                this.forceUpdate();
                break;
            default:
                console.log("Unknown operation");
                break;
        }
    }
}

export default Settings;