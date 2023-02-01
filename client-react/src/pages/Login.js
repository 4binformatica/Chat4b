import React from "react";
import './css/Login.css';
import Manager from '../instructions/Manager';

let needVerify = false;

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager(this.onOpen, this.messageArrived, this.onClose);
        this.needVerify = false;
    }

    render() {
      if(!this.needVerify) {
      return (
        <div>
            <h1>Login</h1>
            <input id="username" type="text" className="user" placeholder="Your username..."></input>
            <br></br>
            <input id="password" type="password" className="pass" placeholder="Your password..."></input>
            <br></br>
            <button id="login" type="submit" className="login" onClick={this.loginButtonPressed}>Login</button>
            <button id="register" type="submit" className="register" onClick={this.registerButtonPressed}>Register</button>
            <button id="forgot" type="submit" className="forgot" onClick={this.forgotButtonPressed}>Forgot password</button>
        </div>
      );
      } else {
        return (
          <div>
              <h1>Verify</h1>
              <input id="verify" type="text" className="verify" placeholder="Your verification code..."></input>
              <br></br>
              <button id="verify" type="submit" className="verify" onClick={this.verifyButtonPressed}>Verify</button>
          </div>
      );
        }
    }



    loginButtonPressed = () => {
        let username = document.getElementById("username").value;
        let password = document.getElementById("password").value;
        let message = {
            "operation": "login",
            "username": username,
            "receiver": "server",
            "data": password,
            "date": new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }

    verifyButtonPressed = () => {
        let verify = document.getElementById("verify").value;
        let message = {
            "operation": "checkVerificationCode",
            "username": this.manager.getStoredValue("username"),
            "receiver": "server",
            "data": verify,
            "date": new Date().getTime()
        }
        this.manager.sendToServer(JSON.stringify(message));
    }


    registerButtonPressed = () => {
      this.props.setCurrentComponent("register");
    }

    forgotButtonPressed = () => {
      this.props.setCurrentComponent("forgot");
    }






    onOpen = (event) => {
        console.log("Connection established");
    }

    messageArrived = (event) => {
      let operation = JSON.parse(event.data).operation;
      let username = JSON.parse(event.data).username;
      let receiver = JSON.parse(event.data).receiver;
      let data = JSON.parse(event.data).data;
  
      switch(operation) {
          case "loginID":
              this.manager.deleteStoredValue("loginID");
              this.manager.storeValue("loginID", data);
              break;
          case "login":
              if(data === "success") {
                  this.manager.storeValue("username", username);
  
                  window.location.pathname = 'Client/chat.html';
              } else {
                  alert("Login failed");
              }
              break;
          case "checkLoginID":
              if(data == "true"){
                  window.location.pathname = 'Client/chat.html';
              }else{
                  if(!window.location.pathname == 'Client/index.html')
                      window.location.pathname = 'Client/index.html';
              }
          case "checkVerificationCode":
                console.log(data);
                if(data == "success"){
                    alert("Verification successful");
                    
                    this.props.setCurrentComponent("chat");
                }else{
                    alert("Verification failed");
                }
                break;
          case "needVerification":
              this.needVerify = true;
              this.forceUpdate();
          default:
              console.log(data);
              
      }
    }

    onClose = (event) => {
        console.log("Connection closed");
    }


    

    
    
  }

export default Login;
  