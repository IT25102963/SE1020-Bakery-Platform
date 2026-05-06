<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="lk.sliit.it25.bakeryweb.model.DeliverySlot" %>
<!DOCTYPE html>
<html>
<head>
    <title>Delivery Scheduler</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 24px; }
        h1, h2 { margin-bottom: 12px; }
        form { margin-bottom: 20px; }
        input { margin: 6px 0; padding: 8px; width: 320px; display: block; }
        button { padding: 8px 14px; }
        table { border-collapse: collapse; width: 100%; max-width: 900px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background: #f5f5f5; }
        .message { color: #0a6c22; margin: 10px 0; }
    </style>
</head>
<body>
<h1>Delivery Scheduler</h1>

<% String message = (String) request.getAttribute("message"); %>
<% if (message != null && !message.isBlank()) { %>
<p class="message"><%= message %></p>
<% } %>

<h2>Schedule Time</h2>
<form method="post" action="${pageContext.request.contextPath}/delivery/schedule">
    <label>Customer Name</label>
    <input type="text" name="customerName" required />

    <label>Address</label>
    <input type="text" name="address" required />

    <label>Delivery Date</label>
    <input type="date" name="deliveryDate" required />

    <label>Delivery Time</label>
    <input type="time" name="deliveryTime" required />

    <button type="submit">Schedule</button>
</form>

<h2>View Roster</h2>
<table>
    <thead>
    <tr>
        <th>Slot ID</th>
        <th>Customer</th>
        <th>Address</th>
        <th>Date</th>
        <th>Time</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<DeliverySlot> slots = (List<DeliverySlot>) request.getAttribute("slots");
        if (slots != null && !slots.isEmpty()) {
            for (DeliverySlot slot : slots) {
    %>
    <tr>
        <td><%= slot.getId() %></td>
        <td><%= slot.getCustomerName() %></td>
        <td><%= slot.getAddress() %></td>
        <td><%= slot.getDeliveryDate() %></td>
        <td><%= slot.getDeliveryTime() %></td>
        <td>
            <form method="post" action="${pageContext.request.contextPath}/delivery/cancel">
                <input type="hidden" name="slotId" value="<%= slot.getId() %>" />
                <button type="submit">Cancel Slot</button>
            </form>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr>
        <td colspan="6">No scheduled slots yet.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>
</body>
</html>
