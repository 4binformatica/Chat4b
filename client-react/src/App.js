import React, { createContext} from "react";
import { createStore } from 'state-pool';
import Login from './pages/Login.js';
import Register from './pages/Register.js';
import Forgot from './pages/Forgot.js';
import Window from './pages/Window.js';

const store = createStore();
store.setState("currentComponent", "window");

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
    case "window":
      return (
        <div>
          <Window setCurrentComponent={setCurrentComponent}/>
        </div>
      );
    default:
      return (
        <div>
          <Login setCurrentComponent={setCurrentComponent}/>
        </div>
      );
      }

}

export default App;