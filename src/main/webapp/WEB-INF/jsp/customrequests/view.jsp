<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.sliit.it25.bakeryweb.customrequests.CustomRequest" %>
<!DOCTYPE html>
<html>
<head>
    <title>Custom Cake Requests</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="text-secondary mb-0">🍰 Custom Cake Orders & Requests</h2>
                    <a href="/custom-requests/submit" class="btn btn-success px-4 py-2 shadow-sm">+ Submit New Request</a>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover align-middle border-light">
                        <thead class="table-dark">
                            <tr>
                                <th scope="col" class="py-3">Request ID</th>
                                <th scope="col" class="py-3">Customer Name</th>
                                <th scope="col" class="py-3 text-center">Tiers</th>
                                <th scope="col" class="py-3">Theme</th>
                                <th scope="col" class="py-3 text-center">Status</th>
                                <th scope="col" class="py-3 text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<CustomRequest> requests = (List<CustomRequest>) request.getAttribute("requests");
                                if(requests != null && !requests.isEmpty()) {
                                    for(CustomRequest req : requests) {
                                        String badgeClass = "bg-warning text-dark";
                                        if(req.getStatus().equals("Approved")) badgeClass = "bg-primary";
                                        else if(req.getStatus().equals("Completed")) badgeClass = "bg-success";
                            %>
                            <tr>
                                <td class="fw-bold text-muted">#<%= req.getRequestId() %></td>
                                <td class="fw-semibold"><%= req.getCustomerName() %></td>
                                <td class="text-center"><span class="badge bg-light text-dark border"><%= req.getTiers() %> Tiers</span></td>
                                <td><span class="text-secondary"><%= req.getTheme() %></span></td>
                                <td class="text-center">
                                    <span class="badge <%= badgeClass %> px-3 py-2"><%= req.getStatus() %></span>
                                </td>
                                <td class="text-center">
                                    <a href="/custom-requests/edit?id=<%= req.getRequestId() %>" class="btn btn-outline-primary btn-sm px-3">Edit / Update</a>
                                </td>
                            </tr>
                            <%
                                    }
                                } else {
                            %>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">No requests found.</td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</body>
</html>