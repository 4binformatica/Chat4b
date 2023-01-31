import React, { createContext} from "react";
import { createStore } from 'state-pool';
import Login from './components/Login.js';
import Register from './components/Register.js';
import Forgot from './components/Forgot.js';

const store = createStore();
store.setState("currentComponent", "login");

function App() {

  const [currentComponent, setCurrentComponent] = store.useState("currentComponent");


  switch (currentComponent) {
    case "login":
      return (
        <div>
          <Login setCurrentComponent={setCurrentComponent}/>
        </div>
      );
    case "register":
      return (
        <div>
          <Register setCurrentComponent={setCurrentComponent}/>
        </div>
      );
    case "forgot":
      return (
        <div>
          <Forgot setCurrentComponent={setCurrentComponent}/>
        </div>
      );
      /*case "chat":
        return (
          <div>
            <Chat setCurrentComponent={setCurrentComponent}/>
          </div>
        );
      case "settings":
        return (
          <div>
            <Settings setCurrentComponent={setCurrentComponent}/>
          </div>
        );*/
    default:
      return (
        <div>
          <Login setCurrentComponent={setCurrentComponent}/>
        </div>
      );
      }

}

export default App;