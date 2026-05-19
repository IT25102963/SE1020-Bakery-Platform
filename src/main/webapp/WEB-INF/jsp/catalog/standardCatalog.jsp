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
            <a href="/catalog" class="btn btn-primary btn-sm rounded-pill px-3">
                <i class="fa-solid fa-store me-1"></i> Customer Catalog
            </a>
            <a href="/delivery" class="btn btn-warning btn-sm rounded-pill px-3 text-white">
                <i class="fa-solid fa-truck me-1"></i> Delivery
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
                <form action="/standard-catalog/add" method="post">
                    <div class="mb-3">
                        <label class="form-label">Name</label>
                        <input class="form-control" type="text" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Flavor</label>
                        <input class="form-control" type="text" name="flavor">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Size</label>
                        <input class="form-control" type="text" name="size">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Price (LKR)</label>
                        <input class="form-control" type="number" name="price" min="1" step="0.01" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" rows="3"></textarea>
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
                            <th>Flavor</th>
                            <th>Size</th>
                            <th>Price</th>
                            <th>Description</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cakes}" var="c">
                            <tr>
                                <td>${c.name}</td>
                                <td>${c.flavor}</td>
                                <td>${c.size}</td>
                                <td>${c.price}</td>
                                <td>${c.description}</td>
                                <td>
                                    <form action="/standard-catalog/delete" method="post">
                                        <input type="hidden" name="id" value="${c.id}">
                                        <button class="btn btn-danger btn-sm" type="submit">Delete</button>
                                    </form>
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
