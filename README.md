# 🎂 Bakery Order and Custom Cake Booking Platform

Welcome to the repository for Group WD237's Object-Oriented Programming Project.

This project is a web-based Bakery Platform built utilizing Java Servlets, JSP, and File Handling. This document outlines our project architecture and collaboration guidelines to ensure a smooth development process and help everyone achieve maximum marks.

---

## 📌 Version Control & Individual Contributions
To ensure every team member receives their full 10 marks for individual contribution during the Viva, we must maintain a clear commit history.
* **Guideline:** Please commit and push your own code using your personal GitHub account. Avoid sharing code files for others to upload, as your individual commits serve as proof of your work to the lecturers.

---

## 📁 Project Architecture & File Structure
Our repository follows a standard Maven directory structure. To avoid merge conflicts and keep our codebase organized, please work strictly within your assigned module folders.

* **Backend Logic (Java Models & Servlets):** `src/main/java/lk/sliit/it25/bakeryweb/`
    * Please place your `.java` files exclusively inside your assigned sub-package (e.g., `customer/`, `catalog/`).
* **Frontend UI (JSP/HTML/CSS):** `src/main/webapp/`
    * Please place your `.jsp` files inside your assigned directory (e.g., `customer/`, `catalog/`).
* **Data Storage:** `src/main/webapp/data/`
    * Per our project requirements, we are using file handling instead of a database. All text files (`.txt`) for data persistence will be stored here.

---

## 👥 Module Assignments
Each team member is responsible for implementing exactly 3 CRUD operations and designing a minimum of 3 UI pages for their specific module.

| Role | Module Focus | Assigned Text File | Required UIs (Min. 3) | Assigned CRUD Tasks |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **Customer Identity** | `customers.txt` | Register Page, Profile Dashboard, Edit Profile | Create Account, Read Profile, Update Details |
| **2** | **Standard Cake Catalog** | `standard_cakes.txt` | Add Cake Form, Catalog Page, Delete Confirmation | Add Cake, Read Catalog, Delete Cake |
| **3** | **Custom Cake Requests** | `custom_requests.txt` | Request Form, Requests Dashboard, Edit Request | Submit Request, Read Status, Update Details |
| **4** | **Order & Bookings** | `bookings.txt` | Checkout Form, Receipt Page, Status Update UI | Place Booking, Read Receipt, Update Status |
| **5** | **Delivery Scheduler** | `schedules.txt` | Schedule Form, Daily Roster Page, Cancel UI | Schedule Slot, Read Roster, Delete/Cancel Slot |
| **6** | **Customer Reviews** | `reviews.txt` | Review Form, Testimonials Page, Delete UI | Submit Review, Read Reviews, Delete Review |

---

## 📚 Recommended Learning Topics
To successfully build your module, here are the three core technical concepts to focus on. Short tutorials on these specific topics will be highly beneficial:

1.  **JSP Fundamentals:** How to create basic HTML `<form>` elements and style them (using standard CSS, Tailwind, or Bootstrap) within a `.jsp` file.
2.  **Java Servlets (`HttpServlet`):** How to utilize the `doPost()` and `doGet()` methods to retrieve user input from your JSP frontend.
3.  **Java File I/O:** How to use `FileWriter`/`BufferedWriter` to save data to your `.txt` file, and `BufferedReader`/`Scanner` to retrieve it.

---

## 🚀 Getting Started Workflow
When you are ready to begin coding, please follow this standard Git workflow:

1.  Open your IDE terminal.
2.  Sync your local repository: `git pull origin main`
3.  Develop your features inside your assigned folders.
4.  Stage, commit, and push your changes:
    * `git add .`
    * `git commit -m "Briefly describe your update here"`
    * `git push origin main`

Let's build something great together. Good luck, team!