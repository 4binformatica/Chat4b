import "./css/Msg.css";

function Msg(props) {


    const { username, imgSrc, text, time, isOwn} = props.message;
    const timeString = new Date(time).toLocaleTimeString([], {
        hour: '2-digit',
        minute: '2-digit',
    });
    const dateString = new Date(time).toLocaleDateString([], {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    });
    

    return (
        // JSX

        <div className = {isOwn ? "message sent" : "message received"}>
         <h1>{username}</h1>
            <img src={imgSrc} alt="user image" />
            <p>{text}</p>
            <p>{timeString}</p>
            <p>{dateString}</p>

        </div>
    );
    
}

export default Msg;