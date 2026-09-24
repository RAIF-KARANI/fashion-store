// Globals accessible by other scripts on the same page
window.isLoggedIn = false;
window.loggedUserName = '';

document.addEventListener('DOMContentLoaded', () => {
    // Initial check from cache if available
    const cachedUser = sessionStorage.getItem('user');
    if (cachedUser) {
        const user = JSON.parse(cachedUser);
        updateNavbar(true, user.name);
    }

    checkLoginStatus();
});

function updateNavbar(isLoggedIn, name) {
    const loginBtn = document.getElementById('loginBtn');
    if (!loginBtn) return;

    if (isLoggedIn) {
        window.isLoggedIn = true;
        window.loggedUserName = name;
        loginBtn.href = 'profile.html';
        loginBtn.title = 'Edit Profile';
        loginBtn.innerHTML = `
            <span style="font-size:0.85rem; margin-right:4px; font-weight:600; max-width:100px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">${name}</span>
            <i class="fas fa-user-circle" style="font-size:1.2rem;"></i>
        `;
        loginBtn.style.cssText = 'display:flex; align-items:center; gap:4px; text-decoration:none; color:inherit;';
    } else {
        window.isLoggedIn = false;
        window.loggedUserName = '';
        loginBtn.href = 'login.html';
        loginBtn.title = 'Login';
        loginBtn.innerHTML = `<i class="fas fa-user"></i>`;
        loginBtn.style.cssText = '';
    }
}

function checkLoginStatus() {
    fetch('/FashionStore/profile')
        .then(res => res.json())
        .then(data => {
            if (data.success && data.user) {
                sessionStorage.setItem('user', JSON.stringify(data.user));
                updateNavbar(true, data.user.name);
            } else {
                sessionStorage.removeItem('user');
                updateNavbar(false, '');
            }
        })
        .catch(err => {
            console.error("Auth check failed:", err);
            window.isLoggedIn = false;
        });
}

function logout() {
    fetch('/FashionStore/logout')
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                sessionStorage.removeItem('user');
                window.isLoggedIn = false;
                window.loggedUserName = '';
                window.location.href = 'login.html';
            }
        });
}
