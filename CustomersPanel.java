import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomersPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable customerTable;
    DefaultTableModel tableModel;
    JTextField nameField, emailField, phoneField, searchField;
    JTextArea notesArea;

    public CustomersPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Customer Management", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JPanel main = new JPanel(new BorderLayout());
        main.add(createFormPanel(), BorderLayout.WEST);
        main.add(createTablePanel(), BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(main, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        p.setPreferredSize(new Dimension(370, 0));

        JLabel title = new JLabel("Customer Details", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel fields = new JPanel();
        fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
        fields.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        nameField = new JTextField(22);
        emailField = new JTextField(22);
        phoneField = new JTextField(22);
        notesArea = new JTextArea(3, 22);

        fields.add(Box.createVerticalStrut(5));
        fields.add(rowPanel("Name:", nameField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(rowPanel("Email:", emailField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(rowPanel("Phone:", phoneField));
        fields.add(Box.createVerticalStrut(8));
        fields.add(rowPanel("Notes:", new JScrollPane(notesArea)));
        fields.add(Box.createVerticalStrut(5));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        clearBtn.addActionListener(e -> clearFields());
        btns.add(addBtn); btns.add(updateBtn); btns.add(deleteBtn); btns.add(clearBtn);

        p.add(title, BorderLayout.NORTH);
        p.add(fields, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private JPanel rowPanel(String label, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(80, 25));
        row.add(lbl);
        row.add(comp);
        return row;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        top.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton listBtn = new JButton("List All");
        searchBtn.addActionListener(e -> searchCustomers());
        listBtn.addActionListener(e -> listAllCustomers());
        top.add(searchField); top.add(searchBtn); top.add(listBtn);

        tableModel = new DefaultTableModel(new String[]{"ID","Name","Email","Phone","Orders","Spent","Registered","Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        customerTable = new JTable(tableModel);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerTable.setRowHeight(25);

        addSampleCustomers();

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(customerTable), BorderLayout.CENTER);
        return p;
    }

    private void addSampleCustomers() {
        // Try to load customers from file first
        java.util.ArrayList<String[]> loadedCustomers = FileHandler.loadCustomers();
        if (loadedCustomers != null && !loadedCustomers.isEmpty()) {
            for (String[] row : loadedCustomers) tableModel.addRow(row);
            System.out.println("Loaded " + loadedCustomers.size() + " customers from file");
        } else {
            // Default customers if file doesn't exist
            String[][] data = {
                {"C001","John Smith","john@email.com","+123","15","$1234","2024-01-01","Active"},
                {"C002","Sarah Johnson","sarah@email.com","+124","8","$567","2024-01-05","Active"},
                {"C003","Michael Brown","michael@email.com","+125","23","$2345","2024-01-10","VIP"},
                {"C004","Emily Davis","emily@email.com","+126","5","$234","2024-01-15","Active"},
                {"C005","Robert Wilson","robert@email.com","+127","12","$890","2024-01-20","Active"}
            };
            for (String[] row : data) tableModel.addRow(row);
            
            // Save default customers to file
            saveCustomersToFile();
        }
    }
    
    private void saveCustomersToFile() {
        java.util.ArrayList<String[]> customers = new java.util.ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String[] row = new String[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j).toString();
            }
            customers.add(row);
        }
        FileHandler.saveCustomers(customers);
    }

    private void addCustomer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        String id = "C" + String.format("%03d", tableModel.getRowCount() + 1);
        tableModel.addRow(new Object[]{id, name, email, phone, "0", "$0.00", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "Active"});
        clearFields();
        saveCustomersToFile(); // Save to file after adding
    }

    private void updateCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) return;
        tableModel.setValueAt(nameField.getText().trim(), row, 1);
        tableModel.setValueAt(emailField.getText().trim(), row, 2);
        tableModel.setValueAt(phoneField.getText().trim(), row, 3);
        clearFields();
        saveCustomersToFile(); // Save to file after updating
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) return;
        if (JOptionPane.showConfirmDialog(panel, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            clearFields();
            saveCustomersToFile(); // Save to file after deleting
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        notesArea.setText("");
        customerTable.clearSelection();
    }

    private void searchCustomers() {
        String q = searchField.getText().trim().toLowerCase();
        if (q.isEmpty()) return;
        listAllCustomers();
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            boolean match = false;
            for (int j = 0; j < tableModel.getColumnCount(); j++)
                if (tableModel.getValueAt(i, j).toString().toLowerCase().contains(q)) { match = true; break; }
            if (!match) tableModel.removeRow(i);
        }
    }

    private void listAllCustomers() {
        tableModel.setRowCount(0);
        addSampleCustomers();
    }
}