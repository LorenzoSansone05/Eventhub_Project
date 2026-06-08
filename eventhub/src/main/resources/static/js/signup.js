document.addEventListener("DOMContentLoaded", function() {
    var signupForm = document.getElementById("signup-form");
    
    if (signupForm) {
        var errorDivSignup = document.getElementById("error-message");

        signupForm.addEventListener("submit", function(event) {
            event.preventDefault();

            if (errorDivSignup) {
                errorDivSignup.innerText = "";
            }

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
                    if (errorDivSignup) {
                        errorDivSignup.innerText = "Errore durante la registrazione. Riprova.";
                    }
                }
            })
            .catch(function(error) {
                if (errorDivSignup) {
                    errorDivSignup.innerText = "Impossibile connettersi al server.";
                }
            });
        });
    }
});