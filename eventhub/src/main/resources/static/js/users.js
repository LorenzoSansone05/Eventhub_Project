const API_BASE_URL = 'http://localhost:8080/api/users'; 

document.addEventListener('DOMContentLoaded', () => {
    loadUsers();
});

function getIdentityFromToken(token) {
    try {
        if (!token) return null;
        const payload = token.split(".")[1]; 
        const payloadString = atob(payload); 
        const userData = JSON.parse(payloadString); 
        return userData.sub; 
    } catch (e) {
        console.error("Errore nella decodifica del token JWT:", e);
        return null;
    }
}

async function loadUsers() {
    const token = localStorage.getItem('token');
    const headers = { 'Content-Type': 'application/json' };
    if (token) headers['Authorization'] = `Bearer ${token}`;

    try {
        const response = await fetch(API_BASE_URL, { headers });
        if (!response.ok) throw new Error('Impossibile recuperare gli utenti dal server');
        
        const users = await response.json();
        renderUsersList(users, token);
    } catch (error) {
        console.error("Errore di comunicazione con il backend:", error);
        const container = document.getElementById('users-container');
        container.innerHTML = `<div class="detail-item">Errore nel caricamento degli utenti. Controllare che il server sia attivo su localhost.</div>`;
    }
}

function renderUsersList(users, token) {
    const container = document.getElementById('users-container');
    container.innerHTML = '';

    const currentAdminEmail = getIdentityFromToken(token);

    users.forEach(user => {
        if (user.email === currentAdminEmail) {
            return; 
        }

        const card = document.createElement('div');
        card.className = 'user-card';
        card.id = `user-card-${user.id}`;

        const banBtnClass = user.banned ? 'btn-ban-inactive' : 'btn-ban';
        const banBtnText = user.banned ? 'Sblocca' : 'Banna';
        const statusText = user.banned ? '🔴 Bannato' : '🟢 Attivo';

        card.innerHTML = `
            <span class="user-id-badge">ID: ${user.id}</span>
            <h3>${user.email}</h3>
            
            <div class="user-details">
                <div class="detail-item"><span>Ruolo Attuale:</span> ${user.roleName || 'Nessuno'}</div>
                <div class="detail-item"><span>Stato Account:</span> ${statusText}</div>
                
                <div class="detail-item edit-role-box" id="edit-role-box-${user.id}">
                    <span>Nuovo Ruolo:</span>
                    <select class="role-select" id="select-role-${user.id}">
                        <option value="ROLE_USER" ${user.roleName === 'ROLE_USER' ? 'selected' : ''}>USER</option>
                        <option value="ROLE_ORGANIZER" ${user.roleName === 'ROLE_ORGANIZER' ? 'selected' : ''}>ORGANIZER</option>
                    </select>
                    <button class="btn-edit-save btn-edit">Salva</button>
                    <button class="btn-edit-cancel btn-delete">Annulla</button>
                </div>
            </div>
            
            <hr />
            <div class="admin-actions">
                <button class="btn-toggle-edit btn-edit">Modifica Ruolo</button>
                <button class="btn-action-ban ${banBtnClass}">${banBtnText}</button>
                <button class="btn-user-delete btn-delete">Elimina</button>
            </div>
        `;

        
        const editBox = card.querySelector(`#edit-role-box-${user.id}`);
        const btnToggleEdit = card.querySelector('.btn-toggle-edit');
        const btnEditCancel = card.querySelector('.btn-edit-cancel');
        const btnEditSave = card.querySelector('.btn-edit-save');
        const btnBan = card.querySelector('.btn-action-ban');
        const btnDelete = card.querySelector('.btn-user-delete');

        btnToggleEdit.addEventListener('click', () => {
            editBox.classList.toggle('active');
        });

        btnEditCancel.addEventListener('click', () => {
            editBox.classList.remove('active');
        });

        btnEditSave.addEventListener('click', async () => {
            const selectRole = card.querySelector(`#select-role-${user.id}`);
            await submitRoleUpdate(user.id, selectRole.value);
        });

        btnBan.addEventListener('click', async () => {
            await toggleBanUser(user.id, user.banned);
        });

        btnDelete.addEventListener('click', async () => {
            await deleteUser(user.id);
        });

        container.appendChild(card);
    });
}

async function toggleBanUser(id, currentBanStatus) {
    const token = localStorage.getItem('token');
    const newBanStatus = !currentBanStatus; 
    const actionName = newBanStatus ? 'bannare' : 'sbannare';

    if (!confirm(`Vuoi veramente ${actionName} l'utente con ID ${id}?`)) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}/ban?banned=${newBanStatus}`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.ok) {
            const msg = await response.text();
            alert(msg);
            loadUsers(); 
        } else {
            alert("Errore durante l'aggiornamento dello stato di ban sul server.");
        }
    } catch (error) {
        console.error("Errore di rete durante il ban:", error);
        alert("Errore di rete: impossibile raggiungere il server backend.");
    }
}

async function deleteUser(id) {
    if (!confirm(`Sei sicuro di voler eliminare definitivamente l'utente con ID ${id}?`)) return;
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });

        if (response.status === 204 || response.ok) {
            alert('Utente eliminato con successo.');
            const card = document.getElementById(`user-card-${id}`);
            if (card) card.remove();
        } else {
            alert("Errore del server durante l'eliminazione dell'utente.");
        }
    } catch (error) {
        console.error("Errore di rete durante la cancellazione:", error);
        alert("Errore di rete: impossibile raggiungere il server backend.");
    }
}

async function submitRoleUpdate(id, selectedRole) {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_BASE_URL}/${id}/role`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ newRole: selectedRole })
        });

        if (response.ok) {
            alert('Ruolo utente aggiornato con successo.');
            loadUsers(); 
        } else {
            alert("Errore del server durante la modifica del ruolo.");
        }
    } catch (error) {
        console.error("Errore di rete durante il cambio ruolo:", error);
        alert("Errore di rete: impossibile raggiungere il server backend.");
    }
}