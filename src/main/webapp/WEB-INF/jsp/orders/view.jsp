<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.sliit.it25.bakeryweb.orders.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Order Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #fdfcfc; color: #2d3748; }

        /* Navbar Styles */
        .navbar { background-color: #ffffff; padding: 15px 0; border-bottom: 1px solid #f1f5f9; }
        .navbar-brand { font-weight: 800; color: #fb7185 !important; font-size: 1.5rem; letter-spacing: -0.5px; }
        .nav-link-custom { color: #4a5568; font-weight: 600; font-size: 0.95rem; padding: 8px 18px !important; margin: 0 5px; }
        .nav-pill-active { background-color: #ffe4e6; color: #fb7185 !important; border-radius: 20px; }

        /* Dashboard Styles */
        .dashboard-heading { font-weight: 800; font-size: 2.5rem; color: #1e293b; letter-spacing: -1px; }
        .dashboard-sub { color: #64748b; font-size: 1.05rem; }

        /* Table Styles */
        .table-card { border: none; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); overflow: hidden; background: #fff; }
        .table-header-custom { padding: 20px 25px; border-bottom: 1px solid #f1f5f9; }
        .search-box { background-color: #f1f5f9; border: none; border-radius: 8px; padding: 10px 15px; font-size: 0.9rem; width: 250px; }

        .table { margin-bottom: 0; }
        .table thead th { font-size: 0.75rem; text-transform: uppercase; letter-spacing: 1px; color: #64748b; border-bottom: 2px solid #f1f5f9; padding: 18px 25px; font-weight: 700; background: #fff; }
        .table tbody td { padding: 18px 25px; vertical-align: middle; border-bottom: 1px solid #f1f5f9; font-size: 0.9rem; font-weight: 500; color: #1e293b; }

        /* Status Badges */
        .status-pill { padding: 6px 14px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; letter-spacing: 0.5px; }
        .status-pending { background-color: #fef3c7; color: #d97706; }
        .status-shipped { background-color: #e0f2fe; color: #0369a1; }
        .status-delivered { background-color: #d1fae5; color: #059669; }

        .btn-edit-sm { background-color: #f3e8ff; color: #9333ea; border: none; padding: 5px 12px; border-radius: 8px; font-weight: 600; font-size: 0.8rem; text-decoration: none; transition: all 0.2s; }
        .btn-edit-sm:hover { background-color: #e9d5ff; color: #7e22ce; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg mb-5">
        <div class="container-fluid px-5">
            <a class="navbar-brand d-flex align-items-center" href="/">
                <span class="fs-3 me-2">🧁</span>
                <span class="text-dark">cake</span>CakeForge
            </a>
            <div class="ms-auto d-flex align-items-center">
                <a href="#" class="nav-link nav-link-custom">Catalog</a>
                <a href="/orders" class="nav-link nav-link-custom nav-pill-active">Orders</a>
                <a href="/custom-requests" class="nav-link nav-link-custom">Custom Requests</a>
            </div>
        </div>
    </nav>

    <div class="container-fluid px-5 pb-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h1 class="dashboard-heading mb-2">Orders</h1>
                <p class="dashboard-sub">Manage and track customer cake orders.</p>
            </div>
        </div>

        <div class="card table-card">
            <div class="table-header-custom d-flex justify-content-between align-items-center">
                <h5 class="mb-0 fw-bold" style="color: #1e293b;">Recent Orders</h5>
                <div class="d-flex align-items-center search-box">
                    <i class="bi bi-search text-muted me-2"></i>
                    <input type="text" placeholder="Search orders..." style="background: transparent; border: none; outline: none; width: 100%;">
                </div>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order ID</th>
                            <th>Customer Name</th>
                            <th>Order Date</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Order> orders = new ArrayList<>();
                            orders.add(new Order("101", "John Doe", new Date(), "Shipped"));
                            orders.add(new Order("102", "Jane Smith", new Date(), "Pending"));
                            orders.add(new Order("103", "Peter Jones", new Date(), "Delivered"));

                            for(Order order : orders) {
                                String statusClass = "status-pending";
                                if("Shipped".equals(order.getStatus())) statusClass = "status-shipped";
                                else if("Delivered".equals(order.getStatus())) statusClass = "status-delivered";
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border px-2 py-1">#<%= order.getOrderId() %></span></td>
                            <td><%= order.getCustomerName() %></td>
                            <td><%= new SimpleDateFormat("MMM dd, yyyy").format(order.getOrderDate()) %></td>
                            <td>
                                <span class="status-pill <%= statusClass %>"><%= order.getStatus() %></span>
                            </td>
                            <td>
                                <a href="/orders/edit?orderId=<%= order.getOrderId() %>" class="btn-edit-sm">
                                    <i class="bi bi-pencil-fill me-1"></i> Edit
                                </a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>