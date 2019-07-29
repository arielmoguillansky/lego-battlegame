document.getElementById("lclip").style.display = 'none';
document.getElementById("wclip").style.display = 'none';
document.getElementById("tclip").style.display = 'none';
document.getElementById("shoot_btn").style.display = 'none';
var gamesData;
var actualPlayer;
var opponent;
var actualPlayerSide;

var defShipsArr = [{shipType: 'IRONMAN', locations:['A1','A2','A3','A4','A5']},
{shipType: 'CPAMERICA', locations:['C1','C2','C3','C4']},
{shipType: 'DRSTRANGE', locations:['E1','E2','E3']},
{shipType: 'SPIDERMAN', locations:['G1','G2','G3']},
{shipType: 'ROCKET', locations:['I1','I2']}]

//get the gamePlayer id from the query parameter on the url
var gpId = paramObj(window.location.href).gp


fetch("/api/game_view/"+gpId)
.then(function(response){
	return response.json()
})
.then(function(json){

  gamesData = json;
  var st = gamesData.gameState;
    swapStyleSheet();
    WhoIsWho();
    //the loadGrid() function loads the grid for ships with gridstack.js
  //first check if the player has already placed ships
  if(st === 'place ships'){
      //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
      loadGrid(false);
      createGrid(11, $(".grid-salvoes"), 'salvoes');
      defaultShips();
      noTabAlert();

  } else if(st === 'wait opponent'){
      //if true, the grid is initialized in static mode, that is, the ships can't be moved
      createGrid(11, $(".grid-salvoes"), 'salvoes');//loads the grid for salvoes without gridstack.js
      loadGrid(true);
      noTabAlert();

  }else{
      createGrid(11, $(".grid-salvoes"), 'salvoes');//loads the grid for salvoes without gridstack.js
      loadGrid(true);
   //shows who is the player related to the gamePlayer and who is the opponent
      //setSalvoes(); loads the salvoes
      document.getElementById("start_game").style.display = 'none';
      document.getElementById("unableGrid").style.display = 'none';
      displayGameStats();
  }

}).then(function(){ showTeam();})
.catch(function(error){
	console.log(error)
})

function salvoFetch(){
    fetch("/api/game_view/"+gpId)
        .then(function(response){
            return response.json()
        })
        .then(function(json){

            gamesData = json;

            gameSt(gamesData.gameState);
        })
}

$(document).ready(setInterval(function(){salvoFetch();}, 5000));

function WhoIsWho(){
  for(let i = 0; i < gamesData.gamePlayers.length; i++){
    if(gamesData.gamePlayers[i].gpid == gpId){
      actualPlayer = gamesData.gamePlayers[i].Players
    }else{opponent = gamesData.gamePlayers[i].Players}
    }

  let logger = document.getElementById("logger");
  let userN =  document.getElementById("userInfo");
    userN.innerHTML = ` ${actualPlayer.user_name}`;


}

function paramObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });

  return obj;
}

function defaultShips(){

for(var i=0; i<defShipsArr.length;i++){
let shipType = (defShipsArr[i].shipType).toLowerCase()
        let x = +(defShipsArr[i].locations[0][1]) - 1 //the number of the first position belongs to the x axis. To match the framework structure beginning at 0, we must substract 1 from it
        let y = stringToInt(defShipsArr[i].locations[0][0].toUpperCase())//the letter of the first position belongs to y axis. In this case we must transform the string to a number, starting from 0.

var w = defShipsArr[i].locations.length;
var h = 1;
var orientation = "Horizontal";
grid.addWidget($('<div id="'+shipType+'"><div class="grid-stack-item-content '+shipType+orientation+'"></div><div/>'),
        x, y, w, h);

}
   rotateShips("ironman", 5)
        rotateShips("cpamerica", 4)
        rotateShips("drstrange",3)
        rotateShips("spiderman", 3)
        rotateShips("rocket",2)

}

//adds a listener to the ships, wich shoots its rotation when clicked
const rotateShips = function(shipType, cells){

        $(`#${shipType}`).click(function(){
        	if($(this).children().hasClass(`${shipType}Horizontal`)){
                                       if ((parseInt($(this).attr("data-gs-y")) + parseInt($(this).attr("data-gs-width")) < 11) &&
                                       grid.isAreaEmpty(parseInt($(this).attr("data-gs-x")),parseInt($(this).attr("data-gs-y"))+1,
                                       parseInt($(this).attr("data-gs-height")), parseInt($(this).attr("data-gs-width"))-1)){
                                       grid.resize($(this),1,cells);
                                       $(this).children().removeClass(`${shipType}Horizontal`);
                                       $(this).children().addClass(`${shipType}Vertical`);
                                       }

                                   }else{
                                       if((parseInt($(this).attr("data-gs-x")) + parseInt($(this).attr("data-gs-height")) <11) &&
                                       grid.isAreaEmpty(parseInt($(this).attr("data-gs-x"))+1,parseInt($(this).attr("data-gs-y")),
                                       parseInt($(this).attr("data-gs-height"))-1, parseInt($(this).attr("data-gs-width")))){
                                       grid.resize($(this),cells,1);
                                       $(this).children().addClass(`${shipType}Horizontal`);
                                       $(this).children().removeClass(`${shipType}Vertical`);
                                       }

                                   }
                                });

}

function shipFetch() {


  let shipArray = []
  let shipTypeArray = ["ironman", "cpamerica", "drstrange", "spiderman", "rocket"]


  for (let i = 0; i < shipTypeArray.length; i++) {
    let ship = document.querySelector("#" + shipTypeArray[i]);

    let width = ship.getAttribute("data-gs-width");
    let height = ship.getAttribute("data-gs-height");
    let x = parseInt(ship.getAttribute("data-gs-x"));
    let y = parseInt(ship.getAttribute("data-gs-y"));
    let locations = []

    if (height == 1) {
      for (let j = 1; j <= width; j++) {
        let currentX = x + j
        let yLetter = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.charAt(y);
        locations.push(yLetter + currentX);
      }
    } else if (width == 1) {
      for (let j = 0; j < height; j++) {
        let currentY = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.charAt(y + j);
        let xPlusOne = x + 1;
        locations.push(currentY + xPlusOne)
      }
    }
    shipArray.push({
      shipType: shipTypeArray[i].toUpperCase(),
      locations: locations
    })
  }


    fetch('/api/games/players/' + gpId + '/ships', {
      method: 'POST',
      body: JSON.stringify(shipArray),
      headers: {'Content-Type': 'application/json'},
      credentials: 'same-origin'
    })
        .then(function (response) {
          response.status
          if (response.status == 201) {

            console.log("Ships Fetched");
          } else {
            console.log("Invalid")
          }
        })
        .catch(function (error) {
          console.log(error);
        })

}


const start = document.getElementById('start_game_btn');
start.addEventListener('click', function(e){
  e.preventDefault();
    document.getElementById("start_game").style.display = 'none';
  grid = $('#grid').data('gridstack');
  grid.setStatic(true);
  shipFetch();
})

var counter = 0;
function salvoesClick(){

  const salvo_cell = document.getElementsByClassName('grid-cell');
  for(var i=0;i<salvo_cell.length;i++) {

    salvo_cell[i].addEventListener('click', function (e) {
      e.preventDefault();
      var cellId = e.target.id;
      var cellShot = document.getElementById(cellId);
      document.getElementById('shoot').classList.remove('hideEl');

      if(cellShot.classList.contains('smash')) {
        cellShot = document.getElementById(cellId).classList.remove('smash');
        counter --;
      }else if(!cellShot.classList.contains('smash') && counter<5){
         cellShot = document.getElementById(cellId).classList.add('smash');
         counter ++;
      }
    })
  }

}



function salvoesFetch() {
salvoLoc = [];
    let salvoes = document.querySelectorAll(".smash");
    for (let i = 0; i < salvoes.length; i++) {
        var salvoId = salvoes[i].id.slice(-2);
        let x = +(salvoId[1]) + 1;
        let y = intToString(parseInt(salvoId[0]));
        salvoLoc.push(y + x);
  }


  fetch('/api/games/players/' + gpId + '/salvoes', {
    method: 'POST',
    body: JSON.stringify(salvoLoc),
    headers: {'Content-Type': 'application/json'},
    credentials: 'same-origin'
  })
      .then(function (response) {
        response.status
        if (response.status == 201) {

          console.log("Salvoes Fetched");
        } else {
          console.log("Invalid")
        }
      }).then(function () {
      salvoFreez = document.querySelectorAll(".smash");
      for(var i=0;i<salvoFreez.length;i++) {
         salvoFreez[i].classList.add("smashFreez");
         salvoFreez[i].classList.remove("smash");
      }
  }).then(function(){counter = 0;})
      .catch(function (error) {
        console.log(error);
      })

}

 const shootSalvoBtn = document.getElementById('shoot_btn');
      shootSalvoBtn.addEventListener('click', function(e){
        e.preventDefault();

        salvoesFetch();
      })

const intToString = function(int){
    switch(int){
        case 0:
            return "A";
        case 1:
            return "B";
        case 2:
            return "C";
        case 3:
            return "D";
        case 4:
            return "E";
        case 5:
            return "F";
        case 6:
            return "G";
        case 7:
            return "H";
        case 8:
            return "I";
        case 9:
            return "J";
    }
}

const gameSt = function(state){
    switch(state){
        case 'place ships':
            document.getElementById("start_game").style.display = 'block';
            document.getElementById("unableGrid").style.display = 'block';
            document.getElementById("shoot_btn").style.display = 'none';
            document.getElementById("logger").innerHTML = "Wating opponent";
            document.getElementById("unableGrid").style.display = 'block';
            document.getElementById("waitText").innerText = 'Waiting Opponent';
            break;
        case 'wait opponent':
            document.getElementById("shoot_btn").style.display = 'none';
            document.getElementById("logger").innerHTML = "Wating opponent";
            document.getElementById("unableGrid").style.display = 'block';
            document.getElementById("waitText").innerText = 'Waiting Opponent';

            break;
        case 'wait opponent ships':
            document.getElementById("logger").innerHTML = "In battle";
            document.getElementById("shoot_btn").style.display = 'none';
            document.getElementById("unableGrid").style.display = 'block';
            document.getElementById("waitText").innerText = 'Opponent assembling';
            break;
        case "shoot":
            setSalvoes();
            salvoesClick();
            document.getElementById("logger").innerHTML = "In battle";
            document.getElementById("shoot_btn").style.display = 'block';
            document.getElementById("unableGrid").style.display = 'none';

            break;
            case "wait":
                getHits();
                document.getElementById("logger").innerHTML = "In battle";
            document.getElementById("shoot_btn").style.display = 'none';
                document.getElementById("unableGrid").style.display = 'block';
                document.getElementById("waitText").innerText = 'Opponent turn';

                break;
        case "win":
            myAudio.pause();
            document.getElementById("gameView").style.display = 'none';
            document.getElementById("wclip").style.display = 'block';
            document.getElementById("winClip").play();
            setTimeout(function(){
                document.getElementById("returnBtn").style.display = 'block';
            }, 12000);
            break;
        case "lose":
            myAudio.pause();
            document.getElementById("gameView").style.display = 'none';
            document.getElementById("lclip").style.display = 'block';
            document.getElementById("loseClip").play();
            setTimeout(function(){
                document.getElementById("returnBtn").style.display = 'block';
            }, 12000);
            break;
        case "tie":
            myAudio.pause();
            document.getElementById("gameView").style.display = 'none';
            document.getElementById("tclip").style.display = 'block';
            document.getElementById("tieClip").play();
            setTimeout(function(){
                document.getElementById("returnBtn").style.display = 'block';
            }, 7000);
            break;
    }
};

function swapStyleSheet(){
    for(let i = 0; i < gamesData.gamePlayers.length; i++){
        if(gamesData.gamePlayers[i].gpid == gpId){
           actualPlayerSide = gamesData.gamePlayers[i].side;
            document.getElementById('playerSide').innerHTML = 'Avenger';
            if(actualPlayerSide === 'VILLAIN'){
                document.getElementById('pagestyle').setAttribute('href', 'styles/villain.css');
                document.getElementById('picvil').src='/web/styles/heroes/picvil.png';
                document.getElementById('playerSide').innerHTML = 'Villain';
            }
    }
    }
}
function showTeam() {

    for (let i = 0; i < gamesData.gamePlayers.length; i++) {
        if (gamesData.gamePlayers[i].gpid == gpId) {

            var teamName = " Team " + gamesData.gamePlayers[i].side;
            document.getElementById("teaminfo").innerHTML = teamName;
        }else{
            var teamName = " Team " + gamesData.gamePlayers[i].side;
            document.getElementById("opteaminfo").innerHTML = teamName;
        }
    }
}

function returnG(){
    window.location.href = "/web/games.html";
}

var myAudio = document.getElementById("music");
var isPlaying = false;

function togglePlay() {
    if (isPlaying) {
        myAudio.pause();
    } else {
        myAudio.play();
        myAudio.volume = 0.5;
    }
};
myAudio.onplaying = function() {
    isPlaying = true;
};
myAudio.onpause = function() {
    isPlaying = false;
};
function change (iconID){
    if(document.getElementById(iconID).className=="fas fa-volume-up"){
        document.getElementById(iconID).className = "fas fa-volume-mute";
    }else{
        document.getElementById(iconID).className = "fas fa-volume-up";
    }
}

$("#close-sidebar").click(function() {
    $(".page-wrapper").removeClass("toggled");
});
$("#show-sidebar").click(function() {
    $(".page-wrapper").addClass("toggled");
});

function displayGameStats() {

    for(let i = 0; i < gamesData.gamePlayers.length; i++){
        if(gamesData.gamePlayers[i].gpid == gpId){
            actualPlayer = gamesData.gamePlayers[i].Players
        }else{opponent = gamesData.gamePlayers[i].Players}
    }

    document.getElementById('gameStatTab').innerText = "Vs." + opponent;
   /* var scoreTableHeader={
        position:"Vs. " + opponent ,

    };
    var score_data="<thead><th>" + scoreTableHeader.position + "</th></thead>";

     score_data += '<tr><td>' + 'si' + '</td><td>' + player.user + '</td><td>' + player.score + '</td><td>'
        + player.win + '</td><td>' + player.tie + '</td><td>' + player.lost + '</td></tr>';
    document.getElementById("gameStatTab").innerHTML = score_data;*/

}

function noTabAlert(){
    let logger = document.getElementById("gameTab")
    let p1 = document.createElement('SPAN')
    p1.innerHTML = 'No stats to display'
    logger.appendChild(p1)
}
function logout(evt) {
    evt.preventDefault();
    $.post("/api/logout").done(function() {
        //window.location ="games.html" -- opcion 2 que recarga la pagina una vez que se sale. Cuando se carga, empieza con el fetch del principio
        window.location.href = "/web/login.html"
        console.log("logged out"); })
}