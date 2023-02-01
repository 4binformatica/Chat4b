import React from 'react';
import Chat from '../components/Chat.js';
import Settings from '../components/Settings.js';
import { createStore } from 'state-pool';

const store = createStore();
store.setState("currentWindow", "chat");
store.setState("selectedContact", null);
store.setState("myUsername", null);
store.setState("selectedSection", null);

class Window extends React.Component{
    constructor(props){
        super(props);
        this.onStart();
    }
    render(){
        
            switch(this.currentWindow){
                default:
                    return(
                        <Chat store={store}/>
                    );

            }
    };

    onStart(){
        this.currentWindow = store.getState("currentWindow");
        this.selectedContact = store.getState("selectedContact");
        this.myUsername = store.getState("myUsername");
        this.selectedSection = store.getState("selectedSection");
        this.setCurrentWindow = store.setState("currentWindow");
    }

}

export default Window;