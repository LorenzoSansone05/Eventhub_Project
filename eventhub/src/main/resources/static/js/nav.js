function getIdentityFromToken(token) {
  try {
    if (!token) return null;
    var payload = token.split(".")[1]; // Recupero il payload dopo il primo punto
    var payloadString = atob(payload); // Trasformo il payload in una stringa JSON
    var userData = JSON.parse(payloadString); // Trasformo il JSON in un oggetto JS
    return userData; // Restituisco l'intero oggetto contenente sub, roles, ecc.
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
    var userData = getIdentityFromToken(token);
    
    if (userData) {
      var userEmail = userData.sub; 
      var userRole = userData.roles && userData.roles[0] ? userData.roles[0].authority : null;

      var navbarHtml = "";

      if (userRole === "ROLE_USER") {
        navbarHtml +=
          "<li><a href='tickets.html'>Biglietti</a></li>" +
          "<li><a href='profile.html'>Profilo</a></li>" +
          "<li><a href='#' id='btn-logout'>Esci</a></li>";
      }

      if (userRole === "ROLE_ORGANIZER") {
        navbarHtml +=
          `<li><a href='create-events.html'>Crea Eventi</a></li>` +
          "<li><a href='#' id='btn-logout'>Esci</a></li>";
      }

      if (userRole === "ROLE_ADMIN") {
        navbarHtml +=
          "<li><a href='users.html'>Utenti</a></li>" +
          "<li><a href='venues.html'>Sedi</a></li>" +
          "<li><a href='speakers.html'>Relatori</a></li>" +
          "<li><a href='feedbacks.html'>Recensioni</a></li>" +
          "<li><a href='tags.html'>Tag</a></li>" +
          "<li><a href='#' id='btn-logout'>Esci</a></li>";
      }

      navbar.insertAdjacentHTML("beforeend", navbarHtml);

      var logoutBtn = document.getElementById("btn-logout");
      if (logoutBtn) {
        logoutBtn.addEventListener("click", function (event) {
          event.preventDefault();
          localStorage.removeItem("EventHubToken");
          window.location.href = "index.html";
        });
      }
    }
  }
});