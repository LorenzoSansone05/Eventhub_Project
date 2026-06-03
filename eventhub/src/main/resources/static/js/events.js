var currentPage = 0;

document.addEventListener("DOMContentLoaded", function () {
  var filtersForm = document.getElementById("filters-form");

  loadEvents(filtersForm);

  filtersForm.addEventListener("submit", function (event) {
    event.preventDefault();
    currentPage = 0;
    loadEvents(filtersForm);
  });

  document.getElementById("btn-prev").addEventListener("click", function () {
    if (currentPage > 0) {
      currentPage = currentPage - 1;
      loadEvents(filtersForm);
    }
  });

  document.getElementById("btn-next").addEventListener("click", function () {
    currentPage = currentPage + 1;
    loadEvents(filtersForm);
  });
  
});

function loadEvents(formElement) {
  var formData = new FormData(formElement);

  var dateValue = formData.get("date");
  var tagValue = formData.get("tag");
  var venueValue = formData.get("venueName");
  var organizerValue = formData.get("organizerName");

  var url = "http://localhost:8080/api/events?page=" + currentPage + "&size=10";

  if (dateValue !== "") {
    url = url + "&date=" + dateValue;
  }
  if (tagValue !== "") {
    url = url + "&tag=" + tagValue;
  }
  if (venueValue !== "") {
    url = url + "&venueName=" + venueValue;
  }
  if (organizerValue !== "") {
    url = url + "&organizerName=" + organizerValue;
  }

  var container = document.querySelector(".events-grid");

  fetch(url)
    .then(function (response) {
      return response.json();
    })
    .then(function (data) {
      var eventList = data.content;

      container.innerHTML = "";
      var tuttoHtml = "";

      for (var i = 0; i < eventList.length; i++) {
        var currentEvent = eventList[i];
        var tagsHtml = "";
        if (currentEvent.tags !== null && currentEvent.tags !== undefined) {
          for (var j = 0; j < currentEvent.tags.length; j++) {
            tagsHtml =
              tagsHtml +
              "<span class='tag-badge'>" +
              currentEvent.tags[j] +
              "</span>";
          }
        }

        var cardHtml =
          "<div class='event-card'>" +
          "<h2 class='event-title'>" +
          currentEvent.name +
          "</h2>" +
          "<p class='event-info'><span>Data:</span> " +
          currentEvent.eventDate +
          "</p>" +
          "<p class='event-info'><span>Ora:</span> " +
          currentEvent.startTime +
          "</p>" +
          "<p class='event-info'><span>Città:</span> " +
          currentEvent.venueCity +
          "</p>" +
          "<p class='event-info'><span>Prezzo:</span> €" +
          currentEvent.priceStandard +
          "</p>" +
          "<div class='tags-container'>" +
          tagsHtml +
          "</div>" +
          "<a href='detail.html?id=" +
          currentEvent.id +
          "' class='btn-detail'>Vedi Dettaglio</a>" +
          "</div>";
        tuttoHtml = tuttoHtml + cardHtml;
      }

      container.innerHTML = tuttoHtml;

      document.getElementById("page-number").innerText =
        "Pagina " + (currentPage + 1);
    });
}
