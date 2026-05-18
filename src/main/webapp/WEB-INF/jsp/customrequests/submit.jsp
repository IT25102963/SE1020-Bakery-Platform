<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Submit Custom Request</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm border-0">
                    <div class="card-body p-4">
                        <h3 class="text-primary mb-4">🎂 Submit Custom Cake Request</h3>
                        <form action="/custom-requests/submit" method="post">
                            <div class="mb-3">
                                <label class="form-label fw-semibold">Request ID</label>
                                <input type="text" name="requestId" class="form-control" placeholder="e.g., 2005" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Customer Name</label>
                                <input type="text" name="customerName" class="form-control" placeholder="John Doe" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-semibold">Number of Tiers</label>
                                <input type="number" name="tiers" class="form-control" min="1" placeholder="1" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-semibold">Cake Theme</label>
                                <input type="text" name="theme" class="form-control" placeholder="e.g., Birthday, Wedding" required>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary px-4">Submit Request</button>
                                <a href="/custom-requests" class="btn btn-light border px-4">Cancel</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>