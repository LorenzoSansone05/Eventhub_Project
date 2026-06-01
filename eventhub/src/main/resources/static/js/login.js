document.addEventListener("DOMContentLoaded", function() {
    var loginForm = document.querySelector("form");
    var errorDiv = document.getElementById("error-message");

    loginForm.addEventListener("submit", function(event) {
        event.preventDefault();

        errorDiv.innerText = "";

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
            if (error.message === "Email o password errate.") {
                errorDiv.innerText = error.message;
            } else {
                errorDiv.innerText = "Impossibile connettersi al server.";
            }
        });
    });
});