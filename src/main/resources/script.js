const BASE_LOGIN = "https://localhost:4567"
const BASE_SERVER = "https://localhost:2703"



const button = document.getElementById("inputButton"); 
const userInput = document.getElementById("userInput"); 
const passwordInput = document.getElementById("passwordInput"); 
const logResponse = document.getElementById("logResponse"); 
const serverResponse = document.getElementById("serverResponse"); 


const send = async(user, password) =>{
    console.log("SEND");
    console.log(user);
    
    const option = {
        method: 'POST', 
        headers: {
            'Content-Type' : 'application/json'
        },
        body: JSON.stringify({
            name: user,
            password : password
        })
    }

    const response = await fetch(`${BASE_LOGIN}/login`, option); 

    const received =  await response.json();

    
    
    updateLog(received); 

    // myServer(); 
    
}



const myServer = async() =>{
    const option ={
        method: 'GET',
        headers: {
            'Content-Type' : 'application/json'
        }
    }

    const response = await fetch(`${BASE_LOGIN}/login/service`, option); 

    console.log(response)
    // debugger; 

    const received =  await response.json();

    console.log(received); 

    updateServer(received); 


}



function updateLog(data){
    logResponse.innerHTML = `${data.result}`;
    serverResponse.innerHTML = `${data.server}`;
}

function updateServer(data){
    serverResponse.innerHTML = `${data.result}`;
}


button.addEventListener('click', (event =>{
    event.preventDefault(); 
    send(userInput.value, passwordInput.value); 
    

}))