const BASE_URL = '/FashionStore';

document.addEventListener('DOMContentLoaded', function () {
    loadHomeData();
    loadCartCount();
    setupSearch();
});

function loadHomeData() {
    showSkeletons();
    fetch(BASE_URL + '/home')
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.success) {
                renderCategories(data.categories);
                renderFeaturedProducts(data.featuredProducts);
            }
        })
        .catch(function(err) { console.error('Error:', err); });
}

function renderCategories(categories) {
    const grid = document.getElementById('categoriesGrid');
    grid.innerHTML = '';

    if (!categories || categories.length === 0) {
        grid.innerHTML = '<p>No categories found</p>';
        return;
    }

    const categoryImages = {
        'Men':   'assets/images/men1.jpg',
        'Women': 'assets/images/women1.jpg',
        'Kids':  'assets/images/kids1.jpg',
        'Shoes': 'assets/images/men4.jpg',
        'Accessories': 'assets/images/women4.jpg'
    };

    const categoryClass = {
        'Men':   'men',
        'Women': 'women',
        'Kids':  'kids',
        'Shoes': 'shoes',
        'Accessories': 'accessories'
    };

    categories.forEach(function(cat) {
        const card = document.createElement('div');
        card.className = 'category-card ' + (categoryClass[cat.name] || '');
        card.onclick = function() {
            window.location.href = 'product-list.html?categoryId=' + cat.categoryId;
        };
        const imgPath = categoryImages[cat.name] || 'assets/images/men1.jpg';
        const imgUrl = BASE_URL + (imgPath.startsWith('/') ? '' : '/') + imgPath;

        card.innerHTML =
            '<img src="' + imgUrl + '" ' +
            'alt="' + cat.name + '" ' +
            'style="width:100%; height:100%; object-fit:cover; position:absolute; top:0; left:0;">' +
            '<div class="category-card-overlay"><span>' + cat.name + '</span></div>';
        grid.appendChild(card);
    });
}

function renderFeaturedProducts(products) {
    const grid = document.getElementById('productsGrid');
    grid.innerHTML = '';

    if (!products || products.length === 0) {
        grid.innerHTML = '<p style="text-align:center; padding:20px;">No products found.</p>';
        return;
    }

    products.forEach(function(product) {
        const card = document.createElement('div');
        card.className = 'product-card';
        card.onclick = function() {
            window.location.href = 'product-detail.html?productId=' + product.productId;
        };
        const imgUrl = product.imageUrl && product.imageUrl.startsWith('http') 
            ? product.imageUrl 
            : BASE_URL + (product.imageUrl ? (product.imageUrl.startsWith('/') ? '' : '/') + product.imageUrl : '/assets/images/placeholder.jpg');

        card.innerHTML =
            '<img src="' + imgUrl + '" ' +
            'alt="' + product.name + '" ' +
            'style="width:100%; height:250px; object-fit:cover; background:#f5f0eb;" ' +
            'onerror="this.src=\'https://images.pexels.com/photos/1656684/pexels-photo-1656684.jpeg\'">' +
            '<div class="product-card-info">' +
            '<p class="brand">' + (product.brand || '') + '</p>' +
            '<h4>' + product.name + '</h4>' +
            '<p class="price">&#8377;' + product.price.toLocaleString('en-IN') + '</p>' +
            '</div>';
        grid.appendChild(card);
    });
}

function loadCartCount() {
    fetch(BASE_URL + '/cart')
        .then(function(res) { return res.json(); })
        .then(function(data) {
            if (data.success) {
                document.getElementById('cartCount').textContent = data.itemCount;
            }
        })
        .catch(function() {});
}

function setupSearch() {
    document.getElementById('searchBtn').addEventListener('click', function() {
        document.getElementById('searchBar').classList.toggle('active');
    });
}

function searchProducts() {
    const keyword = document.getElementById('searchInput').value.trim();
    if (keyword) {
        window.location.href = 'product-list.html?keyword=' + encodeURIComponent(keyword);
    }
}

function showSkeletons() {
    const grid = document.getElementById('productsGrid');
    grid.innerHTML = '';
    for (let i = 0; i < 8; i++) {
        grid.innerHTML += '<div class="skeleton"></div>';
    }
}