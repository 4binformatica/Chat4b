

let sendToServer = (message) => {
    socket.send(message);
}

let writingEffect = async (str, element, startTime, cancelTime) => {
    console.log(str)
    await sleep(startTime);
    for(let i = 0; i < str.length; i++){
        await sleep(Math.floor(Math.random() * (300 - 60) + 60));
        if(str.charAt(i) === ' '){
            element.innerText += '\u00A0';
        }else{
            element.innerText += str.charAt(i);
        }
    }
    await sleep(cancelTime);
    for(let i = 0; i < str.length; i++){
        await sleep(Math.floor(Math.random() * (300 - 60) + 60));
        element.innerText =  element.innerText.slice(0, -1)
    }
}

let writeArray = async (array, element, startTime, endTime) => {
    let str = array[Math.floor(Math.random() * array.length)];
    await writingEffect(str, element, startTime, endTime);
    writeArray(array, element, startTime, endTime);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

