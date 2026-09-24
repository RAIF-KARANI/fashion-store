const BASE_URL = '/FashionStore';

document.getElementById('registerForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const address = document.getElementById('address').value;
    const password = document.getElementById('password').value;

    fetch(`${BASE_URL}/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `name=${encodeURIComponent(name)}&email=${encodeURIComponent(email)}&phone=${encodeURIComponent(phone)}&address=${encodeURIComponent(address)}&password=${encodeURIComponent(password)}`
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            alert('Registration successful! Please login.');
            window.location.href = 'login.html';
        } else {
            alert(data.message || 'Registration failed. Please check your inputs.');
        }
    })
    .catch(err => {
        console.error('Registration error:', err);
        alert('An error occurred. Please try again.');
    });
});

document.addEventListener('DOMContentLoaded', () => {
    fetch(BASE_URL + '/cart')
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById('cartCount').textContent = data.itemCount || 0;
            }
        })
        .catch(() => {});
});
