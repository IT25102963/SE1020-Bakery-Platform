# 🥐 BakeryWeb Platform - Developer Guide

Welcome to the BakeryWeb project! We have upgraded our architecture from a manual Tomcat server to a modern **Spring Boot** engine. This makes routing, security, and running the application much easier.

**IMPORTANT:** Please read this guide completely before writing any new code to avoid breaking the application.

---

## 🚀 Step 1: Getting Started
Before you do anything, make sure you have the latest architecture. Open your terminal in IntelliJ and run:
`git pull origin main`

### How to Start the Server
❌ **DO NOT** use the old Tomcat configuration.
✅ **DO THIS:** 1. In the project tree, go to `src/main/java/lk/sliit/it25/bakeryweb/`
2. Right-click **`BakerywebApplication.java`**
3. Click **▶️ Run 'BakerywebApplication.main()'**
4. Wait for the large "SPRING" logo to appear in the terminal.
5. Open your browser and go to `http://localhost:8080/`

---

## 📁 Step 2: The New Folder Structure
Spring Boot is strict about where files live. Please stick to your assigned module folders!

* **Frontend UI (`.jsp` files):** `src/main/webapp/WEB-INF/jsp/[your-module]/`
  *(Note: Files must go in WEB-INF/jsp/ so Spring Boot can secure them!)*
* **Backend Brains (`.java` files):** `src/main/java/lk/sliit/it25/bakeryweb/[your-module]/`
* **Text File Database (`.txt` files):** `/data/` (At the root of the project).

---

## 🛠️ Step 3: How to Create a New Web Page
If you want to create a new page (for example, an Add Cake page for the Catalog), follow this 2-step workflow:

**1. Create the JSP File:**
Create your `addCake.jsp` file inside `WEB-INF/jsp/catalog/`.

**2. Route it in your Controller:**
Open your `CatalogController.java` and tell Spring Boot how to find it:
```java
@GetMapping("/add-cake")
public String showAddCakePage() {
    // This automatically looks inside WEB-INF/jsp/ and adds .jsp
    return "catalog/addCake"; 
}
