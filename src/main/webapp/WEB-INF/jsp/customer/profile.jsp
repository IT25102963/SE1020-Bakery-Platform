<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - My Profile</title>
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

        .profile-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.03); background: #fff; }
        .avatar-circle { width: 100px; height: 100px; background-color: #ffe4e6; color: #fb7185; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 3rem; font-weight: 700; margin-bottom: 20px; }
        
        .info-label { font-weight: 600; color: #64748b; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 5px; }
        .info-value { font-weight: 600; color: #1e293b; font-size: 1.05rem; margin-bottom: 20px; }
        
        .btn-rose { background-color: #fb7185; color: white; border-radius: 12px; font-weight: 600; transition: all 0.2s; }
        .btn-rose:hover { background-color: #f43f5e; color: white; transform: translateY(-1px); }
        .btn-light-custom { background-color: #f1f5f9; color: #475569; border-radius: 12px; font-weight: 600; transition: all 0.2s; }
        .btn-light-custom:hover { background-color: #e2e8f0; color: #1e293b; }
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
                <div class="card profile-card">
                    <div class="card-body p-5">
                        <div class="text-center mb-5">
                            <div class="d-flex justify-content-center">
                                <div class="avatar-circle">
                                    <i class="bi bi-person-fill"></i>
                                </div>
                            </div>
                            <h3 class="fw-800 mb-1" style="font-weight: 800;">My Profile</h3>
                            <p class="text-muted">Manage your personal information</p>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <p class="info-label">Full Name</p>
                                <p class="info-value">${customer.customerName}</p>
                            </div>
                            <div class="col-md-6">
                                <p class="info-label">Email Address</p>
                                <p class="info-value">${customer.customerEmail}</p>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <p class="info-label">Phone Number</p>
                                <p class="info-value">${customer.customerPhone}</p>
                            </div>
                            <div class="col-md-6">
                                <p class="info-label">Member Since</p>
                                <p class="info-value">May 2026</p>
                            </div>
                        </div>

                        <div class="mb-4">
                            <p class="info-label">Delivery Address</p>
                            <p class="info-value">${customer.customerAddress}</p>
                        </div>

                        <hr class="my-4" style="opacity: 0.1;">

                        <div class="d-grid gap-2 d-md-flex justify-content-md-center">
                            <a href="/editProfile" class="btn btn-rose px-5 py-2 me-md-2">Edit Profile</a>
                            <a href="/logout" class="btn btn-light-custom px-4 py-2">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>