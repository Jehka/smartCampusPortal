# Smart Campus University Portal — User Guide

## Prerequisites

Before launching the app, make sure:
- MySQL is running
- The `campus_portal` database exists (run `setup_database.sql` if not done)
- Tomcat 9 is started in Eclipse (Run As → Run on Server)
- The app is accessible at: `http://localhost:8080/SmartCampusPortal/`

---

## Home Page

Open your browser and go to:
```
http://localhost:8080/SmartCampusPortal/index.html
```

You will see links for all 8 features. Use this as your starting point every time.

---

## 1. Register a User

**Link:** Register User → `register.html`

**Steps:**
1. Click **Register User** on the home page
2. Fill in the form:
   - **User ID** — a unique number (e.g. `101`)
   - **Name** — full name (e.g. `Aarav Shah`)
   - **Email** — valid email (e.g. `aarav@upes.ac.in`)
   - **Role** — select from Student / Faculty / Administrator
3. Click **Register**
4. Success message: `User registered successfully! User ID: 101`

**Rules:**
- User ID must be unique — entering a duplicate ID will give a DB error
- A user must exist before you can submit a service request for them (foreign key constraint)

---

## 2. Submit a Service Request

**Link:** Submit Service Request → `request.html`

**Steps:**
1. Click **Submit Service Request** on the home page
2. Fill in the form:
   - **Request ID** — a unique number (e.g. `1`)
   - **User ID** — must match an already registered user (e.g. `101`)
   - **Service Name** — select from the dropdown:
     - Transport
     - Library
     - Lab
     - Complaint Desk
     - Hostel
     - IT Support
   - **Request Details** — describe the request (e.g. `Need bus pass renewal`)
   - **Status** — usually set to `Pending` for new requests
3. Click **Submit Request**
4. Success message: `Service request submitted successfully! Request ID: 1`

**Rules:**
- Request ID must be unique
- User ID must already exist in the users table — register the user first

---

## 3. View All Requests

**Link:** View All Requests → hits `ViewRequestsServlet` directly

**Steps:**
1. Click **View All Requests** on the home page
2. A table loads showing all service requests from the database
3. Data is fetched into an `ArrayList<ServiceRequest>` and displayed

**What you see:**

| Request ID | User ID | Service | Details | Status |
|---|---|---|---|---|
| 1 | 101 | Transport | Bus pass renewal | Pending |
| 2 | 102 | Library | Book reservation | Approved |

---

## 4. View Unique Services

**Link:** View Unique Services → hits `UniqueServicesServlet`

**Steps:**
1. Click **View Unique Services** on the home page
2. Shows a list of all distinct service types that have been requested
3. Uses a `HashSet<String>` — duplicates are automatically removed

**Example output:**
```
• Transport
• Library
• IT Support
• Hostel
```
Useful to see which services are actually being used on campus.

---

## 5. View User Request Summary

**Link:** View User Request Summary → hits `UserRequestSummaryServlet`

**Steps:**
1. Click **View User Request Summary** on the home page
2. Shows how many requests each user has submitted
3. Uses a `HashMap<Integer, Integer>` (User ID → Request Count)

**Example output:**

| User ID | Number of Requests |
|---|---|
| 101 | 3 |
| 102 | 1 |
| 103 | 2 |

---

## 6. View Pending Request Queue

**Link:** View Pending Request Queue → hits `RequestQueueServlet`

**Steps:**
1. Click **View Pending Request Queue** on the home page
2. Shows all requests with status = `Pending`
3. Uses a `LinkedList<ServiceRequest>` — the first item is highlighted as "Next to Process"

**Example output:**
```
Next Request to Process: Request ID 1 — Transport

[table of all pending requests]
```

---

## 7. Update Request Status

**Link:** Update Request Status → `update.html`

**Steps:**
1. Click **Update Request Status** on the home page
2. Fill in:
   - **Request ID** — the ID of the request to update (e.g. `1`)
   - **New Status** — select from:
     - Pending
     - Approved
     - Rejected
     - Processing
     - Completed
3. Click **Update Status**
4. Success message: `Request ID 1 status updated to: Approved`

**Tip:** Use **View All Requests** first to check which Request IDs exist and their current status.

---

## 8. Delete a Request

**Link:** Delete Request → `delete.html`

**Steps:**
1. Click **Delete Request** on the home page
2. Enter the **Request ID** to delete (e.g. `1`)
3. Click **Delete Request**
4. Output: `Number of records deleted: 1` and a confirmation message

**Warning:** Deletion is permanent — there is no confirmation prompt.

---

## Typical Workflow (Start to Finish)

Follow this order when testing the app fresh:

```
1. Register User          → create user ID 101
2. Register User          → create user ID 102
3. Submit Service Request → request ID 1, user 101, Transport, Pending
4. Submit Service Request → request ID 2, user 101, Library, Pending
5. Submit Service Request → request ID 3, user 102, IT Support, Approved
6. View All Requests      → see all 3 requests in the table
7. View Unique Services   → see: Transport, Library, IT Support
8. View User Summary      → user 101 has 2, user 102 has 1
9. View Pending Queue     → see requests 1 and 2 (status = Pending)
10. Update Status         → change request 1 to Approved
11. View Pending Queue    → now only request 2 remains
12. Delete Request        → delete request 2
13. View All Requests     → only request 3 remains
```

---

## Common Errors

| Error | Cause | Fix |
|---|---|---|
| HTTP 404 on servlet | Project not rebuilt after code change | Project → Clean → Run on Server |
| DB error: Duplicate entry | User ID or Request ID already exists | Use a different unique ID |
| DB error: foreign key constraint | User ID doesn't exist in users table | Register the user first |
| DB error: Communications link failure | MySQL is not running | Start MySQL from Services or Workbench |
| Blank page after submit | Wrong Tomcat version / jakarta imports | Ensure javax.servlet imports are used |

---

## Database Quick Reference

```sql
-- View all users
SELECT * FROM users;

-- View all service requests
SELECT * FROM service_request;

-- Reset everything for fresh testing
DELETE FROM service_request;
DELETE FROM users;
```

Run these in MySQL Workbench if you want to reset the data between tests.
