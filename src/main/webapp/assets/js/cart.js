const BASE_URL = '/FashionStore';
let isLoggedIn = false;

document.addEventListener('DOMContentLoaded', () => {
    loadCart();
});

function loadCart() {
    fetch(`${BASE_URL}/cart`)
        .then(res => res.json())
        .then(data => {
            if(data.success) {
                isLoggedIn = data.isLoggedIn || false;
                renderCart(data.cartItems);
                document.getElementById('cartCount').textContent = data.itemCount || 0;
            } else {
                if(data.message && data.message.toLowerCase().includes('login')) {
                    window.location.href = 'login.html';
                }
            }
        });
}

function renderCart(items) {
    const container = document.getElementById('cartItems');
    const summary = document.getElementById('cartSummary');
    
    if(!items || items.length === 0) {
        container.innerHTML = '<h3 style="text-align:center; padding: 40px; color:#777;">Your cart is empty. <br><br><a href="product-list.html" style="color:#e67e22;text-decoration:none;">Go Shopping</a></h3>';
        summary.style.display = 'none';
        return;
    }
    
    let html = '';
    let total = 0;
    
    items.forEach(item => {
        const name = item.productName || item.product?.name || 'Product';
        const price = item.price || item.product?.price || 0;
        const img = item.imageUrl || item.product?.imageUrl || 'https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg';
        const qty = item.quantity || 1;
        
        total += (price * qty);
        
        const imgUrl = img.startsWith('http') ? img : BASE_URL + (img.startsWith('/') ? '' : '/') + img;
        
        html += `
            <div class="cart-item">
                <img src="${imgUrl}" alt="${name}">
                <div class="item-details">
                    <h4>${name}</h4>
                    <p>Qty: ${qty}</p>
                </div>
                <div class="item-actions">
                    <p style="font-weight:bold; font-size:1.1rem;">&#8377;${(price * qty).toLocaleString('en-IN')}</p>
                    <button class="remove-btn" onclick="removeItem(${item.cartId || item.id})">Remove</button>
                </div>
            </div>
        `;
    });
    
    container.innerHTML = html;
    summary.style.display = 'block';
    
    document.getElementById('cartSubtotal').innerHTML = '&#8377;' + total.toLocaleString('en-IN');
    document.getElementById('cartTotal').innerHTML = '&#8377;' + total.toLocaleString('en-IN');
}

function removeItem(cartId) {
    fetch(`${BASE_URL}/cart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `action=remove&cartId=${cartId}`
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            loadCart();
        } else {
            alert('Error removing item');
        }
    });
}

function checkout() {
    if (!isLoggedIn) {
        alert("Please login to proceed to checkout!");
        window.location.href = 'login.html';
        return;
    }

    fetch(`${BASE_URL}/checkout`, {
        method: 'POST'
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            alert(data.message || "Order placed successfully!");
            window.location.href = 'home.html';
        } else {
            alert(data.message || "Checkout failed");
            if (data.message && data.message.toLowerCase().includes('login')) {
                window.location.href = 'login.html';
            }
        }
    });
}
