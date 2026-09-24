const BASE_URL = '/FashionStore';
let currentProduct = null;
let variants = [];
let selectedSize = null;
let selectedColor = null;

document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const productId = params.get('productId');
    if (productId) {
        loadProductDetail(productId);
    } else {
        document.getElementById('productContainer').innerHTML = '<h2>Product not found</h2>';
    }
    loadCartCount();
});

function loadProductDetail(id) {
    fetch(`${BASE_URL}/product-detail?productId=${id}`)
        .then(res => res.json())
        .then(data => {
            if(data.success) {
                currentProduct = data.product;
                variants = data.variants || [];
                renderProduct(data);
            }
        });
}

function renderProduct(data) {
    const container = document.getElementById('productContainer');
    const p = data.product;
    
    let sizesHtml = data.sizes.map(s => `<button class="size-btn" onclick="selectSize('${s}', this)">${s}</button>`).join('');
    let colorsHtml = data.colors.map(c => `<button class="color-btn" onclick="selectColor('${c}', this)">${c}</button>`).join('');

        const imgUrl = p.imageUrl && p.imageUrl.startsWith('http') 
            ? p.imageUrl 
            : BASE_URL + (p.imageUrl ? (p.imageUrl.startsWith('/') ? '' : '/') + p.imageUrl : '/assets/images/placeholder.jpg');

        container.innerHTML = `
        <div class="product-image">
            <img src="${imgUrl}" alt="${p.name}" onerror="this.src='https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg'">
        </div>
        <div class="product-info">
            <p style="color:#888; text-transform:uppercase; font-weight:bold; letter-spacing:1px;">${p.brand || 'Premium Brand'}</p>
            <h1>${p.name}</h1>
            <div class="price">&#8377;${p.price.toLocaleString('en-IN')}</div>
            <p>${p.description || 'Experience the best quality and comfort.'}</p>
            
            <div class="options-group">
                <h4>Select Size</h4>
                <div>${sizesHtml || '<span style="color:red">Out of stock</span>'}</div>
            </div>
            
            <div class="options-group">
                <h4>Select Color</h4>
                <div>${colorsHtml || '<span style="color:red">Out of stock</span>'}</div>
            </div>
            
            <button class="add-to-cart-btn" onclick="addToCart()">Add to Cart</button>
        </div>
    `;
}

function selectSize(size, btn) {
    selectedSize = size;
    document.querySelectorAll('.size-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
}

function selectColor(color, btn) {
    selectedColor = color;
    document.querySelectorAll('.color-btn').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
}

function addToCart() {
    if(!selectedSize || !selectedColor) {
        alert("Please select both size and color!");
        return;
    }
    
    const variant = variants.find(v => v.size === selectedSize && v.color === selectedColor);
    if(!variant) {
        alert("This combination is currently out of stock!");
        return;
    }
    
    fetch(`${BASE_URL}/cart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `action=add&variantId=${variant.variantId}&quantity=1`
    })
    .then(res => res.json())
    .then(data => {
        if(data.success) {
            alert('Awesome! Added to cart successfully.');
            loadCartCount();
        } else if(data.requireLogin) {
            sessionStorage.setItem('loginReturnTo', window.location.href);
            alert('Please login to add items to cart!');
            window.location.href = 'login.html';
        } else {
            alert(data.message || 'Failed to add to cart!');
        }
    });
}

function loadCartCount() {
    fetch(BASE_URL + '/cart')
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                document.getElementById('cartCount').textContent = data.itemCount || 0;
            }
        })
        .catch(() => {});
}
