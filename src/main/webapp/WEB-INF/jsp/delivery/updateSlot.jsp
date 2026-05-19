<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.DeliverySlot" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Delivery Booking | Bakery Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>.glass-card { max-width: 760px; margin: 3rem auto; }</style>
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/catalog">
            <span>CakeForge</span>
        </a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/catalog">Catalog</a></li>
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
    <h1>Update Delivery Booking</h1>
    <p>Edit route details and reschedule as needed.</p>
</header>
<div class="glass-card">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold mb-0">Update Delivery Booking</h3>
        <span class="badge bg-light text-dark border"><%= slot.getId() %></span>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/delivery/update">
        <input type="hidden" name="slotId" value="<%= slot.getId() %>" />

        <div class="row g-2">
            <div class="col-md-6">
                <label class="form-label">Customer Name</label>
                <input class="form-control" type="text" name="customerName" value="<%= slot.getCustomerName() %>" required />
            </div>
            <div class="col-md-6">
                <label class="form-label">Address</label>
                <input class="form-control" type="text" name="address" value="<%= slot.getAddress() %>" readonly />
            </div>
            <div class="col-md-6">
                <label class="form-label">Delivery Date</label>
                <input class="form-control" type="date" name="deliveryDate" value="<%= slot.getDeliveryDate() %>" required />
            </div>
            <div class="col-md-6">
                <label class="form-label">Delivery Time</label>
                <input class="form-control" type="time" name="deliveryTime" value="<%= slot.getDeliveryTime() %>" required />
            </div>
        </div>

        <div class="d-flex gap-2 mt-4">
            <button class="btn btn-warning text-white" type="submit">
                <i class="fa-solid fa-floppy-disk me-2"></i>Save Changes
            </button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/delivery">
                <i class="fa-solid fa-arrow-left me-2"></i>Back
            </a>
        </div>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
