<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.Booking" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.DeliverySlot" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delivery Scheduler | Bakery Platform</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .roster-card {
            border-radius: 20px;
            overflow: hidden;
        }
        .roster-card .table thead th {
            font-size: 0.82rem;
            letter-spacing: 0.06em;
            color: #2d3436;
        }
        .roster-card .table tbody td {
            border-top: 1px solid #e9ecef;
        }
        .slot-form-actions {
            white-space: nowrap;
        }
        .slot-form-actions .btn-action {
            width: 38px;
            height: 38px;
            border-radius: 11px;
            margin: 0 3px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 6px rgba(0,0,0,0.06);
        }
        .slot-form-actions .btn-action i {
            color: inherit !important;
        }
        .slot-form-actions .btn-receipt {
            background: #efe9ff;
            color: #7c4dff !important;
        }
        .slot-form-actions .btn-edit {
            background: #e6f4ff;
            color: #42a5f5 !important;
        }
        .slot-form-actions .btn-status {
            background: #fff3dd;
            color: #e59b00 !important;
        }
        .badge-status {
            padding: 0.42rem 1rem;
            border-radius: 999px;
            font-size: 0.76rem;
            font-weight: 700;
        }
        .status-pending {
            background: #f8e7a8;
            color: #b08700;
        }
        .status-completed {
            background: #d9f7e8;
            color: #1f8f5f;
        }
        .brand-logo {
            height: 34px;
            width: auto;
            display: block;
        }
        .cake-navbar .navbar-brand {
            padding: 0.15rem 0;
            display: inline-flex;
            align-items: center;
            gap: 10px;
            background: none;
            -webkit-background-clip: initial;
            -webkit-text-fill-color: initial;
            color: #ef6b9a !important;
            font-family: 'Montserrat', sans-serif;
            font-weight: 800;
            font-size: 2.2rem;
        }
        .cake-navbar .navbar-brand span {
            color: #ef6b9a !important;
        }
        .stat-card {
            background: rgba(255, 255, 255, 0.85);
            border: 1px solid rgba(255, 255, 255, 0.5);
            border-radius: 16px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
            padding: 1.4rem 1.5rem;
            display: flex;
            align-items: center;
            gap: 14px;
        }
        .stat-icon {
            width: 52px;
            height: 52px;
            border-radius: 12px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 1.25rem;
            color: #fff;
        }
        .icon-pink {
            background: linear-gradient(135deg, #ff9a8b 0%, #ff6a88 100%);
        }
        .icon-green {
            background: #56e0b8;
        }
        .stat-info h3 {
            margin: 0;
            font-weight: 800;
            line-height: 1.1;
        }
        .stat-info p {
            margin: 0.25rem 0 0;
            color: #455a64;
            font-size: 1.05rem;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/catalog">
            <img src="${pageContext.request.contextPath}/theme/img/logo.png" alt="CakeForge" class="brand-logo">
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

<div class="container py-4">
    <%
        List<Booking> catalogOrders = (List<Booking>) request.getAttribute("catalogOrders");
        List<CustomRequest> customRequests = (List<CustomRequest>) request.getAttribute("customRequests");
        Map<String, DeliverySlot> deliverySlotMap = (Map<String, DeliverySlot>) request.getAttribute("deliverySlotMap");
        Map<String, String> customSlotIdMap = (Map<String, String>) request.getAttribute("customSlotIdMap");

        int totalSlots = (catalogOrders == null ? 0 : catalogOrders.size()) + (customRequests == null ? 0 : customRequests.size());
        int completedSlots = 0;

        if (catalogOrders != null) {
            for (Booking order : catalogOrders) {
                String status = order.getStatus() == null ? "" : order.getStatus().trim().toLowerCase();
                if (status.equals("completed") || status.equals("delivered")) {
                    completedSlots++;
                }
            }
        }
        if (customRequests != null) {
            for (CustomRequest customRequest : customRequests) {
                String status = customRequest.getStatus() == null ? "" : customRequest.getStatus().trim().toLowerCase();
                if (status.equals("completed")) {
                    completedSlots++;
                }
            }
        }
    %>

    <header class="page-header container">
        <div class="d-flex justify-content-between align-items-end">
            <div>
                <h1>Delivery Management</h1>
                <p>Schedule delivery date/time and update order status.</p>
            </div>
        </div>
    </header>

    <% String message = (String) request.getAttribute("message"); %>
    <% if (message != null && !message.isBlank()) { %>
    <div class="alert alert-success glass-card border-0 mb-4"><i class="fa-solid fa-circle-check me-2"></i><%= message %></div>
    <% } %>

    <div class="row g-4 mb-5">
        <div class="col-md-6">
            <div class="stat-card">
                <div class="stat-icon icon-pink"><i class="fa-solid fa-truck"></i></div>
                <div class="stat-info"><h3><%= totalSlots %></h3><p>Total Delivery Bookings</p></div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="stat-card">
                <div class="stat-icon icon-green"><i class="fa-solid fa-circle-check"></i></div>
                <div class="stat-info"><h3><%= completedSlots %></h3><p>Completed Delivery Bookings</p></div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <div class="col-lg-12">
            <div class="glass-card p-0 overflow-hidden roster-card">
                <div class="p-4 border-bottom d-flex justify-content-between align-items-center">
                    <h5 class="mb-0 fw-bold">Catalog Orders</h5>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Customer</th>
                            <th>Cake</th>
                            <th>Delivery Date</th>
                            <th>Delivery Time</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if (catalogOrders != null && !catalogOrders.isEmpty()) {
                                for (Booking order : catalogOrders) {
                                    String orderStatus = (order.getStatus() == null || order.getStatus().isBlank()) ? "Pending" : order.getStatus();
                                    String normalizedOrderStatus = orderStatus.trim().toLowerCase();
                                    String statusClass = (normalizedOrderStatus.equals("completed") || normalizedOrderStatus.equals("delivered"))
                                            ? "status-completed"
                                            : "status-pending";

                                    String slotKey = order.getBookingId() == null ? "" : order.getBookingId().trim().toUpperCase();
                                    DeliverySlot slot = deliverySlotMap == null ? null : deliverySlotMap.get(slotKey);

                                    String deliveryDateDisplay = (slot != null && slot.getDeliveryDate() != null && !slot.getDeliveryDate().isBlank())
                                            ? slot.getDeliveryDate()
                                            : (order.getDeliveryDate() == null ? "-" : order.getDeliveryDate().toString());
                                    String deliveryTimeDisplay = (slot != null && slot.getDeliveryTime() != null && !slot.getDeliveryTime().isBlank())
                                            ? slot.getDeliveryTime()
                                            : "Not set";
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border"><%= order.getBookingId() %></span></td>
                            <td><%= order.getCustomerName() %></td>
                            <td><%= order.getCakeName() %></td>
                            <td><%= deliveryDateDisplay %></td>
                            <td><%= deliveryTimeDisplay %></td>
                            <td><span class="badge-status <%= statusClass %>"><%= orderStatus %></span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View Receipt" href="${pageContext.request.contextPath}/bookings/receipt/<%= order.getBookingId() %>"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-edit" title="Schedule Delivery" href="${pageContext.request.contextPath}/delivery/edit?slotId=<%= order.getBookingId() %>"><i class="fa-solid fa-clock"></i></a>
                                <a class="btn-action btn-status" title="Change Status" href="${pageContext.request.contextPath}/bookings/status/<%= order.getBookingId() %>"><i class="fa-solid fa-gear"></i></a>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="7" class="text-center py-4 text-muted">No catalog orders found.</td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-lg-12">
            <div class="glass-card p-0 overflow-hidden roster-card">
                <div class="p-4 border-bottom d-flex justify-content-between align-items-center">
                    <h5 class="mb-0 fw-bold">Custom Cake Order Management</h5>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th>Request ID</th>
                            <th>Customer</th>
                            <th>Tiers</th>
                            <th>Theme</th>
                            <th>Delivery Date</th>
                            <th>Delivery Time</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if (customRequests != null && !customRequests.isEmpty()) {
                                for (CustomRequest customRequest : customRequests) {
                                    String customStatus = (customRequest.getStatus() == null || customRequest.getStatus().isBlank())
                                            ? "Pending"
                                            : customRequest.getStatus();
                                    String normalizedCustomStatus = customStatus.trim().toLowerCase();
                                    String customStatusClass = normalizedCustomStatus.equals("completed")
                                            ? "status-completed"
                                            : "status-pending";

                                    String requestId = customRequest.getRequestId() == null ? "" : customRequest.getRequestId();
                                    String customSlotId = customSlotIdMap == null ? requestId : customSlotIdMap.get(requestId);
                                    if (customSlotId == null || customSlotId.isBlank()) {
                                        customSlotId = requestId;
                                    }

                                    DeliverySlot customSlot = deliverySlotMap == null ? null : deliverySlotMap.get(customSlotId.trim().toUpperCase());
                                    String customDateDisplay = (customSlot != null && customSlot.getDeliveryDate() != null && !customSlot.getDeliveryDate().isBlank())
                                            ? customSlot.getDeliveryDate()
                                            : "Not scheduled";
                                    String customTimeDisplay = (customSlot != null && customSlot.getDeliveryTime() != null && !customSlot.getDeliveryTime().isBlank())
                                            ? customSlot.getDeliveryTime()
                                            : "Not set";
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border"><%= customRequest.getRequestId() %></span></td>
                            <td><%= customRequest.getCustomerName() %></td>
                            <td><%= customRequest.getTiers() %></td>
                            <td><%= customRequest.getTheme() %></td>
                            <td><%= customDateDisplay %></td>
                            <td><%= customTimeDisplay %></td>
                            <td><span class="badge-status <%= customStatusClass %>"><%= customStatus %></span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View Receipt" href="${pageContext.request.contextPath}/custom-requests/details?id=<%= customRequest.getRequestId() %>"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-edit" title="Schedule Delivery" href="${pageContext.request.contextPath}/delivery/edit?slotId=<%= customSlotId %>"><i class="fa-solid fa-clock"></i></a>
                                <a class="btn-action btn-status" title="Change Status" href="${pageContext.request.contextPath}/custom-requests/edit?id=<%= customRequest.getRequestId() %>"><i class="fa-solid fa-gear"></i></a>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="8" class="text-center py-4 text-muted">No custom cake requests found.</td>
                        </tr>
                        <%
                            }
                        %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
