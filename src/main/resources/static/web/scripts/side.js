$(".btnSide").click(function(){
    $(".container").toggleClass("log-in");
});
$(".container-form .btnSide").click(function(){
    $(".container").addClass("active");
});

function sideBtn(d) {
  var selectSide = d.getAttribute("data-type");
  var selectBtn = d.getAttribute("data-gamebtn");
  var gameId = d.getAttribute("data-gameids");
  if(selectBtn === "create"){
        gameCreationFetch(selectSide);
        }
        else{
        joinGameFetch(selectSide,gameId);
        }

}

function closeTab(){
    document.getElementById('out').classList.add('hideEl');
}

function thanosSound(){
    var audio = document.getElementById("thanosAudio");
    audio.play();
}function furySound(){
    var audio = document.getElementById("furyAudio");
    audio.play();
}
var w = document.getElementById("furyBtn").style.display = "none";
function btnDisplay() {
    var x = document.getElementById("furyBtn");
    var y = document.getElementById("thanosBtn");
    if (x.style.display === "none") {
        x.style.display = "block";
        y.style.display = "none";
    } else {
        x.style.display = "none";
        y.style.display = "block";
    }
}
