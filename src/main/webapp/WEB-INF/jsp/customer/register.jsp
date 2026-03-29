<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Customer Registration | Bakery Platform</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .register-card {
            background: white;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
            width: 100%;
            max-width: 400px;
        }
        .register-card h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-weight: bold;
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box; /* Ensures padding doesn't break width */
        }
        .submit-btn {
            width: 100%;
            padding: 10px;
            background-color: #d2691e; /* Chocolate/Bakery color */
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 10px;
        }
        .submit-btn:hover {
            background-color: #a0522d;
        }
    </style>
</head>
<body>

<div class="register-card">
    <h2>Create an Account</h2>

    <form action="/registerCustomer" method="POST">

        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="customerName" required placeholder="e.g. Anura Rohana">
        </div>

        <div class="form-group">
            <label>Email Address</label>
            <input type="email" name="customerEmail" required placeholder="anura@example.com">
        </div>

        <div class="form-group">
            <label>Phone Number</label>
            <input type="text" name="customerPhone" required placeholder="077xxxxxxx">
        </div>

        <div class="form-group">
            <label>Delivery Address</label>
            <textarea name="customerAddress" rows="3" required placeholder="Enter full address"></textarea>
        </div>

        <div class="form-group">
            <label>Password</label>
            <input type="password" name="customerPassword" required>
        </div>

        <button type="submit" class="submit-btn">Register</button>
    </form>
</div>

</body>
</html>