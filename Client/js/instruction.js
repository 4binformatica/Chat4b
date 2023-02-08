

let sendToServer = (message) => {
    socket.send(message);
}

let writingEffect = async (str, element, startTime, cancelTime) => {
    await sleep(startTime);
    for(let i = 0; i < str.length; i++){
        await sleep(Math.floor(Math.random() * (300 - 60) + 60));
        element.innerText =  element.innerText + str[i];
    }
    await sleep(cancelTime);
    for(let i = 0; i < str.length; i++){
        await sleep(Math.floor(Math.random() * (300 - 60) + 60));
        element.innerText =  element.innerText.slice(0, -1)
    }

    callback();
}

let writing = async (array, element) => {
    let str = array[Math.floor(Math.random() * array.length)];

    await writingEffect(str, element, 200, 1000);

    writing(array, element);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

