import React from 'react';
import Header from '../components/Header.js';
import { createStore } from 'state-pool';


class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.onStart();
    }
  render() {
    return (
      <div>
        <Header />
      </div>
    );
  }

  //when state changes, this function is called
    componentDidUpdate() {
        this.props.setState({
            currentWindow: this.state.currentWindow,
            selectedContact: this.state.selectedContact,
            myUsername: this.state.myUsername,
            selectedSection: this.state.selectedSection,
        });
    }

    onStart(){
      this.currentWindow = this.props.store.getState("currentWindow");
      this.selectedContact = this.props.store.getState("selectedContact");
      this.myUsername = this.props.store.getState("myUsername");
      this.selectedSection = this.props.store.getState("selectedSection");
    }
}

export default Chat;