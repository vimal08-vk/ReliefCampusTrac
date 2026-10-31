# ReliefCamp – Disaster Relief Camp Resource Tracker

A complete, production-grade Disaster Relief Camp Resource Tracking System built for college assessment and final viva demonstration using **Java 21, Spring Boot 3, Spring Data JPA, Hibernate, MySQL, HTML5, CSS3, and JavaScript (Fetch API)**.

---

## 📌 Project Overview
During natural disasters (floods, cyclones, landslides), relief camps receive urgent supplies (food, water, blankets, medicines) and sheltered families arrive in large numbers. 

**ReliefCamp** provides camp officers with a centralized digital dashboard to:
1. **Manage Relief Camps:** Monitor location, total capacity, and real-time occupancy.
2. **Track Supplies & Inventory:** Real-time stock counts that automatically deplete upon distribution.
3. **Register Displaced Families:** Headcount tracking with strict **Camp Capacity Protection** (rejects check-in if camp is full).
4. **Supply Distribution Workflow:** Distributes items to families with strict **Inventory Validation** (rejects distribution exceeding available stock).
5. **Operational Reports & Dashboard:** Real-time metrics calculated from the database with printable assessment summaries.

---

## 🏛️ System Architecture (3-Tier Layered Architecture)
The application strictly follows industry best practices and clean college assessment design:

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                     │
│      HTML5 + Vanilla CSS3 + JavaScript (Fetch API)          │
│   (Dashboard, Camps, Supplies, Families, Inventory, Dist.)   │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTP / REST (JSON)
┌──────────────────────────────▼──────────────────────────────┐
│                       Controller Layer                      │
│   CampController, SupplyController, FamilyController, etc.  │
│   @RestController, @Valid, GlobalExceptionHandler           │
└──────────────────────────────┬──────────────────────────────┘
                               │ DTOs / Entities
┌──────────────────────────────▼──────────────────────────────┐
│                    Service Layer (Business Rules)           │
│   CampService, SupplyService, FamilyService, DistService    │
│   - Capacity Check: (Occupancy + New Members <= Capacity)   │
│   - Inventory Check: (Available Qty >= Requested Qty)       │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                      Data Access Layer                      │
│   Spring Data JPA Repositories (CampRepository, etc.)       │
└──────────────────────────────┬──────────────────────────────┘
                               │ SQL
┌──────────────────────────────▼──────────────────────────────┐
│                       Database Layer                        │
│             MySQL / H2 (MySQL Mode Compatibility)           │
└─────────────────────────────────────────────────────────────┘
```

---

## ⚡ Business Rules Implemented
1. **Rule 1 – Inventory Protection:** A distribution must **never** exceed available inventory. If requested > available, request is rejected with `Insufficient inventory` (HTTP 400).
2. **Rule 2 – Camp Capacity Protection:** A family cannot be admitted if camp occupancy would exceed total capacity. Over-capacity registrations are rejected with `Camp capacity exceeded` (HTTP 400).
3. **Rule 3 – Non-Negative & Positive Validations:** Capacity and member counts must be $\ge 1$; quantities cannot be negative.
4. **Rule 4 – Auto-Inventory Reduction & Rollback:** When supplies are distributed, inventory is automatically decreased. If a distribution is canceled/rolled back, inventory stock is immediately restored.

---

## 🚀 How to Run the Application

### Option A: Immediate One-Click Run (Default Embedded Database)
The project comes pre-configured with an embedded, zero-setup in-memory database running in MySQL compatibility mode. It works immediately on any machine without needing MySQL installed:

```powershell
# In project root (d:\javasem):
.\mvnw.bat spring-boot:run
```
Once started, open your web browser:
👉 **Web Application:** `http://localhost:8080/` (or `http://localhost:8080/dashboard`)  
👉 **H2 Web Console:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:reliefcamp`, User: `sa`, Password: blank)

---

### Option B: Run with MySQL (Workbench / XAMPP)
If demonstrating live with MySQL:
1. Ensure MySQL is running on port `3306`.
2. Create database or let Hibernate auto-create it:
   ```sql
   CREATE DATABASE reliefcamp;
   ```
3. Run with the MySQL profile:
   ```powershell
   .\mvnw.bat spring-boot:run -Dspring-boot.run.profiles=mysql
   ```
   *(Or adjust credentials in `src/main/resources/application-mysql.properties` if your MySQL root password differs).*

---

## 📋 Pre-Seeded Demonstration Data
On first launch, realistic disaster relief data is automatically loaded for instant demonstration:
- **3 Relief Camps:**
  - `Pollachi Relief Camp` (Capacity: 100, Location: Pollachi)
  - `Valparai Hill Camp` (Capacity: 60, Location: Valparai)
  - `Mettupalayam Center` (Capacity: 80, Location: Mettupalayam)
- **Supplies in Stock:**
  - Food Packets (Initial 500 $\rightarrow$ 480 available after distribution)
  - Drinking Water (1000 Litres)
  - Thermal Blankets (200 Pieces)
  - First Aid Kits (75 Boxes)
- **Families Registered:**
  - Kumar Family (5 members)
  - Priya Family (4 members)
  - Ramesh Family (3 members)
- **Distributions:**
  - 20 Food Packets distributed to Kumar Family (verifying the specification's 500 $\rightarrow$ 480 formula).

---

## 🧪 Postman & REST API Documentation

| Module | Method | Endpoint | Description |
|---|---|---|---|
| **Camps** | `POST` | `/api/camps` | Create a new relief camp |
| | `GET` | `/api/camps` | List all relief camps |
| | `GET` | `/api/camps/{id}` | Get specific camp details |
| | `PUT` | `/api/camps/{id}` | Update camp details |
| | `DELETE` | `/api/camps/{id}` | Delete camp and cascade data |
| **Supplies** | `POST` | `/api/supplies` | Add incoming relief supplies |
| | `GET` | `/api/supplies` | List all relief supplies |
| | `GET` | `/api/supplies/{id}` | Get supply by ID |
| | `PUT` | `/api/supplies/{id}` | Update supply details |
| | `DELETE` | `/api/supplies/{id}` | Remove supply item |
| **Families** | `POST` | `/api/families` | Register family (Enforces capacity) |
| | `GET` | `/api/families` | List all sheltered families |
| | `GET` | `/api/families/{id}` | Get family by ID |
| | `GET` | `/api/families/camp/{campId}` | List families sheltered in a camp |
| | `PUT` | `/api/families/{id}` | Update family headcount/details |
| | `DELETE` | `/api/families/{id}` | Discharge family and free occupancy |
| **Distribution** | `POST` | `/api/distributions` | Distribute supplies (Enforces inventory) |
| | `GET` | `/api/distributions` | List distribution history |
| | `DELETE` | `/api/distributions/{id}` | Roll back distribution & restore stock |
| **Inventory** | `GET` | `/api/inventory` | Live stock calculation |
| **Dashboard** | `GET` | `/api/dashboard/summary` | Real-time database metrics |

---

## 🎓 Examiner Demonstration Walkthrough (Viva Script)

When presenting to your examiner, follow this 2-minute flow:

1. **Dashboard Overview (`/dashboard`):** Show the real-time counters (Total Camps, Families, Stock Balance, and Distributions). Note that they are computed directly from SQL queries, not hardcoded.
2. **Camp Management (`/camps`):** Create a new camp (e.g., *"Coimbatore South Camp"*, Capacity: 20). Show it appear in the table.
3. **Incoming Supplies (`/supplies`):** Add incoming supply (*"Emergency Food Kits"*, Quantity: 50, Unit: Packets) assigned to that camp.
4. **Capacity Enforcement Test (`/families`):**
   - Register a family with 15 members in *"Coimbatore South Camp"* (Allowed $\rightarrow$ Occupancy becomes 15/20).
   - Attempt to register another family with 10 members (15 + 10 = 25 > 20).
   - **Show the Examiner:** The system rejects it with red alert: **`Camp capacity exceeded: Cannot accommodate 10 members. Available spots: 5`**.
5. **Inventory Enforcement Test (`/distributions`):**
   - Attempt to distribute 60 Food Kits when only 50 exist.
   - **Show the Examiner:** The system rejects it with red alert: **`Insufficient inventory: Requested 60, but only 50 available in stock`**.
   - Distribute 20 Food Kits.
   - **Show the Examiner:** Stock is immediately updated to 30 in `/inventory`.
6. **Reports (`/reports`):** Show the printable executive summary and print/PDF layout.
