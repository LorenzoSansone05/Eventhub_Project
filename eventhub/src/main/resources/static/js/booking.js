document.addEventListener("DOMContentLoaded", function () {
  var urlParams = new URLSearchParams(window.location.search);
  var eventId = urlParams.get("id");

  var priceStandard = 0;
  var priceVip = 0;

  var ticketSelect = document.getElementById("ticket-type");

  if (eventId) {
    loadEventFields(eventId);
    document.getElementById("back-link").href = "event-detail.html?id=" + eventId;
  } else {
    console.error("Nessun ID evento trovato nell'URL!");
  }

  function loadEventFields(id) {
    var url = "http://localhost:8080/api/events/" + id;

    fetch(url)
      .then(function (response) {
        return response.json();
      })
      .then(function (event) {
        document.getElementById("event-title").value = event.name;
        document.getElementById("event-date").value = event.eventDate;
        document.getElementById("event-time-start").value = event.startTime;
        document.getElementById("event-time-end").value = event.endTime;
        document.getElementById("event-venue").value = event.venueName + " (" + event.venueCity + ")";

        priceStandard = Number(event.priceStandard);
        priceVip = Number(event.priceVip);

        updateTotalPrice();
      })
      .catch(function (error) {
        console.error("Errore nel recupero delle info dell'evento:", error);
      });
  }

  ticketSelect.addEventListener("change", function () {
    updateTotalPrice();
  });

  function updateTotalPrice() {
    var selectedType = ticketSelect.value;
    var finalPrice = selectedType === "standard" ? priceStandard : priceVip;
    document.getElementById("total-price").innerText = "€" + finalPrice.toFixed(2);
  }

  var bookingForm = document.getElementById("booking-form");
  bookingForm.addEventListener("submit", function (e) {
    e.preventDefault();

    var bookingData = {
      type: ticketSelect.value.toUpperCase()
    };

    var postUrl = "http://localhost:8080/api/tickets/events/" + eventId + "/book";

    fetch(postUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(bookingData)
    })
    .then(function (response) {
      if (response.status === 201) {
        alert("Biglietto prenotato con successo!");
        window.location.href = "events.html"; 
      } else if (response.status === 401) {
        alert("Errore: Devi prima effettuare l'accesso (Richiede autenticazione).");
        window.location.href = "login.html";
      } else if (response.status === 404) {
        alert("Errore: L'evento specificato non esiste nel sistema.");
      } else if (response.status === 409) {
        alert("Impossibile procedere: l'evento è già iniziato, hai già registrato una prenotazione o i posti sono esauriti.");
      } else {
        alert("Errore durante la prenotazione. Controlla i dati inseriti.");
      }
    })
    .catch(function (error) {
      console.error("Errore di rete durante l'invio della prenotazione:", error);
      alert("Impossibile connettersi al server. Riprova più tardi.");
    });
  });
});