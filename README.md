# Oracle-Inspired ERP System

This project implements a comprehensive Enterprise Resource Planning (ERP) system inspired by Oracle's functionalities. It is built using Java and PostgreSQL JDBC to interact with a relational database (PostgreSQL) hosted on Supabase. The system provides modules for managing various aspects of a business, including Finance, Supply Chain, Manufacturing, Customer Relationship, and Human Capital.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Database Schema](#database-schema)
- [Modules](#modules)
  - [Finance Management](#finance-management)
  - [Supply Chain Management](#supply-chain-management)
  - [Manufacturing Management](#manufacturing-management)
  - [Customer Relationship Management (CRM)](#customer-relationship-management-crm)
  - [Human Capital Management (HCM)](#human-capital-management-hcm)
  - [User Management](#user-management)
- [Database Setup](#database-setup)
- [Setup](#setup)
- [Running the Application from Eclipse](#running-the-application-from-eclipse)
- [Usage](#usage)
- [Video](#video)

## Introduction

The Oracle-Inspired ERP System aims to provide a robust and modular solution for managing core business processes. It leverages object-oriented principles and database connectivity to offer a scalable and maintainable platform. The system is designed to cater to the needs of various departments within an organization, enabling efficient data management and streamlined workflows.

## Features

### Core Features

- **Modular Design:** The system is divided into distinct modules, allowing for independent development and maintenance.
- **Database Integration:** Utilizes JDBC for seamless interaction with a relational database.
- **User Management:** Supports user registration, login, and role-based access control.
- **Comprehensive Functionalities:** Offers a wide range of functionalities within each module to cover essential business processes.

## Technologies Used

### Languages:

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

### Database:

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

### Tools:

![Eclipse](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
![PostgreSQL JDBC](https://img.shields.io/badge/PostgreSQL%20JDBC-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)

## Prerequisites

- JDK 11 or higher
- PostgreSQL JDBC Driver
- Supabase Account
- Eclipse IDE

## Modules

### Finance Management

The Finance Management module provides comprehensive financial operations and reporting capabilities.

#### Features:

1. **Transaction Management**

   - Record new transactions
   - View transaction history
   - Update existing transactions
   - Delete transactions
   - Generate transaction summaries

2. **Budget Management**

   - Create annual budgets
   - Track departmental budgets
   - View budget allocations
   - Monitor budget utilization

3. **Tax Management**

   - Calculate tax for transactions
   - Generate tax reports
   - Track tax liabilities
   - Period-wise tax summaries

4. **Financial Reporting**

   - Generate comprehensive financial reports
   - Income and expense tracking
   - Profit/Loss calculations
   - Transaction summary reports

5. **Billing Operations**
   - Process customer billing
   - Track billing history
   - Manage payment records

### Supply Chain Management

The Supply Chain Management module handles inventory and order management operations.

#### Features:

1. **Inventory Management**

   - Add new inventory items
   - Track stock levels
   - Update item details
   - Remove inventory items
   - Low stock alerts

2. **Order Processing**

   - Create new orders
   - Track order status
   - Update order information
   - Order fulfillment tracking

3. **Stock Movement**

   - Record stock movements
   - Track inventory changes
   - Maintain stock history
   - Automatic low stock notifications

4. **Shipment Tracking**
   - Monitor order shipments
   - Update shipment status
   - Track delivery progress

### Manufacturing Management

The Manufacturing Management module handles production planning and execution.

#### Features:

1. **Production Order Management**

   - Create production orders
   - Track production status
   - Update order progress
   - Manage completion records

2. **Material Requirements Planning**

   - Check material availability
   - Generate material requirements
   - Manage bill of materials
   - Automatic purchase order generation

3. **Production Scheduling**

   - View production schedule
   - Update production status
   - Track completion dates
   - Monitor production efficiency

4. **Production Reporting**
   - Generate production reports
   - Track efficiency metrics
   - Monitor production costs
   - Analyze production trends

## Customer Relationship Management

The Customer Relationship Management (CRM) module handles customer-related operations and issue management.

### Features:

1. **Customer Management**

   - Add new customers
   - View customer information
   - Update customer details
   - Delete customer records
   - Filter customers by location

2. **Sales Management**

   - Record new sales
   - Generate sales reports
   - Track customer purchases

3. **Customer Issue Management**
   - Create customer issues
   - View and update issue status
   - Resolve customer issues
   - Generate issue reports

## Human Capital Management

The Human Capital Management (HCM) module manages employee-related operations and performance tracking.

### Features:

1. **Employee Management**

   - Add new employees
   - View employee information
   - Update employee details
   - Search for specific employees
   - Delete employee records

2. **Payroll Management**

   - Calculate employee payroll
   - Apply tax deductions
   - Generate payroll reports

3. **Performance Management**
   - Record employee performance ratings
   - Add performance comments
   - Generate employee performance reports

## User Management

The User Management module handles system user operations and access control.

### Features:

1. **User Administration**

   - View system users
   - Update user information
   - Delete user accounts

2. **Security Management**
   - Hash user passwords for secure storage

## Database Setup

### Create Tables

Open the Supabase SQL editor or a database management GUI connected to your Supabase PostgreSQL database.

**Execute the first SQL script**: This script creates the necessary tables for your ERP system:

```sql
-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Employees Table
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    salary DECIMAL(10, 2) NOT NULL
);

-- Employee Performance Table
CREATE TABLE employee_performance (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER REFERENCES employees(id) NOT NULL,
    date DATE NOT NULL,
    rating VARCHAR(20) NOT NULL,
    comments VARCHAR(100)
);

-- Customers Table
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    location VARCHAR(50) NOT NULL
);

-- Customer Issues Table
CREATE TABLE customer_issues (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    creation_date DATE NOT NULL,
    last_updated DATE,
    resolved_date DATE,
    resolution TEXT
);

-- Sales Table
CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customers(id) NOT NULL,
    product_id INTEGER,
    quantity INTEGER NOT NULL,
    sale_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL
);

-- Inventory Table
CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL
);

-- Orders Table
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id INTEGER NOT NULL,
    item_id INTEGER REFERENCES inventory(id) NOT NULL,
    quantity INTEGER NOT NULL,
    order_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Budgets Table
CREATE TABLE budgets (
    id SERIAL PRIMARY KEY,
    year INTEGER NOT NULL CHECK (year > 0),
    department VARCHAR(100) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount >= 0)
);

-- Financial Transactions Table
CREATE TABLE financial_transactions (
    id SERIAL PRIMARY KEY,
    transaction_date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2)
);

-- Production Orders Table
CREATE TABLE production_orders (
    id SERIAL PRIMARY KEY,
    product_id INTEGER UNIQUE NOT NULL,
    quantity INTEGER NOT NULL,
    total_amount DECIMAL(10, 2),
    start_date DATE NOT NULL,
    completion_date DATE,
    order_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'Planned',
    CONSTRAINT chk_valid_quantity CHECK (quantity > 0)
);

-- Bill of Materials Table
CREATE TABLE bill_of_materials (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES production_orders(product_id),
    material_id INTEGER REFERENCES inventory(id) NOT NULL,
    quantity_required INTEGER NOT NULL,
    UNIQUE (product_id, material_id)
);
```

**Execute the second SQL script**: This script enables Row Level Security (RLS) on your tables and defines policies for various modules:

```sql
-- Enable RLS on all tables
ALTER TABLE public.financial_transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.inventory ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.employees ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customers ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.employee_performance ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.sales ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.production_orders ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.bill_of_materials ENABLE ROW LEVEL SECURITY;

-- Financial Management Policies
CREATE POLICY "Accountants and admins can view transactions" ON public.financial_transactions
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('accountant', 'admin'));

CREATE POLICY "Accountants can insert transactions" ON public.financial_transactions
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('accountant', 'admin'));

CREATE POLICY "Accountants can update transactions" ON public.financial_transactions
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('accountant', 'admin'));

CREATE POLICY "Accountants can delete transactions" ON public.financial_transactions
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('accountant', 'admin'));

-- Supply Chain Management Policies
CREATE POLICY "Supply Chain Managers and admins can view inventory" ON public.inventory
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('SUPPLY_CHAIN_MANAGER', 'admin'));

CREATE POLICY "Supply Chain Managers can add inventory items" ON public.inventory
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('SUPPLY_CHAIN_MANAGER', 'admin'));

CREATE POLICY "Supply Chain Managers can update inventory" ON public.inventory
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('SUPPLY_CHAIN_MANAGER', 'admin'));

CREATE POLICY "Supply Chain Managers can delete inventory" ON public.inventory
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('SUPPLY_CHAIN_MANAGER', 'admin'));

-- Human Capital Management Policies
CREATE POLICY "HR and admins can view employees" ON public.employees
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can add employees" ON public.employees
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can update employees" ON public.employees
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can delete employees" ON public.employees
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

-- Employee Performance Policies
CREATE POLICY "HR and admins can view performance" ON public.employee_performance
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can record performance" ON public.employee_performance
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can update performance" ON public.employee_performance
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

CREATE POLICY "HR can delete performance records" ON public.employee_performance
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('hr', 'admin'));

-- Customer Relationship Management Policies
CREATE POLICY "CRM Managers and admins can view customers" ON public.customers
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can add customers" ON public.customers
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can update customers" ON public.customers
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can delete customers" ON public.customers
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

-- Orders Policies
CREATE POLICY "CRM Managers and admins can view orders" ON public.orders
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can create orders" ON public.orders
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can update orders" ON public.orders
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can delete orders" ON public.orders
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

-- Sales Policies
CREATE POLICY "CRM Managers and admins can view sales" ON public.sales
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can record sales" ON public.sales
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can update sales records" ON public.sales
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

CREATE POLICY "CRM Managers can delete sales" ON public.sales
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

-- Manufacturing Management Policies
CREATE POLICY "Production managers and admins can view production orders" ON public.production_orders
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

CREATE POLICY "Production managers can create production orders" ON public.production_orders
    FOR INSERT TO authenticated
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

CREATE POLICY "Production managers can update production orders" ON public.production_orders
    FOR UPDATE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

CREATE POLICY "Production managers can delete production orders" ON public.production_orders
    FOR DELETE TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

CREATE POLICY "Production managers and admins can view bill of materials" ON public.bill_of_materials
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

CREATE POLICY "Production managers can manage bill of materials" ON public.bill_of_materials
    FOR ALL TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('production_manager', 'admin'));

-- Users Policies
CREATE POLICY "Users can view their own user info" ON public.users
    FOR SELECT TO authenticated
    USING (username = current_user OR (SELECT role FROM users WHERE username = current_user) = 'admin');

CREATE POLICY "Admin can manage users" ON public.users
    FOR ALL TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) = 'admin')
    WITH CHECK ((SELECT role FROM users WHERE username = current_user) = 'admin');
```

**Execute the third SQL script**: This script enables RLS and defines policies for budgets and customer issues:

```sql
-- Enable RLS on the budgets and customer_issues tables
ALTER TABLE public.budgets ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customer_issues ENABLE ROW LEVEL SECURITY;

-- Budget Policies
CREATE POLICY "Finance Managers manage budgets" ON public.budgets
    FOR ALL TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('finance_manager', 'admin'));

-- Customer Issues Policies
CREATE POLICY "Customer Support manages issues" ON public.customer_issues
    FOR ALL TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('CRM_MANAGER', 'admin'));

-- Administration Module Policies
CREATE POLICY "Administration can view budgets" ON public.budgets
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('finance_manager', 'CRM_MANAGER', 'admin'));

CREATE POLICY "Administration can view issues" ON public.customer_issues
    FOR SELECT TO authenticated
    USING ((SELECT role FROM users WHERE username = current_user) IN ('finance_manager', 'CRM_MANAGER', 'admin'));
```

**Execute the fourth SQL script**: This script creates indexes:

```sql
-- bill_of_materials table
CREATE INDEX bill_of_materials_material_id_idx ON bill_of_materials (material_id);

-- customer_issues table
CREATE INDEX customer_issues_customer_id_idx ON customer_issues (customer_id);

-- employee_performance table
CREATE INDEX employee_performance_employee_id_idx ON employee_performance (employee_id);

-- orders tab;es
CREATE INDEX orders_item_id_idx ON orders (item_id);

-- sales table
CREATE INDEX sales_customer_id_idx ON sales (customer_id);
```

## Setup

### 1. Clone the repository

- Clone the repository using the following command:

  ```cmd
  git clone https://github.com/mohdimrandev/oracle-inspired-erp-system.git
  ```

### 2. Navigate to the project directory

- Change to the project directory using the following command:

  ```cmd
  cd oracle-inspired-erp-system
  ```

### 4. Import the project into your IDE

If using an IDE like IntelliJ IDEA or Eclipse, import the project as a Maven project.

Maven will automatically manage the PostgreSQL JDBC driver dependency specified in your `pom.xml`.

### 4. Configure database connection.java file:

Replace <your-supabase-port>, <your-supabase-user>, and <your-supabase-password> with the actual values from your Supabase database connection string.

```properties
db.url=your_supabase_url
db.user=your_username
db.password=your_password
```

### 5. Define Module Dependency (If using Java Modules)

If your project uses Java modules, add the following to your `module-info.java` file:

```java
requires java.sql;
```

## Running the Application from Eclipse

**1.Perform a Clean Install (using Maven):**

- Right-click on your project in the Project Explorer.

- Select "Run As" > "Maven build...".

- In the "Goals" field, enter `clean install`.

- Click "Run".

This will clean the project (remove old build artifacts) and then build the project from scratch, including downloading dependencies and compiling the code.

**2. Ensure Database Connection:**

- Double-check that your `DatabaseConnection.java` file has the correct database connection details (URL, username, and password).
- Make sure your PostgreSQL database is up and running in Supabase.

**3. Run as Java Application:**

- Locate the `ERPSystem.java` file in your Eclipse Project Explorer.
- Right-click on `ERPSystem.java`.
- Select "Run As" > "Java Application".

**Alternative: Running as Maven build:**

If you prefer to run using Maven directly within Eclipse:

- Right-click on your project in the Project Explorer.
- Select "Run As" > "Maven build...".
- In the "Goals" field, enter `exec:java`.
- Click "Run".

When you run as a Java Application, Eclipse compiles your code and uses the Maven dependencies to create a classpath. It then executes the `main` method in `ERPSystem.java`, starting your application.

When you run as a Maven build with the `exec:java` goal, Eclipse uses the `exec-maven-plugin` to achieve the same result.

**Choose the method that best fits your workflow:**

- If you need to frequently debug your application, running as a Java Application provides more debugging capabilities within Eclipse.
- For simpler projects or automating deployment tasks, using Maven might be more suitable.

## Usage

- **Register/Login:** If you are a new user, you'll need to register an account with a username, password, and assigned role. Otherwise, log in with your existing credentials.

- **Navigate modules:** Use the menu options to access different modules like Finance, Supply Chain, Manufacturing, CRM, and HCM, depending on your role and needs.

- **Perform actions:**

  - Within each module, you can perform specific actions, such as adding employees, recording transactions, managing inventory, tracking orders, etc.
  - The system will present a menu-driven interface for interacting with the various modules (Finance, Supply Chain, Manufacturing, CRM, Human Capital, User Management, etc.). You can choose options from the menu to perform actions within each module.
  - Enter the number corresponding to the desired action and press Enter to proceed. The system will guide you through the steps for each operation.

- **Logout:** When finished, choose the logout option to securely exit the system.

## Video

[Demo Video](https://youtu.be/0vY5uToqJqE)
