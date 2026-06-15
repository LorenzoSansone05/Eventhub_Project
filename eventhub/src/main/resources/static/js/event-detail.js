document.addEventListener("DOMContentLoaded", function () {
  var urlParams = new URLSearchParams(window.location.search);
  var eventId = urlParams.get("id");

  if (eventId) {
    loadEventDetail(eventId);
    loadEventRating(eventId);
    var btnPrenota = document.querySelector(".btn-booking-fixed");
    if (btnPrenota) {
      btnPrenota.href = "booking.html?id=" + eventId;
    }
  } else {
    console.error("Nessun ID evento trovato nell'URL!");
  }
});

function getIdentityFromToken(token) {
  try {
    if (!token) return null;
    const payload = token.split(".")[1];
    const base64Url = payload.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64Url).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload);
  } catch (e) {
    console.error("Errore nella decodifica del token JWT:", e);
    return null;
  }
}

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

      checkBookingPrivileges();
    })
    .catch(function (error) {
      console.error("Errore nel recupero dei dettagli dell'evento:", error);
    });
}

function checkBookingPrivileges() {
  const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
  const userData = getIdentityFromToken(token);

  var btnPrenota = document.querySelector(".btn-booking-fixed");

  if (btnPrenota) {
    if (!userData) {
      btnPrenota.removeAttribute("href");
      btnPrenota.style.backgroundColor = "#95a5a6";
      btnPrenota.style.cursor = "not-allowed";
      btnPrenota.title = "Effettua il login per prenotare un biglietto";
      return;
    }

    const userRoles = userData.roles ? userData.roles.map(r => r.authority) : [];
    const hasOrganizerRole = userRoles.includes('ROLE_ORGANIZER') || userRoles.includes('ORGANIZER');
    const hasAdminRole = userRoles.includes('ROLE_ADMIN') || userRoles.includes('ADMIN');

    if (hasOrganizerRole || hasAdminRole) {
      btnPrenota.removeAttribute("href");
      btnPrenota.style.backgroundColor = "#95a5a6";
      btnPrenota.style.cursor = "not-allowed";
      btnPrenota.title = "Azione non consentita per questo account";
    }
  }
}

function loadEventRating(id) {
  var ratingUrl = "http://localhost:8080/api/events/" + id + "/rating";

  fetch(ratingUrl)
    .then(function (response) {
      if (!response.ok) {
        throw new Error("Errore di rete durante il recupero del rating: " + response.status);
      }
      return response.text(); 
    })
    .then(function (ratingSummary) {
      var reviewsContainer = document.getElementById("reviews");
      if (ratingSummary && ratingSummary.trim() !== "") {
        reviewsContainer.innerText = ratingSummary;
      } else {
        reviewsContainer.innerText = "Nessuna recensione disponibile";
      }
    })
    .catch(function (error) {
      console.error("Errore nel recupero del rating standalone:", error);
    });
}