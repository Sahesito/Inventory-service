## 📦 Inventory Service – SmartCommerce
This module manages product stock levels across the SmartCommerce ecosystem. It ensures real-time inventory control, stock validation, and secure stock mutations while remaining fully isolated from product and authentication domains.

## 🎯 Why This Module Exists
- Stock Isolation
  Inventory logic is separated from Product and Order domains to maintain clean microservice boundaries.

- Real-Time Availability Validation
  Provides fast availability checks for order processing without exposing full inventory management endpoints.

- Controlled Stock Mutations
  Enforces strict validation before increasing or reducing stock to prevent inconsistencies.

- Product Integrity Validation
  Uses OpenFeign to verify product existence and active status before creating inventory records.

- Role-Based Access Control
  Only ADMIN and SELLER roles can manage inventory; deletion is restricted to ADMIN.

- Business Rule Enforcement
  Includes low-stock detection, stock thresholds, and fulfillment validation.

- Stateless Security Architecture
  JWT-based authentication with role extraction from token claims.

## 🔑 Core Capabilities
Public Access
- Get inventory by product ID
- Check stock availability (/check-availability)

Restricted Access (ADMIN, SELLER)
- List all inventory
- Get inventory by ID
- Get low-stock items
- Filter by warehouse location
- Create inventory
- Update inventory
- Add stock
- Reduce stock

Admin Only
- Delete inventory

## 🛡️ Security Design
- Stateless session policy
- Custom JWT filter
- Role extracted from token claim (ROLE_ADMIN, ROLE_SELLER)
- Method-level authorization via @PreAuthorize
- Global exception handling for validation and access control
- CORS configured for Angular frontend (localhost:4200)

## 🧠 Business Logic Highlights
- availableQuantity = quantity - reservedQuantity
- Low stock detected when quantity <= minStockLevel
- Prevents stock reduction if available quantity is insufficient
- One inventory record per product (unique constraint)
- Product must exist and be active before inventory creation

## 🗄️ Persistence
- Database: PostgreSQL (smartcommerce_inventory)
- JPA with Hibernate (ddl-auto: update)
- Automatic timestamps (createdAt, updatedAt)
- Seeded warehouse data (A–F)
- Unique constraint on product_id

## ⚙️ Configuration
- Port: 8084
- Registered in Eureka
- OpenFeign enabled for Product Service communication
- Actuator endpoints enabled (health, info)
- Debug logging enabled
- JWT secret shared with Auth Service
