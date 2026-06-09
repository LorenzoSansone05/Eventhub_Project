const API_URL = 'http://localhost:8080/api/speakers';
const token = localStorage.getItem('EventHubToken');

const headersConfig = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
};

document.addEventListener('DOMContentLoaded', function () {

    loadSpeakersFromServer();

    const createForm = document.getElementById('create-speaker-form');
    if (createForm) {
        createForm.addEventListener('submit', function (event) {
            event.preventDefault();

            const inputName = document.getElementById('speaker-name');
            const inputSurname = document.getElementById('speaker-surname');
            const textareaBio = document.getElementById('speaker-bio');

            const nameValue = inputName.value.trim();
            const surnameValue = inputSurname.value.trim();
            const bioValue = textareaBio.value.trim();

            if (nameValue === "" || surnameValue === "" || bioValue === "") {
                alert("Tutti i campi sono obbligatori per creare un relatore!");
                return;
            }

            const dataToSend = {
                name: nameValue,
                surname: surnameValue,
                bio: bioValue
            };

            fetch(API_URL, {
                method: 'POST',
                headers: headersConfig,
                body: JSON.stringify(dataToSend)
            }).then(function (response) {
                if (response.ok) {
                    alert("Relatore aggiunto con successo!");
                    inputName.value = "";
                    inputSurname.value = "";
                    textareaBio.value = "";
                    loadSpeakersFromServer();
                } else {
                    alert("Errore durante l'aggiunta del relatore.");
                }
            }).catch(function (error) {
                console.error("Errore durante la fetch di creazione:", error);
            });
        });
    }

    const container = document.getElementById('speakers-container');
    if (container) {
        
        container.addEventListener('click', function (event) {
            const target = event.target;
            const card = target.closest('.speaker-card');
            if (!card) return;
            
            const speakerId = card.id.replace('speaker-card-', '');
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

                if (confirm("Sei sicuro di voler eliminare in modo permanente questo relatore?")) {
                    fetch(API_URL + '/' + speakerId, {
                        method: 'DELETE',
                        headers: headersConfig
                    }).then(function (response) {
                        if (response.ok || response.status === 204) {
                            alert("Relatore eliminato con successo!");
                            card.remove();
                        } else {
                            alert("Errore durante l'eliminazione del relatore.");
                        }
                    }).catch(function (error) {
                        console.error("Errore durante la fetch di eliminazione:", error);
                    });
                }
            }
        });

        container.addEventListener('submit', function (event) {
            event.preventDefault();
            
            const target = event.target;
            if (target.classList.contains('edit-speaker-form')) {
                const card = target.closest('.speaker-card');
                const speakerId = card.id.replace('speaker-card-', '');
                
                const inputs = target.querySelectorAll('input[type="text"]');
                const textarea = target.querySelector('textarea');
                
                const newName = inputs[0].value.trim();
                const newSurname = inputs[1].value.trim();
                const newBio = textarea.value.trim();

                if (newName === "" || newSurname === "" || newBio === "") {
                    alert("I campi di modifica non possono essere vuoti!");
                    return;
                }

                const dataToSend = {
                    name: newName,
                    surname: newSurname,
                    bio: newBio
                };

                fetch(API_URL + '/' + speakerId, {
                    method: 'PUT',
                    headers: headersConfig,
                    body: JSON.stringify(dataToSend)
                })
                .then(function (response) {
                    if (response.ok) {
                        return response.json(); 
                    } else {
                        throw new Error("Errore durante l'aggiornamento del relatore.");
                    }
                })
                .then(function (updatedSpeaker) {
                    alert("Relatore aggiornato con successo!");
                    
                    card.querySelector('h3').textContent = updatedSpeaker.name + " " + updatedSpeaker.surname;
                    
                    const details = card.querySelectorAll('.detail-item');
                    if (details.length >= 3) {
                        details[0].innerHTML = '<span>Nome :</span> ' + updatedSpeaker.name;
                        details[1].innerHTML = '<span>Cognome :</span> ' + updatedSpeaker.surname;
                        details[2].innerHTML = '<span>Biografia :</span> ' + updatedSpeaker.bio;
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

function loadSpeakersFromServer() {
    fetch(API_URL, {
        method: 'GET',
        headers: headersConfig
    })
    .then(function (response) {
        if (!response.ok) {
            throw new Error("Impossibile caricare l'anagrafica dei relatori dal server.");
        }
        return response.json();
    })
    .then(function (speakerList) {
        const container = document.getElementById('speakers-container');
        if (!container) return;

        container.innerHTML = '';

        speakerList.forEach(function (speaker) {
            container.innerHTML += `
                <div class="speaker-card" id="speaker-card-${speaker.id}">
                  <span class="speaker-id-badge">ID: ${speaker.id}</span>

                  <h3>${speaker.name} ${speaker.surname}</h3>

                  <div class="speaker-display-details">
                    <div class="detail-item"><span>Nome :</span> ${speaker.name}</div>
                    <div class="detail-item"><span>Cognome :</span> ${speaker.surname}</div>
                    <div class="detail-item full-width">
                      <span>Biografia :</span> ${speaker.bio}
                    </div>
                  </div>

                  <div class="edit-form-container" id="edit-form-box-${speaker.id}">
                    <h4 class="edit-form-title">Modifica Dettagli Relatore</h4>
                    <form class="edit-speaker-form">
                      <div class="speaker-details-grid">
                        <div class="form-group-speaker">
                          <label>Nome</label>
                          <input type="text" value="${speaker.name}" required />
                        </div>
                        <div class="form-group-speaker">
                          <label>Cognome</label>
                          <input type="text" value="${speaker.surname}" required />
                        </div>
                      </div>
                      <div class="form-group-speaker" style="margin-top: 15px;">
                        <label>Biografia</label>
                        <textarea rows="4" required>${speaker.bio}</textarea>
                      </div>
                      <div class="form-actions">
                        <button type="button" class="btn-cancel">Annulla</button>
                        <button type="submit" class="btn-update">Aggiorna</button>
                      </div>
                    </form>
                  </div>

                  <hr />
                  <div class="admin-actions">
                    <button class="btn-edit">Modifica Relatore</button>
                    <button class="btn-delete">Elimina</button>
                  </div>
                </div>
            `;
        });
    })
    .catch(function (error) {
        console.error("Errore riscontrato nel recupero dei relatori:", error);
    });
}