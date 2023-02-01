import React from "react";
import "./css/Contacts.css";
import Manager from "../instructions/Manager";
import Contact from "../components/Contact";

class Contacts extends React.Component {
  constructor(props) {
    super(props);
    this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
    this.contacts = [];
  }

  render() {
    return (
        <>
            <div id="contacts">
                {this.contacts}
            </div>
        </>
    );
  }

  onOpen = (event) => {
    console.log("Connection opened");
    this.requestContacts();
  }

  onClose = (event) => {
    console.log("Connection closed");
  }

    requestContacts = () => {
        let message = {
            "operation": "getContacts",
            "username": this.props.myUsername,
            "receiver": "server",
            "data": "",
            "date" : new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    addContact = (username, pic) => {
        const { setSelectedContact } = this.props;
        console.log("Adding contact: " + username);
        let contact = <Contact username={username} pic={pic} key={username} setSelectedContact={setSelectedContact}/>;
        this.contacts.push(contact);
        this.setState({ contacts: this.contacts });
    }


    messageArrived = (event) => {
        let operation = JSON.parse(event.data).operation;
        let username = JSON.parse(event.data).username;
        let receiver = JSON.parse(event.data).receiver;
        let data = JSON.parse(event.data).data;

        switch (operation) {
            case "contact":
                console.log("Adding contact: " + username);
                this.addContact(username, data);
                break;
            default:
                console.log("Unknown operation");
                break;
        }
    }
}

export default Contacts;