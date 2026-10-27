# Shop Management System

## Description
A Shop Management System developed using Java, JDBC, and Oracle Database. This application helps manage products and stock quantities efficiently.

## Features
- Add new products with product ID, name, price, and quantity.
- Update stock quantities after sales.
- Display products with stock below 10.
- Search for products by ID or name.
- Store product information in Oracle Database.

## Technologies Used
- Java
- JDBC (Java Database Connectivity)
- Oracle Database
- Visual Studio Code

## Database Table
The application uses a `products` table with the following columns:

| Column | Description |
|---|---|
| `product_id` | Unique product ID |
| `name` | Product name |
| `price` | Product price |
| `quantity` | Available stock quantity |

## Database Setup
Execute this SQL command in Oracle SQL Worksheet:

```sql
CREATE TABLE products (
    product_id NUMBER PRIMARY KEY,
    name VARCHAR2(50) NOT NULL,
    price NUMBER(10,2) NOT NULL,
    quantity NUMBER DEFAULT 0 NOT NULL
);
```

## How to Run
1. Install Java and Oracle Database.
2. Download the Oracle JDBC driver and place `ojdbc17.jar` in the project folder.
3. Configure the database URL and credentials in `ShopManagement.java`.
4. Open the VS Code terminal in the project folder.
5. Compile the program:

   `javac -cp ".;ojdbc17.jar" ShopManagement.java`

6. Run the program:

   `java -cp ".;ojdbc17.jar" ShopManagement`

## Author
Student Project Submission