<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | CakeForge Studio</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<c:set var="avatarUrl" value="${user.photoUrl}" />
<c:if test="${empty avatarUrl}">
    <c:set var="avatarUrl" value="https://ui-avatars.com/api/?name=${fn:replace(user.name,' ','+')}&background=ff6a88&color=ffffff&size=128" />
</c:if>

<nav class="navbar navbar-expand-lg cake-navbar">
    <div class="container">
        <a class="navbar-brand" href="/catalog">
            <img src="/theme/img/logo.png" alt="CakeForge" class="brand-logo">
            <span>CakeForge</span>
        </a>
        <div class="ms-auto d-flex gap-2">
            <a href="/catalog" class="btn btn-outline-secondary btn-sm rounded-pill px-4">Back to Catalog</a>
            <a href="/logout" class="btn btn-primary btn-sm rounded-pill px-4">Logout</a>
        </div>
    </div>
</nav>

<header class="page-header container">
    <h1>My Profile</h1>
    <p>Manage your account details, profile photo, and security settings.</p>
</header>

<main class="container py-2">
    <c:if test="${param.updated == 'true'}">
        <div class="alert alert-success alert-dismissible fade show glass-card border-0 mb-4" role="alert">
            <i class="fa-solid fa-circle-check me-2"></i> Profile updated successfully.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.error == 'update'}">
        <div class="alert alert-danger">Unable to update profile right now. Please try again.</div>
    </c:if>
    <c:if test="${param.error == 'delete'}">
        <div class="alert alert-danger">Unable to delete profile right now. Please try again.</div>
    </c:if>

    <div class="row g-4">
        <div class="col-lg-5">
            <div class="glass-card text-center">
                <img src="${avatarUrl}" alt="Profile Photo" class="profile-photo mb-3">
                <h4 class="fw-bold mb-1">${user.name}</h4>
                <p class="text-muted mb-4">${user.email}</p>

                <div class="d-grid gap-2">
                    <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#editProfileForm" aria-expanded="${param.edit == 'true'}">
                        <i class="fa-solid fa-pen-to-square me-2"></i> Edit Profile
                    </button>
                    <form action="/profile/delete" method="post">
                        <button type="submit" class="btn btn-outline-danger w-100 js-confirm" data-confirm-message="Delete your profile permanently? This action cannot be undone.">
                            <i class="fa-solid fa-trash me-2"></i> Delete Profile
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-lg-7">
            <div class="glass-card mb-4">
                <h5 class="fw-bold mb-3">Profile Details</h5>
                <div class="profile-detail-row"><span>Full Name</span><strong>${user.name}</strong></div>
                <div class="profile-detail-row"><span>Email Address</span><strong>${user.email}</strong></div>
                <div class="profile-detail-row"><span>Phone Number</span><strong>${user.phone}</strong></div>
                <div class="profile-detail-row"><span>Address</span><strong>${user.address}</strong></div>
            </div>

            <div class="glass-card collapse ${param.edit == 'true' ? 'show' : ''}" id="editProfileForm">
                <h5 class="fw-bold mb-3">Edit Profile</h5>
                <form action="/profile/update" method="post">
                    <div class="mb-3">
                        <label for="name" class="form-label fw-semibold">Full Name</label>
                        <input type="text" class="form-control" id="name" name="name" value="${user.name}" required>
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label fw-semibold">Phone Number</label>
                        <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}" required>
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label fw-semibold">Address</label>
                        <input type="text" class="form-control" id="address" name="address" value="${user.address}" required>
                    </div>
                    <div class="mb-3">
                        <label for="photoUrl" class="form-label fw-semibold">Profile Photo URL</label>
                        <input type="url" class="form-control" id="photoUrl" name="photoUrl" value="${user.photoUrl}" placeholder="https://example.com/photo.jpg">
                    </div>
                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">New Password (Leave blank to keep current)</label>
                        <input type="password" class="form-control" id="password" name="password">
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fa-solid fa-floppy-disk me-2"></i> Save Changes
                    </button>
                </form>
            </div>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/js/script.js"></script>
</body>
</html>
