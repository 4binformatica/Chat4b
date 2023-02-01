import React from 'react';
import Manager from '../instructions/Manager';

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
    }
    render() {
      return (
        <div>
            <h1>Register</h1>
            <input id="username" type="text" className="user" placeholder="Your username..."></input>
            <br></br>
            <input id="mail" type="text" className="mail" placeholder="Your mail..."></input>
            <br></br>
            <input id="password" type="password" className="pass" placeholder="Your password..."></input>
            <br></br>
            <input id="password2" type="password" className="pass" placeholder="Repeat your password..."></input>
            <button id="register" type="submit" className="register" onClick={this.registerButtonPressed}>Register</button>
        </div>
      );
    }

    registerButtonPressed = () => {
        let username = document.getElementById("username").value;
        let mail = document.getElementById("mail").value;
        let password = document.getElementById("password").value;
        let password2 = document.getElementById("password2").value;
        if(!mail.includes("@") || !mail.includes(".")){
            alert("The mail is not valid");
        }
        else if(password.length < 8){
            alert("The password is not valid");
        }
        else if(password != password2){
            alert("The passwords are not the same");
        }
        let message = {
            "operation": "register",
            "username": username,
            "receiver": mail,
            "data": password,
            "date": new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    onOpen = (event) => {
        console.log("Connection established");
    }

    messageArrived = (event) => {
    console.log(event.data);
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation){
        case "register":
            if(data == "success"){
                alert("Registration successful, please check your mail for the verification code");
                this.manager.storeValue("username", username);
                this.props.setCurrentComponent("login");
            }else{
                alert(data);
            }
            break;
        case "loginID":
            this.manager.deleteStoredValue("loginID");
            this.manager.storeValue("loginID", data);
            this.manager.storeValue(document.getElementById("username1").value, username);
            break;
        default:
            console.log(data);
        }
    }

    onClose = (event) => {
        console.log("Connection closed");
    }


    

    
    
  }

export default Register;
  