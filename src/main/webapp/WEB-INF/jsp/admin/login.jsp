<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login | CakeForge Studio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/bookings/products">
            <img src="${pageContext.request.contextPath}/img/cake-brand.png" alt="CakeForge" class="brand-logo">
            <span>CakeForge</span>
        </a>
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings/products">Catalog</a></li>
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/bookings/my-orders">Bookings</a></li>
                <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/delivery">Delivery</a></li>
            </ul>
        </div>
    </div>
</nav>

<header class="page-header container">
    <h1>Admin Login</h1>
    <p>Sign in to manage CakeForge operations.</p>
</header>

<main class="container py-2">
    <div class="row justify-content-center">
        <div class="col-lg-5 col-md-8">
            <div class="glass-card">
                <c:if test="${param.registered == 'true'}">
                    <div class="alert alert-success">Admin registration successful. Please log in.</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="/admin/processLogin" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label fw-semibold">Email Address</label>
                        <input type="email" class="form-control" id="email" name="email" required>
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-lock me-2"></i> Admin Login
                    </button>
                </form>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
