<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CakeForge - Create Account</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #fdfcfc; color: #2d3748; min-height: 100vh; }

        /* Navbar Styles */
        .navbar { background-color: #ffffff; padding: 15px 0; border-bottom: 1px solid #f1f5f9; }
        .navbar-brand { font-weight: 800; color: #fb7185 !important; font-size: 1.5rem; letter-spacing: -0.5px; }
        .nav-link-custom { color: #4a5568; font-weight: 600; font-size: 0.95rem; padding: 8px 18px !important; margin: 0 5px; }

        .register-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.04); background: #fff; overflow: hidden; }
        .btn-rose { background-color: #fb7185; color: white; border-radius: 12px; font-weight: 600; transition: all 0.2s; padding: 12px; }
        .btn-rose:hover { background-color: #f43f5e; color: white; transform: translateY(-1px); box-shadow: 0 5px 15px rgba(251, 113, 133, 0.3); }
        .form-label { font-weight: 600; color: #1e293b; font-size: 0.85rem; text-transform: uppercase; letter-spacing: 0.5px; }
        .form-control { border-radius: 12px; border: 1px solid #e2e8f0; padding: 12px 15px; font-size: 0.95rem; background-color: #f8fafc; }
        .form-control:focus { border-color: #fb7185; background-color: #fff; box-shadow: 0 0 0 4px rgba(251, 113, 133, 0.1); }
        
        .welcome-section { background: linear-gradient(135deg, #fb7185 0%, #f43f5e 100%); color: white; display: flex; flex-direction: column; justify-content: center; padding: 40px; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg">
        <div class="container-fluid px-5">
            <a class="navbar-brand d-flex align-items-center" href="/">
                <span class="fs-3 me-2">🧁</span>
                <span class="text-dark">cake</span>CakeForge
            </a>
            <div class="ms-auto d-flex align-items-center">
                <a href="#" class="nav-link nav-link-custom">Catalog</a>
                <a href="/custom-requests" class="nav-link nav-link-custom">Custom Requests</a>
                <a href="/login" class="btn border-0 fw-bold ms-3" style="color: #fb7185;">Login</a>
            </div>
        </div>
    </nav>

    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-lg-10">
                <div class="card register-card">
                    <div class="row g-0">
                        <div class="col-md-5 welcome-section d-none d-md-flex">
                            <h2 class="fw-800 mb-4" style="font-weight: 800;">Join the CakeForge family!</h2>
                            <p class="opacity-75 mb-4">Create an account to track your custom orders, save your favorite designs, and get exclusive bakery updates.</p>
                            <div class="mt-auto">
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <span>Fast & Easy Custom Requests</span>
                                </div>
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <span>Real-time Order Tracking</span>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-check-circle-fill me-2"></i>
                                    <span>Exclusive Member Rewards</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-7">
                            <div class="card-body p-5">
                                <h3 class="fw-bold mb-4" style="color: #1e293b;">Create Account</h3>
                                
                                <form action="/registerCustomer" method="POST">
                                    <div class="row">
                                        <div class="col-md-12 mb-3">
                                            <label class="form-label">Full Name</label>
                                            <input type="text" name="customerName" class="form-control" required placeholder="Anura Rohana">
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6 mb-3">
                                            <label class="form-label">Email Address</label>
                                            <input type="email" name="customerEmail" class="form-control" required placeholder="anura@example.com">
                                        </div>
                                        <div class="col-md-6 mb-3">
                                            <label class="form-label">Phone Number</label>
                                            <input type="text" name="customerPhone" class="form-control" required placeholder="077xxxxxxx">
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label class="form-label">Delivery Address</label>
                                        <textarea name="customerAddress" rows="2" class="form-control" required placeholder="Enter your full address"></textarea>
                                    </div>

                                    <div class="mb-4">
                                        <label class="form-label">Password</label>
                                        <div class="input-group">
                                            <input type="password" name="customerPassword" class="form-control" required placeholder="••••••••">
                                        </div>
                                        <div class="form-text small">Must be at least 8 characters long.</div>
                                    </div>

                                    <button type="submit" class="btn btn-rose w-100 mb-3">Create Account</button>
                                    
                                    <p class="text-center text-muted small mb-0">
                                        Already have an account? <a href="/login" class="fw-bold text-decoration-none" style="color: #fb7185;">Sign In</a>
                                    </p>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>