

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
        } 
        //if it's a html code like &#44; then it will be replaced with a comma
        else if(str.charAt(i) === '&' && str.charAt(i + 1) === '#'){
            let code = "";
            let j = i + 2;
            while(str.charAt(j) != ';'){
                code += str.charAt(j);
                j++;
            }
            element.innerText += String.fromCharCode(code);
            i = j;
        }
        else{
            element.innerText += str.charAt(i);
        }
    }
    await sleep(cancelTime);
    while(element.innerText.length > 0){
        await sleep(Math.floor(Math.random() * (300 - 60) + 60));
        element.innerText =  element.innerText.slice(0, -1)
    }
}

let writeArray = async (array, element, startTime, endTime) => {
    let str = array[Math.floor(Math.random() * array.length)];
    await writingEffect(str, element, startTime, endTime);
    writeArray(array, element, startTime, endTime);
}

let fromStringToArray = (str) => {
    str = str.replace("[", "");
    str = str.replace("]", "");

    let array = str.split(",");
    for(let i = 0; i < array.length; i++){
        array[i] = array[i].replace("\"", "");
        array[i] = array[i].replace("\"", "");
         //remove initial spaces and last space if there is one
        if(array[i].charAt(0) === ' '){
            array[i] = array[i].slice(1);
        }
        if(array[i].charAt(array[i].length - 1) === ' '){
            array[i] = array[i].slice(0, -1);
        }


    }
   
    return array;
}

async function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

