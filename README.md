# 🎂 Bakery Order and Custom Cake Booking Platform

Welcome to Group WD237's Object-Oriented Programming Project!

This project is a web-based Bakery Platform built utilizing Java Servlets, JSP, and File Handling. Since we are all learning, this document includes a complete step-by-step guide to setting up your PC so you don't get any errors!

---

## 👥 Module Assignments (Our 6 Roles)
Each member must do exactly 3 CRUD operations and 3 UI pages for their module to get the full marks.

| Role | Module Focus | Assigned Text File | Required UIs (Min. 3) | Assigned CRUD Tasks |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **Customer Identity** | `customers.txt` | Register Page, Profile, Edit Profile | Create Account, Read Profile, Update Details |
| **2** | **Standard Cake Catalog** | `standard_cakes.txt` | Add Cake, Catalog Page, Delete UI | Add Cake, Read Catalog, Delete Cake |
| **3** | **Custom Cake Requests** | `custom_requests.txt` | Request Form, Dashboard, Edit UI | Submit Request, Read Status, Update Details |
| **4** | **Order & Bookings** | `bookings.txt` | Checkout Form, Receipt, Status UI | Place Booking, Read Receipt, Update Status |
| **5** | **Delivery Scheduler** | `schedules.txt` | Schedule Form, Roster, Cancel UI | Schedule Slot, Read Roster, Delete Slot |
| **6** | **Customer Reviews** | `reviews.txt` | Review Form, Testimonials, Delete UI | Submit Review, Read Reviews, Delete Review |

---

## 📁 Where Do I Put My Code?
To avoid deleting each other's work, **ONLY work inside your assigned folder!**

* 🧠 **BACKEND (Java):** `src/main/java/lk/sliit/it25/bakeryweb/[your_module_name]/`
* 🎨 **FRONTEND (JSP/HTML):** `src/main/webapp/[your_module_name]/`
* 🗄️ **DATA (Text Files):** `src/main/webapp/data/` *(Note: When the server runs, look for your saved text files inside the `target/` folder at the bottom of IntelliJ!)*

---

## 🛠️ THE STEP-BY-STEP SETUP GUIDE (READ THIS FIRST)
If you do not follow these steps, your project will not run.

### Step 1: Download the Project
1. Open a terminal or Git Bash on your computer.
2. Navigate to where you want to save the project (e.g., `cd Documents`).
3. Run: `git clone https://github.com/[Your-Username]/[Repo-Name].git`
4. Open **IntelliJ IDEA**, click **Open**, and select the downloaded folder.

### Step 2: Set up the Tomcat Server (Crucial!)
Since we don't have a database, we need Tomcat to run our Java Servlets.
1. Download **Apache Tomcat 10.1** (the zip file) from the official website and extract it somewhere safe on your computer (like `C:\Tomcat10`).
2. In IntelliJ, look at the very top right and click **Current File** or **Add Configuration**.
3. Click the **+** button -> **Tomcat Server** -> **Local**.
4. Click **Configure...** and select the folder where you extracted Tomcat 10.1.
5. Give the server a name at the top (e.g., "Bakery Server").

### Step 3: Fix the "Deployment" Error
If you skip this, you will get a 404 Error when you click play.
1. In that same Tomcat Configuration window, click the **Deployment** tab at the top.
2. Click the **+** button and select **Artifact...**
3. Select **`BakeryWeb:war exploded`**.
4. Important: Look at the "Application context" box at the bottom. Delete whatever is there and just type `/` (a single forward slash).
5. Click **Apply** and **OK**.

### Step 4: Run the Project
* Click the Green **Play ▶️** button at the top right of IntelliJ.
* Your browser should open automatically and say "Hello World!". To see your specific pages, type your folder name in the URL (e.g., `http://localhost:8080/customer/register.jsp`).

---

## 🚀 How to Save Your Work to GitHub (MANDATORY)
The lecturers will check GitHub to give us our 10 Individual Marks. You must push your own code! When you finish working for the day, open the IntelliJ terminal and type:

1. `git pull origin main` *(Always do this first to get your team's latest updates!)*
2. `git add .` *(Adds your changed files)*
3. `git commit -m "Your simple message here"` *(Explains what you did)*
4. `git push origin main` *(Sends it to GitHub)*

Let's crush this project!