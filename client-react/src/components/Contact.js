import React from "react";
import "./css/Contact.css";

class Contact extends React.Component {
    constructor(props) {
        super(props);
        this.pic = this.props.pic;
    }
    render() {
        return (
        <div className="contact" onClick={this.contactClicked}>
            <img src={this.pic} alt="Profile Picture" className="contact-picture" />
            <div className="contact-info">
                <h2>{this.props.username}</h2>
                <p>{this.props.status}</p>
            </div>
        </div>
        );
    }

    contactClicked = () => {
        console.log("Contact clicked: " + this.props.username);
        this.props.setSelectedContact(this.props.username);
    }
}

export default Contact;