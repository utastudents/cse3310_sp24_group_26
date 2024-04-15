// buttonclicked() is called when the username is submitted 
// for reference, check anchor tag above the form tag in html
// should check if input is empty
// if true, make object into JSON string and change pages
// if false, notify user

class UserEvent{
    request;
    GameId;
    UserId;
  }


serverUrl = "ws://" + window.location.hostname +":"+ (parseInt(location.port) + 100);
connection = new WebSocket(serverUrl);
connection.onopen = function (evt) {
    console.log("open");
}


function buttonclick(i) {
    U = new UserEvent();
    U.request = 1;
    U.UserId = "whit";
    connection.send(JSON.stringify(U));
    console.log("BOUTTA SEND");
    console.log(JSON.stringify(U));
}


function buttonclicked(){
    let username = document.forms["userForm"]["fname"].value;
    console.log("username is trying to join: " + username);
    const user = {
        username : username
    }
    if(username != ""){
        localStorage.setItem("userJSON", JSON.stringify(user)) //JSON string is saved into local storage so that match.html can access
        window.location.href = "match.html";
    }
    else{
        document.forms["userForm"]["fname"].value = "";
        document.forms["userForm"]["fname"].placeholder = "ENTER A USERNAME!";
    }

    
}