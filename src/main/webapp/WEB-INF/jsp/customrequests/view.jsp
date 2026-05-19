<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Custom Requests</title>
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

        /* Stat Cards */
        .stat-card { border: none; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); transition: transform 0.2s; }
        .stat-card:hover { transform: translateY(-3px); }
        .icon-box { width: 50px; height: 50px; border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; }
        .bg-red-light { background-color: #ffe4e6; color: #f43f5e; }
        .bg-green-light { background-color: #d1fae5; color: #10b981; }

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
        .status-approved { background-color: #e0e7ff; color: #4f46e5; }
        .status-completed { background-color: #d1fae5; color: #059669; }

        /* Action Buttons */
        .action-btn { width: 34px; height: 34px; border-radius: 8px; display: inline-flex; align-items: center; justify-content: center; text-decoration: none; border: none; font-size: 1rem; transition: all 0.2s; margin-right: 5px; }
        .btn-edit { background-color: #f3e8ff; color: #9333ea; }
        .btn-edit:hover { background-color: #e9d5ff; color: #7e22ce; }
        .btn-delete { background-color: #ffe4e6; color: #e11d48; }
        .btn-delete:hover { background-color: #fecdd3; color: #be123c; }
    </style>
</head>
<body>

    <%
        List<CustomRequest> requests = (List<CustomRequest>) request.getAttribute("requests");
        int totalRequests = 0;
        int completedRequests = 0;
        if(requests != null) {
            totalRequests = requests.size();
            for(CustomRequest req : requests) {
                if(req.getStatus().equals("Completed")) completedRequests++;
            }
        }
    %>

    <nav class="navbar navbar-expand-lg mb-5">
        <div class="container-fluid px-5">
            <a class="navbar-brand d-flex align-items-center" href="#">
                <span class="fs-3 me-2">🧁</span>
                <span class="text-dark">cake</span>CakeForge
            </a>
            <div class="ms-auto d-flex align-items-center">
                <a href="#" class="nav-link nav-link-custom">Catalog</a>
                <a href="#" class="nav-link nav-link-custom">Bookings</a>
                <a href="/custom-requests" class="nav-link nav-link-custom nav-pill-active">Custom Requests</a>
            </div>
        </div>
    </nav>

    <div class="container-fluid px-5 pb-5">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h1 class="dashboard-heading mb-2">Dashboard</h1>
                <p class="dashboard-sub">Manage custom cake requests and specifications.</p>
            </div>
            <a href="/custom-requests/submit" class="btn text-white px-4 py-2" style="background-color: #fb7185; border-radius: 10px; font-weight: 600;">+ New Request</a>
        </div>

        <div class="row mb-5">
            <div class="col-md-6">
                <div class="card stat-card h-100 p-4">
                    <div class="d-flex align-items-center">
                        <div class="icon-box bg-red-light me-4">
                            <i class="bi bi-box-seam-fill"></i>
                        </div>
                        <div>
                            <h2 class="mb-0 fw-bold" style="font-size: 2rem;"><%= totalRequests %></h2>
                            <p class="text-muted mb-0 fw-medium">Total Custom Requests</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card stat-card h-100 p-4">
                    <div class="d-flex align-items-center">
                        <div class="icon-box bg-green-light me-4">
                            <i class="bi bi-check-circle-fill"></i>
                        </div>
                        <div>
                            <h2 class="mb-0 fw-bold" style="font-size: 2rem;"><%= completedRequests %></h2>
                            <p class="text-muted mb-0 fw-medium">Completed Custom Requests</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="card table-card">
            <div class="table-header-custom d-flex justify-content-between align-items-center">
                <h5 class="mb-0 fw-bold" style="color: #1e293b;">Recent Requests</h5>
                <div class="d-flex align-items-center search-box">
                    <i class="bi bi-search text-muted me-2"></i>
                    <input type="text" placeholder="Search requests..." style="background: transparent; border: none; outline: none; width: 100%;">
                </div>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Request ID</th>
                            <th>Customer Name</th>
                            <th>Tiers</th>
                            <th>Theme</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if(requests != null && !requests.isEmpty()) {
                                for(CustomRequest req : requests) {
                                    String statusClass = "status-pending";
                                    if(req.getStatus().equals("Approved")) statusClass = "status-approved";
                                    else if(req.getStatus().equals("Completed")) statusClass = "status-completed";
                        %>
                        <tr>
                            <td><span class="badge bg-light text-dark border px-2 py-1">#<%= req.getRequestId() %></span></td>
                            <td><%= req.getCustomerName() %></td>
                            <td><%= req.getTiers() %></td>
                            <td><%= req.getTheme() %></td>
                            <td>
                                <span class="status-pill <%= statusClass %>"><%= req.getStatus() %></span>
                            </td>
                            <td>
                                <a href="/custom-requests/edit?id=<%= req.getRequestId() %>" class="action-btn btn-edit" title="Edit">
                                    <i class="bi bi-file-earmark-text-fill"></i>
                                </a>
                                <a href="/custom-requests/delete?id=<%= req.getRequestId() %>" class="action-btn btn-delete" title="Delete" onclick="return confirm('Are you sure you want to delete this request?');">
                                    <i class="bi bi-trash-fill"></i>
                                </a>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr>
                            <td colspan="6" class="text-center text-muted py-5">
                                <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                No custom requests found.
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