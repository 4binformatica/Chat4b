import React from 'react';
import './css/Header.css';

class Header extends React.Component {
    constructor(props){
        super(props);
    }
  render() {
    return (
        <>
        <div className='header'>
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Apple_logo_grey.svg/1724px-Apple_logo_grey.svg.png" alt="Discord Logo" className="logo"/> 
            <button className={this.props.currentWindow === 'chat' ? 'button selected' : 'button not-selected'} onClick={this.chatButtonClicked}>Chat</button>
            <button className={this.props.currentWindow === 'settings' ? 'button selected' : 'button not-selected'} onClick={this.settingsButtonClicked}>Settings</button>
        </div>
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