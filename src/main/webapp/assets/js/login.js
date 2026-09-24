const BASE_URL = '/FashionStore';

document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    fetch(`${BASE_URL}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            // Cache user in sessionStorage so navbar updates instantly on next page
            sessionStorage.setItem('user', JSON.stringify({ name: data.name, email: email }));

            // Redirect to the page they came from, or home
            const returnTo = sessionStorage.getItem('loginReturnTo');
            sessionStorage.removeItem('loginReturnTo');
            window.location.href = returnTo || 'home.html';
        } else {
            alert(data.message || 'Login failed. Please check your credentials.');
        }
    })
    .catch(err => {
        console.error('Login error:', err);
        alert('An error occurred. Please try again.');
    });
});
