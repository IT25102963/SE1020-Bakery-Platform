<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Profile | CakeForge Studio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="/catalog">
            <i class="fa-solid fa-cake-candles me-2"></i><span>CakeForge</span>
        </a>
        <div class="ms-auto d-flex gap-2">
            <a href="/catalog" class="btn btn-outline-dark nav-action-btn">Back to Catalog</a>
            <a href="/logout" class="btn btn-dark nav-action-btn">Logout</a>
        </div>
    </div>
</nav>

<main class="auth-page container py-5">
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="glass-card auth-card">
                <h2 class="text-center mb-4">Admin Profile</h2>
                <div class="profile-row"><span>Name</span><strong>${admin.name}</strong></div>
                <div class="profile-row"><span>Email</span><strong>${admin.email}</strong></div>
                <div class="profile-row"><span>Role</span><strong>Administrator</strong></div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
