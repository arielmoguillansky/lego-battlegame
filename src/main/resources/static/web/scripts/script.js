

function userFetch(){
fetch("/api/games", {mode: "no-cors"})
    .then(function(response){

    return response.json()})

    .then(function(json){
         player_data=[];
         games_data=[];

        games_data = json.games;
        player_data = json.player;

        changeDateFormat();
        getPlayers();

        displayGames();
        getScores();
        displayLeaderboard();
        theadClass();
        showUser();

        createGameBtn();

    }).then(function(){joinGameBtn();})
    .catch(function(error) {
      console.log('Hubo un problema con la petición Fetch:' + error.message);
    });
}

function showUser(){

if(player_data !== "Guest"){

            var userShow = document.getElementById("user_data").classList.remove("hideEl");
            var showUserName = player_data.user_name;
            document.getElementById("userInfo").innerHTML = showUserName;
            document.getElementById("cbtn").classList.remove("hideEl");
}
}



function loginFetch(){

 var body = "username=" + document.querySelector("#username").value + "&password=" + document.querySelector("#password").value;
 //body: JSON.stringify(data), headers:{'Content-Type': 'application/json'}
    fetch('/api/login', {method: 'POST', body: body, headers: {'Content-Type': 'application/x-www-form-urlencoded'}, credentials: 'same-origin'})
        .then(function(response){
            response.status
        if (response.status == 200){
            window.location.href="/web/games.html";
            console.log("logged in");
        }else{
            showAlert();
        } })

        .catch(function(error){
        console.log(error);
        })
}

function signUpFetch(){

    var body = "playerName=" + document.querySelector("#firstName").value + "&username=" + document.querySelector("#newusername").value + "&password=" + document.querySelector("#newpassword").value;
    //body: JSON.stringify(data), headers:{'Content-Type': 'application/json'}
    fetch('/api/players', {method: 'POST', body: body, headers: {'Content-Type': 'application/x-www-form-urlencoded'}, credentials: 'same-origin'})
        .then(function(response){
            response.status
            if (response.status == 201){
                showRegister();
                console.log("sign up correct");
            }else{
                alert("Something went wrong, try again");
            } })
        .catch(function(error){
            console.log(error);
        })
}

function hideTableGames(){
    if(player_data !== 'Guest') {
        var element = document.getElementById("gameInfo").childNodes;
        var elementnum = document.getElementById("gameInfo").childElementCount;
        if(elementnum >2) {
            element[0].remove();
            element[0].remove();
        }
    }
}

function logout(evt) {
  evt.preventDefault();
 $.post("/api/logout").done(function() {
 //window.location ="games.html" -- opcion 2 que recarga la pagina una vez que se sale. Cuando se carga, empieza con el fetch del principio
window.location.href = "/web/login.html"
 console.log("logged out"); })
}

function createGameBtn() {
    const createGame = document.getElementById('cbtn');
    createGame.addEventListener('click', function (e) {
        e.preventDefault();
        let dataGame = e.target.dataset.gamebtn
        //gameCreationFetch();
        document.getElementById("out").classList.remove("hideEl");
        document.getElementById("villimg").dataset.gamebtn = dataGame;
        document.getElementById("heroimg").dataset.gamebtn = dataGame;
        $(".page-wrapper").removeClass("toggled");
            })
}

function gameCreationFetch(f){
var body = "side=" + f;
 //body: JSON.stringify(data), headers:{'Content-Type': 'application/json'}
    fetch('/api/games', {method: 'POST', body: body, headers:{'Content-Type': 'application/x-www-form-urlencoded'}, credentials: 'same-origin'})
        .then(function(response){
            response.status
        if (response.status == 201){
            console.log("game created");
            return response.json();
        }else{
            alert("Game cannot be created")
        } }).then(function(json){
            window.location.href = 'game_view.html?gp=' + json.gpid;
        })
        .catch(function(error){
        console.log(error);
        })
}

function joinGameFetch(a,b){
    var body = "side=" + a;

    fetch('/api/game/' + b + '/players', {method: 'POST', body:body, headers: {'Content-Type': 'application/x-www-form-urlencoded'}, credentials: 'same-origin'})
        .then(function(response){
            response.status
            if (response.status == 201){
                console.log("game Player created");
                return response.json();
            }else{
                alert("Game cannot be joined")
            } }).then(function(json){
        window.location.href = 'game_view.html?gp=' + json.gpid;
    })
        .catch(function(error){
            console.log(error);
        })
}

function joinGameBtn(){
    const createGamePlayer = document.getElementsByClassName('joinBtn');
    for(var i=0;i<createGamePlayer.length;i++){
    createGamePlayer[i].addEventListener('click', function (e) {
        e.preventDefault();
        let dataGame = e.target.dataset.gamebtn;
        var gameid = e.target.dataset.game;
        document.getElementById("out").classList.remove("hideEl");
        document.getElementById("villimg").dataset.gamebtn = dataGame
        document.getElementById("villimg").dataset.gameids = gameid
        document.getElementById("heroimg").dataset.gamebtn = dataGame
        document.getElementById("heroimg").dataset.gameids = gameid

    })

}
}

function getPlayers() {
    dataArr = [];
    for (var i = 0; i < games_data.length; i++) {
        for (var j = 0; j < games_data[i].gamePlayers.length; j++) {
            if (games_data[i].gamePlayers[j].score !== null) {
                var obj = new Object;
                obj.id = games_data[i].id;					//Nuevo objeto 				//Asigno nuevos keys con los valores de las keys padres
                obj.created = games_data[i].created;
                obj.user = games_data[i].gamePlayers[j].Players.user;
                obj.user_id = games_data[i].gamePlayers[j].Players.id;
                obj.score = games_data[i].gamePlayers[j].score;
                obj.win=0;
                obj.tie=0;
                obj.lost=0;
                dataArr.push(obj);

            }
        }

    }
}

function getScores(){
     newArr = [];

    for(var x in dataArr) {
        var newScore = dataArr[x].score;
        for(var y in newArr) {
            var found = false;
            if(dataArr[x].user_id == newArr[y].user_id) {

                found = true;

                switch (true) {
                    case newScore === 3:
                        newArr[y].win++;
                        break;
                    case newScore === 1:
                        newArr[y].tie++;
                        break;
                    case newScore === 0:
                        newArr[y].lost++;
                        break;
                }
                newArr[y].score += dataArr[x].score;
                break;
            }
        }
        if(!found) {
            newArr.push(dataArr[x]);
            switch (true) {
                case newScore === 3:
                    dataArr[x].win++;
                    break;
                case newScore === 1:
                    dataArr[x].tie++;
                    break;
                case newScore === 0:
                    dataArr[x].lost++;
                    break;
            }
        }
    }


}

function displayLeaderboard() {

       var scoreTableHeader={
           position:"Position",
           player:"Player",
           totalScore:"Total Score",
           winGame:"Win",
           tieGame:"Tie",
           lostGame:"Lost",
       };
       var score_data="<thead><th>" + scoreTableHeader.position + "</th><th>" + scoreTableHeader.player + "</th><th>"+ scoreTableHeader.totalScore+ "</th><th>"
           + scoreTableHeader.winGame + "</th><th>"+ scoreTableHeader.tieGame + "</th><th>"+ scoreTableHeader.lostGame + "</th></thead>";
var counter = 1;
        newArr.sort((aPlayer, bPlayer) => bPlayer.score - aPlayer.score);
        newArr.forEach((player) => { var pos = counter++; score_data += '<tr><td>' + pos + '</td><td>' + player.user + '</td><td>' + player.score + '</td><td>'
            + player.win + '</td><td>' + player.tie + '</td><td>' + player.lost + '</td></tr>'});
        document.getElementById("scoreTab").innerHTML = score_data;

   }

function joinGameLoc(){
      location.href='game_view.html?gp=' + player_data.id;
  }

   const displayGames = () => {

   	let table = document.getElementById('gameInfo')
   	let head = document.createElement('THEAD')
   	let row = document.createElement('TR')
   	let cell1 = document.createElement('TH')
   	cell1.innerText = '#'
   	row.appendChild(cell1)
   	let cell2 = document.createElement('TH')
   	cell2.innerText = 'CREATED'
   	row.appendChild(cell2)
   	let cell3 = document.createElement('TH')
   	cell3.innerText = 'PLAYERS'
   	cell3.colSpan = 2
   	row.appendChild(cell3)
   	head.appendChild(row)

   	table.appendChild(head)


   	let body = document.createElement('TBODY')
   	games_data.forEach(game => {
   	    var gameNum = game.id
   		let row = document.createElement('TR')
   		for(item in game){

   			if(typeof game[item] == 'object'){
   				if(game[item].length == 1){
   					let cell = document.createElement('TD')
   					let cell2 = document.createElement('TD')
                    if(game[item][0].Players.user === player_data.user) {
                        cell.innerHTML = '<a href="game_view.html?gp=' + game[item][0].gpid + '">Continue Playing</a>'
                    }else{
                        cell.innerText = game[item][0].Players.user
                    }
   					game[item].forEach(gamePlayers =>{
   					if(player_data === 'Guest' || gamePlayers.Players.user === player_data.user ){
   					cell2.innerText = 'waiting...'
   					}else{
   					cell2.innerHTML = '<button class="btn btn-dark joinBtn" data-gamebtn="joined" data-game="'+ gameNum +'" >Join game</button>'
   					}
   					row.appendChild(cell)
   					row.appendChild(cell2)
   					})
   				} else{
   					game[item].forEach(gamePlayers => {

   						let cell = document.createElement('TD')
   						if(gamePlayers.Players.user === player_data.user){
   						cell.innerHTML = '<a href="game_view.html?gp=' + gamePlayers.gpid + '">Continue Playing</a>'
   						row.appendChild(cell)
                       }else{
                                cell.innerText = gamePlayers.Players.user
                          	    row.appendChild(cell)
                       }
   					})
   				}

   			} else{
   			    let cell = document.createElement('TD')

   				cell.innerText = game[item]
   				row.appendChild(cell)
   			}

   		}

   		body.appendChild(row)

   	})

   	table.appendChild(body)
   }

   function theadClass(){
       var list;
       list = document.querySelectorAll("thead");
       for (var i = 0; i < list.length; ++i) {
           list[i].classList.add('thead-dark');
       }
   }


const changeDateFormat = () => {
    for (var i in games_data){
        var newDate = new Date(games_data[i].created).toLocaleString();
        games_data[i].created = newDate
    }
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
    if(document.getElementById(iconID).className=="icon-volume-medium"){
        document.getElementById(iconID).className = "icon-volume-mute2";
    }else{
        document.getElementById(iconID).className = "icon-volume-medium";
    }
}

function setVolumeDown() {
    var myAudio = document.getElementById("music");
    myAudio.volume = 0.1; //Changed this to 0.5 or 50% volume since the function is called Set Half Volume ;)
}
function setVolumeUp() {
    var myAudio = document.getElementById("music");
    myAudio.volume = 0.5; //Changed this to 0.5 or 50% volume since the function is called Set Half Volume ;)
}

$("#close-sidebar").click(function() {
    $(".page-wrapper").removeClass("toggled");
});
$("#show-sidebar").click(function() {
    $(".page-wrapper").addClass("toggled");
});

