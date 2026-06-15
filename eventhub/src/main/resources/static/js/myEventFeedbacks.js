document.addEventListener("DOMContentLoaded", function () {
    var urlParams = new URLSearchParams(window.location.search);
    var eventId = urlParams.get('eventId');

    if (!eventId) {
        console.error("Event ID not found in the URL.");
        document.getElementById("event-title").innerText = "Errore";
        document.getElementById("feedbacks-container").innerHTML = 
            "<p style='color: red;'>Errore: Nessun ID evento specificato nell'URL.</p>";
        return;
    }

    var apiUrl = "http://localhost:8080/api/feedbacks/event/" + eventId;

    fetch(apiUrl)
        .then(function (response) {
            if (!response.ok) {
                throw new Error("Network error: " + response.status);
            }
            return response.json();
        })
        .then(function (feedbacks) {
            renderFeedbacks(feedbacks, eventId);
        })
        .catch(function (error) {
            console.error("Error fetching feedbacks:", error);
            document.getElementById("event-title").innerText = "Feedback Evento";
            document.getElementById("feedbacks-container").innerHTML = 
                "<p>Impossibile caricare i feedback al momento.</p>";
        });
});

function renderFeedbacks(feedbacks, eventId) {
    var container = document.getElementById("feedbacks-container");
    var eventTitleElement = document.getElementById("event-title");
    
    container.innerHTML = "";

    if (feedbacks.length === 0) {
        eventTitleElement.innerText = "Feedback Evento (ID: " + eventId + ")";
        container.innerHTML = "<p>Nessun feedback presente per questo evento.</p>";
        return;
    }

    if (feedbacks[0] && feedbacks[0].eventName) {
        eventTitleElement.innerText = "Feedback per: " + feedbacks[0].eventName;
    } else {
        eventTitleElement.innerText = "Feedback Evento";
    }

    for (var i = 0; i < feedbacks.length; i++) {
        var fb = feedbacks[i];

        var fullStars = "";
        for (var j = 0; j < fb.rating; j++) {
            fullStars += "⭐";
        }
        var emptyStars = "";
        for (var k = 0; k < (5 - fb.rating); k++) {
            emptyStars += "★";
        }
        var starString = fullStars + emptyStars;

        var username = fb.username;
        if (!username) {
            username = "Anonimo";
        }

        var card = document.createElement("div");
        card.className = "feedback-card";
        
        card.innerHTML = 
            '<span class="feedback-id-badge">ID Feedback: ' + fb.id + '</span>' +
            '<h3>Utente: ' + username + '</h3>' +
            '<div class="feedback-display-details">' +
                '<div class="detail-item">' +
                    '<span>Valutazione :</span> ' +
                    '<span class="stars-display">' + starString + ' (' + fb.rating + '/5)</span>' +
                '</div>' +
                '<div class="detail-item full-width">' +
                    '<span>Testo del Feedback :</span> ' + fb.feedbackText +
                '</div>' +
            '</div>';

        container.appendChild(card);
    }
}