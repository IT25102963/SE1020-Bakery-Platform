# Order & Bookings Management System

Spring Boot CRUD web application module for a Bakery System to manage only Order & Bookings.

## Tech Stack

- Java 17
- Spring Boot 3.5.13
- Maven
- Thymeleaf
- Bean Validation
- Text-file data storage (CSV in `bookings.txt`)

## Package

`com.cakecraft.orderbookings`

## Features

- Create Booking
- View All Bookings
- View Single Booking Receipt by ID
- Search Booking by ID
- Edit Booking Details
- Update Booking Status
- Delete Booking
- Cancel Booking (status -> Cancelled)

## Booking Data File

All booking records are stored in:

`src/main/resources/data/bookings.txt`

Each booking is one CSV line:

`bookingId,customerName,phone,cakeName,orderType,quantity,bookingDate,deliveryDate,totalPrice,status`

Example:

`B001,Lakshitha,0771234567,Chocolate Truffle,Standard,1,2026-03-27,2026-03-30,4500.00,Pending`

## Routes

- `/bookings`
- `/bookings/new`
- `/bookings/save`
- `/bookings/search`
- `/bookings/receipt/{id}`
- `/bookings/edit/{id}`
- `/bookings/update/{id}`
- `/bookings/status/{id}`
- `/bookings/delete/{id}`
- `/bookings/cancel/{id}`

## Run Instructions

1. Ensure Java 17 is installed.
2. Open project in IntelliJ IDEA or VS Code.
3. Run the app:
   - macOS/Linux: `./mvnw spring-boot:run`
   - Windows: `mvnw.cmd spring-boot:run`
4. Open browser:
   - `http://localhost:8080/bookings`

## Notes

- No JPA, Hibernate, SQL, or MySQL is used.
- Application follows MVC architecture with Controller, Service, Repository, Model, and Utility layers.
