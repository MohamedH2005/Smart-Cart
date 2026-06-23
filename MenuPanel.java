import javax.swing.*;
import java.awt.*;

public class MenuPanel {
    Dashboard dashboard;
    JPanel panel;

    public MenuPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Main Menu", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        content.add(createWelcomePanel());
        content.add(Box.createVerticalStrut(20));
        content.add(createQuickAccessPanel());
        content.add(Box.createVerticalStrut(20));
        content.add(createSystemInfoPanel());

        panel.add(header, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
    }

    private JPanel createWelcomePanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("Welcome to SmartCart Hyper Market System");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(label);
        return p;
    }

    private JPanel createQuickAccessPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Quick Access");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        String role = dashboard.currentUserRole;

        buttons.add(moduleBtn("Admin", () -> {
            // Admin and HR Manager only
            if (role.equals("Admin") || role.equals("HR Manager")) 
                dashboard.cardLayout.show(dashboard.contentPanel, "admin");
            else accessDenied();
        }));
        buttons.add(moduleBtn("Marketing", () -> {
            // Admin, Manager, Marketing only
            if (role.equals("Admin") || role.equals("Manager") || role.equals("Marketing")) 
                dashboard.cardLayout.show(dashboard.contentPanel, "marketing");
            else accessDenied();
        }));
        buttons.add(moduleBtn("Inventory", () -> {
            // Admin, Manager, Supervisor, Inventory Manager only
            if (role.equals("Admin") || role.equals("Manager") || role.equals("Supervisor") || role.equals("Inventory Manager")) 
                dashboard.cardLayout.show(dashboard.contentPanel, "inventory");
            else accessDenied();
        }));
        buttons.add(moduleBtn("Sales", () -> {
            // All roles have access
            dashboard.cardLayout.show(dashboard.contentPanel, "sales");
        }));
        buttons.add(moduleBtn("Profile", () -> {
            // All roles have access
            dashboard.cardLayout.show(dashboard.contentPanel, "profile");
        }));
        buttons.add(moduleBtn("Reports", () -> {
            // Admin, Manager, Supervisor, Marketing, Accountant only
            if (role.equals("Admin") || role.equals("Manager") || role.equals("Supervisor") || 
                role.equals("Marketing") || role.equals("Accountant")) 
                dashboard.cardLayout.show(dashboard.contentPanel, "reports");
            else accessDenied();
        }));

        p.add(title, BorderLayout.NORTH);
        p.add(buttons, BorderLayout.CENTER);
        return p;
    }

    private JButton moduleBtn(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e -> action.run());
        btn.setPreferredSize(new Dimension(150, 80));
        return btn;
    }

    private void accessDenied() {
        JOptionPane.showMessageDialog(panel, "Access Denied");
    }

    private JPanel createSystemInfoPanel() {// This panel shows some dummy system info like active users, today's sales, etc.
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        p.add(infoCard("Status", "Online"));
        p.add(infoCard("Users", String.valueOf(dashboard.getActiveUsers())));
        p.add(infoCard("Today", "$" + String.format("%.2f", dashboard.getTodaySales())));
        p.add(infoCard("Uptime", "99.9%"));
        return p;
    }

    private JPanel infoCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(150, 60));
        JLabel t = new JLabel(title, JLabel.CENTER);
        JLabel v = new JLabel(value, JLabel.CENTER);
        v.setFont(new Font("Arial", Font.BOLD, 18));
        card.add(t, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}