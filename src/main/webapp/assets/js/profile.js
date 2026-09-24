const BASE_URL = '/FashionStore';

document.addEventListener('DOMContentLoaded', () => {
    loadProfile();
    
    document.getElementById('profileForm').addEventListener('submit', (e) => {
        e.preventDefault();
        updateProfile();
    });
});

function loadProfile() {
    fetch(`${BASE_URL}/profile`)
        .then(res => res.json())
        .then(data => {
            if(data.success && data.user) {
                document.getElementById('name').value = data.user.name || '';
                document.getElementById('email').value = data.user.email || '';
                document.getElementById('phone').value = data.user.phone || '';
                document.getElementById('address').value = data.user.address || '';
            } else {
                alert("Please login to view profile");
                window.location.href = 'login.html';
            }
        })
        .catch(err => {
            console.error("Error loading profile", err);
            window.location.href = 'login.html';
        });
}

function updateProfile() {
    const form = document.getElementById('profileForm');
    const formData = new URLSearchParams(new FormData(form)).toString();
    
    fetch(`${BASE_URL}/profile`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            alert(data.message || 'Profile updated successfully!');
            // Reload to update username in navbar
            window.location.reload();
        } else {
            alert(data.message || 'Failed to update profile');
        }
    })
    .catch(err => {
        console.error("Error updating profile", err);
        alert('An error occurred while updating profile.');
    });
}
