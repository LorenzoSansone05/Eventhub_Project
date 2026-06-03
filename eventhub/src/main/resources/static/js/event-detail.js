document.addEventListener("DOMContentLoaded", function () {
  var urlParams = new URLSearchParams(window.location.search);
  var eventId = urlParams.get("id");

  if (eventId) {
    loadEventDetail(eventId);
  } else {
    console.error("Nessun ID evento trovato nell'URL!");
  }
});

function loadEventDetail(id) {
  var url = "http://localhost:8080/api/events/" + id; 

  fetch(url)
    .then(function (response) {
      return response.json();
    })
    .then(function (event) {
      document.getElementById("event-title").innerText = event.name;
      document.getElementById("event-date").innerText = event.eventDate;
      document.getElementById("event-time-start").innerText = event.startTime;
      document.getElementById("event-time-end").innerText = event.endTime;
      document.getElementById("event-venue-name").innerText = event.venueName;
      document.getElementById("event-venue-city").innerText = event.venueCity;
      document.getElementById("event-venue-address").innerText = event.venueAddress;
      document.getElementById("event-price-standard").innerText = "€" + event.priceStandard;
      document.getElementById("event-price-vip").innerText = "€" + event.priceVip;
      document.getElementById("event-organizer-email").innerText = event.organizerEmail;

      var tagsContainer = document.getElementById("event-tags");
      tagsContainer.innerHTML = "";
      if (event.tags) {
        for (var i = 0; i < event.tags.length; i++) {
          tagsContainer.innerHTML += "<span class='tag-badge'>" + event.tags[i] + "</span>";
        }
      }

      var speakersContainer = document.getElementById("event-speakers-list");
      speakersContainer.innerHTML = "";
      if (event.speakerNames) {
        for (var j = 0; j < event.speakerNames.length; j++) {
          speakersContainer.innerHTML += "<li>" + event.speakerNames[j] + "</li>";
        }
      }

      return fetch(url + "/rating");
    })
    .then(function (response) {
      return response.json();
    })
    .then(function (averageRating) {
      var reviewsContainer = document.getElementById("reviews");
      
      if (averageRating !== null && averageRating !== undefined) {
        var ratingFormatted = Number(averageRating).toFixed(1);
        
        reviewsContainer.innerText = ratingFormatted + "⭐ Feedbacks";
      } else {
        reviewsContainer.innerText = "Nessuna recensione";
      }
    })
    .catch(function (error) {
      console.error("Errore nel recupero dei dettagli dell'evento o del rating:", error);
    });
}