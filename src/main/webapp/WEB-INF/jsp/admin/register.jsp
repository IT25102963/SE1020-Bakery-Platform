<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Register | CakeForge Studio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="/catalog">
            <img src="/theme/img/logo.png" alt="CakeForge" class="brand-logo">
            <span>CakeForge</span>
        </a>
        <div class="ms-auto d-flex gap-2">
            <a href="/admin/login" class="btn btn-primary btn-sm rounded-pill px-4 auth-nav-btn">Admin Login</a>
        </div>
    </div>
</nav>

<header class="page-header container">
    <h1>Create Admin Account</h1>
    <p>Register an administrator profile for platform management.</p>
</header>

<main class="container py-2">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-9">
            <div class="glass-card">
                <form action="/admin/processRegister" method="post">
                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Full Name</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email Address</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-user-shield me-2"></i> Register Admin
                    </button>
                </form>

                <p class="small text-muted text-center mt-4 mb-0">
                    Already have an admin account? <a href="/admin/login">Login now</a>
                </p>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
