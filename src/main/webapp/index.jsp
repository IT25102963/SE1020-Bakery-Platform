<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CakeForge - Custom Cakes & Bakery</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #fdfcfc; color: #2d3748; }

        /* Navbar Styles */
        .navbar { background-color: #ffffff; padding: 15px 0; border-bottom: 1px solid #f1f5f9; }
        .navbar-brand { font-weight: 800; color: #fb7185 !important; font-size: 1.5rem; letter-spacing: -0.5px; }
        .nav-link-custom { color: #4a5568; font-weight: 600; font-size: 0.95rem; padding: 8px 18px !important; margin: 0 5px; }
        
        /* Hero Section */
        .hero-section { 
            padding: 100px 0;
            background: linear-gradient(rgba(255,255,255,0.9), rgba(255,255,255,0.9)), url('https://images.unsplash.com/photo-1578985545062-69928b1d9587?ixlib=rb-4.0.3&auto=format&fit=crop&w=1920&q=80');
            background-size: cover;
            background-position: center;
        }
        .hero-title { font-size: 4rem; font-weight: 800; color: #1e293b; letter-spacing: -2px; line-height: 1.1; }
        .hero-subtitle { font-size: 1.25rem; color: #64748b; max-width: 600px; margin: 20px 0 40px 0; }
        
        .btn-rose { background-color: #fb7185; color: white; border-radius: 12px; font-weight: 700; padding: 15px 35px; font-size: 1.1rem; transition: all 0.2s; border: none; }
        .btn-rose:hover { background-color: #f43f5e; color: white; transform: translateY(-2px); box-shadow: 0 10px 20px rgba(251, 113, 133, 0.2); }
        
        .feature-card { border: none; border-radius: 20px; padding: 40px; background: #fff; box-shadow: 0 4px 25px rgba(0,0,0,0.03); transition: all 0.3s; height: 100%; }
        .feature-card:hover { transform: translateY(-5px); box-shadow: 0 10px 30px rgba(0,0,0,0.05); }
        .feature-icon { width: 60px; height: 60px; border-radius: 18px; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; margin-bottom: 25px; }
        .bg-rose-light { background-color: #ffe4e6; color: #fb7185; }
        .bg-purple-light { background-color: #f3e8ff; color: #9333ea; }
        .bg-blue-light { background-color: #e0f2fe; color: #0ea5e9; }
    </style>
</head>
<body>

    <nav class="navbar navbar-expand-lg sticky-top">
        <div class="container-fluid px-5">
            <a class="navbar-brand d-flex align-items-center" href="/">
                <span class="fs-3 me-2">🧁</span>
                <span class="text-dark">cake</span>CakeForge
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <div class="ms-auto d-flex align-items-center">
                    <a href="#" class="nav-link nav-link-custom">Catalog</a>
                    <a href="/custom-requests" class="nav-link nav-link-custom">Custom Requests</a>
                    <a href="/profile" class="nav-link nav-link-custom">Profile</a>
                    <a href="/register" class="btn btn-rose ms-3 py-2 px-4" style="font-size: 0.95rem; padding: 10px 25px !important;">Get Started</a>
                </div>
            </div>
        </div>
    </nav>

    <header class="hero-section">
        <div class="container px-5">
            <div class="row align-items-center">
                <div class="col-lg-7">
                    <span class="badge px-3 py-2 rounded-pill mb-3" style="background-color: #ffe4e6; color: #fb7185; font-weight: 700;">ARTISAN BAKERY</span>
                    <h1 class="hero-title">Where Every Cake <br> Tells a <span style="color: #fb7185;">Story.</span></h1>
                    <p class="hero-subtitle">Crafting premium, handcrafted cakes for your most precious moments. From elegant weddings to whimsical birthdays, we bring your vision to life.</p>
                    <div class="d-flex gap-3">
                        <a href="/custom-requests/submit" class="btn btn-rose">Design Your Cake</a>
                        <a href="#" class="btn btn-light border-0 fw-bold px-4 py-3" style="border-radius: 12px; color: #4a5568;">View Catalog</a>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <section class="py-5 my-5">
        <div class="container px-5">
            <div class="text-center mb-5">
                <h2 class="fw-800" style="font-weight: 800; font-size: 2.5rem;">The CakeForge Experience</h2>
                <p class="text-muted">Discover why thousands trust us with their celebrations.</p>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon bg-rose-light">
                            <i class="bi bi-palette-fill"></i>
                        </div>
                        <h4 class="fw-bold mb-3">Fully Custom Designs</h4>
                        <p class="text-muted mb-0">From tiers to themes, every detail is tailored to your unique preference and celebration style.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon bg-purple-light">
                            <i class="bi bi-stars"></i>
                        </div>
                        <h4 class="fw-bold mb-3">Premium Ingredients</h4>
                        <p class="text-muted mb-0">We use only the finest, freshest ingredients to ensure your cake tastes as good as it looks.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card">
                        <div class="feature-icon bg-blue-light">
                            <i class="bi bi-truck"></i>
                        </div>
                        <h4 class="fw-bold mb-3">Safe Delivery</h4>
                        <p class="text-muted mb-0">Our specialized handling ensures your masterpiece arrives in perfect condition, right on time.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <footer class="py-5 border-top bg-white">
        <div class="container px-5">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <div class="navbar-brand d-flex align-items-center mb-0">
                        <span class="fs-3 me-2">🧁</span>
                        <span class="text-dark">cake</span>CakeForge
                    </div>
                    <p class="text-muted small mb-0 mt-2">© 2026 CakeForge Bakery Platform. All rights reserved.</p>
                </div>
                <div class="col-md-6 text-md-end">
                    <div class="d-flex justify-content-md-end gap-3 mt-4 mt-md-0">
                        <a href="#" class="text-muted"><i class="bi bi-instagram fs-4"></i></a>
                        <a href="#" class="text-muted"><i class="bi bi-facebook fs-4"></i></a>
                        <a href="#" class="text-muted"><i class="bi bi-twitter-x fs-4"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>