document.addEventListener("DOMContentLoaded", function() {
    
    var loginForm = document.getElementById("login-form");
    
    if (loginForm) {
        var errorDivLogin = document.getElementById("error-message");

        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();
            
            if (errorDivLogin) {
                errorDivLogin.innerText = "";
            }

            var formData = new FormData(loginForm);
            var credentials = {
                email: formData.get("email"),
                password: formData.get("password")
            };

            var url = "http://localhost:8080/api/auth/login";

            fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(credentials)
            })
            .then(function(response) {
                if (response.ok) {
                    return response.json(); 
                } else {
                    throw new Error("Email o password errate.");
                }
            })
            .then(function(data) {
                var jwtToken = data.token;
                localStorage.setItem("EventHubToken", jwtToken);
                window.location.href = "index.html";
            })
            .catch(function(error) {
                if (errorDivLogin) {
                    if (error.message === "Email o password errate.") {
                        errorDivLogin.innerText = error.message;
                    } else {
                        errorDivLogin.innerText = "Impossibile connettersi al server.";
                    }
                }
            });
        });
    }
});