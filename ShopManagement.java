import java.sql.*;
import java.util.Scanner;
public class ShopManagement {
    static Scanner sc = new Scanner(System.in);
    static void addProduct(Connection con) throws SQLException {
        System.out.print("Enter product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        System.out.print("Enter price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Enter quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        String sql = "INSERT INTO products VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);
            ps.executeUpdate();
            System.out.println("Product added successfully.");
        }
    }
    static void sellProduct(Connection con) throws SQLException {
        System.out.print("Enter product ID: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Enter quantity sold: ");
        int sold = Integer.parseInt(sc.nextLine());
        if (sold <= 0) {
            System.out.println("Quantity sold must be positive.");
            return;
        }
        String sql = "UPDATE products SET quantity = quantity - ? " +
                     "WHERE product_id = ? AND quantity >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sold);
            ps.setInt(2, id);
            ps.setInt(3, sold);
            int rows = ps.executeUpdate();
            if (rows == 1) {
                con.commit();
                System.out.println("Sale successful. Stock updated.");
            } else {
                con.rollback();
                System.out.println(
                    "Product not found or insufficient stock.");
            }
        }
    }
    static void lowStock(Connection con) throws SQLException {
        String sql = "SELECT * FROM products WHERE quantity < 10";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Products with stock below 10:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("product_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getDouble("price") + " | " +
                    rs.getInt("quantity"));
            }
        }
    }
    static void searchProduct(Connection con) throws SQLException {
        System.out.println("1. Search by ID");
        System.out.println("2. Search by name");
        int choice = Integer.parseInt(sc.nextLine());
        String sql;
        if (choice == 1) {
            System.out.print("Enter product ID: ");
            int id = Integer.parseInt(sc.nextLine());
            sql = "SELECT * FROM products WHERE product_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                display(ps);
            }
        } else if (choice == 2) {
            System.out.print("Enter product name: ");
            String name = sc.nextLine();
            sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + name + "%");
                display(ps);
            }
        } else {
            System.out.println("Invalid search choice.");
        }
    }
    static void display(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("product_id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getDouble("price") + " | " +
                    rs.getInt("quantity"));
            }
            if (!found)
                System.out.println("No product found.");
        }
    }
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        String user = "system";
        String password = "adi123";
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            con.setAutoCommit(false);
            while (true) {
                System.out.println("\n--- SHOP MANAGEMENT ---");
                System.out.println("1. Add Product");
                System.out.println("2. Sell Product");
                System.out.println("3. Display Low Stock");
                System.out.println("4. Search Product");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(sc.nextLine());
                try {
                    switch (choice) {
                        case 1:
                            addProduct(con);
                            con.commit();
                            break;
                        case 2:
                            sellProduct(con);
                            break;
                        case 3:
                            lowStock(con);
                            break;
                        case 4:
                            searchProduct(con);
                            break;
                        case 5:
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid choice.");
                    }
                } catch (SQLException e) {
                    con.rollback();
                    System.out.println("Database error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    con.rollback();
                    System.out.println("Please enter valid numeric input.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
