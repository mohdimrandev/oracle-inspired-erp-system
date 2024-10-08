package erp.system;

import java.sql.*;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class FinanceManagement {
	
  public static void recordTransaction(Connection conn, Scanner scanner) {
        System.out.print("Enter transaction date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        String sql = "INSERT INTO financial_transactions (transaction_date, description, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2, description);
            pstmt.setDouble(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Transaction recorded successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error recording transaction: " + e.getMessage());
        }
    }

    public static void viewFinancialTransactions(Connection conn) {
        String sql = "SELECT * FROM financial_transactions";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            double balance = 0.0;
            while (rs.next()) {
                balance += rs.getDouble("amount"); 
                System.out.println("ID: " + rs.getInt("id") + 
                        ", Date: " + rs.getDate("transaction_date") + 
                        ", Description: " + rs.getString("description") + 
                        ", Amount: " + rs.getDouble("amount") + 
                        ", Current Balance: " + balance);
            }
        } catch (SQLException e) {
            System.out.println("Error viewing transactions: " + e.getMessage());
        }
    }

    public static void generateTransactionSummaryReport(Connection conn) {
        String sql = "SELECT transaction_date, SUM(amount) AS total_amount " +
                "FROM financial_transactions " +
                "GROUP BY transaction_date " +
                "ORDER BY transaction_date";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	
         	System.out.println("Transaction Summary Report:");
            while (rs.next()) {
                System.out.println("Date: " + rs.getDate("transaction_date") + ", Total Amount: " + rs.getDouble("total_amount"));

            }

        } catch (SQLException e) {
            System.out.println("Error generating transaction summary report: " + e.getMessage());
        }
    }

    public static void updateTransaction(Connection conn, Scanner scanner) {
        System.out.print("Enter transaction ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new transaction date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new amount: ");
        double amount = scanner.nextDouble();

        String sql = "UPDATE financial_transactions SET transaction_date = ?, description = ?, amount = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2, description);
            pstmt.setDouble(3, amount);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Transaction updated successfully.");
            } else {
                System.out.println("No transaction found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
        }
    }
    
    // Budget
    public static void createBudget(Connection conn, Scanner scanner) {
        System.out.print("Enter budget year: ");
        int year = scanner.nextInt();
        System.out.print("Enter department: ");
        scanner.nextLine(); 
        String department = scanner.nextLine();
        System.out.print("Enter budget amount: ");
        double amount = scanner.nextDouble();

        String sql = "INSERT INTO budgets (year, department, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            pstmt.setString(2, department);
            pstmt.setDouble(3, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Budget created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating budget: " + e.getMessage());
        }
    }

    public static void viewBudget(Connection conn, Scanner scanner) {
        System.out.print("Enter budget year: ");
        int year = scanner.nextInt();
        
        String sql = "SELECT * FROM budgets WHERE year = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	System.out.println("Department: " + rs.getString("department") + ", Amount: " + rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing budget: " + e.getMessage());
        }
    }

    // Tax Management
    public static void calculateTax(Connection conn, Scanner scanner) {
        System.out.print("Enter transaction ID: ");
        int transactionId = scanner.nextInt();
        System.out.print("Enter tax rate (%): ");
        double taxRate = scanner.nextDouble() / 100.0;

        String sql = "SELECT amount FROM financial_transactions WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double amount = rs.getDouble("amount");
                double taxAmount = amount * taxRate;
                System.out.println("Tax amount: " + taxAmount);

                String updateSql = "UPDATE financial_transactions SET tax_amount = ? WHERE id = ?";
                try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                    updatePstmt.setDouble(1, taxAmount);
                    updatePstmt.setInt(2, transactionId);
                    updatePstmt.executeUpdate();
                    System.out.println("Transaction updated with tax information.");
                }
            } else {
                System.out.println("Transaction not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating tax: " + e.getMessage());
        }
    }

    public static void generateTaxReport(Connection conn, Scanner scanner) {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDate = scanner.nextLine();

        String sql = "SELECT SUM(tax_amount) as total_tax FROM financial_transactions WHERE transaction_date BETWEEN ? AND ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(startDate));
            pstmt.setDate(2, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
            	System.out.println("Total tax for the period: " + rs.getDouble("total_tax"));
            }
        } catch (SQLException e) {
            System.out.println("Error generating tax report: " + e.getMessage());
        }
    }
    
    public static void generateFinancialReport(Connection conn) {
        String sqlIncome = "SELECT SUM(amount) AS total_income FROM financial_transactions WHERE amount > 0";
        String sqlExpenses = "SELECT SUM(ABS(amount)) AS total_expenses FROM financial_transactions WHERE amount < 0";

        double totalIncome = 0;
        double totalExpenses = 0;

        try (Statement stmt = conn.createStatement()) {

            ResultSet rsIncome = stmt.executeQuery(sqlIncome);
            if (rsIncome.next()) {
                totalIncome = rsIncome.getDouble("total_income");
            }
            rsIncome.close(); 

  
            ResultSet rsExpenses = stmt.executeQuery(sqlExpenses);
            if (rsExpenses.next()) {
                totalExpenses = rsExpenses.getDouble("total_expenses");
            }
            rsExpenses.close(); 
        } catch (SQLException e) {
            System.out.println("Error generating financial report: " + e.getMessage());
        }

        System.out.println("\nFinancial Report:");
        System.out.println("Total Income: $" + totalIncome);
        System.out.println("Total Expenses: $" + totalExpenses);
        System.out.println("Net Profit/Loss: $" + (totalIncome - totalExpenses));
    }


    public static void processBilling(Connection conn, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter bill amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        String sql = "INSERT INTO financial_transactions (transaction_date, description, amount) VALUES (CURRENT_DATE, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "Bill for customer " + customerId);
            pstmt.setDouble(2, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Bill processed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error processing bill: " + e.getMessage());
        }
    }
    
    public static void deleteTransaction(Connection conn, Scanner scanner) {
        System.out.print("Enter transaction ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM financial_transactions WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Transaction deleted successfully.");
            } else {
                System.out.println("No transaction found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
    }
}

class SupplyChainManagement {
	
	public static void addInventoryItem(Connection conn, Scanner scanner) {
    	System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter unit price: ");
        double unitPrice = scanner.nextDouble();

        String sql = "INSERT INTO inventory (item_name, quantity, unit_price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, unitPrice);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Inventory item added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding inventory item: " + e.getMessage());
        }
    }

    public static void viewInventory(Connection conn) {
        String sql = "SELECT * FROM inventory";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	System.out.println("ID: " + rs.getInt("id") + 
                        ", Item: " + rs.getString("item_name") + 
                        ", Quantity: " + rs.getInt("quantity") + 
                        ", Unit Price: " + rs.getDouble("unit_price"));

            }
        } catch (SQLException e) {
            System.out.println("Error viewing inventory: " + e.getMessage());
        }
    }

    public static void updateInventoryItem(Connection conn, Scanner scanner) {
        System.out.print("Enter inventory item ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new item name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter new unit price: ");
        double unitPrice = scanner.nextDouble();

        String sql = "UPDATE inventory SET item_name = ?, quantity = ?, unit_price = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, unitPrice);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Inventory item updated successfully.");
            } else {
                System.out.println("No inventory item found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating inventory item: " + e.getMessage());
        }
    }
    
    public static void createOrder(Connection conn, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter item ID: ");
        int itemId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        String sql = "INSERT INTO orders (customer_id, item_id, quantity, order_date, status) " +
                "VALUES (?, ?, ?, CURRENT_DATE, 'Pending') RETURNING id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantity);
            ResultSet rs  =  pstmt.executeQuery();
                if (rs.next()) {
                    int orderId = rs.getInt("id"); 
                    System.out.println("Order created successfully with Order ID: " + orderId);
                }
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
        }
    }

    public static void recordStockMovement(Connection conn, Scanner scanner) {
        System.out.print("Enter item ID: ");
        int itemId = scanner.nextInt();
        System.out.print("Enter quantity (positive for in, negative for out): ");
        int quantityChange = scanner.nextInt();

        String sql = "UPDATE inventory SET quantity = quantity + ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setInt(2, itemId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Inventory updated successfully.");

                checkLowStock(conn, itemId);
            } else {
                System.out.println("No item found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating inventory: " + e.getMessage());
        }
    }
    
    enum OrderStatus {
        PENDING, PROCESSING, SHIPPED
    }
    
    public static void updateOrderStatus(Connection conn, Scanner scanner) {
        System.out.print("Enter order ID: ");
        int orderId = scanner.nextInt();
	// Added to consume the leftover newline
	scanner.nextLine();
        System.out.print("Enter new status (Pending, Processing, Shipped): ");
        OrderStatus newStatus = OrderStatus.valueOf(scanner.nextLine().toUpperCase()); 

        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, newStatus.name());
            pstmt.setInt(2, orderId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Order status updated successfully.");
            } else {
                System.out.println("No order found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating order status: " + e.getMessage());
        }
    }
          
    public static void trackShipment(Connection conn, Scanner scanner) {
        System.out.print("Enter order ID to track: ");
        int orderId = scanner.nextInt();

        String sql = "SELECT * FROM orders WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Order Status: " + rs.getString("status"));
                System.out.println("Order Date: " + rs.getDate("order_date"));
            } else {
                System.out.println("No order found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error tracking shipment: " + e.getMessage());
        }
    }

    public static void checkLowStock(Connection conn, int itemId) {
        String sql = "SELECT item_name, quantity FROM inventory WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                if (quantity <= 5) {
                    System.out.println("WARNING: Low stock for item: " + rs.getString("item_name") + " (Quantity: " + quantity + ")");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking low stock: " + e.getMessage());
        }
    }
    
    public static void deleteInventoryItem(Connection conn, Scanner scanner) {
        System.out.print("Enter inventory item ID to delete: ");
        int itemId = scanner.nextInt();

        String checkOrderSql = "SELECT COUNT(*) FROM orders WHERE item_id = ?";
        try (PreparedStatement pstmtCheck = conn.prepareStatement(checkOrderSql)) {
            pstmtCheck.setInt(1, itemId);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Cannot delete item. It is referenced in one or more orders.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking item references: " + e.getMessage());
            return;
        }
        
        String sql = "DELETE FROM inventory WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Inventory item deleted successfully.");
            } else {
                System.out.println("No inventory item found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting inventory item: " + e.getMessage());
        }
    }
}

class ManufacturingManagement {
    
    public static void createProductionOrder(Connection conn, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity to produce: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();

        if (!checkMaterialAvailability(conn, productId, quantity)) {
            System.out.println("Insufficient materials in inventory. Creating purchase orders for required materials.");
            generateMaterialRequirements(conn, productId, quantity);
            return;
        }

        String sql = "INSERT INTO production_orders (product_id, quantity, start_date, status) VALUES (?, ?, ?, 'Planned')";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, quantity);
            pstmt.setDate(3, Date.valueOf(startDate));
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Production order created successfully.");
                
                deductMaterials(conn, productId, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Error creating production order: " + e.getMessage());
        }
    }
    
    private static boolean checkMaterialAvailability(Connection conn, int productId, int quantity) {
        String sql = "SELECT m.material_id, m.quantity_required * ? as required, i.quantity as available " +
                    "FROM bill_of_materials m " +
                    "JOIN inventory i ON m.material_id = i.id " +
                    "WHERE m.product_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("required") > rs.getInt("available")) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error checking material availability: " + e.getMessage());
            return false;
        }
    }

    private static void generateMaterialRequirements(Connection conn, int productId, int quantity) {
        String sql = "SELECT m.material_id, m.quantity_required * ? - i.quantity as needed " +
                    "FROM bill_of_materials m " +
                    "JOIN inventory i ON m.material_id = i.id " +
                    "WHERE m.product_id = ? AND m.quantity_required * ? > i.quantity";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int materialId = rs.getInt("material_id");
                int neededQuantity = rs.getInt("needed");
                generatePurchaseOrder(conn, materialId, neededQuantity);
            }
        } catch (SQLException e) {
            System.out.println("Error generating material requirements: " + e.getMessage());
        }
    }
    
    private static void generatePurchaseOrder(Connection conn, int itemId, int quantity) {
        String sql = "SELECT unit_price FROM inventory WHERE id = ?";
        try (PreparedStatement fetchPstmt = conn.prepareStatement(sql)) {
            fetchPstmt.setInt(1, itemId);
            ResultSet rs = fetchPstmt.executeQuery();
            if (rs.next()) {
                double unitPrice = rs.getDouble("unit_price");
                double totalAmount = quantity * unitPrice;

                String insertSql = "INSERT INTO production_orders (product_id, quantity, total_amount, order_date, status) VALUES (?, ?, ?, CURRENT_DATE, 'In Progress')";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                    insertPstmt.setInt(1, itemId);
                    insertPstmt.setInt(2, quantity);
                    insertPstmt.setDouble(3, totalAmount);
                    int affectedRows = insertPstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Purchase order generated successfully for " + quantity + " units of item ID: " + itemId);
                    }
                }
            } else {
                System.out.println("Item not found in inventory.");
            }
        } catch (SQLException e) {
            System.out.println("Error generating purchase order: " + e.getMessage());
        }
    }

    private static void deductMaterials(Connection conn, int productId, int quantity) {
        String sql = "UPDATE inventory " + 
                    "SET quantity = quantity - (b.quantity_required * ?) " + 
                    "FROM bill_of_materials b " + 
                    "WHERE b.product_id = ? AND inventory.id = b.material_id"; 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deducting materials: " + e.getMessage());
        }
    }
    
    public static void viewProductionSchedule(Connection conn) {
        String sql = "SELECT * FROM production_orders ORDER BY start_date";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	System.out.println("Order ID: " + rs.getInt("id") +
                        ", Product ID: " + rs.getInt("product_id") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Start Date: " + rs.getDate("start_date") +
                        ", Status: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing production schedule: " + e.getMessage());
        }
    }

    public static void updateProductionStatus(Connection conn, Scanner scanner) {
        System.out.print("Enter production order ID: ");
        int orderId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter new status (In Progress/Completed/Cancelled): ");
        String newStatus = scanner.nextLine();

        String sql = "UPDATE production_orders SET status = ?, " +
                    "completion_date = CASE WHEN ? = 'Completed' THEN CURRENT_DATE ELSE completion_date END " +
                    "WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, newStatus);
            pstmt.setInt(3, orderId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Production order status updated successfully.");
                if (newStatus.equals("Completed")) {
                    updateInventoryOnCompletion(conn, orderId);
                }
            } else {
                System.out.println("No production order found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating production order status: " + e.getMessage());
        }
    }

    private static void updateInventoryOnCompletion(Connection conn, int orderId) {
        String sql = "UPDATE inventory i " +
                    "JOIN production_orders po ON i.id = po.product_id " +
                    "SET i.quantity = i.quantity + po.quantity " +
                    "WHERE po.id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
            System.out.println("Inventory updated with completed production.");
        } catch (SQLException e) {
            System.out.println("Error updating inventory: " + e.getMessage());
        }
    }

    public static void generateProductionReport(Connection conn) {
        String sql = "SELECT product_id, " +
        	    "COUNT(id) AS total_orders, " +
        	    "SUM(CASE WHEN status = 'Completed' THEN quantity ELSE 0 END) AS total_produced, " +
        	    "AVG(CASE WHEN status = 'Completed' THEN " +
        	    "(completion_date - start_date) " +
        	    "ELSE NULL END) AS avg_production_days " +
        	"FROM production_orders " +
        	"GROUP BY product_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nProduction Report");
            while (rs.next()) {
                System.out.println("Product ID: " + rs.getInt("product_id") +
                        "\nTotal Orders: " + rs.getInt("total_orders") +
                        "\nTotal Produced: " + rs.getInt("total_produced") +
                        "\nAverage Production Days: " + rs.getDouble("avg_production_days") + "\n");
            }
        } catch (SQLException e) {
            System.out.println("Error generating production report: " + e.getMessage());
        }
    }
    
    public static void manageBillOfMaterials(Connection conn, Scanner scanner) {
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        
        while (true) {
            System.out.print("Enter material ID (or 0 to finish): ");
            int materialId = scanner.nextInt();
            if (materialId == 0) break;
            
            System.out.print("Enter quantity required: ");
            int quantityRequired = scanner.nextInt();
            
            updateBillOfMaterials(conn, productId, materialId, quantityRequired);
        }
    }

    private static void updateBillOfMaterials(Connection conn, int productId, int materialId, int quantityRequired) {
        String sql = "INSERT INTO bill_of_materials (product_id, material_id, quantity_required) " +
                     "VALUES (?, ?, ?) " +
                     "ON CONFLICT (product_id, material_id) DO UPDATE " +
                     "SET quantity_required = EXCLUDED.quantity_required";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, materialId);
            pstmt.setInt(3, quantityRequired);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Bill of materials updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating bill of materials: " + e.getMessage());
        }
    }

}

class CustomerRelationshipManagement {
	
	public static void addCustomer(Connection conn, Scanner scanner) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter location: ");
        String location = scanner.nextLine();

        String sql = "INSERT INTO customers (name, email, phone, location) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, location);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

	public static void viewCustomers(Connection conn) {
        String sql = "SELECT * FROM customers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	System.out.println("ID: " + rs.getInt("id") + 
                        ", Name: " + rs.getString("name") + 
                        ", Email: " + rs.getString("email") + 
                        ", Phone: " + rs.getString("phone") + 
                        ", Location: " + rs.getString("location"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing customers: " + e.getMessage());
        }
    }
	
	public static void displayCustomersByLocation(Connection conn, Scanner scanner) {
	    System.out.print("Enter location to filter customers: ");
	    String location = scanner.nextLine();

	    String sql = "SELECT name, email, phone FROM customers WHERE location = ? ORDER BY name";

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, location);
	        ResultSet rs = pstmt.executeQuery();

            System.out.println("Customers in Location: " + location);
	        while (rs.next()) {
	            System.out.println("Customers in Location: " + location);
	            System.out.println("Name: " + rs.getString("name") + 
                        ", Email: " + rs.getString("email") + 
                        ", Phone: " + rs.getString("phone"));
	        }

	    } catch (SQLException e) {
	        System.out.println("Error displaying customers for the specified location: " + e.getMessage());
	    }
	}


	public static void updateCustomer(Connection conn, Scanner scanner) {
        System.out.print("Enter customer ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter new location: ");
        String location = scanner.nextLine();

        String sql = "UPDATE customers SET name = ?, email = ?, phone = ?, location = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setString(4, location);
            pstmt.setInt(5, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("No customer found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

	public static void recordSale(Connection conn, Scanner scanner) {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter sale amount: ");
        double amount = scanner.nextDouble();

        String sql = "INSERT INTO sales (customer_id, product_id, quantity, sale_date, amount) " +
                "VALUES (?, ?, ?, CURRENT_DATE, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, amount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Sale recorded successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error recording sale: " + e.getMessage());
        }
    }

	public static void generateSalesReport(Connection conn) {
	    String sql = "SELECT c.name AS customer_name, s.product_id, s.quantity, s.sale_date, s.amount " +
	                 "FROM sales s " +
	                 "JOIN customers c ON s.customer_id = c.id " +
	                 "ORDER BY s.sale_date";

	    try (Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {

	        System.out.println("Sales Report:");

	        while (rs.next()) {
	            System.out.println("Customer: " + rs.getString("customer_name") + 
                        ", Product ID: " + rs.getInt("product_id") + 
                        ", Quantity: " + rs.getInt("quantity") + 
                        ", Date: " + rs.getDate("sale_date") + 
                        ", Amount: " + rs.getDouble("amount"));
 }
	    } catch (SQLException e) {
	        System.out.println("Error generating sales report: " + e.getMessage());
	    }
	}

	enum IssuePriority {
	    HIGH, MEDIUM, LOW
	}

	 public static void createCustomerIssue(Connection conn, Scanner scanner) {
	        System.out.print("Enter customer ID: ");
	        int customerId = scanner.nextInt();
	        scanner.nextLine(); 
	        System.out.print("Enter issue description: ");
	        String description = scanner.nextLine();
	        System.out.print("Enter priority (High/Medium/Low): ");
	        IssuePriority priority = IssuePriority.valueOf(scanner.nextLine().toUpperCase());

	        String sql = "INSERT INTO customer_issues (customer_id, description, priority, status, creation_date) VALUES (?, ?, ?, 'Open', CURRENT_DATE)";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, customerId);
	            pstmt.setString(2, description);
	            pstmt.setString(3, priority.name());
	            int affectedRows = pstmt.executeUpdate();
	            if (affectedRows > 0) {
	                System.out.println("Customer issue created successfully.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error creating customer issue: " + e.getMessage());
	        }
	    }

    public static void viewCustomerIssues(Connection conn) {
        String sql = "SELECT * FROM customer_issues ORDER BY creation_date DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	System.out.println("Issue ID: " + rs.getInt("id") +
                        ", Customer ID: " + rs.getInt("customer_id") +
                        ", Description: " + rs.getString("description") +
                        ", Priority: " + rs.getString("priority") +
                        ", Status: " + rs.getString("status") +
                        ", Created: " + rs.getDate("creation_date"));

            }
        } catch (SQLException e) {
            System.out.println("Error viewing customer issues: " + e.getMessage());
        }
    }
    
    enum IssueStatus {
        OPEN, RESOLVED, CLOSED
    }

    public static void updateIssueStatus(Connection conn, Scanner scanner) {
        System.out.print("Enter issue ID to update: ");
        int issueId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter new status (Open/Resolved/Closed): ");
        IssueStatus newStatus = IssueStatus.valueOf(scanner.nextLine().toUpperCase()); 

        String sql = "UPDATE customer_issues SET status = ?, last_updated = CURRENT_DATE WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, newStatus.name());
            pstmt.setInt(2, issueId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Issue status updated successfully.");
            } else {
                System.out.println("No issue found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating issue status: " + e.getMessage());
        }
    }

    public static void generateIssueReport(Connection conn) {
        String sql = "SELECT status, COUNT(*) as count FROM customer_issues GROUP BY status";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nCustomer Issue Report: ");
            while (rs.next()) {
            	System.out.println(rs.getString("status") + ": " + rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.out.println("Error generating issue report: " + e.getMessage());
        }
    }
    
    public static void resolveCustomerIssue(Connection conn, Scanner scanner) {
        System.out.print("Enter issue ID to resolve: ");
        int issueId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter resolution details: ");
        String resolution = scanner.nextLine();

        String sql = "UPDATE customer_issues SET resolution = ?, status = 'Resolved', resolved_date = CURRENT_DATE WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resolution);
            pstmt.setInt(2, issueId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer issue resolved successfully.");
            } else {
                System.out.println("No issue found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error resolving customer issue: " + e.getMessage());
        }
    }
    

	public static void deleteCustomer(Connection conn, Scanner scanner) {
        System.out.print("Enter customer ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM customers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("No customer found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }
}

class HumanCapitalManagement {
    public static void addEmployee(Connection conn, Scanner scanner) {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter position: ");
        String position = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();

        String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding employee: " + e.getMessage());
        }
    }

    public static void viewEmployees(Connection conn) {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
            	System.out.println("ID: " + rs.getInt("id") + 
                        ", Name: " + rs.getString("name") + 
                        ", Position: " + rs.getString("position") + 
                        ", Salary: " + rs.getDouble("salary"));

            }
        } catch (SQLException e) {
            System.out.println("Error viewing employees: " + e.getMessage());
        }
    }

    public static void updateEmployee(Connection conn, Scanner scanner) {
        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new position: ");
        String position = scanner.nextLine();
        System.out.print("Enter new salary: ");
        double salary = scanner.nextDouble();

        String sql = "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No employee found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }
    
    public static void searchEmployee(Connection conn, Scanner scanner) {
        System.out.print("Enter employee ID to search: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        String sql = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Employee found:");
            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                        ", Name: " + rs.getString("name") + 
                        ", Position: " + rs.getString("position") + 
                        ", Salary: " + rs.getDouble("salary"));
            } else {
                System.out.println("No employee found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error searching employee: " + e.getMessage());
        }
    }

    public static void calculatePayroll(Connection conn, Scanner scanner) {
        System.out.print("Enter tax rate (%): ");
        double taxRate = scanner.nextDouble() / 100.0;
        
        String sql = "SELECT id, name, salary FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	
    	    System.out.println("Payroll Calculation:");

        	while (rs.next()) {
        	    int id = rs.getInt("id");
        	    String name = rs.getString("name");
        	    double salary = rs.getDouble("salary");

        	    double tax = salary * taxRate; 
        	    double netPay = salary - tax;
        	    System.out.println("ID: " + id + ", Name: " + name + 
        	                       ", Gross Pay: " + salary + 
        	                       ", Tax: " + tax + 
        	                       ", Net Pay: " + netPay);
        	}
        } catch (SQLException e) {
            System.out.println("Error calculating payroll: " + e.getMessage());
        }
    }

    enum PerformanceRating {
        EXCELLENT, GOOD, SATISFACTORY, NEEDS_IMPROVEMENT, POOR
    }
    
    public static void recordEmployeePerformance(Connection conn, Scanner scanner) {
        System.out.print("Enter employee ID: ");
        int employeeId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter performance rating (EXCELLENT, GOOD, SATISFACTORY, NEEDS_IMPROVEMENT, POOR): ");
        PerformanceRating rating = PerformanceRating.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Enter comments: ");
        String comments = scanner.nextLine();
        
        String sql = "INSERT INTO employee_performance (employee_id, date, rating, comments) " +
                "VALUES (?, CURRENT_DATE, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, rating.name());
            pstmt.setString(3, comments);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee performance recorded successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error recording employee performance: " + e.getMessage());
        }
    }

    public static void generateEmployeePerformanceReport(Connection conn) {
        String sql = "SELECT e.name, ep.date, ep.rating, ep.comments " +
                     "FROM employees e " +
                     "JOIN employee_performance ep ON e.id = ep.employee_id " +
                     "ORDER BY e.name, ep.date";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        	
            System.out.println("Employee Performance Report:");
            while (rs.next()) {
                System.out.println("Employee: " + rs.getString("name") + 
                        ", Date: " + rs.getDate("date") + 
                        ", Rating: " + rs.getString("rating") + 
                        ", Comments: " + rs.getString("comments"));
            }

        } catch (SQLException e) {
            System.out.println("Error generating employee performance report: " + e.getMessage());
        }
    }

    
    public static void deleteEmployee(Connection conn, Scanner scanner) {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No employee found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }
}


class UserManagement {

    public static void viewUsers(Connection conn) {
        String sql = "SELECT id, username, role FROM users"; 
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + 
                                   ", Username: " + rs.getString("username") + 
                                   ", Role: " + rs.getString("role"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing users: " + e.getMessage());
        }
    }

    public static void updateUser(Connection conn, Scanner scanner) {
        System.out.print("Enter user ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter new role: ");
        String role = scanner.nextLine();

        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, ERPSystem.SecurityManager.hashPassword(password));
            pstmt.setString(3, role);
            pstmt.setInt(4, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("No user found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public static void deleteUser(Connection conn, Scanner scanner) {
        System.out.print("Enter user ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("No user found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}
	
public class ERPSystem {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database successfully.");
                run(conn);
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    private static void run(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        String currentUserRole = null; 

        while (running) {
        	printMenu();

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
            case 1: // Register
                if (currentUserRole == null) {
                    registerUser(conn, scanner);
                } else {
                    System.out.println("You are already logged in.");
                }
                break;
            case 2: // Login
                if (currentUserRole == null) {
                    currentUserRole = login(conn, scanner);
                } else {
                    System.out.println("You are already logged in.");
                }
                break;
            case 3: // Finance Management
                if (currentUserRole != null && (currentUserRole.equals("accountant") || currentUserRole.equals("admin"))) {
                    financeManagement(conn, scanner);
                } else {
                    System.out.println("You don't have permission to access Finance Management.");
                }
                break;
            case 4: // Supply Chain Management
            	 if (currentUserRole != null && (currentUserRole.equals("supply_chain_manager") || currentUserRole.equals("admin"))) {
                    supplyChainManagement(conn, scanner);
                } else {
                    System.out.println("You don't have permission to access Supply Chain Management.");
                }
                break;
            case 5: // Manufacturing Management
                if (currentUserRole != null && (currentUserRole.equals("production_manager") || currentUserRole.equals("admin"))) {
                    manageProduction(conn, scanner);
                } else {
                    System.out.println("You don't have permission to access Manufacturing Management.");
                }
                break;
            case 6: // Customer Relationship Management
            	if (currentUserRole != null && (currentUserRole.equals("crm_manager") || currentUserRole.equals("admin"))) {
                    customerRelationshipManagement(conn, scanner);
                } else {
                    System.out.println("You don't have permission to access Customer Relationship Management.");
                }
                break;
            case 7: // Human Capital Management
                if (currentUserRole != null && (currentUserRole.equals("hr") || currentUserRole.equals("admin"))) {
                    humanCapitalManagement(conn, scanner);
                } else {
                    System.out.println("You don't have permission to access Human Capital Management.");
                }
                break;
            case 8: // User Management 
                if (currentUserRole != null && currentUserRole.equals("admin")) {
                    userManagement(conn, scanner); 
                } else {
                    System.out.println("You don't have permission to access User Management.");
                }
                break;
            case 9: // Logout
                    logout();
                    currentUserRole = null;
                    running = false;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");

            }
        }

        System.out.println("Thank you for using the ERP System. Goodbye!");
    }
    
    private static void printMenu() {
        System.out.println("\nOracle-Inspired ERP System");
        System.out.println("1. Register"); 
        System.out.println("2. Login");
        System.out.println("3. Finance Management");
        System.out.println("4. Supply Chain Management");
        System.out.println("5. Manufacturing Management");
        System.out.println("6. Customer Relationship Management");
        System.out.println("7. Human Capital Management"); 
        System.out.println("8. User Management (Admin Only)");
        System.out.println("9. Logout");
        System.out.print("Enter your choice: ");
    	
    } 
   
    enum UserRole {
    	ACCOUNTANT, HR, CRM_MANAGER, PRODUCTION_MANAGER, SUPPLY_CHAIN_MANAGER, ADMIN
    }
    
    private static void registerUser(Connection conn, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role (accountant/hr/crm_manager/production_manager/supply_chain_manager/admin): ");
        UserRole role = null;
        try {
            role = UserRole.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role specified. Please choose from the following:");
            for (UserRole validRole : UserRole.values()) {
                System.out.println("- " + validRole.name().toLowerCase()); 
            }
            return;
        }

        String hashedPassword = SecurityManager.hashPassword(password);

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            // Changed to lowercase to match case-insensitive comparison in access control checks
            pstmt.setString(3, role.name().toLowerCase());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("Error registering user.");
            }
        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
        }
    }

    private static String login(Connection conn, Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String sql = "SELECT password, role FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (SecurityManager.verifyPassword(password, storedHash)) {
                    String role = rs.getString("role");
                    System.out.println("Login successful. Welcome, " + username + " (Role: " + role + ")!");                
                    return role;
                } else {
                    System.out.println("Invalid password.");
                    return null;
                }
            } else {
                System.out.println("Invalid username.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return null;
        }
    }

    private static void logout() {
        System.out.println("Logged out successfully.");
    }

    public class SecurityManager {

        public static String hashPassword(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedBytes = md.digest(password.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedBytes) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Error hashing password", e);
            }
        }

        public static boolean verifyPassword(String inputPassword, String storedHash) {
            String inputHash = hashPassword(inputPassword);
            return inputHash.equals(storedHash);
        }
    }
    
    private static void financeManagement(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("\nFinance Management:");
            System.out.println("1. Record Transaction");
            System.out.println("2. View Financial Transactions");
            System.out.println("3. Generate Transaction Summary Report");
            System.out.println("4. Update Transaction");
            System.out.println("5. Create Budget");
            System.out.println("6. View Budget");
            System.out.println("7. Calculate Tax");
            System.out.println("8. Generate Tax Report");
            System.out.println("9. Generate Financial Report");
            System.out.println("10. Process Billing");
            System.out.println("11. Delete Transaction");
            System.out.println("12. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    FinanceManagement.recordTransaction(conn, scanner);
                    break;
                case 2:
                    FinanceManagement.viewFinancialTransactions(conn);
                    break;
                case 3:
                    FinanceManagement.generateTransactionSummaryReport(conn);
                    break;
                case 4:
                    FinanceManagement.updateTransaction(conn, scanner);
                    break;
                case 5:
                    FinanceManagement.createBudget(conn, scanner);
                    break;
                case 6:
                    FinanceManagement.viewBudget(conn, scanner);
                    break;
                case 7:
                    FinanceManagement.calculateTax(conn, scanner);
                    break;
                case 8:
                    FinanceManagement.generateTaxReport(conn, scanner);
                    break;
                case 9:
                    FinanceManagement.generateFinancialReport(conn);
                    break;
                case 10:
                    FinanceManagement.processBilling(conn, scanner);
                    break;
                case 11:
                    FinanceManagement.deleteTransaction(conn, scanner);
                    break;
                case 12:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

  

    private static void supplyChainManagement(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("\nSupply Chain Management:");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Inventory Item");
            System.out.println("4. Create Order");
            System.out.println("5. Record Stock Movement");
            System.out.println("6. Update Order Status");
            System.out.println("7. Track Shipment");
            System.out.println("8. Delete Inventory Item");
            System.out.println("9. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    SupplyChainManagement.addInventoryItem(conn, scanner);
                    break;
                case 2:
                    SupplyChainManagement.viewInventory(conn);
                    break;
                case 3:
                    SupplyChainManagement.updateInventoryItem(conn, scanner);
                    break;
                case 4:
                    SupplyChainManagement.createOrder(conn, scanner);
                    break;
                case 5:
                    SupplyChainManagement.recordStockMovement(conn, scanner);
                    break;
                case 6:
                    SupplyChainManagement.updateOrderStatus(conn, scanner);
                    break;
                case 7:
                    SupplyChainManagement.trackShipment(conn, scanner);
                    break;
                case 8:
                    SupplyChainManagement.deleteInventoryItem(conn, scanner);
                    break;
                case 9:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    public static void manageProduction(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("\nProduction Management:");
            System.out.println("1. Create Production Order");
            System.out.println("2. View Production Schedule");
            System.out.println("3. Update Production Status");
            System.out.println("4. Generate Production Report");
            System.out.println("5. Manage Bill of Materials");
            System.out.println("6. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    ManufacturingManagement.createProductionOrder(conn, scanner);
                    break;
                case 2:
                    ManufacturingManagement.viewProductionSchedule(conn);
                    break;
                case 3:
                    ManufacturingManagement.updateProductionStatus(conn, scanner);
                    break;
                case 4:
                    ManufacturingManagement.generateProductionReport(conn);
                    break;
                case 5:
                    ManufacturingManagement.manageBillOfMaterials(conn, scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void customerRelationshipManagement(Connection conn, Scanner scanner) {
        while (true) {
	    	System.out.println("\nCustomer Relationship Management: ");
	        System.out.println("1. Add Customer");
	        System.out.println("2. View Customers");
	        System.out.println("3. Display Customers by Location"); 
	        System.out.println("4. Record Sale"); 
	        System.out.println("5. Generate Sales Report"); 
	        System.out.println("6. Update Customer");
	        System.out.println("7. Delete Customer");
	        System.out.println("8. Return to Main Menu");
	        System.out.print("Enter your choice: ");
	
	        int choice = scanner.nextInt();
	        scanner.nextLine(); 
	
	        switch (choice) {
	            case 1:
	            	CustomerRelationshipManagement.addCustomer(conn, scanner);
	                break;
	            case 2:
	            	CustomerRelationshipManagement.viewCustomers(conn);
	                break;
	            case 3:
	            	CustomerRelationshipManagement.displayCustomersByLocation(conn, scanner);
	                break;
	            case 4:
	            	CustomerRelationshipManagement.recordSale(conn, scanner);
	                break;
	            case 5:
	            	CustomerRelationshipManagement.generateSalesReport(conn);
	                break;
	            case 6:
	            	CustomerRelationshipManagement.updateCustomer(conn, scanner);
	                break;
	            case 7:
	            	CustomerRelationshipManagement.deleteCustomer(conn, scanner);
	                break;
	            case 8:
	                return;
	            default:
	                System.out.println("Invalid choice.");
	        }
        }
    }
    
    private static void humanCapitalManagement(Connection conn, Scanner scanner) {
        while (true) {
	    	System.out.println("\nHuman Capital Management:");
	        System.out.println("1. Add Employee");
	        System.out.println("2. View Employees");
            System.out.println("3. Search Employee");
	        System.out.println("4. Calculate Payroll"); 
	        System.out.println("5. Record Employee Performance");
	        System.out.println("6. Generate Employee Performance Report"); 
	        System.out.println("7. Update Employee");
	        System.out.println("8. Delete Employee");
	        System.out.println("9. Return to Main Menu");
	        System.out.print("Enter your choice: ");
	
	        int choice = scanner.nextInt();
	        scanner.nextLine(); 
	
	        switch (choice) {
	            case 1:
	            	HumanCapitalManagement.addEmployee(conn, scanner);
	                break;
	            case 2:
	            	HumanCapitalManagement.viewEmployees(conn);
	                break;
                case 3: 
                	HumanCapitalManagement.searchEmployee(conn, scanner); 
                	break;
	            case 4:
	            	HumanCapitalManagement.calculatePayroll(conn,scanner);
	                break;
	            case 5:
	            	HumanCapitalManagement.recordEmployeePerformance(conn, scanner);
	                break;
	            case 6:
	            	HumanCapitalManagement.generateEmployeePerformanceReport(conn);
	                break;
	            case 7:
	            	HumanCapitalManagement.updateEmployee(conn, scanner);
	                break;
	            case 8:
	            	HumanCapitalManagement.deleteEmployee(conn, scanner);
	                break;
	            case 9:
	                return;
	            default:
	                System.out.println("Invalid choice.");
	        }
        }
    }
        
    private static void userManagement(Connection conn, Scanner scanner) {
        while (true) {
            System.out.println("\nUser Management:");
            System.out.println("1. View Users");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    UserManagement.viewUsers(conn);
                    break;
                case 2:
                    UserManagement.updateUser(conn, scanner);
                    break;
                case 3:
                    UserManagement.deleteUser(conn, scanner);
                    break;
                case 4:
                    return; 
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
