<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Standard Cake Catalog | Bakery Platform</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f4f4f9;
            margin: 0;
            padding: 24px;
            color: #222;
        }
        .wrap {
            max-width: 980px;
            margin: 0 auto;
        }
        h1 {
            margin: 0 0 12px 0;
            color: #3b2b22;
        }
        .grid {
            display: grid;
            grid-template-columns: 360px 1fr;
            gap: 18px;
            align-items: start;
        }
        .card {
            background: white;
            border-radius: 12px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.08);
            padding: 18px;
        }
        .msg {
            padding: 10px 12px;
            border-radius: 10px;
            margin-bottom: 12px;
            font-weight: 600;
        }
        .msg.ok { background: #e9f7ef; color: #1e7e34; }
        .msg.err { background: #fdecea; color: #b02a37; }
        label {
            display: block;
            margin-top: 10px;
            font-weight: 700;
            color: #5a463a;
        }
        input, textarea {
            width: 100%;
            box-sizing: border-box;
            margin-top: 6px;
            padding: 10px;
            border: 1px solid #d7d7d7;
            border-radius: 8px;
        }
        textarea { resize: vertical; }
        .btn {
            display: inline-block;
            border: none;
            border-radius: 10px;
            padding: 10px 12px;
            cursor: pointer;
            font-weight: 800;
        }
        .btn.primary {
            width: 100%;
            margin-top: 12px;
            background: #d2691e;
            color: #fff;
        }
        .btn.primary:hover { background: #a0522d; }
        .btn.danger {
            background: #b02a37;
            color: #fff;
        }
        .btn.danger:hover { background: #8c1f2a; }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            text-align: left;
            padding: 10px 8px;
            border-bottom: 1px solid #eee;
            vertical-align: top;
        }
        th {
            color: #5a463a;
            font-size: 13px;
            text-transform: uppercase;
            letter-spacing: 0.03em;
        }
        .muted { color: #666; font-size: 13px; }
        @media (max-width: 860px) {
            .grid { grid-template-columns: 1fr; }
        }
    </style>
</head>
<body>
<div class="wrap">
    <h1>Standard Cake Catalog</h1>
    <div class="muted">Add pre-made cakes, view the catalog, and delete items. Storage file: <b>data/standard_cakes.txt</b></div>

    <c:if test="${not empty success}">
        <div class="msg ok">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="msg err">${error}</div>
    </c:if>

    <div class="grid">
        <div class="card">
            <h2 style="margin:0 0 6px 0;">Add Cake</h2>
            <form action="/catalog/add" method="POST">
                <label>Name *</label>
                <input type="text" name="name" required placeholder="e.g. Chocolate Fudge Cake" />

                <label>Flavor</label>
                <input type="text" name="flavor" placeholder="e.g. Chocolate" />

                <label>Size</label>
                <input type="text" name="size" placeholder="e.g. 1kg / 8-inch" />

                <label>Price (LKR) *</label>
                <input type="number" name="price" min="1" step="0.01" required placeholder="e.g. 3500.00" />

                <label>Description</label>
                <textarea name="description" rows="4" placeholder="Short description (optional)"></textarea>

                <button type="submit" class="btn primary">Add to Catalog</button>
            </form>
        </div>

        <div class="card">
            <h2 style="margin:0 0 6px 0;">Catalog</h2>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Flavor</th>
                    <th>Size</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cakes}" var="c">
                    <tr>
                        <td><b><c:out value="${c.name}"/></b></td>
                        <td><c:out value="${c.flavor}"/></td>
                        <td><c:out value="${c.size}"/></td>
                        <td><c:out value="${c.price}"/></td>
                        <td class="muted"><c:out value="${c.description}"/></td>
                        <td>
                            <form action="/catalog/delete" method="POST" style="margin:0;">
                                <input type="hidden" name="id" value="${c.id}"/>
                                <button type="submit" class="btn danger" onclick="return confirm('Delete this cake?');">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty cakes}">
                    <tr>
                        <td colspan="6" class="muted">No cakes yet. Add your first pre-made cake using the form.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
