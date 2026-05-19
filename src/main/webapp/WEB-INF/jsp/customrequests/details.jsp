<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Custom Request Receipt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #fdfcfc; color: #2d3748; }
        .navbar { background-color: #ffffff; padding: 15px 0; border-bottom: 1px solid #f1f5f9; }
        .navbar-brand { font-weight: 800; color: #fb7185 !important; font-size: 1.5rem; letter-spacing: -0.5px; }
        .nav-link-custom { color: #4a5568; font-weight: 600; font-size: 0.95rem; padding: 8px 18px !important; margin: 0 5px; }
        .nav-pill-active { background-color: #ffe4e6; color: #fb7185 !important; border-radius: 20px; }
        .card-box { border: none; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); background: #fff; }
        .meta-label { font-size: 0.78rem; text-transform: uppercase; letter-spacing: 0.06em; color: #64748b; font-weight: 700; }
        .meta-value { font-size: 1rem; font-weight: 600; color: #1e293b; margin-bottom: 0; }
        .status-pill { padding: 6px 14px; border-radius: 20px; font-size: 0.75rem; font-weight: 700; letter-spacing: 0.5px; }
        .status-pending { background-color: #fef3c7; color: #d97706; }
        .status-confirmed { background-color: #e0e7ff; color: #4f46e5; }
        .status-completed { background-color: #d1fae5; color: #059669; }
    </style>
</head>
<body>
<%
    CustomRequest req = (CustomRequest) request.getAttribute("request");
    Boolean isAdminAttr = (Boolean) request.getAttribute("isAdmin");
    boolean isAdmin = isAdminAttr != null && isAdminAttr;
    String status = req == null ? "" : (req.getStatus() == null ? "" : req.getStatus().trim());
    String statusClass = "status-pending";
    if ("Confirmed".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(status)) statusClass = "status-confirmed";
    if ("Completed".equalsIgnoreCase(status)) statusClass = "status-completed";
%>

<nav class="navbar navbar-expand-lg mb-5">
    <div class="container-fluid px-5">
        <a class="navbar-brand d-flex align-items-center" href="/catalog">
            <span class="fs-3 me-2">🧁</span>
            <span class="text-dark">cake</span>CakeForge
        </a>
        <div class="ms-auto d-flex align-items-center">
            <a href="/catalog" class="nav-link nav-link-custom">Catalog</a>
            <a href="/custom-requests" class="nav-link nav-link-custom nav-pill-active">Custom Requests</a>
        </div>
    </div>
</nav>

<div class="container mt-4 mb-5">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card card-box">
                <div class="card-body p-5">
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <div>
                            <h3 class="fw-bold mb-1" style="color: #1e293b;">Custom Request Receipt</h3>
                            <p class="text-muted mb-0">Request details and current status</p>
                        </div>
                        <% if(req != null) { %>
                        <span class="badge bg-light text-dark border px-3 py-2">#<%= req.getRequestId() %></span>
                        <% } %>
                    </div>

                    <% if(req == null) { %>
                    <div class="alert alert-warning mb-0">Request not found.</div>
                    <% } else { %>
                    <div class="row g-4">
                        <div class="col-md-6">
                            <div class="meta-label">Customer</div>
                            <p class="meta-value"><%= req.getCustomerName() %></p>
                        </div>
                        <div class="col-md-6">
                            <div class="meta-label">Status</div>
                            <p class="mb-0"><span class="status-pill <%= statusClass %>"><%= status %></span></p>
                        </div>
                        <div class="col-md-6">
                            <div class="meta-label">Number of Tiers</div>
                            <p class="meta-value"><%= req.getTiers() %></p>
                        </div>
                        <div class="col-md-6">
                            <div class="meta-label">Cake Theme</div>
                            <p class="meta-value"><%= req.getTheme() %></p>
                        </div>
                    </div>
                    <% } %>

                    <div class="d-flex gap-2 mt-4">
                        <a href="/custom-requests" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left me-1"></i> Back
                        </a>
                        <% if(isAdmin && req != null) { %>
                        <a href="/custom-requests/edit?id=<%= req.getRequestId() %>" class="btn btn-primary">
                            <i class="bi bi-gear me-1"></i> Update Status
                        </a>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
