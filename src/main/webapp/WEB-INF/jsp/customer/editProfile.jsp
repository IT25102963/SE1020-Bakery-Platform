<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile | BakeryWeb</title>
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
                        <h3 class="card-title m-0">Edit Profile</h3>
                        <a href="/profile" class="btn btn-warning btn-sm rounded-pill px-3 text-white">
                            <i class="fa-solid fa-arrow-left me-1"></i> Back
                        </a>
                    </div>
                    <form action="/customer/editProfile" method="post" enctype="multipart/form-data">
                        <div class="text-center mb-3">
                            <c:choose>
                                <c:when test="${not empty user.photoUrl}">
                                    <img id="profilePreview" src="${user.photoUrl}" alt="Profile Picture" class="rounded-circle mb-3" width="150" height="150">
                                    <i id="profileIcon" class="fa-solid fa-user-circle fa-5x text-muted d-none"></i>
                                </c:when>
                                <c:otherwise>
                                    <img id="profilePreview" src="" alt="Profile Picture" class="rounded-circle mb-3 d-none" width="150" height="150">
                                    <i id="profileIcon" class="fa-solid fa-user-circle fa-5x text-muted"></i>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="mb-3">
                            <label for="profilePicture" class="form-label">Profile Picture</label>
                            <input type="file" class="form-control" id="profilePicture" name="profilePicture" accept="image/*">
                        </div>
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input type="text" class="form-control" id="name" name="name" value="${user.name}" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email address</label>
                            <input type="email" class="form-control" id="email" value="${user.email}" readonly>
                        </div>
                        <div class="mb-3">
                            <label for="phone" class="form-label">Mobile Number</label>
                            <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}">
                        </div>
                        <div class="mb-3">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address" value="${user.address}">
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Update Profile</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    const profileInput = document.getElementById("profilePicture");
    const previewImage = document.getElementById("profilePreview");
    const profileIcon = document.getElementById("profileIcon");

    profileInput.addEventListener("change", function () {
        const file = this.files && this.files[0];
        if (!file) {
            return;
        }

        const reader = new FileReader();
        reader.onload = function (event) {
            previewImage.src = event.target.result;
            previewImage.classList.remove("d-none");
            profileIcon.classList.add("d-none");
        };
        reader.readAsDataURL(file);
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
