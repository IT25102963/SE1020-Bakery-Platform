<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <a href="/standard-catalog" class="btn btn-warning text-white nav-action-btn">Manage Cakes</a>
            <a href="/bookings/my-orders" class="btn btn-outline-dark nav-action-btn">Orders Dashboard</a>
            <a href="/logout" class="btn btn-dark nav-action-btn">Logout</a>
        </div>
    </div>
</nav>

<main class="auth-page container py-5">
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
            <div class="glass-card auth-card">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2 class="m-0">Admin Profile</h2>
                    <a href="/admin/editProfile" class="btn btn-primary btn-sm rounded-pill px-3">
                        <i class="fa-solid fa-pen-to-square me-1"></i> Edit
                    </a>
                </div>
                <div class="profile-row"><span>Name</span><strong>${admin.name}</strong></div>
                <div class="profile-row"><span>Email</span><strong>${admin.email}</strong></div>
                <div class="profile-row"><span>Role</span><strong>Administrator</strong></div>
                <div class="d-flex gap-2 mt-4">
                    <a href="/standard-catalog" class="btn btn-warning text-white w-100">Manage Cake Catalog</a>
                    <a href="/bookings/my-orders" class="btn btn-outline-secondary w-100">Manage Orders</a>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
