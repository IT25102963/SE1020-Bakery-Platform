<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
                <c:if test="${not empty sessionScope.admin}">
                    <li class="nav-item"><a class="nav-link" href="/delivery">Delivery</a></li>
                </c:if>
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
                <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                    <a href="/custom-requests/submit" class="btn btn-light btn-lg px-4 rounded-pill fw-bold">Start Request</a>
                </div>
            </div>
        </div>
    </div>

    <section class="glass-card p-4 mt-4" id="catalogReviews" data-reviews-version="${reviewsVersion}">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
            <div>
                <h4 class="mb-1">Customer Reviews</h4>
                <p class="text-muted mb-0">See what customers are saying about our cakes.</p>
            </div>
            <div class="d-flex gap-2 mt-2 mt-md-0">
                <a href="/reviews/testimonials" class="btn btn-outline-secondary rounded-pill px-4">All Reviews (<c:out value="${reviewCount}" />)</a>
                <c:if test="${not empty sessionScope.user}">
                    <a href="/reviews/new" class="btn btn-warning rounded-pill px-4">Write Review</a>
                </c:if>
                <c:if test="${not empty sessionScope.admin}">
                    <a href="/reviews/admin" class="btn btn-primary rounded-pill px-4">Review Admin</a>
                </c:if>
            </div>
        </div>

        <div class="row g-4" id="reviews-preview">
            <c:choose>
                <c:when test="${not empty approvedReviews}">
                    <div class="col-lg-5">
                        <div class="border rounded-4 p-3 bg-white h-100">
                            <h5 class="mb-2 fw-bold">Customer reviews</h5>
                            <div class="d-flex align-items-center gap-2 mb-2">
                                <div class="fs-4">
                                    <c:forEach var="star" begin="1" end="5">
                                        <i class="fa-solid fa-star ${star <= averageRating ? 'text-warning' : 'text-muted'}"></i>
                                    </c:forEach>
                                </div>
                                <div class="fw-semibold fs-4">
                                    <fmt:formatNumber value="${averageRating}" maxFractionDigits="1" minFractionDigits="1"/> out of 5
                                </div>
                            </div>
                            <p class="text-muted mb-3"><fmt:formatNumber value="${reviewCount}" type="number"/> global ratings</p>

                            <div class="d-flex align-items-center gap-2 mb-2">
                                <span class="small text-muted" style="width:56px;">5 star</span>
                                <div class="progress flex-grow-1" style="height:10px;">
                                    <div class="progress-bar bg-warning" role="progressbar" style="width:${star5Pct}%"></div>
                                </div>
                                <span class="small text-muted" style="width:40px;">${star5Pct}%</span>
                            </div>
                            <div class="d-flex align-items-center gap-2 mb-2">
                                <span class="small text-muted" style="width:56px;">4 star</span>
                                <div class="progress flex-grow-1" style="height:10px;">
                                    <div class="progress-bar bg-warning" role="progressbar" style="width:${star4Pct}%"></div>
                                </div>
                                <span class="small text-muted" style="width:40px;">${star4Pct}%</span>
                            </div>
                            <div class="d-flex align-items-center gap-2 mb-2">
                                <span class="small text-muted" style="width:56px;">3 star</span>
                                <div class="progress flex-grow-1" style="height:10px;">
                                    <div class="progress-bar bg-warning" role="progressbar" style="width:${star3Pct}%"></div>
                                </div>
                                <span class="small text-muted" style="width:40px;">${star3Pct}%</span>
                            </div>
                            <div class="d-flex align-items-center gap-2 mb-2">
                                <span class="small text-muted" style="width:56px;">2 star</span>
                                <div class="progress flex-grow-1" style="height:10px;">
                                    <div class="progress-bar bg-warning" role="progressbar" style="width:${star2Pct}%"></div>
                                </div>
                                <span class="small text-muted" style="width:40px;">${star2Pct}%</span>
                            </div>
                            <div class="d-flex align-items-center gap-2">
                                <span class="small text-muted" style="width:56px;">1 star</span>
                                <div class="progress flex-grow-1" style="height:10px;">
                                    <div class="progress-bar bg-warning" role="progressbar" style="width:${star1Pct}%"></div>
                                </div>
                                <span class="small text-muted" style="width:40px;">${star1Pct}%</span>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-7">
                        <div id="reviewCarousel" class="carousel slide" data-bs-ride="carousel" data-bs-interval="5000">
                            <div class="carousel-inner">
                                <c:forEach var="review" items="${approvedReviews}" varStatus="loop">
                                    <div class="carousel-item ${loop.first ? 'active' : ''}">
                                        <div class="border rounded-4 p-3 bg-white">
                                            <div class="d-flex align-items-center gap-2 mb-3">
                                                <img
                                                        src="https://ui-avatars.com/api/?name=${fn:replace(review.customerName,' ','+')}&background=ff6a88&color=ffffff&size=64"
                                                        alt="${review.customerName}"
                                                        class="rounded-circle border"
                                                        width="42"
                                                        height="42">
                                                <div>
                                                    <h6 class="mb-0">${review.customerName}</h6>
                                                    <small class="text-muted">${review.cakeName}</small>
                                                </div>
                                                <span class="ms-auto badge ${review.approved ? 'bg-success-subtle text-success border' : 'bg-warning-subtle text-warning border'}">
                                                    ${review.approved ? 'Approved' : 'Pending'}
                                                </span>
                                            </div>
                                            <div class="mb-2">
                                                <c:forEach var="star" begin="1" end="5">
                                                    <i class="fa-solid fa-star ${star <= review.rating ? 'text-warning' : 'text-muted'}"></i>
                                                </c:forEach>
                                            </div>
                                            <p class="small text-muted mb-0">${review.comment}</p>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <c:if test="${reviewCount > 1}">
                                <button class="carousel-control-prev" type="button" data-bs-target="#reviewCarousel" data-bs-slide="prev">
                                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Previous</span>
                                </button>
                                <button class="carousel-control-next" type="button" data-bs-target="#reviewCarousel" data-bs-slide="next">
                                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                    <span class="visually-hidden">Next</span>
                                </button>
                            </c:if>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="text-center text-muted py-4 border rounded-4 bg-white">
                            No approved reviews yet.
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<footer class="container py-5 text-center text-muted">
    <p>&copy; 2026 CakeForge Studio. All rights reserved.</p>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
<script>
    (function () {
        const reviewsRoot = document.getElementById('catalogReviews');
        if (!reviewsRoot) return;
        let currentVersion = reviewsRoot.dataset.reviewsVersion || '0';

        async function pollReviews() {
            if (document.hidden) return;
            try {
                const response = await fetch('/catalog/reviews/version?ts=' + Date.now(), {
                    cache: 'no-store',
                    credentials: 'same-origin'
                });
                if (!response.ok) return;
                const payload = await response.json();
                const nextVersion = String(payload.version || '0');
                if (nextVersion !== currentVersion) {
                    window.location.reload();
                    return;
                }
                currentVersion = nextVersion;
            } catch (e) {
                // ignore transient polling issues
            }
        }

        setInterval(pollReviews, 4000);
    })();
</script>
</body>
</html>
