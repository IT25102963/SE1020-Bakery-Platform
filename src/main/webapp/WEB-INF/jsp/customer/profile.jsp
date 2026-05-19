<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | BakeryWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <div class="card glass-card mt-5">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <a href="/catalog" class="btn btn-primary btn-sm rounded-pill px-3">
                            <i class="fa-solid fa-house me-1"></i> Home
                        </a>
                        <h3 class="card-title m-0">My Profile</h3>
                        <a href="/customer/editProfile" class="btn btn-warning btn-sm rounded-pill px-3 text-white">
                            <i class="fa-solid fa-pen-to-square me-1"></i> Edit
                        </a>
                    </div>

                    <div class="text-center">
                    <c:choose>
                        <c:when test="${not empty user.photoUrl}">
                            <img src="${user.photoUrl}" alt="Profile Picture" class="rounded-circle mb-3" width="150" height="150">
                        </c:when>
                        <c:otherwise>
                            <i class="fa-solid fa-user-circle fa-5x text-muted mb-3"></i>
                        </c:otherwise>
                    </c:choose>
                    </div>

                    <ul class="list-group list-group-flush text-start mt-3">
                        <li class="list-group-item">
                            <div class="text-muted small">Name</div>
                            <div class="fw-semibold">${user.name}</div>
                        </li>
                        <li class="list-group-item">
                            <div class="text-muted small">Email</div>
                            <div class="fw-semibold">${user.email}</div>
                        </li>
                        <li class="list-group-item">
                            <div class="text-muted small">Phone</div>
                            <div class="fw-semibold">${empty user.phone ? 'Not set' : user.phone}</div>
                        </li>
                        <li class="list-group-item">
                            <div class="text-muted small">Address</div>
                            <div class="fw-semibold">${empty user.address ? 'Not set' : user.address}</div>
                        </li>
                        <li class="list-group-item">
                            <div class="text-muted small">Password</div>
                            <div class="fw-semibold">********</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
