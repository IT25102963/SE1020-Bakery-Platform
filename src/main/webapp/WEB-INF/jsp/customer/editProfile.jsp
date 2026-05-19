<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Edit Profile</title>
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

        .form-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff; }
        .btn-rose { background-color: #fb7185; color: white; border-radius: 12px; font-weight: 600; transition: all 0.2s; }
        .btn-rose:hover { background-color: #f43f5e; color: white; transform: translateY(-1px); }
        .form-label { font-weight: 600; color: #1e293b; font-size: 0.9rem; }
        .form-control { border-radius: 12px; border: 1px solid #e2e8f0; padding: 12px 15px; font-size: 0.95rem; }
        .form-control:focus { border-color: #fb7185; box-shadow: 0 0 0 4px rgba(251, 113, 133, 0.1); }
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
                <a href="/custom-requests" class="nav-link nav-link-custom">Custom Requests</a>
                <a href="/profile" class="nav-link nav-link-custom nav-pill-active">Profile</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card form-card">
                    <div class="card-body p-5">
                        <div class="d-flex align-items-center mb-4">
                            <div class="p-3 rounded-3 me-3" style="background-color: #ffe4e6; color: #fb7185;">
                                <i class="bi bi-pencil-fill fs-4"></i>
                            </div>
                            <div>
                                <h3 class="fw-800 mb-0" style="font-weight: 800;">Edit Profile</h3>
                                <p class="text-muted mb-0">Update your account details</p>
                            </div>
                        </div>

                        <form action="/updateProfile" method="POST">
                            <div class="mb-3">
                                <label class="form-label">Full Name</label>
                                <input type="text" name="customerName" class="form-control" value="${customer.customerName}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Email Address</label>
                                <input type="email" name="customerEmail" class="form-control" value="${customer.customerEmail}" required readonly style="background-color: #f8fafc;">
                                <div class="form-text small">Email cannot be changed.</div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Phone Number</label>
                                <input type="text" name="customerPhone" class="form-control" value="${customer.customerPhone}" required>
                            </div>

                            <div class="mb-4">
                                <label class="form-label">Delivery Address</label>
                                <textarea name="customerAddress" rows="3" class="form-control" required>${customer.customerAddress}</textarea>
                            </div>

                            <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                <a href="/profile" class="btn btn-light px-4 py-2 border-0 fw-semibold" style="color: #64748b;">Cancel</a>
                                <button type="submit" class="btn btn-rose px-5 py-2">Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>