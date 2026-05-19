<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Standard Cake Catalog | BakeryWeb</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="m-0">Standard Cake Catalog (Manage)</h2>
        <div class="d-flex gap-2">
            <a href="/admin/profile" class="btn btn-primary btn-sm rounded-pill px-3">
                <i class="fa-solid fa-user-shield me-1"></i> Admin Profile
            </a>
            <a href="/bookings/my-orders" class="btn btn-outline-dark btn-sm rounded-pill px-3">
                <i class="fa-solid fa-clipboard-list me-1"></i> Orders Dashboard
            </a>
            <a href="/catalog" class="btn btn-warning btn-sm rounded-pill px-3 text-white">
                <i class="fa-solid fa-store me-1"></i> Customer Catalog
            </a>
        </div>
    </div>

    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="row g-4">
        <div class="col-lg-4">
            <div class="glass-card">
                <h5 class="fw-bold mb-3">Add Cake</h5>
                <form action="/standard-catalog/add" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label class="form-label">Name</label>
                        <input class="form-control" type="text" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Category</label>
                        <input class="form-control" type="text" name="category" placeholder="Signature Cakes">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Price (LKR)</label>
                        <input class="form-control" type="number" name="price" min="1" step="0.01" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Cake Image</label>
                        <input class="form-control" type="file" name="cakeImage" accept="image/*">
                    </div>
                    <button class="btn btn-primary w-100" type="submit">Add to Catalog</button>
                </form>
            </div>
        </div>

        <div class="col-lg-8">
            <div class="glass-card">
                <h5 class="fw-bold mb-3">Catalog Items</h5>
                <div class="table-responsive">
                    <table class="table align-middle">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Description</th>
                            <th>Image</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cakes}" var="c">
                            <tr>
                                <td>${c.name}</td>
                                <td>${c.category}</td>
                                <td>${c.price}</td>
                                <td>${c.description}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty c.imageUrl}">
                                            <img src="${c.imageUrl}" alt="${c.name}" style="width:60px;height:60px;object-fit:cover;border-radius:8px;">
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted small">No image</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-center">
                                    <div class="d-inline-flex align-items-center gap-1">
                                        <a href="/standard-catalog/edit?id=${c.id}"
                                           class="btn-action btn-edit"
                                           title="Edit Cake">
                                            <i class="fa-solid fa-pen"></i>
                                        </a>
                                        <form action="/standard-catalog/delete" method="post" class="d-inline"
                                              onsubmit="return confirm('Delete this cake from catalog?');">
                                            <input type="hidden" name="id" value="${c.id}">
                                            <button class="btn-action btn-delete" type="submit" title="Delete Cake">
                                                <i class="fa-solid fa-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty cakes}">
                            <tr>
                                <td colspan="6" class="text-muted">No cakes available.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
