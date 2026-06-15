document.addEventListener("DOMContentLoaded", function () {
  var urlParams = new URLSearchParams(window.location.search);
  var eventId = urlParams.get("eventId");

  if (!eventId) {
    console.error("Event ID not found in the URL.");
    document.getElementById("event-title").innerText = "Errore";
    document.getElementById("participants-container").innerHTML =
      "<p style='color: red;'>Errore: Nessun ID evento specificato nell'URL.</p>";
    return;
  }

  document.getElementById("event-title").innerText =
    "Partecipanti per l'Evento ID: " + eventId;

  var apiUrl = "http://localhost:8080/api/events/" + eventId + "/participants";

  var token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');

  fetch(apiUrl, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token,
    },
  })
    .then(function (response) {
      if (!response.ok) {
        throw new Error("Network error: " + response.status);
      }
      return response.json(); 
    })
    .then(function (data) {
      renderParticipants(data);
    })
    .catch(function (error) {
      console.error("Error fetching participants:", error);
      document.getElementById("participants-container").innerHTML =
        "<p>Impossibile caricare la lista dei partecipanti al momento. Verifica l'autenticazione o i permessi dell'organizzatore.</p>";
    });
});

function renderParticipants(participants) {
  var container = document.getElementById("participants-container");

  container.innerHTML = "";

  if (participants.length === 0) {
    container.innerHTML =
      "<p>Nessun partecipante prenotato per questo evento.</p>";
    return;
  }

  for (var i = 0; i < participants.length; i++) {
    var p = participants[i];

    var badgeClass = "type-standard";
    var currentType = p.ticketType;

    if (currentType) {
      currentType = currentType.toUpperCase();
      if (currentType === "VIP") {
        badgeClass = "type-vip";
      }
    } else {
      currentType = "STANDARD";
    }

    var card = document.createElement("div");
    card.className = "participant-card";

    card.innerHTML =
      '<div class="participant-header-badges">' +
      '<span class="ticket-type-badge ' + badgeClass + '">' + currentType + '</span> ' +
      '<span class="participant-id-badge">ID Biglietto: ' + p.ticketId + '</span>' +
      '</div>' +
      '<h3>' + p.email + '</h3>' +
      '<div class="participant-details">' +
      '<div class="detail-item"><span>ID Utente:</span> ' + p.userId + '</div>' +
      '<div class="detail-item"><span>Stato Biglietto:</span> ' + (p.ticketStatus || "ATTIVO") + '</div>' +
      '</div>';

    container.appendChild(card);
  }
}