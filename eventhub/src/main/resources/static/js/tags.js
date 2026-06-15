const API_URL = 'http://localhost:8080/api/tags';
const token = localStorage.getItem('EventHubToken');

const headersConfig = {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + token
};


document.addEventListener('DOMContentLoaded', function () {

    loadTagsFromServer();

    const btnAdd = document.querySelector('.btn-add');
    if (btnAdd) {
        btnAdd.addEventListener('click', function () {
            const input = document.getElementById('new-tag-name');
            const tagName = input.value.trim();

            if (tagName === "") {
                alert("Inserisci il tag da creare");
                return;
            }

            fetch(API_URL, {
                method: 'POST',
                headers: headersConfig,
                body: JSON.stringify({ name: tagName })
            }).then(function (response) {
                if (response.ok) {
                    alert("Tag creato con successo!");
                    input.value = ""; 
                    loadTagsFromServer(); 
                } else {
                    alert("Errore durante la creazione del tag");
                }
            });
        });
    }

    const container = document.getElementById('tags-container');
    if (container) {
        container.addEventListener('click', function (event) {
            const target = event.target;
            const card = target.closest('.tag-card');
            if (!card) return;
            
            const tagId = card.id.replace('tag-card-', '');

            
            if (target.classList.contains('btn-toggle-edit')) {
                document.getElementById('edit-tag-box-' + tagId).classList.add('active');
            }

            if (target.classList.contains('btn-edit-cancel')) {
                document.getElementById('edit-tag-box-' + tagId).classList.remove('active');
            }

            if (target.classList.contains('btn-tag-delete')) {
                if (confirm("Sei sicuro di voler eliminare questo tag?")) {
                    fetch(API_URL + '/' + tagId, {
                        method: 'DELETE',
                        headers: headersConfig
                    }).then(function (response) {
                        if (response.ok) {
                            alert("Tag cancellato!");
                            card.remove();
                        } else {
                            alert("Errore durante l'eliminazione del tag");
                        }
                    });
                }
            }

            if (target.classList.contains('btn-edit-save')) {
                const inputEdit = document.getElementById('input-tag-' + tagId);
                const newName = inputEdit.value.trim();

                if (newName === "") {
                    alert("Aggiungi il nuovo tag per favore");
                    return;
                }

                fetch(API_URL + '/' + tagId, {
                    method: 'PUT',
                    headers: headersConfig,
                    body: JSON.stringify({ name: newName })
                }).then(function (response) {
                    if (response.ok) {
                        alert("Tag modificato!");
                        card.querySelector('h3').textContent = '#' + newName.toUpperCase();
                        document.getElementById('edit-tag-box-' + tagId).classList.remove('active');
                    } else {
                        alert("Errore durante la modifica del tag");
                    }
                });
            }
        });
    }
});

function loadTagsFromServer() {
    fetch(API_URL, {
        method: 'GET',
        headers: headersConfig
    })
    .then(function (response) {
        return response.json();
    })
    .then(function (tagList) {
        const container = document.getElementById('tags-container');
        container.innerHTML = '';

        tagList.forEach(function (tag) {
            container.innerHTML += `
                <div class="tag-card" id="tag-card-${tag.id}">
                    <span class="tag-id-badge">ID: ${tag.id}</span>
                    <h3>#${tag.name}</h3>
                    
                    <div class="tag-details">
                        <div class="detail-item edit-tag-box" id="edit-tag-box-${tag.id}">
                            <span>Nuovo tag:</span>
                            <input type="text" class="tag-input-edit" id="input-tag-${tag.id}" value="${tag.name}">
                            <button class="btn-edit-save btn-edit">Salva</button>
                            <button class="btn-edit-cancel btn-delete">Cancella</button>
                        </div>
                    </div>
                    
                    <hr />
                    <div class="admin-actions">
                        <button class="btn-toggle-edit btn-edit">Modifica</button>
                        <button class="btn-tag-delete btn-delete">Elimina</button>
                    </div>
                </div>
            `;
        });
    })
    .catch(function (error) {
        console.error("Errore durante il caricamento dei tag da parte del server: ", error);
    });
}