document.addEventListener("DOMContentLoaded", function() {
    var signupForm = document.querySelector("form");
    var errorDiv = document.getElementById("error-message");

    signupForm.addEventListener("submit", function(event) {
        event.preventDefault();

        errorDiv.innerText = "";

        var formData = new FormData(signupForm);
        
        var datiUtente = {
            email: formData.get("email"),
            password: formData.get("password")
        };

        var url = "http://localhost:8080/api/auth/signup";

        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datiUtente)
        })
        .then(function(response) {
            if (response.ok) {
                window.location.href = "login.html"; 
            } else {
                errorDiv.innerText = "Errore durante la registrazione. Riprova.";
            }
        })
        .catch(function(error) {
            errorDiv.innerText = "Impossibile connettersi al server.";
        });
    });
});