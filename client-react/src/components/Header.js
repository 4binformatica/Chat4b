import React from 'react';
import './css/Header.css';

class Header extends React.Component {
    constructor(props){
        super(props);
    }
  render() {
    return (
        <>
            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Adidas_Logo.svg/2560px-Adidas_Logo.svg.png" alt="Discord Logo" className="logo"/> 
            <button className={this.props.currentWindow === 'chat' ? 'button selected' : 'button not-selected'} onClick={() => this.props.setCurrentWindow('chat')}>Chat</button>
            <button className={this.props.currentWindow === 'settings' ? 'button selected' : 'button not-selected'} onClick={() => this.props.setCurrentWindow('settings')}>Settings</button>
        </>
    );
  }


    componentDidUpdate() {
        this.props.setState({
            currentWindow: this.state.currentWindow,
            selectedContact: this.state.selectedContact,
            myUsername: this.state.myUsername,
            selectedSection: this.state.selectedSection,
        });
    }

  
}

export default Header;