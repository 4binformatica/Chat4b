import React from 'react';
import Manager from '../instructions/Manager';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.manager = new Manager();
    }
    render() {
      return (
        <div>
            <h1>Login</h1>
            <input id="username" type="text" className="user" placeholder="Your username..."></input>
            <br></br>
            <input id="password" type="password" className="pass" placeholder="Your password..."></input>
            <br></br>
            <button id="login" type="submit" className="login">Login</button>
        </div>
      );
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


    

    
    
  }

export default Login;
  