// buttonclicked() is called when the username is submitted 
// for reference, check anchor tag above the form tag in html
// should check if input is empty
// if true, make object into JSON string and change pages
// if false, notify user
function buttonclicked(){
    let username = document.forms["userForm"]["fname"].value;
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