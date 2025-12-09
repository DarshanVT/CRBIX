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
<meta charset="utf-8"><title>Payment Summary</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/CRBIX/cart.css">

<style>
body {
    font-family: "Segoe UI", Arial, sans-serif;
    background: #eef1f4;
    margin: 0;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
}

.container {
    width: 100%;
    max-width: 450px;
    background: #ffffff;
    border-radius: 14px;
    box-shadow: 0 4px 18px rgba(0,0,0,0.08);
    padding: 28px 30px 35px 30px;
}

.title {
    text-align: center;
    color: #222;
    font-size: 22px;
    margin-bottom: 25px;
    border-bottom: 1px solid #eee;
    padding-bottom: 12px;
}

.amount-section {
    margin-bottom: 20px;
}

.amount-row {
    display: flex;
    justify-content: space-between;
    margin-bottom: 12px;
    padding: 8px 0;
    border-bottom: 1px dashed #e5e5e5;
}

.amount-row:last-child {
    border-bottom: none;
}

.total-row {
    font-weight: 700;
    font-size: 17px;
    margin-top: 15px;
    border-top: 2px solid #e8e8e8;
    padding-top: 12px;
}

.final-row {
    background: #f4f6fa;
    padding: 14px;
    border-radius: 8px;
    margin-top: 10px;
    font-size: 18px;
    color: #2d3e50;
    border: 1px solid #e1e4e8;
}

.label {
    color: #555;
}

.value {
    font-weight: 600;
    color: #111;
}

.discount {
    color: #1fa463;
}

.gst {
    color: #0b77ff;
}

/* CENTERED BUTTONS */
.buttons {
    display: flex;
    justify-content: center;
    gap: 15px;
    margin-top: 30px;
}

.btn {
    padding: 10px 20px;
    font-size: 14px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: 0.2s;
    min-width: 110px;
}

/* Proceed button */
.proceed-btn {
    background: #28a745;
    color: white;
}
.proceed-btn:hover {
    background: #22963b;
}

/* Cancel button */
.cancel-btn {
    background: #6c757d;
    color: white;
}
.cancel-btn:hover {
    background: #5a6268;
}
</style>
</head>

<body>

<div class="container">
    <h2 class="title">Payment Details</h2>

    <div class="amount-section">
        <div class="amount-row">
            <span class="label">Subtotal</span>
            <span class="value">₹ <%= request.getAttribute("subtotal") %></span>
        </div>

        <div class="amount-row">
            <span class="label">GST(18%)</span>
            <span class="value gst">₹ <%= request.getAttribute("gst") %></span>
        </div>

        <div class="amount-row total-row">
            <span class="label">Total Amount</span>
            <span class="value">₹ <%= request.getAttribute("total") %></span>
        </div>

        <div class="amount-row">
            <span class="label">Discount Applied</span>
            <span class="value discount">- ₹${discount}</span>
        </div>

        <div class="amount-row final-row">
            <span class="label">Final Amount</span>
            <span class="value">₹${finalAmount}</span>
        </div>
    </div>

    <div class="buttons">
        <form action="success.jsp"></form>
        <button class="btn proceed-btn" onclick="proceedClicked()">Proceed</button>
        <form action="failed.jsp"></form>
        <button class="btn cancel-btn" onclick="cancelClicked()">Cancel</button>
    </div>
</div>



</body>
</html>