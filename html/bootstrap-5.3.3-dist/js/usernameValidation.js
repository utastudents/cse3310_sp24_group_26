// buttonclicked() is called when the username is submitted 
// for reference, check anchor tag above the form tag in html
// should check if input is empty
// if true, make object into JSON string and change pages
// if false, notify user
let UserId = null;

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

connection.onmessage = function(evt){

    let lobby = document.getElementById("lobby-body");
    lobby.innerHTML = "";
    var msg;
    msg = evt.data;
    let obj = JSON.parse(msg);

    console.log("Message received: " + msg);
    console.log("NUM USERS: " + obj.length)

    obj.forEach(function(obj){
        console.log("OBJECT: " + obj)
        var row = lobby.insertRow();
        var name = row.insertCell(0);
        var ready = row.insertCell(1);

        name.textContent = obj.user;
        if(obj.ready == false){
            ready.textContent = "Not Ready";
        } else{
            ready.textContent = "Ready";
        }
    });
}


function buttonclicked(value){
    U = new UserEvent;
    if(value == 1){
        let username = document.getElementById("username").value;
        this.UserId = username;
        U.UserId = username;
        U.request = 1;
        connection.send(JSON.stringify(U));
        let div = document.getElementById("matchmaking");
        div.innerHTML = "";
    } else if (value == 2){
        U.UserId = this.UserId;
        U.request = 2;
        connection.send(JSON.stringify(U));
    }
}
