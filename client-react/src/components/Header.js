import React from 'react';
import './css/Header.css';

class Header extends React.Component {
    constructor(props){
        super(props);
    }
  render() {
    return (
        <>
            <img src="" alt="Discord Logo" className="logo"/> 
            <button className={this.props.currentWindow === 'chat' ? 'button selected' : 'button not-selected'} onClick={this.chatButtonClicked}>Chat</button>
            <button className={this.props.currentWindow === 'settings' ? 'button selected' : 'button not-selected'} onClick={this.settingsButtonClicked}>Settings</button>
        </>
    );
  }

    chatButtonClicked = () => {
        this.props.setCurrentWindow('chat');
    }

    settingsButtonClicked = () => {
        this.props.setCurrentWindow('settings');
    }


    
  
}

export default Header;