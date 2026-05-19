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
                <c:if test="${not empty sessionScope.admin}">
                    <li class="nav-item"><a class="nav-link" href="/standard-catalog">Manage Cakes</a></li>
                </c:if>
                <li class="nav-item"><a class="nav-link" href="/custom-requests">Custom Requests</a></li>
                <li class="nav-item"><a class="nav-link" href="/delivery">Delivery</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.admin}">
                        <li class="nav-item"><a class="nav-link" href="/bookings/my-orders">Dashboard</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="/bookings/my-orders">My Orders</a></li>
                    </c:otherwise>
                </c:choose>
                <li class="nav-item"><a class="nav-link" href="/reviews/testimonials">Reviews</a></li>
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item ms-lg-3">
                        <a href="/bookings/checkout" class="nav-link cart-icon-wrapper" aria-label="Checkout">
                            <i class="fa-solid fa-cart-shopping fa-lg"></i>
                            <span class="cart-badge js-cart-count">${cartCount}</span>
                        </a>
                    </li>
                </c:if>

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
    <c:if test="${param.deleted == 'true'}">
        <div class="alert alert-success alert-dismissible fade show glass-card border-0 mb-4" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> Profile deleted successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success alert-dismissible fade show glass-card border-0 mb-4" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> ${successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
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
                                    <div class="d-grid gap-2">
                                        <form action="/bookings/cart/buy-now" method="post">
                                            <input type="hidden" name="cakeName" value="${cake.name}">
                                            <input type="hidden" name="unitPrice" value="${cake.price}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-primary w-100">
                                                <i class="fa-solid fa-bolt me-2"></i> Buy Now
                                            </button>
                                        </form>
                                        <form action="/bookings/cart/add-from-catalog" method="post" class="js-add-to-cart-form" data-ajax-action="/bookings/cart/add-ajax">
                                            <input type="hidden" name="cakeName" value="${cake.name}">
                                            <input type="hidden" name="unitPrice" value="${cake.price}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-warning w-100">
                                                <i class="fa-solid fa-cart-plus me-2"></i> Add to Cart
                                            </button>
                                        </form>
                                    </div>
                                </c:when>
                                <c:when test="${not empty sessionScope.admin}">
                                    <a href="/bookings/my-orders" class="btn btn-primary w-100">
                                        <i class="fa-solid fa-user-shield me-2"></i> Manage Orders
                                    </a>
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

    <div id="custom-request" class="custom-cake-req glass-card text-white">
        <div class="row align-items-center">
            <div class="col-md-8">
                <h2>Custom Cake Request</h2>
                <p class="mb-0 text-white-50">Have a specific design in mind? Share your theme, flavors, and tiers with us.</p>
            </div>
            <div class="col-md-4 text-md-end mt-3 mt-md-0">
                <a href="/custom-requests/submit" class="btn btn-light btn-lg px-5 rounded-pill fw-bold">Start Request</a>
            </div>
        </div>
    </div>
</main>

<footer class="container py-5 text-center text-muted">
    <p>&copy; 2026 CakeForge Studio. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
</body>
</html>
