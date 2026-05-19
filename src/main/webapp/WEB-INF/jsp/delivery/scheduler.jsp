<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.DeliverySlot" %>
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
        .slot-form-actions {
            white-space: nowrap;
        }
        .slot-form-actions form {
            display: inline;
        }
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
        .slot-form-actions .btn-action i { color: inherit !important; }
        .slot-form-actions .btn-receipt { background: #efe9ff; color: #7c4dff !important; }
        .slot-form-actions .btn-edit { background: #e6f4ff; color: #42a5f5 !important; }
        .slot-form-actions .btn-delete { background: #fdecef; color: #ef5350 !important; border: 0; }
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
        .search-fake {
            background: #f8f9fa;
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
        .cake-navbar .navbar-brand span { color: #ef6b9a !important; }
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
        <a class="navbar-brand" href="${pageContext.request.contextPath}/bookings/products">
            <img src="${pageContext.request.contextPath}/img/cake-brand.png" alt="CakeForge" class="brand-logo">
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
<div class="container py-4">
    <%
        List<DeliverySlot> slots = (List<DeliverySlot>) request.getAttribute("slots");
        int totalSlots = (slots == null) ? 0 : slots.size();
        int completedSlots = 0;
        if (slots != null) {
            for (DeliverySlot slot : slots) {
                String id = slot.getId() == null ? "" : slot.getId().toUpperCase();
                if (id.equals("B104") || id.equals("B105") || id.equals("B106")
                        || id.equals("C201") || id.equals("C202") || id.equals("C203")) {
                    completedSlots++;
                }
            }
        }
        List<DeliverySlot> customSlots = new ArrayList<>();
        if (slots != null) {
            for (DeliverySlot slot : slots) {
                if (slot.getId() != null && slot.getId().toUpperCase().startsWith("C")) {
                    customSlots.add(slot);
                }
            }
        }
    %>
    <header class="page-header container">
        <div class="d-flex justify-content-between align-items-end">
            <div>
                <h1>Dashboard</h1>
                <p>Manage delivery slots and route operations.</p>
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
                    <h5 class="mb-0 fw-bold">Recent Orders</h5>
                    <div class="input-group w-25">
                        <span class="input-group-text bg-transparent border-end-0"><i class="fa-solid fa-magnifying-glass text-muted"></i></span>
                        <input type="text" class="form-control border-start-0 ps-0 search-fake" placeholder="Search orders..." disabled>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Customer</th>
                            <th>Address</th>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if (slots != null && !slots.isEmpty()) {
                                for (DeliverySlot slot : slots) {
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border"><%= slot.getId() %></span></td>
                            <td><%= slot.getCustomerName() %></td>
                            <td><%= slot.getAddress() %></td>
                            <td><%= slot.getDeliveryDate() %></td>
                            <td><%= (slot.getDeliveryTime() == null || slot.getDeliveryTime().isBlank()) ? "Not set" : slot.getDeliveryTime() %></td>
                            <%
                                String recentId = slot.getId() == null ? "" : slot.getId().toUpperCase();
                                boolean recentCompleted = recentId.equals("B104") || recentId.equals("B105") || recentId.equals("B106");
                            %>
                            <td><span class="badge-status <%= recentCompleted ? "status-completed" : "status-pending" %>"><%= recentCompleted ? "Completed" : "Pending" %></span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=<%= slot.getId() %>"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=<%= slot.getId() %>"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="<%= slot.getId() %>" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border">B104</span></td>
                            <td>Nimal Perera</td>
                            <td>12 Temple Road, Colombo 05</td>
                            <td>2026-06-18</td>
                            <td>10:30</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=B104"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=B104"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="B104" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="badge bg-light text-dark border">B105</span></td>
                            <td>Shehan Silva</td>
                            <td>44 Lake Drive, Kandy</td>
                            <td>2026-06-19</td>
                            <td>14:00</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=B105"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=B105"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="B105" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="badge bg-light text-dark border">B106</span></td>
                            <td>Rashmi Fernando</td>
                            <td>8 Beach Lane, Galle</td>
                            <td>2026-06-20</td>
                            <td>16:45</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=B106"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=B106"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="B106" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
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
                    <h5 class="mb-0 fw-bold">Custom Cake Orders</h5>
                    <div class="input-group w-25">
                        <span class="input-group-text bg-transparent border-end-0"><i class="fa-solid fa-magnifying-glass text-muted"></i></span>
                        <input type="text" class="form-control border-start-0 ps-0 search-fake" placeholder="Search custom orders..." disabled>
                    </div>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th>Schedule ID</th>
                            <th>Customer</th>
                            <th>Address</th>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Status</th>
                            <th class="text-center">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            if (!customSlots.isEmpty()) {
                                for (DeliverySlot slot : customSlots) {
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border"><%= slot.getId() %></span></td>
                            <td><%= slot.getCustomerName() %></td>
                            <td><%= slot.getAddress() %></td>
                            <td><%= slot.getDeliveryDate() %></td>
                            <td><%= (slot.getDeliveryTime() == null || slot.getDeliveryTime().isBlank()) ? "Not set" : slot.getDeliveryTime() %></td>
                            <%
                                String customId = slot.getId() == null ? "" : slot.getId().toUpperCase();
                                boolean customCompleted = customId.equals("C201") || customId.equals("C202") || customId.equals("C203");
                            %>
                            <td><span class="badge-status <%= customCompleted ? "status-completed" : "status-pending" %>"><%= customCompleted ? "Completed" : "Pending" %></span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=<%= slot.getId() %>"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=<%= slot.getId() %>"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="<%= slot.getId() %>" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border">C201</span></td>
                            <td>Dinithi Jayasuriya</td>
                            <td>21 Rose Avenue, Nugegoda</td>
                            <td>2026-06-21</td>
                            <td>11:15</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=C201"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=C201"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="C201" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="badge bg-light text-dark border">C202</span></td>
                            <td>Kavindu Wijesinghe</td>
                            <td>67 Palm Street, Matara</td>
                            <td>2026-06-22</td>
                            <td>13:30</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=C202"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=C202"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="C202" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
                        </tr>
                        <tr>
                            <td><span class="badge bg-light text-dark border">C203</span></td>
                            <td>Hashini Dissanayake</td>
                            <td>5 Station Road, Kurunegala</td>
                            <td>2026-06-23</td>
                            <td>15:00</td>
                            <td><span class="badge-status status-pending">Pending</span></td>
                            <td class="text-center slot-form-actions">
                                <a class="btn-action btn-receipt" title="View" href="${pageContext.request.contextPath}/delivery/details?slotId=C203"><i class="fa-solid fa-receipt"></i></a>
                                <a class="btn-action btn-status" title="Add Delivery Time" href="${pageContext.request.contextPath}/delivery/edit?slotId=C203"><i class="fa-solid fa-clock"></i></a>
                                <form method="post" action="${pageContext.request.contextPath}/delivery/cancel" onsubmit="return confirm('Cancel this slot?');">
                                    <input type="hidden" name="slotId" value="C203" />
                                    <button class="btn-action btn-delete" title="Cancel" type="submit"><i class="fa-solid fa-trash"></i></button>
                                </form>
                            </td>
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
