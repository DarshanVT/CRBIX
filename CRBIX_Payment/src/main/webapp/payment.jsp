<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="paymentGateway.Cart" %>
<%
    Cart cart = (Cart) request.getAttribute("cart");
    if(cart == null){ response.sendRedirect(request.getContextPath() + "/CRBIX/cart.html"); return; }
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>Payment</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/CRBIX/cart.css">

</head>
<body>

<div class="main-container">
  
  <!-- BANK OFFER SECTION -->
  <div class="bank-offer">
    <h3>Bank Offer</h3>
    <p>10% Instant Discount on ICICI Bank Credit Card, Credit Card EMI, Netbanking on a mini spend of ₹3,500</p>
    <p>Get 7.5% Cashback + Additional 2.5% Instant Discount on SBI card</p>
    <div class="show-more">Show More</div>
  </div>
  
  <!-- CHOOSE PAYMENT MODE TITLE -->
  <div class="payment-mode-title">Choose Payment Mode</div>
  
  <div class="container">
    
    <!-- LEFT PAYMENT OPTIONS MENU -->
    <div class="sidebar">
      <div class="menu-item active" onclick="showSection('cod')">
        <span>Cash On Delivery (Cash/UPI) </span>
      </div>
      <div class="menu-item" onclick="showSection('upi')">
        <span>UPI (Pay via any App)</span>
      </div>
      <div class="menu-item" onclick="showSection('card')">
        <span>Credit/Debit Card</span>
      </div>
      <div class="menu-item" onclick="showSection('paylater')">
        <span>Pay Later</span>
      </div>
      <div class="menu-item" onclick="showSection('wallets')">
        <span>Wallets</span>
      </div>
      <div class="menu-item" onclick="showSection('emi')">
        <span>EMI</span>
      </div>
      <div class="menu-item" onclick="showSection('netbanking')">
        <span>Net Banking</span>
      </div>
    </div>
    
    <!-- CENTER PAYMENT CONTENT AREA -->
    <div class="content-box">
      
      <!-- COD SECTION -->
      <div id="cod">
        <h3 class="section-title">Cash on Delivery</h3>
        <p>You will pay when the product is delivered.</p>
        <button onclick="processCOD()">Continue</button>
      </div>

      <!-- UPI SECTION -->
      <div id="upi" class="hidden">
        <h3 class="section-title">UPI Payment</h3>
        <div class="upi-options">
          <button onclick="showUPIOption('scan')">Scan & Pay</button>
          <button onclick="showUPIOption('enter')">Enter UPI ID</button>
        </div>
        <div id="upi-scan" class="hidden">
          <p>Scan the QR code shown to complete payment.</p>
          <img src="scanner.jpg" alt="QR Scanner" class="scanner-img">
          <button onclick="upiPayment()">Pay Now</button>
        </div>
        <div id="upi-enter" class="hidden">
          <input id="upiInput" type="text" placeholder="Enter UPI ID (e.g., name@okicici)">
          <button onclick="upiPayment()">Pay Now</button>
        </div>
      </div>

      <!-- CARD SECTION -->
      <div id="card" class="hidden">
        <h3 class="section-title">Credit / Debit Card</h3>
        <input type="text" id="card-number" placeholder="Card Number" maxlength="19" />
        <input type="text" placeholder="Name on Card" />
        <div class="row">
          <input type="text" id="expiry" placeholder="MM/YY" maxlength="5" />
          <input type="password" id="cvv" placeholder="CVV" maxlength="3" />
        </div>
        <button onclick="validateCard()">Pay Now</button>
      </div>
      
      <!-- OTHER SECTIONS -->
      <div id="paylater" class="hidden">
        <h3 class="section-title">Pay Later</h3>
        <p>Select a pay later option to complete your purchase.</p>
        <button onclick="alert('Pay Later feature coming soon!')">Continue</button>
      </div>
      
      <div id="wallets" class="hidden">
        <h3 class="section-title">Wallets</h3>
        <p>Select a wallet option to complete your payment.</p>
        <button onclick="alert('Wallet payment coming soon!')">Continue</button>
      </div>
      
      <div id="emi" class="hidden">
        <h3 class="section-title">EMI Options</h3>
        <p>Select your EMI plan to complete the payment.</p>
        <button onclick="alert('EMI feature coming soon!')">Continue</button>
      </div>
      
      <div id="netbanking" class="hidden">
        <h3 class="section-title">Net Banking</h3>
        <p>Select your bank to proceed with net banking payment.</p>
        <button onclick="alert('Net Banking feature coming soon!')">Continue</button>
      </div>
      
    </div>
    
   
    
  </div>
</div>

<script>
  // Set initial active section
  document.addEventListener('DOMContentLoaded', function() {
    showSection('cod');
  });

  /* Switch Sections */
  function showSection(id) {
    // Hide all sections
    document.querySelectorAll('.content-box > div').forEach(sec => sec.classList.add('hidden'));
    document.getElementById(id).classList.remove('hidden');

    // Update active menu item
    document.querySelectorAll('.menu-item').forEach(m => m.classList.remove('active'));
    
    // Find and activate the corresponding menu item
    const menuItems = document.querySelectorAll('.menu-item');
    menuItems.forEach(item => {
      if (item.textContent.includes(getMenuTextForSection(id))) {
        item.classList.add('active');
      }
    });
  }

  // Helper function to map section IDs to menu text
  function getMenuTextForSection(id) {
    const map = {
      'cod': 'Cash On Delivery',
      'upi': 'UPI',
      'card': 'Credit/Debit Card',
      'paylater': 'Pay Later',
      'wallets': 'Wallets',
      'emi': 'EMI',
      'netbanking': 'Net Banking'
    };
    return map[id] || '';
  }

  /* UPI Option Show/Hide */
  function showUPIOption(opt) {
    document.getElementById('upi-scan').classList.add('hidden');
    document.getElementById('upi-enter').classList.add('hidden');

    if (opt === 'scan') document.getElementById('upi-scan').classList.remove('hidden');
    else document.getElementById('upi-enter').classList.remove('hidden');
  }

  /* FINAL UPI VALIDATION */
  function upiPayment() {
    // Check if the Enter UPI ID section is visible
    let enterBox = document.getElementById("upi-enter");
    let isEnterUPIVisible = !enterBox.classList.contains("hidden");

    // If Scan & Pay → Pay Now is clicked → DO NOTHING
    if (!isEnterUPIVisible) {
      return; 
    }

    // If Enter UPI ID → Validate the format
    let upiId = document.getElementById("upiInput").value;

    let upiPattern = /^[a-zA-Z0-9.\-_]{2,}@[a-zA-Z]{2,}$/;

    if (!upiPattern.test(upiId)) {
      alert("Please enter a valid UPI ID (example: name@upi)");
      return;
    }

    alert("UPI payment initiated...");
  }

  /* CARD VALIDATION */

  document.getElementById("card-number").addEventListener("input", function() {
    let value = this.value.replace(/\D/g, "").substring(0, 16);
    let formatted = value.replace(/(.{4})/g, "$1 ").trim();
    this.value = formatted;
  });

  /* Auto-insert slash in MM/YY */
  document.getElementById("expiry").addEventListener("input", function() {
    let value = this.value.replace(/\D/g, "").substring(0, 4);
    if (value.length >= 3) {
      this.value = value.substring(0,2) + "/" + value.substring(2);
    } else {
      this.value = value;
    }
  });

  /* Card & CVV Validation */
  function validateCard() {
    const cardNumber = document.getElementById("card-number").value.replace(/\s/g,"");
    const cvv = document.getElementById("cvv").value.trim();

    if (!/^\d{16}$/.test(cardNumber)) {
      alert("Card number must be exactly 16 digits.");
      return;
    }

    if (!/^\d{3}$/.test(cvv)) {
      alert("CVV must be exactly 3 digits.");
      return;
    }

    alert("Card details are valid. Payment Successful!");
  }

  /* Other payment methods */
  function processCOD() {
    alert("Order placed successfully! Pay when your product is delivered.");
  }

  function processPayment() {
    alert("Order placed successfully!");
  }
</script>

</body>
</html>