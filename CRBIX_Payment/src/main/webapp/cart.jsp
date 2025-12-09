<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trending Products</title>
  <link rel="stylesheet" href="cart.css">
    
</head>
<body>
    <div class="container">
        <h1>Trending Products</h1>
        <form action="payment_summury.jsp" method="post"></form>
        <!-- First Row -->
        <div class="products-grid">
            <!-- Product 1 -->
            <div class="product-card" data-id="1">
                <div class="image-container">
                    <img src="images/bruton.jpg" alt="BRUTON Sneakers" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">BRUTON</h3>
                    <p class="product-name">EVA Lite Sneakers</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="436">₹436</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <input type="number" name="qty_asian" value="1" min="0">
                			<input type="hidden" name="price_asian" value="436">
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>

            <!-- Product 2 -->
            <div class="product-card" data-id="2">
                <div class="image-container">
                    <img src="images/bacca.jpg" alt="Bacca Bucci Shoes" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">Bacca Bucci</h3>
                    <p class="product-name">Men Lace Up Sneaker Shoes</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="1046">₹1,046</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <button class="quantity-btn decrease">-</button>
                            <span class="quantity-display">1</span>
                            <button class="quantity-btn increase">+</button>
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>

            <!-- Product 3 -->
            <div class="product-card" data-id="3">
                <div class="image-container">
                    <img src="images/doctor.jpg" alt="Doctor Extra Soft Shoes" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">DOCTOR EXTRA SOFT</h3>
                    <p class="product-name">Men's Running & Walking Shoes</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="1199">₹1,199</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <button class="quantity-btn decrease">-</button>
                            <span class="quantity-display">1</span>
                            <button class="quantity-btn increase">+</button>
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Second Row -->
        <div class="products-grid">
            <!-- Product 4 -->
            <div class="product-card" data-id="4">
                <div class="image-container">
                    <img src="images/campus.jpg" alt="Campus Sneakers" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">Campus</h3>
                    <p class="product-name">Trendy EVA Lite Sneakers Casual Shoes</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="433">₹433</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <button class="quantity-btn decrease">-</button>
                            <span class="quantity-display">1</span>
                            <button class="quantity-btn increase">+</button>
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>

            <!-- Product 5 -->
            <div class="product-card" data-id="5">
                <div class="image-container">
                    <img src="images/asian.jpg" alt="ASIAN Sneakers" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">ASIAN</h3>
                    <p class="product-name">Men's MEXICO-11 Casual Sneaker Shoes</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="569">₹569</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <button class="quantity-btn decrease">-</button>
                            <span class="quantity-display">1</span>
                            <button class="quantity-btn increase">+</button>
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>

            <!-- Product 6 -->
            <div class="product-card" data-id="6">
                <div class="image-container">
                    <img src="images/langer.jpg" alt="Langer Running Shoes" class="product-image">
                </div>
                <div class="product-content">
                    <h3 class="product-brand">Langer</h3>
                    <p class="product-name">Men First Running Shoes</p>
                    
                    <div class="price-section">
                        <span class="current-price" data-price="1284">₹1284</span>
                    </div>
                    
                    <div class="button-container">
                        <div class="quantity-controls">
                            <button class="quantity-btn decrease">-</button>
                            <span class="quantity-display">1</span>
                            <button class="quantity-btn increase">+</button>
                        </div>
                        <button class="add-to-cart-btn">Add</button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Cart Section - Now at bottom -->
        <div class="cart-section">
            <div class="cart-header">
                <h2 class="cart-title">Your Shopping Cart</h2>
                <div class="cart-summary">
                    Items: <span id="cart-count">0</span> | 
                    Total: <span id="cart-total" class="cart-total-amount">₹0</span>
                </div>
            </div>
            <div class="cart-items" id="cart-items">
                <div class="empty-cart">Your cart is empty</div>
            </div>
            
            <!-- Proceed Button Section -->
            <div class="proceed-section">
                <button class="proceed-btn" id="proceed-btn" disabled>Proceed</button>
            </div>
        </div>
    </div>

    <script>
        // Cart data
        let cart = [];
        
        // DOM elements
        const cartCountElement = document.getElementById('cart-count');
        const cartTotalElement = document.getElementById('cart-total');
        const cartItemsElement = document.getElementById('cart-items');
        const proceedBtn = document.getElementById('proceed-btn');
        
        // Format price with commas
        function formatPrice(price) {
            return '₹' + price.toLocaleString('en-IN');
        }
        
        // Update cart summary
        function updateCartSummary() {
            let totalItems = 0;
            let totalPrice = 0;
            
            cart.forEach(item => {
                totalItems += item.quantity;
                totalPrice += item.price * item.quantity;
            });
            
            cartCountElement.textContent = totalItems;
            cartTotalElement.textContent = formatPrice(totalPrice);
            
            // Show/hide cart items section and enable/disable proceed button
            if (cart.length > 0) {
                cartItemsElement.classList.add('show');
                proceedBtn.disabled = false;
                renderCartItems();
            } else {
                cartItemsElement.classList.remove('show');
                proceedBtn.disabled = true;
            }
        }
        
        // Render cart items
        function renderCartItems() {
            let cartHTML = '';
            
            cart.forEach(item => {
                const itemTotal = item.price * item.quantity;
                cartHTML += `
                    <div class="cart-item" data-id="${item.id}">
                        <img src="${item.image}" alt="${item.name}" class="cart-item-image">
                        <div class="cart-item-details">
                            <div class="cart-item-name">${item.brand} - ${item.name}</div>
                            <div class="cart-item-price">${formatPrice(item.price)} each</div>
                        </div>
                        <div class="cart-item-quantity">
                            <div class="quantity-controls">
                                <button class="quantity-btn decrease" onclick="updateCartItem(${item.id}, -1)">-</button>
                                <span class="quantity-display">${item.quantity}</span>
                                <button class="quantity-btn increase" onclick="updateCartItem(${item.id}, 1)">+</button>
                            </div>
                        </div>
                        <div class="cart-item-total">${formatPrice(itemTotal)}</div>
                    </div>
                `;
            });
            
            cartItemsElement.innerHTML = cartHTML;
        }
        
        // Update cart item quantity
        function updateCartItem(productId, change) {
            const itemIndex = cart.findIndex(item => item.id === productId);
            
            if (itemIndex !== -1) {
                cart[itemIndex].quantity += change;
                
                // Remove item if quantity becomes 0
                if (cart[itemIndex].quantity <= 0) {
                    cart.splice(itemIndex, 1);
                }
                
                updateCartSummary();
                updateProductQuantityDisplay(productId);
            }
        }
        
        // Update product quantity display
        function updateProductQuantityDisplay(productId) {
            const productCard = document.querySelector(`.product-card[data-id="${productId}"]`);
            if (!productCard) return;
            
            const quantityDisplay = productCard.querySelector('.quantity-display');
            const cartItem = cart.find(item => item.id === productId);
            
            if (cartItem) {
                quantityDisplay.textContent = cartItem.quantity;
            } else {
                quantityDisplay.textContent = '1';
            }
        }
        
        // Show notification
        function showNotification(message) {
            // Remove existing notifications
            document.querySelectorAll('.notification').forEach(notification => {
                notification.remove();
            });
            
            // Create new notification
            const notification = document.createElement('div');
            notification.className = 'notification';
            notification.textContent = message;
            
            document.body.appendChild(notification);
            
            // Remove notification after 3 seconds
            setTimeout(() => {
                notification.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => {
                    if (notification.parentNode) {
                        notification.remove();
                    }
                }, 300);
            }, 3000);
        }
        
        // Proceed to checkout functionality
        proceedBtn.addEventListener('click', function() {
            if (cart.length === 0) return;
            
            const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
            const totalPrice = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
            
            showNotification(`Proceeding to checkout with ${totalItems} items. Total: ${formatPrice(totalPrice)}`);
            
           
        });
        
        // Add to cart functionality
        document.querySelectorAll('.add-to-cart-btn').forEach(button => {
            button.addEventListener('click', function() {
                const productCard = this.closest('.product-card');
                const productId = parseInt(productCard.dataset.id);
                const productBrand = productCard.querySelector('.product-brand').textContent;
                const productName = productCard.querySelector('.product-name').textContent;
                const productImage = productCard.querySelector('.product-image').src;
                const productPrice = parseInt(productCard.querySelector('.current-price').dataset.price);
                const quantity = parseInt(productCard.querySelector('.quantity-display').textContent);
                
                // Check if product is already in cart
                const existingItemIndex = cart.findIndex(item => item.id === productId);
                
                if (existingItemIndex !== -1) {
                    // Update existing item
                    cart[existingItemIndex].quantity = quantity;
                    showNotification(`Updated ${productBrand} ${productName} (Quantity: ${quantity})`);
                } else {
                    // Add new item to cart
                    cart.push({
                        id: productId,
                        brand: productBrand,
                        name: productName,
                        image: productImage,
                        price: productPrice,
                        quantity: quantity
                    });
                    showNotification(`Added ${quantity} × ${productBrand} ${productName} to cart!`);
                }
                
                updateCartSummary();
            });
        });
        
        // Quantity controls for products
        document.querySelectorAll('.quantity-btn').forEach(button => {
            button.addEventListener('click', function(e) {
                e.stopPropagation();
                
                const controls = this.closest('.quantity-controls');
                const display = controls.querySelector('.quantity-display');
                let quantity = parseInt(display.textContent);
                
                if (this.classList.contains('increase')) {
                    quantity++;
                } else if (this.classList.contains('decrease') && quantity > 1) {
                    quantity--;
                }
                
                display.textContent = quantity;
                
                // Update cart if item is already in cart
                const productCard = controls.closest('.product-card');
                const productId = parseInt(productCard.dataset.id);
                const cartItem = cart.find(item => item.id === productId);
                
                if (cartItem) {
                    cartItem.quantity = quantity;
                    updateCartSummary();
                }
            });
        });
        
        // Initialize
        updateCartSummary();
    </script>
</body>
</html>