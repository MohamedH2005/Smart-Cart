import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard {
    Color darkBlue = new Color(29, 45, 68);
    Color sidebarColor = new Color(20, 33, 55);
    Color lightPanel = new Color(245, 247, 250);
    Color addBtnColor = new Color(0, 120, 215);
    
    JFrame DashboardFrame = new JFrame();
    JLabel titleLabel = new JLabel("SmartCart - Dashboard");
    JLabel totalLabel = new JLabel("Total: $0.00");
    public JLabel getTotalLabel() { return totalLabel; }
    JTextField searchField = new JTextField("Search products...");
    JButton searchBtn = new JButton("Search");
    JButton cartBtn = new JButton("Cart");
    
    JButton menuBtn = new JButton("MENU");
    JButton inventoryBtn = new JButton("Inventory");
    JButton salesBtn = new JButton("Sales");
    JButton customersBtn = new JButton("Customers");
    JButton reportsBtn = new JButton("Reports");
    JButton logoutBtn = new JButton("Logout");
    JButton AdminBtn = new JButton("Admin");
    JButton MarketingBtn = new JButton("Marketing");
    JButton ProfileBtn = new JButton("Profile");
    
    CardLayout cardLayout = new CardLayout();
    JPanel contentPanel = new JPanel(cardLayout);
    JPanel topBarPanel = new JPanel();
    JPanel sidebarPanel = new JPanel();
    
    double cartTotal = 0.0;
    HashMap<String, Integer> inventory = new HashMap<>();
    ArrayList<String> cartItems = new ArrayList<>();
    String[] productNames = {"Apple", "Banana", "Orange", "Milk", "Bread", "Eggs", "Cheese", "Chicken", "Rice", "Pasta"};
    double[] productPrices = {0.99, 0.59, 0.79, 2.49, 1.99, 3.49, 4.99, 7.99, 1.29, 1.49};
    int[] productQuantities = {100, 80, 60, 50, 40, 30, 25, 20, 35, 45};
    
    String currentUsername;
    public String currentUserRole;
    
    // Real-time statistics
    private int totalProducts = 10;
    private int lowStockItems = 0;
    private double todaySales = 0.0;
    public int activeUsers = 1;
    private int pendingOrders = 0;
    private double totalRevenue = 45678.90;
    
    SalesPanel salesPanel;
    
    Dashboard(HashMap<String, String> loginInfo, String username, String userRole) {
        this.currentUsername = username;
        this.currentUserRole = userRole;
        
        DashboardFrame.setTitle("SmartCart - Hyper Market");
        DashboardFrame.setSize(1200, 700);
        DashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DashboardFrame.setLocationRelativeTo(null);
        DashboardFrame.setResizable(false);
        DashboardFrame.setLayout(new BorderLayout());
        DashboardFrame.getContentPane().setBackground(darkBlue);
        
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 255, 255, 20));
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search products...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search products...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchBtn.setFocusable(false);
        searchBtn.setBackground(addBtnColor);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setPreferredSize(new Dimension(80, 30));
        
        cartBtn.setFocusable(false);
        cartBtn.setBackground(new Color(40, 167, 69));
        cartBtn.setForeground(Color.WHITE);
        cartBtn.setBorderPainted(false);
        cartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        topBarPanel.setBackground(darkBlue);
        topBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 12));
        topBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        topBarPanel.add(titleLabel);
        topBarPanel.add(searchField);
        topBarPanel.add(searchBtn);
        topBarPanel.add(cartBtn);
        topBarPanel.add(totalLabel);
        
        menuBtn.setForeground(new Color(150, 170, 200));
        menuBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        menuBtn.setPreferredSize(new Dimension(200, 30));
        menuBtn.setHorizontalAlignment(JLabel.CENTER);
        
        JButton[] sideBtns = {menuBtn, AdminBtn, MarketingBtn, ProfileBtn, inventoryBtn, salesBtn, customersBtn, reportsBtn};
        
        for (JButton btn : sideBtns) {
            btn.setFocusable(false);
            btn.setForeground(Color.WHITE);
            btn.setBackground(sidebarColor);
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(200, 45));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(40, 60, 90));
                }
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(sidebarColor);
                }
            });
        }
        
        logoutBtn.setFocusable(false);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 13));
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(200, 45));
        
        sidebarPanel.setBackground(sidebarColor);
        sidebarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        sidebarPanel.setPreferredSize(new Dimension(220, 700));
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.DARK_GRAY));
        sidebarPanel.add(menuBtn);
        sidebarPanel.add(AdminBtn);
        sidebarPanel.add(MarketingBtn);
        sidebarPanel.add(ProfileBtn);
        sidebarPanel.add(inventoryBtn);
        sidebarPanel.add(salesBtn);
        sidebarPanel.add(customersBtn);
        sidebarPanel.add(reportsBtn);
        sidebarPanel.add(logoutBtn);
        
        // Apply role-based access control
        applyRoleBasedAccessControl();
        
        // Initialize statistics
        updateStatistics();
        
        // Update title to show current user and role
        titleLabel.setText("SmartCart - Dashboard (" + currentUsername + " - " + currentUserRole + ")");
        
        // Create panels
        HomePanel homePanel = new HomePanel(this);
        salesPanel = new SalesPanel(this);
        
        contentPanel.add(homePanel.panel, "home");
        contentPanel.add(new MenuPanel(this).panel, "menu");
        contentPanel.add(new AdminPanel(this).panel, "admin");
        contentPanel.add(new MarketingPanel(this).panel, "marketing");
        contentPanel.add(new ProfilePanel(this).panel, "profile");
        contentPanel.add(new InventoryPanel(this).panel, "inventory");
        contentPanel.add(salesPanel.panel, "sales");
        contentPanel.add(new CustomersPanel(this).panel, "customers");
        contentPanel.add(new ReportsPanel(this).panel, "reports");
        cardLayout.show(contentPanel, "home");
        
        DashboardFrame.add(topBarPanel, BorderLayout.NORTH);
        DashboardFrame.add(sidebarPanel, BorderLayout.WEST);
        DashboardFrame.add(contentPanel, BorderLayout.CENTER);
        
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim().toLowerCase();

                if (query.isEmpty() || query.equals("search products...")) {
                    JOptionPane.showMessageDialog(DashboardFrame,
                            "Please enter a search term.", "Search",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                StringBuilder results = new StringBuilder();
                int count = 0;

                for (int i = 0; i < productNames.length; i++) {
                    if (productNames[i].toLowerCase().contains(query)) {
                        results.append(productNames[i])
                               .append(" - $")
                               .append(String.format("%.2f", productPrices[i]))
                               .append("\n");
                        count++;
                    }
                }

                if (count == 0) {
                    JOptionPane.showMessageDialog(DashboardFrame,
                            "No products found for: \"" + query + "\"",
                            "Search Results",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    String[] productResults = new String[count];
                    int index = 0;

                    for (int i = 0; i < productNames.length; i++) {
                        if (productNames[i].toLowerCase().contains(query)) {
                            productResults[index] = productNames[i] + " - $" + String.format("%.2f", productPrices[i]);
                            index++;
                        }
                    }

                    String selected = (String) JOptionPane.showInputDialog(
                            DashboardFrame,
                            "Found " + count + " product(s):\nSelect to add to cart:",
                            "Search Results",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            productResults,
                            productResults[0]);

                    if (selected != null) {
                String selectedName = selected.split(" - \\$")[0];
                double selectedPrice = 0;

                for (int i = 0; i < productNames.length; i++) {
                    if (productNames[i].equals(selectedName)) {
                        selectedPrice = productPrices[i];
                        break;
                    }
                }

                // Add to sales panel cart instead of local cart
                salesPanel.addToCartFromSearch(selectedName, selectedPrice);
                
                JOptionPane.showMessageDialog(DashboardFrame,
                        selectedName + " added to cart!",
                        "Added",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            }
            }
        });
// sales actions        
        cartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "sales");
            }
        });
        
        inventoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "inventory");
            }
        });
        
        salesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "sales");
            }
        });
        
        customersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "customers");
            }
        });
        
        reportsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "reports");
            }
        });
        
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(DashboardFrame, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DashboardFrame.dispose();
                    new Login(new IDandPassword().getLoginInfo());
                }
            }
        });
        
        AdminBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "admin");
            }
        });
        
        MarketingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "marketing");
            }
        });
        
        ProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "profile");
            }
        });
        
        menuBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                homePanel.refreshStats();
                cardLayout.show(contentPanel, "home");
            }
        });
        
        DashboardFrame.setVisible(true);
    }
    
    private void applyRoleBasedAccessControl() {
        // Profile is available for all roles
        ProfileBtn.setEnabled(true);
        
        switch (currentUserRole) {
            case "Admin":
                AdminBtn.setEnabled(true);
                MarketingBtn.setEnabled(true);
                inventoryBtn.setEnabled(true);
                salesBtn.setEnabled(true);
                customersBtn.setEnabled(true);
                reportsBtn.setEnabled(true);
                
                break;
            case "Manager":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(true);
                inventoryBtn.setEnabled(true);
                salesBtn.setEnabled(true);
                customersBtn.setEnabled(true);
                reportsBtn.setEnabled(true);
                break;
            case "Supervisor":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(true);
                salesBtn.setEnabled(true);
                customersBtn.setEnabled(true);
                reportsBtn.setEnabled(true);
                break;
            case "Marketing":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(true);
                inventoryBtn.setEnabled(false);
                salesBtn.setEnabled(false);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(true);
                break;
            case "Cashier":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(false);
                salesBtn.setEnabled(true);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(false);
                break;
            case "Inventory Manager":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(true);
                salesBtn.setEnabled(false);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(false);
                break;
            case "HR Manager":
                AdminBtn.setEnabled(true);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(false);
                salesBtn.setEnabled(false);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(false);
                break;
            case "Accountant":
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(false);
                salesBtn.setEnabled(false);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(true);
                break;
            case "User":
            default:
                AdminBtn.setEnabled(false);
                MarketingBtn.setEnabled(false);
                inventoryBtn.setEnabled(false);
                salesBtn.setEnabled(true);
                customersBtn.setEnabled(false);
                reportsBtn.setEnabled(false);
                break;
        }
    }
    
    // Statistics management methods - reads from actual files
    public void updateStatistics() {
        // Count products from file
        java.util.ArrayList<String[]> products = FileHandler.loadProducts();
        totalProducts = (products != null) ? products.size() : 10;
        
        // Count low stock items (quantity < 10)
        lowStockItems = 0;
        if (products != null) {
            for (String[] product : products) {
                try {
                    int qty = Integer.parseInt(product[3]); // Qty column
                    if (qty < 10) lowStockItems++;
                } catch (Exception e) {}
            }
        }
        
        // Count users from file
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("users.txt"));
            activeUsers = 0;
            while (br.readLine() != null) activeUsers++;
            br.close();
        } catch (Exception e) { activeUsers = 1; }
        
        // Calculate sales from file (format: orderId,total,date,status)
        java.util.ArrayList<String[]> sales = FileHandler.loadSales();
        totalRevenue = 0.0;
        todaySales = 0.0;
        pendingOrders = 0;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        if (sales != null) {
            for (String[] sale : sales) {
                try {
                    double amount = Double.parseDouble(sale[1]); // Total column
                    totalRevenue += amount;
                    if (sale.length > 2 && sale[2].contains(today)) {
                        todaySales += amount;
                    }
                    if (sale.length > 3 && sale[3].equalsIgnoreCase("Pending")) {
                        pendingOrders++;
                    }
                } catch (Exception e) {}
            }
        }
    }
    
    public void addProduct(String name, double price, int quantity) {// In real app, would save to file and refresh from file
        // Add to arrays (simplified - in real app would use dynamic list)
        totalProducts++;
        if (quantity < 10) lowStockItems++;
        updateStatistics();
    }
    
    public void recordSale(double amount) {
        todaySales += amount;
        totalRevenue += amount;
    }
    
    public void updateProductQuantity(int index, int newQuantity) {
        if (index >= 0 && index < productQuantities.length) {// Update low stock count based on quantity change
            if (productQuantities[index] < 10 && newQuantity >= 10) lowStockItems--;
            else if (productQuantities[index] >= 10 && newQuantity < 10) lowStockItems++;
            productQuantities[index] = newQuantity;
            updateStatistics();
        }
    }
    
    // Getter methods for statistics
    public int getTotalProducts() { return totalProducts; }
    public int getLowStockItems() { return lowStockItems; }
    public double getTodaySales() { return todaySales; }
    public int getActiveUsers() { return activeUsers; }
    public int getPendingOrders() { return pendingOrders; }
    public double getTotalRevenue() { return totalRevenue; }
    
    public String[] getProductNames() { return productNames; }
    public double[] getProductPrices() { return productPrices; }
    public int[] getProductQuantities() { return productQuantities; }
    
    public String getCurrentUsername() {
        return currentUsername;
    }
    
    public String getCurrentUserRole() {
        return currentUserRole;
    }
    
    public static void main(String[] args) {
        new Dashboard(new HashMap<>(), "test", "Admin");
    }
}
