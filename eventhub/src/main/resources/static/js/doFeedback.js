document.addEventListener("DOMContentLoaded", function () {
  var reviewForm = document.getElementById("review-form");

  var urlParams = new URLSearchParams(window.location.search);
  var eventId = urlParams.get("eventId");

  if (!eventId) {
    alert("Errore: Nessun evento specificato da recensire.");
    window.location.href = "tickets.html";
    return;
  }

  if (reviewForm) {
    reviewForm.addEventListener("submit", async function (event) {
      event.preventDefault();

      var reviewText = document.getElementById("review-text").value;
      var selectedRating = document.querySelector('input[name="rating"]:checked');
      
      if (!selectedRating) {
        alert("Per favore, seleziona una votazione da 1 a 5 stelle.");
        return;
      }

      var token = localStorage.getItem("EventHubToken");

      var feedbackData = {
        eventId: parseInt(eventId, 10),
        rating: parseInt(selectedRating.value, 10),
        feedbackText: reviewText
      };

      try {
        var response = await fetch("http://localhost:8080/api/feedbacks", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
          },
          body: JSON.stringify(feedbackData)
        });

        if (response.status === 201) {
          alert("Feedback inserito con successo!");
          window.location.href = "events.html"; 
          
        } else if (response.status === 400) {
          alert("Errore 400: Dati non validi (voto fuori dai limiti 1-5 o testo troppo lungo).");
          
        } else if (response.status === 401) {
          alert("Errore 401: Sessione scaduta o non autenticato. Effettua nuovamente l'accesso.");
          
        } else if (response.status === 404) {
          alert("Errore 404: L'evento di riferimento non esiste sul server.");
          
        } else {
          alert("Si è verificato un errore imprevisto. Riprova più tardi.");
        }

      } catch (error) {
        console.error("Errore di connessione:", error);
        alert("Impossibile connettersi al server.");
      }
    });
  }
});