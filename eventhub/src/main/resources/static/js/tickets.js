// Global Configuration
const API_BASE_URL = "http://localhost:8080/api/tickets";

document.addEventListener("DOMContentLoaded", function () {
  loadUserTickets();
});

async function loadUserTickets() {
  const container = document.getElementById("tickets-container");
  const token = localStorage.getItem("EventHubToken");

  if (!token) {
    container.innerHTML =
      '<p class="review-status done" style="background-color: #f8d7da; color: #721c24; border-color: #f5c6cb;">Utente non autenticato. Accedi e riprova.</p>';
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/tickets`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("Impossibile caricare i biglietti.");
    }

    const tickets = await response.json();
    renderTickets(tickets);
  } catch (error) {
    console.error(error);
    container.innerHTML =
      '<p class="review-status done" style="background-color: #f8d7da; color: #721c24; border-color: #f5c6cb;">Errore durante il caricamento delle tue prenotazioni.</p>';
  }
}

function renderTickets(tickets) {
  const container = document.getElementById("tickets-container");
  container.innerHTML = "";

  if (tickets.length === 0) {
    container.innerHTML = "<p>Non hai effettuato nessuna prenotazione.</p>";
    return;
  }

  for (const ticket of tickets) {
    const startDateTimeStr = `${ticket.eventDate}T${ticket.startTime}`;
    const endDateTimeStr = `${ticket.eventDate}T${ticket.endTime}`;

    const eventStart = new Date(startDateTimeStr);
    const eventEnd = new Date(endDateTimeStr);
    const now = new Date();

    const isStarted = now >= eventStart;
    const isConcluded = now >= eventEnd;

    const dateOptions = {
      day: "numeric",
      month: "long",
      year: "numeric",
      timeZone: "Europe/Rome",
    };
    const timeOptions = {
      hour: "2-digit",
      minute: "2-digit",
      timeZone: "Europe/Rome",
    };

    const formattedDate = eventStart.toLocaleDateString("en-US", dateOptions);
    const formattedStartTime = eventStart.toLocaleTimeString(
      "en-US",
      timeOptions,
    );
    const formattedEndTime = eventEnd.toLocaleTimeString("en-US", timeOptions);

    let actionContent = "";

    if (ticket.status === "ANNULLATO") {
      actionContent = `<p class="review-status done" style="background-color: #f8d7da; color: #721c24; border-color: #f5c6cb;">✕ Questa prenotazione è stata annullata.</p>`;
    } else {
      const canCancel = !isStarted;
      const cancelBtnHtml = canCancel
        ? `<button class="btn-cancel" onclick="handleDeleteBooking(${ticket.id})">Annulla Prenotazione</button>`
        : "";

      if (ticket.alreadyReviewed) {
        actionContent = `
                    <p class="review-status done">✓ You already reviewed this event. Thank you!</p>
                    ${cancelBtnHtml}
                `;
      } else if (isConcluded) {
        actionContent = `
                    <button class="btn-review" onclick="redirectToFeedback(${ticket.eventId})">Lascia una recensione</button>
                    ${cancelBtnHtml}
                `;
      } else {
        actionContent = `
                    <p class="review-status done" style="background-color: #e2e8f0; color: #475569; border-color: #cbd5e1;">
                       Info: Potrai recensire l'evento quando sarà concluso.
                    </p>
                    ${cancelBtnHtml}
                `;
      }
    }

    const badgeClass = ticket.type === "VIP" ? "type-vip" : "type-standard";

    const cardHtml = `
            <div class="ticket-card" id="ticket-${ticket.id}">
              <div class="ticket-header-badges">
                <span class="ticket-type-badge ${badgeClass}">${ticket.type}</span>
                <span class="ticket-id-badge">ID Evento: ${ticket.eventId}</span>
              </div>

              <h3>${ticket.eventName}</h3>

              <div class="ticket-details">
                <div class="detail-item"><span>Prezzo:</span> € ${ticket.price.toFixed(2).replace(".", ",")}</div>
                <div class="detail-item"><span>Data:</span> ${formattedDate}</div>
                <div class="detail-item"><span>Inizio:</span> ${formattedStartTime}</div>
                <div class="detail-item"><span>Fine:</span> ${formattedEndTime} </div>
              </div>

              <hr />
              
              <div class="ticket-actions">
                ${actionContent}
              </div>
            </div>
        `;

    container.innerHTML += cardHtml;
  }
}

async function handleDeleteBooking(ticketId) {
  if (
    !confirm(
      "Sei sicuro di voler annullare questa prenotazione?.",
    )
  ) {
    return;
  }

  const token = localStorage.getItem("EventHubToken");

  try {
    const response = await fetch(`${API_BASE_URL}${ticketId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.status === 204) {
      alert("Prenotazione cancellata con successo.");
      loadUserTickets();
    } else if (response.status === 403) {
      alert("Non hai il permesso di cancellare questa prenotazione.");
    } else {
      const errorData = await response.json();
      alert(
        `Impossibile cancellare: ${errorData.message || "Errore generico."}`,
      );
    }
  } catch (error) {
    console.error(error);
    alert("Errore lato server nella cancellazione della prenotazione.");
  }
}

function redirectToFeedback(eventId) {
  window.location.href = `feedback.html?eventId=${eventId}`;
}
