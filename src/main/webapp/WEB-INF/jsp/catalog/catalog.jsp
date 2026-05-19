<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cake Catalog | CakeForge Studio</title>
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
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav ms-auto align-items-center">
                <li class="nav-item"><a class="nav-link active" href="/catalog">Catalog</a></li>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:set var="userAvatar" value="${sessionScope.user.photoUrl}" />
                        <c:if test="${empty userAvatar}">
                            <c:set var="userAvatar" value="https://ui-avatars.com/api/?name=${fn:replace(sessionScope.user.name,' ','+')}&background=ff6a88&color=ffffff&size=96" />
                        </c:if>

                        <li class="nav-item dropdown ms-lg-3 profile-menu">
                            <a class="nav-link p-0" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="${userAvatar}" alt="Profile" class="profile-avatar">
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end profile-dropdown">
                                <li><a class="dropdown-item" href="/profile"><i class="fa-solid fa-user me-2"></i>View Profile</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="/logout"><i class="fa-solid fa-right-from-bracket me-2"></i>Logout</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:when test="${not empty sessionScope.admin}">
                        <c:set var="adminAvatar" value="https://ui-avatars.com/api/?name=${fn:replace(sessionScope.admin.name,' ','+')}&background=fda085&color=ffffff&size=96" />
                        <li class="nav-item dropdown ms-lg-3 profile-menu">
                            <a class="nav-link p-0" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                <img src="${adminAvatar}" alt="Admin Profile" class="profile-avatar">
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end profile-dropdown">
                                <li><a class="dropdown-item" href="/admin/profile"><i class="fa-solid fa-user-shield me-2"></i>View Admin Profile</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="/logout"><i class="fa-solid fa-right-from-bracket me-2"></i>Logout</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item ms-lg-2">
                            <a href="/login" class="btn btn-primary btn-sm rounded-pill px-4 auth-nav-btn">Login</a>
                        </li>
                        <li class="nav-item ms-lg-2">
                            <a href="/register" class="btn btn-outline-secondary btn-sm rounded-pill px-4 auth-nav-btn">Register</a>
                        </li>
                        <li class="nav-item ms-lg-2">
                            <a href="/admin/login" class="nav-link">Admin</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<header class="page-header container">
    <h1>Cake Catalog</h1>
    <p>Discover our artisanal creations, handcrafted with love and the finest ingredients.</p>
</header>

<main class="container py-4">
    <c:if test="${param.added == 'true'}">
        <div class="alert alert-success alert-dismissible fade show glass-card border-0 mb-4" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> Cake added successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.cakeDeleted == 'true'}">
        <div class="alert alert-success alert-dismissible fade show glass-card border-0 mb-4" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> Cake deleted successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.admin}">
        <section class="glass-card mb-5">
            <h4 class="mb-3"><i class="fa-solid fa-gear me-2"></i>Manage Catalog</h4>
            <form action="/catalog/admin/add" method="post" enctype="multipart/form-data">
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Cake Name</label>
                        <input type="text" class="form-control" name="name" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Price (LKR)</label>
                        <input type="number" class="form-control" name="price" min="0" step="0.01" required>
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Category</label>
                        <select class="form-select" name="category" required>
                            <option value="Signature Cakes">Signature Cakes</option>
                            <option value="Italian Desserts">Italian Desserts</option>
                            <option value="Fruit Collection">Fruit Collection</option>
                            <option value="Chocolate Collection">Chocolate Collection</option>
                        </select>
                    </div>
                    <div class="col-md-8">
                        <label class="form-label">Description</label>
                        <input type="text" class="form-control" name="description" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Cake Image Upload</label>
                        <input type="file" class="form-control" name="imageFile" accept="image/*">
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-primary">
                            <i class="fa-solid fa-plus me-2"></i>Add Cake
                        </button>
                    </div>
                </div>
            </form>
        </section>
    </c:if>

    <section class="mb-5">
        <div class="row g-4">
            <c:forEach items="${cakes}" var="cake" varStatus="loop">
                <div class="col-sm-6 col-lg-3">
                    <div class="product-card">
                        <c:choose>
                            <c:when test="${not empty cake.imageUrl}">
                                <img src="${cake.imageUrl}" alt="${cake.name}">
                            </c:when>
                            <c:otherwise>
                                <img src="/theme/img/shop/product-${(loop.index % 12) + 1}.jpg" alt="${cake.name}">
                            </c:otherwise>
                        </c:choose>
                        <div class="product-card-body">
                            <h6>${cake.name}</h6>
                            <p class="product-price">LKR ${cake.price}</p>
                            <p class="small text-muted mb-3">${cake.category}</p>
                            <p class="small mb-3">${cake.description}</p>

                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <a href="#custom-request" class="btn btn-primary w-100">
                                        <i class="fa-solid fa-cart-shopping me-2"></i> Buy Now
                                    </a>
                                </c:when>
                                <c:when test="${not empty sessionScope.admin}">
                                    <form action="/catalog/admin/delete" method="post">
                                        <input type="hidden" name="cakeId" value="${cake.id}">
                                        <button type="submit" class="btn btn-danger w-100">
                                            <i class="fa-solid fa-trash me-2"></i> Delete Cake
                                        </button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <a href="/login" class="btn btn-primary w-100">
                                        <i class="fa-solid fa-right-to-bracket me-2"></i> Login to Continue
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>

    <c:if test="${empty sessionScope.admin}">
        <div id="custom-request" class="custom-cake-req glass-card text-white">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h2>Custom Cake Request</h2>
                    <p class="mb-0 text-white-50">Have a specific design in mind? Share your theme, flavors, and tiers with us.</p>
                </div>
                <div class="col-md-4 text-md-end mt-3 mt-md-0">
                    <a href="${not empty sessionScope.user ? '/profile' : '/register'}" class="btn btn-light btn-lg px-5 rounded-pill fw-bold">Start Request</a>
                </div>
            </div>
        </div>
    </c:if>
</main>

<footer class="container py-5 text-center text-muted">
    <p>&copy; 2026 CakeForge Studio. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
</body>
</html>
