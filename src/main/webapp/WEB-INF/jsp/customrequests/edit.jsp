<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Edit Request</title>
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

        .form-card { border: none; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); background: #fff; }
        .btn-rose { background-color: #fb7185; color: white; border-radius: 10px; font-weight: 600; transition: all 0.2s; }
        .btn-rose:hover { background-color: #f43f5e; color: white; transform: translateY(-1px); }
        .form-label { font-weight: 600; color: #1e293b; font-size: 0.9rem; }
        .form-control, .form-select { border-radius: 10px; border: 1px solid #e2e8f0; padding: 12px 15px; font-size: 0.95rem; }
        .form-control:focus, .form-select:focus { border-color: #fb7185; box-shadow: 0 0 0 3px rgba(251, 113, 133, 0.1); }
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
                <a href="/catalog" class="nav-link nav-link-custom">Catalog</a>
                <a href="/bookings/my-orders" class="nav-link nav-link-custom">Dashboard</a>
                <a href="/custom-requests" class="nav-link nav-link-custom nav-pill-active">Custom Requests</a>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card form-card">
                    <div class="card-body p-5">
                        <div class="d-flex align-items-center mb-4">
                            <div class="p-3 rounded-3 me-3" style="background-color: #f3e8ff; color: #9333ea;">
                                <i class="bi bi-pencil-square fs-4"></i>
                            </div>
                            <div>
                                <h3 class="fw-bold mb-0" style="color: #1e293b;">Update Request Status</h3>
                                <p class="text-muted mb-0">Admin can update status only.</p>
                            </div>
                        </div>

                        <% CustomRequest req = (CustomRequest) request.getAttribute("request"); %>
                        <% if(req != null) { %>
                        <form action="/custom-requests/edit" method="post">
                            <input type="hidden" name="requestId" value="<%= req.getRequestId() %>">

                            <div class="mb-4 p-3 rounded-3 bg-light border-0">
                                <div class="row">
                                    <div class="col-6">
                                        <p class="mb-1 text-muted small fw-bold text-uppercase">Request ID</p>
                                        <p class="mb-0 fw-bold" style="color: #fb7185;">#<%= req.getRequestId() %></p>
                                    </div>
                                    <div class="col-6">
                                        <p class="mb-1 text-muted small fw-bold text-uppercase">Customer</p>
                                        <p class="mb-0 fw-bold"><%= req.getCustomerName() %></p>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Number of Tiers</label>
                                <input type="text" value="<%= req.getTiers() %>" class="form-control" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Cake Theme</label>
                                <input type="text" value="<%= req.getTheme() %>" class="form-control" readonly>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Status</label>
                                <select name="status" class="form-select">
                                    <option value="Pending" <%= req.getStatus().equals("Pending") ? "selected" : "" %>>Pending</option>
                                    <option value="Confirmed" <%= (req.getStatus().equals("Confirmed") || req.getStatus().equals("Approved")) ? "selected" : "" %>>Confirmed</option>
                                    <option value="Completed" <%= req.getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
                                    <option value="Delete Requested" <%= req.getStatus().equals("Delete Requested") ? "selected" : "" %>>Delete Requested</option>
                                </select>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="/custom-requests" class="btn btn-light px-4 py-2 border-0 fw-semibold" style="color: #64748b;">Cancel</a>
                                <button type="submit" class="btn btn-rose px-5 py-2">Update Request</button>
                            </div>
                        </form>
                        <% } else { %>
                            <div class="text-center py-4">
                                <div class="bg-red-light p-3 rounded-circle d-inline-block mb-3" style="background-color: #ffe4e6; color: #f43f5e;">
                                    <i class="bi bi-exclamation-triangle-fill fs-2"></i>
                                </div>
                                <h4 class="fw-bold">Request Not Found</h4>
                                <p class="text-muted mb-4">The request you are trying to edit doesn't exist.</p>
                                <a href="/custom-requests" class="btn btn-rose px-4 py-2">Back to Dashboard</a>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
