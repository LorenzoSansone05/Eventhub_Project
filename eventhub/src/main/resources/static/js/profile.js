var BASE_URL = 'http://localhost:8080/api/profiles';

var currentPhotoUrl = '';

document.addEventListener('DOMContentLoaded', function() {

    var fileInput = document.getElementById('profile-picture');
    var profileForm = document.getElementById('profile-form');

    loadUserProfile();
    
    if (fileInput) {
        fileInput.onchange = handleImageUpload;
    }

    if (profileForm) {
        profileForm.onsubmit = saveUserProfile;
    }

    function handleImageUpload() {
        var profilePreview = document.getElementById('profile-preview');

        if (fileInput && fileInput.files && fileInput.files[0]) {
            var file = fileInput.files[0];
            var reader = new FileReader();

            reader.onload = function(e) {
                profilePreview.src = e.target.result;
                currentPhotoUrl = e.target.result;
            };

            reader.readAsDataURL(file);
        }
    }

    function loadUserProfile() {
        var token = localStorage.getItem('EventHubToken');
        
        var options = {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        };

        fetch(BASE_URL + '/me', options)
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Errore nel recupero del profilo');
                }
                return response.json();
            })
            .then(function(profile) {
                console.log('Data received:', profile);

                document.getElementById('first-name').value = profile.name || '';
                document.getElementById('last-name').value = profile.surname || '';
                document.getElementById('birth-date').value = profile.birthDate || '';
                document.getElementById('city').value = profile.city || '';
                document.getElementById('description').value = profile.description || '';
                
                if (profile.photoUrl) {
                    document.getElementById('profile-preview').src = profile.photoUrl;
                    currentPhotoUrl = profile.photoUrl;
                }
            })
            .catch(function(error) {
                console.error('Problem encountered:', error);
            });
    }

    function saveUserProfile(event) {
        event.preventDefault();

        var token = localStorage.getItem('EventHubToken');

        var dataToSend = {
            name: document.getElementById('first-name').value,
            surname: document.getElementById('last-name').value,
            photoUrl: currentPhotoUrl,
            birthDate: document.getElementById('birth-date').value,
            city: document.getElementById('city').value,
            description: document.getElementById('description').value
        };

        var options = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(dataToSend)
        };

        fetch(BASE_URL + '/me', options)
            .then(function(response) {
                if (!response.ok) {
                    throw new Error('Errore durante il salvataggio');
                }
                return response.json();
            })
            .then(function(updatedProfile) {
                alert('Profilo salvato con successo!');
            })
            .catch(function(error) {
                console.error('Save failed:', error);
                alert('Impossibile salvare i dati del profilo. Controlla che tutti i campi siano validi.');
            });
    }
});