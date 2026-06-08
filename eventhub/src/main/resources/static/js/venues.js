document.addEventListener('DOMContentLoaded', function() {
    
    var container = document.getElementById('venues-container');
    var BASE_URL = 'http://localhost:8080/api/venues'; 

    loadAllVenues();

    container.addEventListener('click', function(event) {
        var clickedElement = event.target;

        var isEdit = clickedElement.classList.contains('btn-edit');
        var isDelete = clickedElement.classList.contains('btn-delete');

        if (isEdit || isDelete) {
            var card = clickedElement.closest('.venue-card');
            var idBadge = card.querySelector('.venue-id-badge');
            var idText = idBadge.textContent; 
            var id = idText.replace('ID: ', '').trim();

            if (isEdit) {
                redirectToEditPage(id);
            } else if (isDelete) {
                deleteVenue(id, card);
            }
        }
    });

    function loadAllVenues() {
        fetch(BASE_URL)
            .then(function(response) {
                if (!response.ok) throw new Error("Error loading venues");
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
                        '<div class="venue-details">' +
                            '<div class="detail-item"><span>Città :</span> ' + venue.city + '</div>' +
                            '<div class="detail-item"><span>Capienza :</span> ' + venue.capacity + ' persone</div>' +
                            '<div class="detail-item"><span>Indirizzo :</span> ' + venue.address + '</div>' +
                        '</div>' +
                        '<hr />' +
                        '<div class="admin-actions">' +
                            '<button class="btn-edit">Modifica Location</button>' +
                            '<button class="btn-delete">Elimina</button>' +
                        '</div>';

                    container.appendChild(venueCard);
                }
            })
            .catch(function(error) {
                console.error("Error:", error);
                alert("Errore durante il caricamento delle location.");
            });
    }

    function redirectToEditPage(id) {
        window.location.href = 'edit-venue.html?id=' + id;
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