document.addEventListener('DOMContentLoaded', function() {
    
    var container = document.getElementById('venues-container');
    var createForm = document.getElementById('create-venue-form');
    var BASE_URL = 'http://localhost:8080/api/venues'; 

    loadAllVenues();

    if (createForm) {
        createForm.addEventListener('submit', function(event) {
            event.preventDefault();

            var venueData = {
                name: document.getElementById('venue-name').value.trim(),
                capacity: parseInt(document.getElementById('venue-capacity').value, 10),
                city: document.getElementById('venue-city').value.trim(),
                address: document.getElementById('venue-address').value.trim()
            };

            createVenue(venueData);
        });
    }

    container.addEventListener('click', function(event) {
        var clickedElement = event.target;
        var card = clickedElement.closest('.venue-card');
        
        if (!card) return;

        var idBadge = card.querySelector('.venue-id-badge');
        var id = idBadge.textContent.replace('ID: ', '').trim();

        if (clickedElement.classList.contains('btn-edit')) {
            var editContainer = card.querySelector('.edit-form-container');
            if (editContainer) {
                editContainer.style.display = 'block';
            }
        }

        if (clickedElement.classList.contains('btn-cancel')) {
            var editContainer = card.querySelector('.edit-form-container');
            if (editContainer) {
                editContainer.style.display = 'none';
            }
        }

        if (clickedElement.classList.contains('btn-delete')) {
            deleteVenue(id, card);
        }
    });

    container.addEventListener('submit', function(event) {
        if (event.target.classList.contains('edit-venue-form')) {
            event.preventDefault();

            var form = event.target;
            var card = form.closest('.venue-card');
            var idBadge = card.querySelector('.venue-id-badge');
            var id = idBadge.textContent.replace('ID: ', '').trim();

            var inputs = form.querySelectorAll('input');
            
            var updatedData = {
                name: inputs[0].value.trim(),
                capacity: parseInt(inputs[1].value, 10),
                city: inputs[2].value.trim(),
                address: inputs[3].value.trim()
            };

            updateVenue(id, updatedData, card);
        }
    });

    function loadAllVenues() {
        fetch(BASE_URL)
            .then(function(response) {
                if (!response.ok) throw new Error("Errore nel caricamento delle location");
                return response.json();
            })
            .then(function(venues) {
                container.innerHTML = '';

                for (var i = 0; i < venues.length; i++) {
                    var venue = venues[i];
                    var venueCard = document.createElement('div');
                    venueCard.className = 'venue-card';

                    venueCard.innerHTML = 
                        '<span class="venue-id-badge">ID: ' + venue.id + '</span>' +
                        '<h3>' + venue.name + '</h3>' +
                        
                        '' +
                        '<div class="edit-form-container" style="display: none;">' +
                            '<h4 class="edit-form-title">Modifica Dettagli Location</h4>' +
                            '<form class="edit-venue-form">' +
                                '<div class="venue-details">' +
                                    '<div class="form-group-venue">' +
                                        '<label>Nome Location</label>' +
                                        '<input type="text" value="' + venue.name + '" required />' +
                                    '</div>' +
                                    '<div class="form-group-venue">' +
                                        '<label>Capacità Massima</label>' +
                                        '<input type="number" value="' + venue.capacity + '" min="10" required />' +
                                    '</div>' +
                                    '<div class="form-group-venue">' +
                                        '<label>Città</label>' +
                                        '<input type="text" value="' + venue.city + '" required />' +
                                    '</div>' +
                                    '<div class="form-group-venue">' +
                                        '<label>Indirizzo</label>' +
                                        '<input type="text" value="' + venue.address + '" required />' +
                                    '</div>' +
                                '</div>' +
                                '<div class="form-actions">' +
                                    '<button type="button" class="btn-cancel">Annulla</button>' +
                                    '<button type="submit" class="btn-update">Aggiorna</button>' +
                                '</div>' +
                            '</form>' +
                        '</div>' +

                        '' +
                        '<div class="venue-details">' +
                            '<div class="detail-item"><span>Città :</span> ' + venue.city + '</div>' +
                            '<div class="detail-item"><span>Capienza :</span> ' + venue.capacity.toLocaleString('it-IT') + ' persone</div>' +
                            '<div class="detail-item"><span>Indirizzo :</span> ' + venue.address + '</div>' +
                        '</div>' +
                        '<div class="admin-actions">' +
                            '<button class="btn-edit">Modifica Location</button>' +
                            '<button class="btn-delete">Elimina</button>' +
                        '</div>';

                    container.appendChild(venueCard);
                }
            })
            .catch(function(error) {
                console.error("Error:", error);
                container.innerHTML = '<p style="color: white; padding: 20px;">Errore durante il caricamento delle location.</p>';
            });
    }

    function createVenue(venueData) {
        fetch(BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(venueData)
        })
        .then(function(response) {
            if (response.status === 201 || response.ok) {
                return response.json();
            } else {
                throw new Error("Errore durante la creazione della location");
            }
        })
        .then(function(newVenue) {
            alert("Location '" + newVenue.name + "' creata con successo!");
            createForm.reset();
            loadAllVenues();
        })
        .catch(function(error) {
            console.error("Error:", error);
            alert("Errore durante il salvataggio. Controlla i dati inseriti.");
        });
    }

    function updateVenue(id, updatedData, card) {
        fetch(BASE_URL + '/' + id, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        })
        .then(function(response) {
            if (!response.ok) throw new Error("Aggiornamento fallito");
            return response.json();
        })
        .then(function(updatedVenue) {
            alert("Location aggiornata con successo!");
            loadAllVenues();
        })
        .catch(function(error) {
            console.error("Error:", error);
            alert("Impossibile aggiornare la location. Riprova più tardi.");
        });
    }

    function deleteVenue(id, card) {
        var confirmDelete = confirm("Sei sicuro di voler eliminare definitivamente la location ID " + id + "?");
        if (!confirmDelete) return;

        fetch(BASE_URL + '/' + id, {
            method: 'DELETE'
        })
        .then(function(response) {
            if (response.status === 204 || response.ok) {
                alert("Location eliminata con successo.");
                card.remove();
            } else {
                throw new Error("Delete failed");
            }
        })
        .catch(function(error) {
            console.error("Error:", error);
            alert("Errore durante l'eliminazione. La location potrebbe non esistere più.");
        });
    }
});