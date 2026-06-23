import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel {
    Dashboard dashboard;
    JPanel panel;
    JLabel[] valueLabels;
    
    public HomePanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        
        // Header
        JLabel headerLabel = new JLabel("Welcome to SmartCart Hyper Market System");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(29, 45, 68));
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        // Content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        contentPanel.setBackground(new Color(245, 247, 250));
        
        // Quick stats cards
        JPanel[] cards = createStatCards();
        for (JPanel card : cards) {
            contentPanel.add(card);
        }
        
        // Quick actions panel
        JPanel actionsPanel = createQuickActionsPanel();
        
        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel[] createStatCards() {
        JPanel[] cards = new JPanel[6];
        
        // Get real-time statistics from dashboard
        String totalProductsStr = String.valueOf(dashboard.getTotalProducts());
        String lowStockStr = String.valueOf(dashboard.getLowStockItems());
        String todaySalesStr = "$" + String.format("%.2f", dashboard.getTodaySales());
        String activeUsersStr = String.valueOf(dashboard.getActiveUsers());
        String pendingOrdersStr = String.valueOf(dashboard.getPendingOrders());
        String revenueStr = "$" + String.format("%.2f", dashboard.getTotalRevenue());
        
        String[] titles = {"Total Products", "Low Stock Items", "Today's Sales", "Active Users", "Pending Orders", "Revenue"};
        String[] values = {totalProductsStr, lowStockStr, todaySalesStr, activeUsersStr, pendingOrdersStr, revenueStr};
        String[] icons = {"\uD83D\uDECD", "\u26A0", "$", "\uD83D\uDC65", "\uD83D\uDCE6", "\uD83D\uDCB0"};
        Color[] colors = {
            new Color(0, 120, 215),
            new Color(255, 193, 7),
            new Color(40, 167, 69),
            new Color(220, 53, 69),
            new Color(23, 162, 184),
            new Color(108, 117, 125)
        };
        
        for (int i = 0; i < 6; i++) {// Create a card for each statistic
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(4, 0, 0, 0, colors[i]),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            
            // Top section with icon and title
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            
            JLabel iconLabel = new JLabel(icons[i], JLabel.CENTER);// Use emoji icons for simplicity
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
            iconLabel.setForeground(colors[i]);// Color the icon based on the statistic type
            
            JLabel title = new JLabel(titles[i]);
            title.setFont(new Font("Arial", Font.BOLD, 12));
            title.setForeground(Color.GRAY);
            
            topPanel.add(iconLabel, BorderLayout.WEST);
            topPanel.add(title, BorderLayout.CENTER);
            
            // Value
            JLabel value = new JLabel(values[i]);
            value.setFont(new Font("Arial", Font.BOLD, 24));
            value.setForeground(colors[i]);
            value.setHorizontalAlignment(JLabel.LEFT);
            
            card.add(topPanel, BorderLayout.NORTH);
            card.add(value, BorderLayout.CENTER);
            
            cards[i] = card;
        }
        
        // Store value labels for refresh
        valueLabels = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            Component[] comps = cards[i].getComponents();
            for (Component c : comps) {
                if (c instanceof JLabel) {
                    JLabel lbl = (JLabel) c;
                    if (lbl.getFont().getSize() == 24) {
                        valueLabels[i] = lbl;
                        break;
                    }
                }
            }
        }
        
        return cards;
    }
    
    public void refreshStats() {
        // Re-read statistics from dashboard
        String[] newValues = {
            String.valueOf(dashboard.getTotalProducts()),
            String.valueOf(dashboard.getLowStockItems()),
            "$" + String.format("%.2f", dashboard.getTodaySales()),
            String.valueOf(dashboard.getActiveUsers()),
            String.valueOf(dashboard.getPendingOrders()),
            "$" + String.format("%.2f", dashboard.getTotalRevenue())
        };
        
        for (int i = 0; i < 6; i++) {
            if (valueLabels[i] != null) {
                valueLabels[i].setText(newValues[i]);
            }
        }
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionsPanel.setBackground(new Color(245, 247, 250));
        actionsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        
        String[] actions = {"Add Product", "New Sale", "Generate Report", "Manage Users"};
        Color[] actionColors = {
            new Color(40, 167, 69),
            new Color(0, 120, 215),
            new Color(255, 193, 7),
            new Color(108, 117, 125)
        };
        
        for (int i = 0; i < actions.length; i++) {
            JButton button = new JButton(actions[i]);
            button.setBackground(actionColors[i]);
            button.setForeground(Color.WHITE);
            button.setFocusable(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(150, 40));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            final int index = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String userRole = dashboard.currentUserRole;
                    switch (index) {
                        case 0: // Add Product - Inventory
                            // Admin, Manager, Supervisor, Inventory Manager only
                            if (userRole.equals("Marketing") || userRole.equals("User") || 
                                userRole.equals("Cashier") || userRole.equals("HR Manager") || 
                                userRole.equals("Accountant")) {
                                JOptionPane.showMessageDialog(panel, 
                                    "Access Denied: You don't have permission to access Inventory Module", 
                                    "Access Denied", 
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            dashboard.cardLayout.show(dashboard.contentPanel, "inventory");
                            break;
                        case 1: // New Sale - Sales
                            // All roles have access
                            dashboard.cardLayout.show(dashboard.contentPanel, "sales");
                            break;
                        case 2: // Generate Report - Reports
                            // Admin, Manager, Supervisor, Marketing, Accountant only
                            if (userRole.equals("User") || userRole.equals("Cashier") || 
                                userRole.equals("Inventory Manager") || userRole.equals("HR Manager")) {
                                JOptionPane.showMessageDialog(panel, 
                                    "Access Denied: You don't have permission to access Reports Module", 
                                    "Access Denied", 
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            dashboard.cardLayout.show(dashboard.contentPanel, "reports");
                            break;
                        case 3: // Manage Users - Admin
                            // Admin and HR Manager only
                            if (!userRole.equals("Admin") && !userRole.equals("HR Manager")) {
                                JOptionPane.showMessageDialog(panel, 
                                    "Access Denied: You don't have permission to access Admin Module", 
                                    "Access Denied", 
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            dashboard.cardLayout.show(dashboard.contentPanel, "admin");
                            break;
                    }
                }
            });
            
            actionsPanel.add(button);
        }
        
        return actionsPanel;
    }
}
