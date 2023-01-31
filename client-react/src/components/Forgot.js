import React from 'react';
import Manager from '../instructions/Manager';

class Forgot extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
        this.check = "checkMail";
        this.mail = "";
        this.code = "";
    }

    render() {
      if(this.check == "checkMail"){
        console.log("checkMail");
        return (
            <div>
                <h1>Forgot password</h1>
                <input id="mail" type="text" className="user" placeholder="Your mail..."></input>
                <br></br>
                <button id="forgot" type="submit" className="forgot" onClick={this.checkMailButton}>Send verification code</button>
            </div>
        );}
        else if(this.check == "checkCode"){
        console.log("checkCode");
        return (
            <div>
                <h1>Forgot password</h1>
                <input id="code" type="text" className="verify" placeholder="Your verification code..."></input>
                <br></br>
                <button id="forgot" type="submit" className="forgot" onClick={this.checkCodeButton}>Check verification code</button>
            </div>
        );}
        else if(this.check == "checkPassword"){
        console.log("checkPassword");
        return (
            <div>
                <h1>Forgot password</h1>
                <input id="password" type="password" className="pass" placeholder="Your new password..."></input>
                <br></br>
                <input id="passwordverify" type="password" className="pass" placeholder="Confirm your new password..."></input>
                <br></br>
                <button id="forgot" type="submit" className="forgot" onClick={this.checkPasswordButton}>Change password</button>
            </div>
        );}
    }

    checkMailButton = () => {
        let message = {
            "operation": "forgotPassword",
            "username": document.getElementById("mail").value,
            "receiver": "server",
            "data": document.getElementById("mail").value,
            "date": new Date().toISOString()
        }
        this.mail = document.getElementById("mail").value;
        this.manager.sendToServer(JSON.stringify(message));
        alert("Check your mail for the code");
    }

    checkCodeButton = () => {
        let message = {
            "operation": "checkForgotCode",
            "username": this.mail,
            "receiver": "server",
            "data": document.getElementById("code").value,
            "date": new Date().toISOString()
        }
        this.code = document.getElementById("code").value;
        this.manager.sendToServer(JSON.stringify(message));
    }
    
    checkPasswordButton = () => {
        //check if the passwords match
        if(document.getElementById("password").value != document.getElementById("passwordverify").value){
            alert("The passwords do not match");
            return;
        }
        let message = {
            "operation": "changeForgotPassword",
            "username": this.mail,
            "receiver": this.code,
            "data": document.getElementById("password").value,
            "date": new Date().toISOString()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    onOpen = (event) => {
        console.log("Connection established");
    }

    messageArrived = (event) => {
    let operation = JSON.parse(event.data).operation;
    let username = JSON.parse(event.data).username;
    let receiver = JSON.parse(event.data).receiver;
    let data = JSON.parse(event.data).data;

    switch(operation){
        case "forgotPassword":
            if(data == "success"){
                console.log("success");
                document.getElementById("mail").value = "";
                this.check = "checkCode";
                this.forceUpdate();
                document.getElementById("code").value = "";
            }else{
                alert(data);
            }
            break;
        case "checkForgotCode":
            if(data == "success"){
                this.check = "checkPassword";
                document.getElementById("code").value = "";
                this.forceUpdate();
                document.getElementById("password").value = "";
                document.getElementById("passwordverify").value = "";
            }else{
                alert(data);
            }
            break;
        case "changeForgotPassword":
            if(data == "success"){
                alert("Password changed!");
                this.props.setCurrentComponent("login");
            }else{
                alert(data);
            }
            break;
        default:
            console.log(data);
            break;
        }
    }

    onClose = (event) => {
        console.log("Connection closed");
    }
}

export default Forgot;
  