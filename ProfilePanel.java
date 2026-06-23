import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfilePanel {
    Dashboard dashboard;
    JPanel panel;
    JTextField usernameField, emailField, phoneField, addressField;
    JPasswordField currentPasswordField, newPasswordField, confirmPasswordField;
    JTable historyTable;
    DefaultTableModel historyModel;

    public ProfilePanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("User Profile", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JTabbedPane tabs = new JTabbedPane(); // Create tabs for Profile, Password, and History
        tabs.addTab("Profile", createProfilePanel());
        tabs.addTab("Password", createPasswordPanel());
        tabs.addTab("History", createHistoryPanel());

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameField = new JTextField(dashboard.getCurrentUsername(), 22);
        usernameField.setEditable(false);
        emailField = new JTextField("admin@smartcart.com", 22);
        phoneField = new JTextField("+1234567890", 22);
        addressField = new JTextField("123 Market Street", 22);

        form.add(Box.createVerticalStrut(5));
        form.add(row("Username:", usernameField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Email:", emailField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Phone:", phoneField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Address:", addressField));
        form.add(Box.createVerticalStrut(5));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton updateBtn = new JButton("Update");
        JButton clearBtn = new JButton("Clear");
        updateBtn.addActionListener(e -> updateProfile());
        clearBtn.addActionListener(e -> { emailField.setText(""); phoneField.setText(""); addressField.setText(""); });
        btns.add(updateBtn); btns.add(clearBtn);

        p.add(form, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createPasswordPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        currentPasswordField = new JPasswordField(22);
        newPasswordField = new JPasswordField(22);
        confirmPasswordField = new JPasswordField(22);

        form.add(Box.createVerticalStrut(5));
        form.add(row("Current:", currentPasswordField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("New:", newPasswordField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Confirm:", confirmPasswordField));
        form.add(Box.createVerticalStrut(5));
        form.add(new JLabel("Min 8 chars, upper, lower, number"));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton changeBtn = new JButton("Change");
        JButton clearBtn = new JButton("Clear");
        changeBtn.addActionListener(e -> changePassword());
        clearBtn.addActionListener(e -> { currentPasswordField.setText(""); newPasswordField.setText(""); confirmPasswordField.setText(""); });
        btns.add(changeBtn); btns.add(clearBtn);

        p.add(form, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Action History"));
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Refreshed"));
        top.add(refreshBtn);

        historyModel = new DefaultTableModel(new String[]{"Date", "Action", "Module", "Details", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        historyTable = new JTable(historyModel);
        historyTable.setRowHeight(25);

        addSampleHistory();

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        return p;
    }

    private JPanel row(String label, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(90, 25));
        row.add(lbl);
        row.add(comp);
        return row;
    }

    private void addSampleHistory() {
        String[][] data = {
            {"2024-01-15 09:30", "Login", "Auth", "Logged in", "Success"},
            {"2024-01-15 09:35", "View", "Dashboard", "Opened home", "Success"},
            {"2024-01-15 09:40", "Add", "Inventory", "Added Milk", "Success"},
            {"2024-01-15 09:50", "Create", "Sales", "Order ORD001", "Success"},
            {"2024-01-15 10:10", "Change", "Security", "Changed password", "Success"}
        };
        for (String[] row : data) historyModel.addRow(row);
    }

    private void updateProfile() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        if (email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        JOptionPane.showMessageDialog(panel, "Profile updated");
        historyModel.insertRow(0, new Object[]{new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "Update", "Profile", "Profile updated", "Success"});
    }

    private void changePassword() {
        String curr = new String(currentPasswordField.getPassword());
        String newP = new String(newPasswordField.getPassword());
        String conf = new String(confirmPasswordField.getPassword());
        if (curr.isEmpty() || newP.isEmpty() || conf.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        if (newP.length() < 8 || !newP.matches(".*[A-Z].*") || !newP.matches(".*[a-z].*") || !newP.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(panel, "Password must be 8+ chars with upper, lower, number"); return;
        }
        if (!newP.equals(conf)) {
            JOptionPane.showMessageDialog(panel, "Passwords don't match"); return;
        }
        JOptionPane.showMessageDialog(panel, "Password changed");
        currentPasswordField.setText(""); newPasswordField.setText(""); confirmPasswordField.setText("");
        historyModel.insertRow(0, new Object[]{new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "Change", "Security", "Password changed", "Success"});
    }
}