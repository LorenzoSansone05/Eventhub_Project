const BASE_URL = 'http://localhost:8080/api';
const ENDPOINTS = {
    speakers: BASE_URL + '/speakers',
    venues: BASE_URL + '/venues',
    tags: BASE_URL + '/tags',
    createEvent: BASE_URL + '/events',
    updateEvent: (id) => `${BASE_URL}/events/${id}`,
    deleteEvent: (id) => `${BASE_URL}/events/${id}`,
    getOrganizerEvents: (email) => `${BASE_URL}/events?tagName=&venueName=&organizerName=${encodeURIComponent(email)}`
};

// Variabili globali per memorizzare le liste complete da riutilizzare nelle card di modifica
var globalSpeakers = [];
var globalTags = [];

document.addEventListener('DOMContentLoaded', function() {
    initializeEventForm();
    handleOrganizerEvents();

    const eventForm = document.getElementById('create-event-form');
    if (eventForm) {
        eventForm.addEventListener('submit', handleFormSubmit);
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

function handleOrganizerEvents() {
    const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');

    if (!token) {
        showErrorMessageToUser("Accesso negato. Token non trovato. Effettua il login.");
        window.location.href = 'login.html';
        return;
    }

    const userData = getIdentityFromToken(token);
    if (!userData) {
        showErrorMessageToUser("Sessione non valida. Effettua nuovamente il login.");
        window.location.href = 'login.html';
        return;
    }

    const tokenEmail = userData.sub; 
    const userRoles = userData.roles ? userData.roles.map(r => r.authority) : [];
    const hasOrganizerRole = userRoles.includes('ROLE_ORGANIZER') || userRoles.includes('ORGANIZER');
    
    if (!hasOrganizerRole) {
        showErrorMessageToUser("Accesso negato. Questa sezione è riservata agli organizzatori.");
        window.location.href = 'index.html';
        return;
    }

    loadOrganizerEvents(tokenEmail);
}

function handleFormSubmit(event) {
    event.preventDefault(); 

    const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
    const userData = getIdentityFromToken(token);

    if (!token || !userData) {
        alert("Sessione scaduta o non valida. Effettua nuovamente il login.");
        return;
    }

    const selectedTags = Array.from(document.getElementById('tags-select-container').selectedOptions)
        .map(opt => parseInt(opt.value, 10)); 

    const selectedSpeakers = Array.from(document.getElementById('speakers-select-container').selectedOptions)
        .map(opt => parseInt(opt.value, 10)); 

    const eventPayload = {
        name: document.getElementById('event-name').value,
        eventDate: document.getElementById('event-date').value,
        startTime: document.getElementById('start-time').value,
        endTime: document.getElementById('end-time').value,
        priceStandard: parseFloat(document.getElementById('price-standard').value),
        priceVip: parseFloat(document.getElementById('price-vip').value),
        venueId: parseInt(document.getElementById('venue-select').value, 10), 
        tagIds: selectedTags,       
        speakerIds: selectedSpeakers 
    };

    fetch(ENDPOINTS.createEvent, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify(eventPayload)
    })
    .then(function(response) {
        if (!response.ok) throw new Error("Impossibile salvare l'evento. Stato HTTP: " + response.status);
        return response.json();
    })
    .then(function(savedEventDto) {
        alert("Evento creato con successo!");
        document.getElementById('create-event-form').reset(); 
        loadOrganizerEvents(userData.sub);
    })
    .catch(function(error) {
        console.error("Errore nell'invio del form:", error);
        alert("Impossibile salvare l'evento.");
    });
}

function loadOrganizerEvents(email) {
    const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = 'Bearer ' + token;

    fetch(ENDPOINTS.getOrganizerEvents(email), { headers })
        .then(function(response) {
            if (!response.ok) throw new Error("Errore nel recupero degli eventi.");
            return response.json();
        })
        .then(function(data) {
            const events = data.content || data; 
            renderEventCards(events);
        })
        .catch(function(error) {
            console.error("Errore nel caricamento eventi:", error);
            showErrorMessageToUser("Impossibile caricare i tuoi eventi attivi.");
        });
}

function renderEventCards(events) {
    const container = document.getElementById('events-container');
    if (!container) return;

    container.innerHTML = ''; 

    if (events.length === 0) {
        container.innerHTML = '<p>Non hai ancora creato nessun evento.</p>';
        return;
    }

    events.forEach(function(event) {
        const tagsText = event.tags && event.tags.length > 0 ? event.tags.join(', ') : 'Nessuno';
        const speakersText = event.speakerNames && event.speakerNames.length > 0 ? event.speakerNames.join(', ') : 'Nessuno';

        const cardHtml = `
            <div class="event-card" id="card-${event.id}">
                <span class="event-id-badge">ID: ${event.id}</span>
                <h3>${event.name}</h3>

                <div class="event-details">
                    <div class="detail-item"><span>Data:</span> ${event.eventDate}</div>
                    <div class="detail-item"><span>Struttura:</span> ${event.venueName ? event.venueName : 'Non specificata'}</div>
                    <div class="detail-item"><span>Inizio:</span> ${event.startTime || '--:--'}</div>
                    <div class="detail-item"><span>Fine:</span> ${event.endTime || '--:--'}</div>
                    <div class="detail-item"><span>Prezzo Standard:</span> € ${event.priceStandard || '0.00'}</div>
                    <div class="detail-item"><span>Prezzo VIP:</span> € ${event.priceVip || '0.00'}</div>
                    <div class="detail-item"><span>TAG:</span> ${tagsText}</div>
                    <div class="detail-item"><span>Speakers:</span> ${speakersText}</div>
                </div>

                <div class="edit-event-box">
                    <h4>Modifica Evento</h4>
                    <div class="edit-grid">
                        <div class="form-group-mini" style="grid-column: span 2">
                            <label>Nome Evento</label>
                            <input type="text" class="edit-name" value="${event.name}" />
                        </div>
                        <div class="form-group-mini">
                            <label>Data</label>
                            <input type="date" class="edit-date" value="${event.eventDate}" />
                        </div>
                        <div class="form-group-mini">
                            <label>Struttura (Venue)</label>
                            <select class="edit-venue-select"></select>
                        </div>
                        <div class="form-group-mini">
                            <label>Orario di Inizio</label>
                            <input type="time" class="edit-start-time" value="${event.startTime}" />
                        </div>
                        <div class="form-group-mini">
                            <label>Orario di Fine</label>
                            <input type="time" class="edit-end-time" value="${event.endTime}" />
                        </div>
                        <div class="form-group-mini">
                            <label>Prezzo Standard (€)</label>
                            <input type="number" step="0.01" class="edit-price-standard" value="${event.priceStandard}" />
                        </div>
                        <div class="form-group-mini">
                            <label>Prezzo VIP (€)</label>
                            <input type="number" step="0.01" class="edit-price-vip" value="${event.priceVip}" />
                        </div>
                        
                        <div class="form-group-mini" style="grid-column: span 2">
                            <label>Modifica i Tag associati (Tieni premuto Ctrl / Cmd per scelte multiple):</label>
                            <select class="edit-tags-select modern-multiselect" multiple required></select>
                        </div>
                        <div class="form-group-mini" style="grid-column: span 2">
                            <label>Modifica gli Speakers invitati (Tieni premuto Ctrl / Cmd per scelte multiple):</label>
                            <select class="edit-speakers-select modern-multiselect" multiple required></select>
                        </div>
                    </div>
                    <div class="edit-box-actions">
                        <button class="btn-event-cancel">Annulla</button>
                        <button class="btn-event-save">Salva Modifiche</button>
                    </div>
                </div>

                <div class="organizer-actions">
                    <button class="btn-edit">Modifica</button>
                    <button class="btn-delete">Elimina</button>
                    <a href="feedbacks.html?eventId=${event.id}" class="btn-reviews">Vedi Recensioni</a>
                    <a href="participants.html?eventId=${event.id}" class="btn-users">Lista Partecipanti</a>
                </div>
            </div>
        `;
        
        container.insertAdjacentHTML('beforeend', cardHtml);

        const currentCard = document.getElementById(`card-${event.id}`);
        const editBox = currentCard.querySelector('.edit-event-box');
        const btnEdit = currentCard.querySelector('.btn-edit');
        const btnDelete = currentCard.querySelector('.btn-delete');
        const btnCancel = currentCard.querySelector('.btn-event-cancel');
        const btnSave = currentCard.querySelector('.btn-event-save');

        btnEdit.addEventListener('click', function() {
            const isActive = editBox.classList.contains('active');
            if (!isActive) {
                setupEditVenueSelect(currentCard.querySelector('.edit-venue-select'), event.venueId);
                
                setupEditMultiselect(
                    currentCard.querySelector('.edit-tags-select'), 
                    globalTags, 
                    event.tags, 
                    'tag'
                );
                setupEditMultiselect(
                    currentCard.querySelector('.edit-speakers-select'), 
                    globalSpeakers, 
                    event.speakerNames, 
                    'speaker'
                );

                editBox.classList.add('active');
            } else {
                editBox.classList.remove('active');
            }
        });

        btnCancel.addEventListener('click', function() {
            editBox.classList.remove('active');
        });

        btnDelete.addEventListener('click', function() {
            const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
            const userData = getIdentityFromToken(token);

            if (!token || !userData) {
                alert("Sessione scaduta. Effettua nuovamente il login.");
                return;
            }

            const confirmDeletion = confirm(`Sei sicuro di voler eliminare definitivamente l'evento "${event.name}"? L'azione cancellerà anche i biglietti associati.`);
            if (!confirmDeletion) return;

            fetch(ENDPOINTS.deleteEvent(event.id), {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                }
            })
            .then(function(response) {
                if (response.status === 200 || response.status === 204) {
                    alert("Evento eliminato con successo!");
                    loadOrganizerEvents(userData.sub);
                } else if (response.status === 403) {
                    throw new Error("Non sei autorizzato a eliminare questo evento.");
                } else if (response.status === 404) {
                    throw new Error("L'evento risulta già rimosso dal sistema.");
                } else {
                    throw new Error("Errore durante la cancellazione. Stato server: " + response.status);
                }
            })
            .catch(function(error) {
                console.error("Errore durante la DELETE dell'evento:", error);
                alert(error.message || "Impossibile completare la rimozione dell'evento.");
            });
        });

        btnSave.addEventListener('click', function() {
            const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
            const userData = getIdentityFromToken(token);

            if (!token || !userData) {
                alert("Sessione scaduta. Effettua nuovamente il login.");
                return;
            }

            var startValue = currentCard.querySelector('.edit-start-time').value;
            var endValue = currentCard.querySelector('.edit-end-time').value;
            
            if (startValue && startValue.split(':').length === 2) startValue += ':00';
            if (endValue && endValue.split(':').length === 2) endValue += ':00';

            const updatedTagIds = Array.from(currentCard.querySelector('.edit-tags-select').selectedOptions)
                .map(opt => parseInt(opt.value, 10));

            const updatedSpeakerIds = Array.from(currentCard.querySelector('.edit-speakers-select').selectedOptions)
                .map(opt => parseInt(opt.value, 10));

            const updatedPayload = {
                name: currentCard.querySelector('.edit-name').value,
                eventDate: currentCard.querySelector('.edit-date').value,
                startTime: startValue,
                endTime: endValue,
                priceStandard: parseFloat(currentCard.querySelector('.edit-price-standard').value),
                priceVip: parseFloat(currentCard.querySelector('.edit-price-vip').value),
                venueId: parseInt(currentCard.querySelector('.edit-venue-select').value, 10),
                tagIds: updatedTagIds,         
                speakerIds: updatedSpeakerIds   
            };

            fetch(ENDPOINTS.updateEvent(event.id), {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify(updatedPayload)
            })
            .then(function(response) {
                if (response.status === 200) return response.json();
                if (response.status === 400) throw new Error("Dati non validi (controlla i vincoli sui prezzi o le date).");
                if (response.status === 403) throw new Error("Non sei autorizzato a modificare questo evento.");
                throw new Error("Errore imprevisto lato server. Stato: " + response.status);
            })
            .then(function(updatedEventDto) {
                alert("Evento modificato con successo!");
                editBox.classList.remove('active');
                loadOrganizerEvents(userData.sub); 
            })
            .catch(function(error) {
                console.error("Errore durante la PUT dell'evento:", error);
                alert(error.message || "Impossibile aggiornare l'evento.");
            });
        });
    });
}

function setupEditMultiselect(selectElement, globalItemsList, currentEventItemsNames, type) {
    if (!selectElement) return;
    selectElement.innerHTML = '';
    
    var currentList = currentEventItemsNames || [];

    globalItemsList.forEach(function(item) {
        var labelText = item.name;
        if (type === 'speaker') {
            labelText = item.name + " " + item.surname;
        }

        const option = new Option(labelText, item.id);
        if (currentList.includes(labelText)) {
            option.selected = true;
        }
        selectElement.add(option);
    });
}

function setupEditVenueSelect(selectElement, currentVenueId) {
    const mainSelect = document.getElementById('venue-select');
    if (!selectElement || !mainSelect) return;
    
    selectElement.innerHTML = '';
    
    Array.from(mainSelect.options).forEach(option => {
        if(option.value !== "") { 
            const newOption = new Option(option.text, option.value);
            if(parseInt(option.value, 10) === currentVenueId) {
                newOption.selected = true;
            }
            selectElement.add(newOption);
        }
    });
}

function initializeEventForm() {
    const token = localStorage.getItem('EventHubToken') || sessionStorage.getItem('EventHubToken');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = 'Bearer ' + token;

    Promise.all([
        fetch(ENDPOINTS.speakers, { headers }),
        fetch(ENDPOINTS.venues, { headers }),
        fetch(ENDPOINTS.tags, { headers })
    ])
    .then(function(responses) {
        if (!responses[0].ok || !responses[1].ok || !responses[2].ok) {
            throw new Error("Errore nel recupero dati per l'inizializzazione del form.");
        }
        return Promise.all([responses[0].json(), responses[1].json(), responses[2].json()]);
    })
    .then(function(data) {
        globalSpeakers = data[0];
        globalTags = data[2];

        populateSpeakersComponent(data[0]);
        populateVenuesSelect(data[1]);
        populateTagsComponent(data[2]);
    })
    .catch(function(error) {
        console.error("Errore inizializzazione form:", error);
        showErrorMessageToUser("Impossibile caricare le strutture o i relatori.");
    });
}

function populateVenuesSelect(venues) {
    const select = document.getElementById('venue-select');
    if (select) {
        select.innerHTML = '<option value="">Seleziona una struttura...</option>';
        venues.forEach(function(venue) {
            select.add(new Option(venue.name, venue.id));
        });
    }
}

function populateSpeakersComponent(speakers) {
    const select = document.getElementById('speakers-select-container');
    if (select) {
        select.innerHTML = '';
        speakers.forEach(function(speaker) {
            const fullName = speaker.name + " " + speaker.surname;
            select.add(new Option(fullName, speaker.id));
        });
    }
}

function populateTagsComponent(tags) {
    const select = document.getElementById('tags-select-container');
    if (select) {
        select.innerHTML = '';
        tags.forEach(function(tag) {
            select.add(new Option(tag.name, tag.id));
        });
    }
}

function showErrorMessageToUser(message) {
    alert(message);
}