import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;

public class InventoryPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable productTable;
    DefaultTableModel tableModel;
    JTextField nameField, priceField, quantityField, categoryField, searchField;
    JTextArea notificationArea;

    public InventoryPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Inventory Management", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel main = new JPanel(new BorderLayout());
        main.add(createFormPanel(), BorderLayout.WEST);
        main.add(createTablePanel(), BorderLayout.CENTER);
        main.add(createNotificationPanel(), BorderLayout.SOUTH);

        panel.add(header, BorderLayout.NORTH);
        panel.add(main, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.setPreferredSize(new Dimension(350, 0));

        JLabel title = new JLabel("Product Form", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        nameField = new JTextField(20);
        priceField = new JTextField(20);
        quantityField = new JTextField(20);
        categoryField = new JTextField(20);

        fields.add(Box.createVerticalStrut(5));// Add some spacing at the top
        fields.add(row("Name:", nameField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(row("Price:", priceField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(row("Qty:", quantityField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(row("Category:", categoryField));
        fields.add(Box.createVerticalStrut(5));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        clearBtn.addActionListener(e -> clearFields());
        btns.add(addBtn); btns.add(updateBtn); btns.add(deleteBtn); btns.add(clearBtn);

        p.add(title, BorderLayout.NORTH);
        p.add(fields, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    } 

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        searchBtn.addActionListener(e -> searchProducts());
        refreshBtn.addActionListener(e -> refreshTable());
        top.add(searchField); top.add(searchBtn); top.add(refreshBtn);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty", "Category", "Status", "Updated"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);

        addSampleProducts();

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(productTable), BorderLayout.CENTER);
        return p;
    }

    private JPanel createNotificationPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        p.add(new JLabel("Notifications:"), BorderLayout.NORTH);
        notificationArea = new JTextArea(3, 40);
        notificationArea.setEditable(false);
        p.add(new JScrollPane(notificationArea), BorderLayout.CENTER);
        return p;
    }

    private JPanel row(String label, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(80, 25));
        row.add(lbl);
        row.add(comp);
        return row;
    }

    private void addSampleProducts() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        // Try to load products from file first
        java.util.ArrayList<String[]> loadedProducts = FileHandler.loadProducts();
        if (loadedProducts != null && !loadedProducts.isEmpty()) {
            for (String[] row : loadedProducts) tableModel.addRow(row);
            System.out.println("Loaded " + loadedProducts.size() + " products from file");
        } else {
            // Default products if file doesn't exist
            String[][] data = {
                {"P001", "Milk", "2.99", "5", "Dairy", "Low Stock", today},
                {"P002", "Bread", "1.99", "25", "Bakery", "In Stock", today},
                {"P003", "Eggs", "3.49", "12", "Dairy", "In Stock", today},
                {"P004", "Yogurt", "1.49", "8", "Dairy", "Low Stock", today},
                {"P005", "Cheese", "4.99", "15", "Dairy", "In Stock", today},
                {"P006", "Apple", "0.99", "50", "Fruits", "In Stock", today},
                {"P007", "Banana", "0.59", "30", "Fruits", "In Stock", today},
                {"P008", "Orange", "0.79", "20", "Fruits", "In Stock", today},
                {"P009", "Chicken", "8.99", "10", "Meat", "Low Stock", today},
                {"P010", "Beef", "12.99", "8", "Meat", "Low Stock", today}
            };
            for (String[] row : data) tableModel.addRow(row);
            
            // Save default products to file
            saveProductsToFile();
        }
    }
    
    private void saveProductsToFile() {
        java.util.ArrayList<String[]> products = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String[] row = new String[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j).toString();
            }
            products.add(row);
        }
        FileHandler.saveProducts(products);
    }

    private String getStatus(int qty) {
        if (qty == 0) return "Out of Stock";
        if (qty <= 10) return "Low Stock";
        return "In Stock";
    }

    private void addProduct() {
        String name = nameField.getText().trim();
        String price = priceField.getText().trim();
        String qty = quantityField.getText().trim();
        String cat = categoryField.getText().trim();
        if (name.isEmpty() || price.isEmpty() || qty.isEmpty() || cat.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        try {
            double p = Double.parseDouble(price);// Validate price is a number
            int q = Integer.parseInt(qty);
            String id = "P" + String.format("%03d", tableModel.getRowCount() + 1);
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            tableModel.addRow(new Object[]{id, name, price, qty, cat, getStatus(q), today});
            if (q <= 10) addNotification("Low stock: " + name + " (" + q + ")");
            clearFields();
            saveProductsToFile(); // Save to file after adding
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid number");
        }
    }

    private void updateProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) return;
        String name = nameField.getText().trim();
        String price = priceField.getText().trim();
        String qty = quantityField.getText().trim();
        String cat = categoryField.getText().trim();
        if (name.isEmpty() || price.isEmpty() || qty.isEmpty() || cat.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        try {
            int q = Integer.parseInt(qty); // We only need qty as number to determine status
            tableModel.setValueAt(name, row, 1);
            tableModel.setValueAt(price, row, 2);
            tableModel.setValueAt(qty, row, 3);
            tableModel.setValueAt(cat, row, 4);
            tableModel.setValueAt(getStatus(q), row, 5);
            tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), row, 6);
            if (q <= 10) addNotification("Low stock: " + name + " (" + q + ")");
            clearFields();
            saveProductsToFile(); // Save to file after updating
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid number");
        }
    }

    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) return;
        String name = tableModel.getValueAt(row, 1).toString();
        if (JOptionPane.showConfirmDialog(panel, "Delete " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            addNotification("Deleted: " + name);
            clearFields();
            saveProductsToFile(); // Save to file after deleting
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        categoryField.setText("");
        productTable.clearSelection();
    }

    private void searchProducts() {
        String q = searchField.getText().trim().toLowerCase();
        if (q.isEmpty()) { refreshTable(); return; }
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            boolean match = false;
            for (int j = 0; j < tableModel.getColumnCount(); j++)
                if (tableModel.getValueAt(i, j).toString().toLowerCase().contains(q)) { match = true; break; }
            if (!match) tableModel.removeRow(i);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        addSampleProducts();
    }

    private void addNotification(String msg) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        notificationArea.append("[" + time + "] " + msg + "\n");
    }
}