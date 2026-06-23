import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable productTable, orderTable;
    DefaultTableModel productModel, orderModel;
    JTextField searchField, quantityField;
    JLabel totalLabel;
    double cartTotal = 0.0;

    public SalesPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        productModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Stock"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        orderModel = new DefaultTableModel(new String[]{"Name", "Price", "Qty", "Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        addSampleProducts();

        JLabel header = new JLabel("Sales Module", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(createProductPanel());
        mainPanel.add(createCartPanel());

        panel.add(header, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createProductPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Products"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchField = new JTextField(12);
        JButton searchBtn = new JButton("Search");
        JButton listBtn = new JButton("All");
        searchBtn.addActionListener(e -> searchProducts());
        listBtn.addActionListener(e -> listAllProducts());
        top.add(new JLabel("Search:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(listBtn);

        productTable = new JTable(productModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        quantityField = new JTextField("1", 5);
        JButton addBtn = new JButton("Add to Cart ->");
        addBtn.addActionListener(e -> addToCart());
        bottom.add(new JLabel("Qty:"));
        bottom.add(quantityField);
        bottom.add(addBtn);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(productTable), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createCartPanel() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

        orderTable = new JTable(orderModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel btns = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton removeBtn = new JButton("Remove");
        JButton clearBtn = new JButton("Clear");
        JButton orderBtn = new JButton("Create Order");
        JButton cancelBtn = new JButton("Cancel");
        removeBtn.addActionListener(e -> removeFromCart());
        clearBtn.addActionListener(e -> clearCart());
        orderBtn.addActionListener(e -> createOrder());
        cancelBtn.addActionListener(e -> cancelOrder());
        btns.add(removeBtn); btns.add(clearBtn); btns.add(orderBtn); btns.add(cancelBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.add(totalLabel, BorderLayout.NORTH);
        south.add(btns, BorderLayout.SOUTH);

        p.add(scrollPane, BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

private void addSampleProducts() {
    String[][] data = {
        {"P001", "Milk", "2.99", "50"},
        {"P002", "Bread", "1.99", "25"},
        {"P003", "Eggs", "3.49", "30"},
        {"P004", "Yogurt", "1.49", "40"},
        {"P005", "Cheese", "4.99", "20"},
        {"P006", "Butter", "3.29", "35"},
        {"P007", "Cream", "2.79", "28"},
        {"P008", "Apple", "0.99", "100"},
        {"P009", "Banana", "0.59", "80"},
        {"P010", "Orange", "0.79", "60"},
        {"P011", "Grapes", "2.49", "45"},
        {"P012", "Mango", "1.99", "30"},
        {"P013", "Chicken", "8.99", "15"},
        {"P014", "Beef", "12.99", "10"},
        {"P015", "Fish", "9.99", "12"},
        {"P016", "Rice", "5.99", "35"},
        {"P017", "Pasta", "2.49", "45"},
        {"P018", "Flour", "1.89", "55"},
        {"P019", "Sugar", "1.59", "60"},
        {"P020", "Salt", "0.89", "70"},
        {"P021", "Oil", "4.99", "25"},
        {"P022", "Tomato", "1.29", "55"},
        {"P023", "Onion", "0.89", "70"},
        {"P024", "Potato", "0.69", "90"},
        {"P025", "Carrot", "0.79", "65"},
        {"P026", "Coca Cola", "1.49", "80"},
        {"P027", "Pepsi", "1.49", "75"},
        {"P028", "Water", "0.49", "120"},
        {"P029", "Juice", "2.99", "40"},
        {"P030", "Tea", "3.49", "30"},
        {"P031", "Coffee", "4.99", "25"},
        {"P032", "Chocolate", "2.99", "45"},
        {"P033", "Chips", "1.99", "50"},
        {"P034", "Cookies", "2.49", "40"},
        {"P035", "Ice Cream", "3.99", "20"},
        {"P036", "Soap", "1.29", "60"},
        {"P037", "Shampoo", "4.99", "35"},
        {"P038", "Toothpaste", "2.49", "45"},
        {"P039", "Tissues", "1.99", "70"},
        {"P040", "Detergent", "5.99", "30"}
    };
    for (String[] row : data) productModel.addRow(row);
}

    private void searchProducts() {
        String q = searchField.getText().trim().toLowerCase();
        if (q.isEmpty()) return;
        listAllProducts();
        for (int i = productModel.getRowCount() - 1; i >= 0; i--) {
            boolean match = false;
            for (int j = 0; j < productModel.getColumnCount(); j++)
                if (productModel.getValueAt(i, j).toString().toLowerCase().contains(q)) { match = true; break; }
            if (!match) productModel.removeRow(i);
        }
    }

    private void listAllProducts() {
        productModel.setRowCount(0);
        addSampleProducts();
    }

    private void addToCart() {
        System.out.println("BUTTON CLICKED");
        int row = productTable.getSelectedRow();
    System.out.println("Selected row: " + row);
        if (row == -1) { JOptionPane.showMessageDialog(panel, "Select a product to add"); return; }
        try {
            int qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) { JOptionPane.showMessageDialog(panel, "Qty must be > 0"); return; }
            String name = productModel.getValueAt(row, 1).toString();
            double price = Double.parseDouble(productModel.getValueAt(row, 2).toString());
            int stock = Integer.parseInt(productModel.getValueAt(row, 3).toString());
            if (qty > stock) { JOptionPane.showMessageDialog(panel, "Only " + stock + " in stock"); return; }

            for (int i = 0; i < orderModel.getRowCount(); i++) {
                if (orderModel.getValueAt(i, 0).equals(name)) {
                    int oldQty = Integer.parseInt(orderModel.getValueAt(i, 2).toString());
                    int newQty = oldQty + qty;
                    if (newQty > stock + oldQty) { JOptionPane.showMessageDialog(panel, "Stock limit"); return; }
                    orderModel.setValueAt(newQty, i, 2);
                    orderModel.setValueAt(String.format("%.2f", price * newQty), i, 3);
                    productModel.setValueAt(stock - qty, row, 3);
                    updateTotal();
                    return;
                }
            }
            orderModel.addRow(new Object[]{name, String.format("%.2f", price), qty, String.format("%.2f", price * qty)});
            productModel.setValueAt(stock - qty, row, 3);
            updateTotal();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid quantity");
        }
    }

    public void addToCartFromSearch(String productName, double price) {
        for (int i = 0; i < productModel.getRowCount(); i++) {
            if (productModel.getValueAt(i, 1).toString().equalsIgnoreCase(productName)) {
                productTable.setRowSelectionInterval(i, i);
                quantityField.setText("1");
                addToCart();
                return;
            }
        }
    }

    private void removeFromCart() {
        int row = orderTable.getSelectedRow();
        if (row == -1) return;
        String name = orderModel.getValueAt(row, 0).toString();
        int qty = Integer.parseInt(orderModel.getValueAt(row, 2).toString());
        for (int i = 0; i < productModel.getRowCount(); i++)
            if (productModel.getValueAt(i, 1).equals(name))
                productModel.setValueAt(Integer.parseInt(productModel.getValueAt(i, 3).toString()) + qty, i, 3);
        orderModel.removeRow(row);
        updateTotal();
    }

    private void clearCart() {
        while (orderModel.getRowCount() > 0) {
            orderTable.setRowSelectionInterval(0, 0);
            removeFromCart();
        }
    }

    private void createOrder() {
        if (orderModel.getRowCount() == 0) { JOptionPane.showMessageDialog(panel, "Cart is empty"); return; }
        String oid = "ORD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        StringBuilder sb = new StringBuilder("Order: " + oid + "\n");
        for (int i = 0; i < orderModel.getRowCount(); i++)
            sb.append(orderModel.getValueAt(i, 0)).append(" x").append(orderModel.getValueAt(i, 2))
              .append(" $").append(orderModel.getValueAt(i, 3)).append("\n");
        sb.append("Total: $").append(String.format("%.2f", cartTotal));
        JOptionPane.showMessageDialog(panel, sb.toString());
        
        // Save sale to file
        FileHandler.saveSale(oid, cartTotal, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "Completed");
        
        dashboard.recordSale(cartTotal);
        orderModel.setRowCount(0);
        updateTotal();
    }

    private void cancelOrder() {
        clearCart();
    }

    private void updateTotal() {
        cartTotal = 0;
        for (int i = 0; i < orderModel.getRowCount(); i++)
            cartTotal += Double.parseDouble(orderModel.getValueAt(i, 3).toString());
        totalLabel.setText("Total: $" + String.format("%.2f", cartTotal));
        dashboard.totalLabel.setText("Total: $" + String.format("%.2f", cartTotal));
    }
}