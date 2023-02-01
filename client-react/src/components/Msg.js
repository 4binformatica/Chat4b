import react from "react";
import "./css/Msg.css";

class Msg extends react.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
        // JSX
    
        <div className = {this.props.isOwn ? "message sent" : "message received"}>
         <h1>{this.props.username}</h1>
            <img src={this.props.imgSrc} alt="user image" />
            <p>{this.props.text}</p>
    
        </div>
    );
  }

}

export default Msg;