<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.DeliverySlot" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delivery Booking Details | Bakery Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .glass-card { max-width: 760px; margin: 3rem auto; }
        .meta-label {
            font-size: 0.8rem;
            text-transform: uppercase;
            color: #636e72;
            letter-spacing: 0.04em;
            margin-bottom: 0.2rem;
        }
        .meta-value {
            font-weight: 700;
            margin-bottom: 0.9rem;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/bookings/products">
            <span>CakeForge</span>
        </a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings/products">Catalog</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings/my-orders">Bookings</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/delivery">Delivery</a></li>
            </ul>
        </div>
    </div>
</nav>
<%
    DeliverySlot slot = (DeliverySlot) request.getAttribute("slot");
%>
<header class="page-header container">
    <h1>Delivery Details</h1>
    <p>Review selected slot information.</p>
</header>
<div class="glass-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold mb-0">Delivery Booking Details</h3>
        <span class="badge bg-light text-dark border"><%= slot.getId() %></span>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="meta-label">Customer</div>
            <div class="meta-value"><%= slot.getCustomerName() %></div>
        </div>
        <div class="col-md-6">
            <div class="meta-label">Delivery Time</div>
            <div class="meta-value"><%= slot.getDeliveryTime() %></div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="meta-label">Delivery Date</div>
            <div class="meta-value"><%= slot.getDeliveryDate() %></div>
        </div>
        <div class="col-md-6">
            <div class="meta-label">Address</div>
            <div class="meta-value"><%= slot.getAddress() %></div>
        </div>
    </div>

    <div class="d-flex gap-2 mt-3">
        <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/delivery">
            <i class="fa-solid fa-arrow-left me-2"></i>Back to Scheduler
        </a>
        <a class="btn btn-warning text-white" href="${pageContext.request.contextPath}/delivery/edit?slotId=<%= slot.getId() %>">
            <i class="fa-solid fa-pen me-2"></i>Edit Slot
        </a>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
