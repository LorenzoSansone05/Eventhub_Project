// 1. DICHIARIAMO LA FUNZIONE (All'inizio del file, fuori da tutto)
function getRoleFromToken(token) {
  try {
    var payload = token.split(".")[1]; // Recupero il payload che si trova sempre dopo il primo punto
    var payloadString = atob(payload); // Transformo il payload in un JSON che identifica il ruolo
    var userData = JSON.parse(payloadString); // Trasformo il JSON in un oggetto JS
    return userData.roles[0].authority; // Recupero il ruolo dell'utente loggato sottoforma di String
  } catch (e) {
    console.error("Errore nella lettura del token", e);
    return null;
  }
}

document.addEventListener("DOMContentLoaded", function () {
  var navbar = document.querySelector(".nav-links");
  var loginBtn = document.querySelector(".btn-login");
  var token = localStorage.getItem("EventHubToken");

  if (token) {
    loginBtn.style.display = "none";

    var userRole = getRoleFromToken(token);

    var navbarHtml = ""; // var in cui aggiungiamo gli elementi necessari alla navbar

    if (userRole === "ROLE_USER") {
      navbarHtml +=
        "<li><a href='feedbacks.html'>Recensioni</a></li>" +
        "<li><a href='profile.html'>Profilo</a></li>" +
        "<li><a href='#' id='btn-logout'>Esci</a></li>";
    }

    

    if (userRole === "ROLE_ADMIN") {
      navbarHtml +=
        "<li><a href='users.html'>Utenti</a></li>" +
        "<li><a href='venues.html'>Sedi</a></li>" +
        "<li><a href='feedbacks.html'>Recensioni</a></li>" +
        "<li><a href='#' id='btn-logout'>Esci</a></li>";
    }

    navbar.insertAdjacentHTML("beforeend", navbarHtml);

    var logoutBtn = document.getElementById("btn-logout");

    logoutBtn.addEventListener("click", function (event) {
      event.preventDefault();
      localStorage.removeItem("EventHubToken");
      window.location.href = "index.html";
    });
  }
});
