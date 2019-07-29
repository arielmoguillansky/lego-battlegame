const myForm = document.getElementById('loginform');
myForm.addEventListener('submit', function(e){
    e.preventDefault();
    loginFetch();
})

const myFormSignUp = document.getElementById('signupform');
myFormSignUp.addEventListener('submit', function(e){
    e.preventDefault();
    signUpFetch();
})

function showAlert(){
Swal.fire({
  title: 'Invalid Username or Password',
  text: 'Please, try again',
  imageUrl: '/web/styles/heroes/deadpool.png',
  imageWidth: 50 +'%',
  imageHeight: 50 + '%',
  imageAlt: 'Custom image',
  animation: false,
    customClass: {
        popup: 'animated fadeInUp',

    }
})
}
function showRegister(){
    Swal.fire({
        title: 'You are in!',
        text: 'Please, Log In to continue',
        imageUrl: '/web/styles/heroes/groot.png',
        imageWidth: 50 +'%',
        imageHeight: 50 +'%',
        imageAlt: 'Custom image',
        animation: false,
        customClass: {
            popup: 'animated fadeInUp',

        }
    })
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