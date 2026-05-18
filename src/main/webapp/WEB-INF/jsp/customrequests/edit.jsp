<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Request</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        <h3 class="text-warning mb-4">✏️ Edit Custom Cake Request</h3>
                        <% CustomRequest req = (CustomRequest) request.getAttribute("request"); %>
                        <% if(req != null) { %>
                        <form action="/custom-requests/edit" method="post">
                            <input type="hidden" name="requestId" value="<%= req.getRequestId() %>">
                            <input type="hidden" name="customerName" value="<%= req.getCustomerName() %>">

                            <div class="mb-3">
                                <p class="mb-1 text-muted">Request ID: <span class="fw-bold text-dark">#<%= req.getRequestId() %></span></p>
                                <p class="mb-0 text-muted">Customer Name: <span class="fw-bold text-dark"><%= req.getCustomerName() %></span></p>
                            </div>
                            <hr>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Number of Tiers</label>
                                <input type="number" name="tiers" value="<%= req.getTiers() %>" class="form-control" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Cake Theme</label>
                                <input type="text" name="theme" value="<%= req.getTheme() %>" class="form-control" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-semibold">Status</label>
                                <select name="status" class="form-select">
                                    <option value="Pending" <%= req.getStatus().equals("Pending") ? "selected" : "" %>>Pending</option>
                                    <option value="Approved" <%= req.getStatus().equals("Approved") ? "selected" : "" %>>Approved</option>
                                    <option value="Completed" <%= req.getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
                                </select>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-warning px-4 fw-semibold">Update Request</button>
                                <a href="/custom-requests" class="btn btn-light border px-4">Cancel</a>
                            </div>
                        </form>
                        <% } else { %>
                            <div class="alert alert-danger">Request not found.</div>
                            <a href="/custom-requests" class="btn btn-primary">Back to List</a>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>