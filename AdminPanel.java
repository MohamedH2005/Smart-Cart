import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable userTable;
    DefaultTableModel tableModel;
    JTextField usernameField, roleField;
    JPasswordField passwordField;
    IDandPassword idPassword;

    public AdminPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        this.idPassword = new IDandPassword();
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Administrative Module", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        panel.add(header, BorderLayout.NORTH);
        panel.add(createFormPanel(), BorderLayout.WEST);
        panel.add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.setPreferredSize(new Dimension(350, 0));

        JLabel title = new JLabel("Manage Users", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        roleField = new JTextField(20);

        fields.add(Box.createVerticalStrut(5));
        fields.add(row("Username:", usernameField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(row("Password:", passwordField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(row("Role:", roleField));
        fields.add(Box.createVerticalStrut(5));

        JPanel btns = new JPanel(new GridLayout(2, 3, 8, 8));
        btns.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton removeBtn = new JButton("Remove");
        JButton deleteBtn = new JButton("Delete");
        JButton toggleBtn = new JButton("Toggle Status");
        JButton clearBtn = new JButton("Clear");
        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        removeBtn.addActionListener(e -> deleteUser());
        deleteBtn.addActionListener(e -> deleteUser());
        toggleBtn.addActionListener(e -> toggleUserStatus());
        clearBtn.addActionListener(e -> clearFields());
        btns.add(addBtn); btns.add(updateBtn); btns.add(removeBtn);
        btns.add(deleteBtn); btns.add(toggleBtn); btns.add(clearBtn);

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
        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> searchUsers(searchField.getText().trim()));
        top.add(searchField); top.add(searchBtn);

        tableModel = new DefaultTableModel(new String[]{"Username", "Role", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addSampleUsers();

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(userTable), BorderLayout.CENTER);
        return p;
    }
 
    private JPanel row(String label, JComponent comp) { // Helper to create a label+field row
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(100, 25));
        row.add(lbl);
        row.add(comp);
        return row;
    }

    private void addSampleUsers() { // Add some default users for demonstration
        String[][] data = {
            {"admin", "Admin", "Active"},
            {"manager", "Manager", "Active"},
            {"marketing", "Marketing", "Active"},
            {"user1", "User", "Active"}
        };
        for (String[] row : data) tableModel.addRow(row);
    }

    private void addUser() {
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());
        String role = roleField.getText().trim();
        if (user.isEmpty() || pass.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        for (int i = 0; i < tableModel.getRowCount(); i++)// Check if user already exists
            if (tableModel.getValueAt(i, 0).equals(user)) {
                JOptionPane.showMessageDialog(panel, "User exists"); return;
            }
        tableModel.addRow(new Object[]{user, role, "Active"});
        idPassword.addUser(user, pass, role); // Save to file
        clearFields();
    }

    private void updateUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) return;
        String user = tableModel.getValueAt(row, 0).toString();
        tableModel.setValueAt(usernameField.getText().trim(), row, 0);// Update username in table
        tableModel.setValueAt(roleField.getText().trim(), row, 1);
        idPassword.updateUser(user, roleField.getText().trim()); // Save to file
        clearFields();
    }

    private void deleteUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) return;
        String user = tableModel.getValueAt(row, 0).toString();
        if (JOptionPane.showConfirmDialog(panel, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            idPassword.deleteUser(user); // Save to file
            clearFields();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleField.setText("");
        userTable.clearSelection();
    }

    private void searchUsers(String q) {
        if (q.isEmpty()) { tableModel.setRowCount(0); addSampleUsers(); return; }
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--)
            if (!tableModel.getValueAt(i, 0).toString().toLowerCase().contains(q.toLowerCase()))
                tableModel.removeRow(i);
    }

    private void toggleUserStatus() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(panel, "Select a user first");
            return;
        }
        String currentStatus = tableModel.getValueAt(row, 2).toString();
        String newStatus = currentStatus.equals("Active") ? "Inactive" : "Active";
        tableModel.setValueAt(newStatus, row, 2);
        JOptionPane.showMessageDialog(panel, "User status changed to: " + newStatus);
    }
}