const API_URL = 'http://localhost:8080/api/feedbacks';
const token = localStorage.getItem('EventHubToken');

const headersConfig = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
};

document.addEventListener('DOMContentLoaded', function () {

    loadFeedbacksFromServer();

    const container = document.getElementById('feedbacks-container');
    if (container) {
        
        container.addEventListener('click', function (event) {
            const target = event.target;
            const card = target.closest('.feedback-card');
            if (!card) return;
            
            const feedbackId = card.id.replace('feedback-card-', '');
            const editFormContainer = card.querySelector('.edit-form-container');

            if (target.classList.contains('btn-edit')) {
                if (editFormContainer) {
                    editFormContainer.classList.add('active');
                }
            }

            if (target.classList.contains('btn-cancel')) {
                if (editFormContainer) {
                    editFormContainer.classList.remove('active');
                }
            }

            if (target.classList.contains('btn-delete')) {
                if (target.closest('.form-actions')) return; 

                if (confirm("Sei sicuro di voler eliminare questa recensione?")) {
                    fetch(API_URL + '/' + feedbackId, {
                        method: 'DELETE',
                        headers: headersConfig
                    }).then(function (response) {
                        if (response.ok) {
                            alert("Feedback eliminato con successo!");
                            card.remove(); 
                        } else {
                            alert("Errore durante l'eliminazione del feedback.");
                        }
                    });
                }
            }
        });

        container.addEventListener('submit', function (event) {
            event.preventDefault(); 
            
            const target = event.target;
            if (target.classList.contains('edit-feedback-form')) {
                const card = target.closest('.feedback-card');
                const feedbackId = card.id.replace('feedback-card-', '');
                
                const eventId = parseInt(target.getAttribute('data-event-id'), 10);
                
                const checkedRadio = target.querySelector('input[type="radio"]:checked');
                const textareaText = target.querySelector('textarea');
                
                if (!checkedRadio) {
                    alert("Seleziona una valutazione in stelle!");
                    return;
                }

                const newRating = parseInt(checkedRadio.value, 10);
                const newText = textareaText.value.trim();

                if (newText === "") {
                    alert("Il testo del feedback non può essere vuoto!");
                    return;
                }

                const dataToSend = {
                    rating: newRating,
                    feedbackText: newText,
                    eventId: eventId 
                };

                fetch(API_URL + '/' + feedbackId, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify(dataToSend)
                })
                .then(function (response) {
                    if (response.ok) {
                        return response.json(); 
                    } else {
                        throw new Error("Errore durante l'aggiornamento del feedback. Verifica i dati inseriti.");
                    }
                })
                .then(function (updatedFeedback) {
                    alert("Feedback aggiornato con successo!");
                    
                    const starsDisplay = card.querySelector('.stars-display');
                    if (starsDisplay) {
                        starsDisplay.textContent = "(" + updatedFeedback.rating + "/5)";
                    }
                    
                    const textDisplay = card.querySelector('.detail-item.full-width');
                    if (textDisplay) {
                        textDisplay.innerHTML = '<span>Testo del Feedback :</span> ' + updatedFeedback.feedbackText;
                    }

                    card.querySelector('.edit-form-container').classList.remove('active');
                })
                .catch(function (error) {
                    alert(error.message);
                });
            }
        });
    }
});


function loadFeedbacksFromServer() {
    fetch(API_URL, {
        method: 'GET',
        headers: headersConfig
    })
    .then(function (response) {
        if (!response.ok) {
            throw new Error("Impossibile recuperare le recensioni dal server.");
        }
        return response.json();
    })
    .then(function (feedbackList) {
        const container = document.getElementById('feedbacks-container');
        if (!container) return;

        container.innerHTML = ''; 

        feedbackList.forEach(function (feedback) {
            
            var starsHtml = '';
            for (var i = 5; i >= 1; i--) {
                var isChecked = (feedback.rating === i) ? 'checked' : '';
                starsHtml += `
                    <input type="radio" id="star-${i}-${feedback.id}" name="rating-${feedback.id}" value="${i}" ${isChecked} />
                    <label for="star-${i}-${feedback.id}">★</label>
                `;
            }

            container.innerHTML += `
                <div class="feedback-card" id="feedback-card-${feedback.id}">
                  <span class="feedback-id-badge">ID: ${feedback.id}</span>

                  <h3>Utente: ${feedback.username || 'Anonimo'}</h3>
                  <p style="margin: -10px 0 15px 0; font-size: 0.85em; color: #666;">
                    <strong>Evento:</strong> ${feedback.eventName || 'N/A'} (ID: ${feedback.eventId})
                  </p>

                  <div class="feedback-display-details">
                    <div class="detail-item">
                      <span>Valutazione :</span> 
                      <span class="stars-display">(${feedback.rating}/5)</span>
                    </div>
                    <div class="detail-item full-width">
                      <span>Testo del Feedback :</span> ${feedback.feedbackText}
                    </div>
                  </div>

                  <div class="edit-form-container" id="edit-form-box-${feedback.id}">
                    <h4 class="edit-form-title">Modifica Recensione</h4>
                    <form class="edit-feedback-form" data-event-id="${feedback.eventId}">
                      <div class="feedback-details-grid">
                        <div class="form-group-feedback">
                          <label>Valutazione (Stelle 1-5)</label>
                          <div class="stars-rating-container">
                            ${starsHtml}
                          </div>
                        </div>
                      </div>
                      <div class="form-group-feedback" style="margin-top: 15px;">
                        <label>Testo del Feedback</label>
                        <textarea rows="4" required>${feedback.feedbackText}</textarea>
                      </div>
                      <div class="form-actions">
                        <button type="button" class="btn-cancel">Annulla</button>
                        <button type="submit" class="btn-update">Aggiorna</button>
                      </div>
                    </form>
                  </div>

                  <div class="admin-actions">
                    <button class="btn-edit">Modifica Recensione</button>
                    <button class="btn-delete">Elimina</button>
                  </div>
                </div>
            `;
        });
    })
    .catch(function (error) {
        console.error("Errore durante il caricamento dei feedback:", error);
    });
}