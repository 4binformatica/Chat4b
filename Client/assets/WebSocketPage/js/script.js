var type = new TypeIt('#element', { cursor: false});
addCommand(type,400, 55,`<p class='console'> access main program</p>`);
addCommand(type,1300,65,`<p class='console'> access main security</p>`);
addCommand(type,1000,45,`<p class='console'> access security</p>`);
addCommand(type,1000,45,`<p class='console'> access main security grid</p>`, true);

magicWord(type);

setTimeout(() => {document.getElementsByClassName("denied")[0].removeAttribute("hidden"); document.getElementById("unmuteButton").removeAttribute("hidden");},12000);



function addCommand(type, pause, speed, command, last) {
  const basicResponse = 'access: PERMISSION DENIED.';
  let response = last ? basicResponse + '....and....' : basicResponse;
  type.pause(pause)
      .options({speed: speed})
      .type(command)
      .pause(200)
      .options({speed: 0})
      .type(response)
      .break();
}

function magicWord(type) {
  type.pause(1500);
  for (var index=0;index<40;index++) {
    type.pause(4)
      .options({speed: 0})
      .type(`YOU DIDN'T SAY THE MAGIC WORD!`)
      .break();
  }
}

const sound = document.getElementById('player');

sound.addEventListener('play', (event) => {
    document.getElementById("unmuteButton").hidden = true;
});

sound.addEventListener('pause', (event) => {
    document.getElementById("unmuteButton").hidden = false;
});

function playSound() {
    document.getElementById("player").play();
  }