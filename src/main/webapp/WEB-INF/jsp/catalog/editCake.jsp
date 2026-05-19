<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Cake | BakeryWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="m-0">Edit Cake</h2>
        <a href="/standard-catalog" class="btn btn-outline-secondary btn-sm rounded-pill px-3">
            <i class="fa-solid fa-arrow-left me-1"></i> Back to Catalog Manage
        </a>
    </div>

    <div class="glass-card">
        <form action="/standard-catalog/edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${cake.id}">

            <div class="row g-4">
                <div class="col-lg-7">
                    <div class="mb-3">
                        <label class="form-label">Name</label>
                        <input class="form-control" type="text" name="name" value="${cake.name}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Category</label>
                        <input class="form-control" type="text" name="category" value="${cake.category}" placeholder="Signature Cakes">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Price (LKR)</label>
                        <input class="form-control" type="number" name="price" min="1" step="0.01" value="${cake.price}" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" rows="4">${cake.description}</textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Replace Image (optional)</label>
                        <input class="form-control" type="file" name="cakeImage" accept="image/*">
                    </div>
                </div>

                <div class="col-lg-5">
                    <label class="form-label">Current Image</label>
                    <div class="border rounded-3 p-3 bg-light h-100 d-flex align-items-center justify-content-center">
                        <c:choose>
                            <c:when test="${not empty cake.imageUrl}">
                                <img src="${cake.imageUrl}" alt="${cake.name}" style="max-width:100%;max-height:280px;object-fit:cover;border-radius:12px;">
                            </c:when>
                            <c:otherwise>
                                <span class="text-muted">No image available</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-4">
                <a href="/standard-catalog" class="btn btn-outline-secondary">Cancel</a>
                <button class="btn btn-primary" type="submit">Save Changes</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
