import React from 'react';
import Chat from '../components/Chat.js';
import Settings from '../components/Settings.js';


class Window extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            currentWindow: "chat",
            selectedSelection: "all",
            selectedContact: null,
            myUsername: "kap",

          };
    }
    render(){
            switch(this.state.currentWindow){
                case "chat":
                    return(
                        <Chat currentWindow={this.state.currentWindow} setCurrentWindow={this.setCurrentWindow} selectedSelection={this.state.selectedSelection} setSelectedSelection={this.setSelectedSelection} selectedContact={this.state.selectedContact} setSelectedContact={this.setSelectedContact} myUsername={this.state.myUsername} setMyUsername={this.setMyUsername} />
                    );
                case "settings":
                    return(
                        <Settings currentWindow={this.state.currentWindow} setCurrentWindow={this.setCurrentWindow} />
                    );
                default:
                    return(
                        <Chat currentWindow={this.state.currentWindow} setCurrentWindow={this.setCurrentWindow} selectedSelection={this.state.selectedSelection} setSelectedSelection={this.setSelectedSelection} />
                    );

            }
    };

    setCurrentWindow = (newWindow) => {
        this.setState({currentWindow: newWindow});
      }

    setSelectedSelection = (newSelection) => {
        console.log("new selection: " + newSelection);
        this.setState({selectedSelection: newSelection});
    }

    setSelectedContact = (newContact) => {
        this.setState({selectedContact: newContact});
    }

    setMyUsername = (newUsername) => {
        this.setState({myUsername: newUsername});
    }


      

}

export default Window;