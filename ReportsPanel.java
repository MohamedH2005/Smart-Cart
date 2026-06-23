import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable reportsTable;
    DefaultTableModel tableModel;
    JTextArea reportArea;
    JComboBox<String> reportTypeCombo, periodCombo;
    JTextField startDateField, endDateField;

    public ReportsPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Reports & Analytics", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setTopComponent(createGeneratePanel());
        split.setBottomComponent(createDisplayPanel());
        split.setDividerLocation(250);

        panel.add(header, BorderLayout.NORTH);
        panel.add(split, BorderLayout.CENTER);
    }

    private JPanel createGeneratePanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Generate Report", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        reportTypeCombo = new JComboBox<>(new String[]{"Sales", "Inventory", "Customer", "Financial", "Employee", "Product"});
        periodCombo = new JComboBox<>(new String[]{"Today", "This Week", "This Month", "This Year", "Custom"});
        periodCombo.addActionListener(e -> updateDates());

        startDateField = new JTextField("2024-01-01", 18);
        endDateField = new JTextField("2024-12-31", 18);

        form.add(Box.createVerticalStrut(5));
        form.add(row("Type:", reportTypeCombo));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Period:", periodCombo));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Start:", startDateField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("End:", endDateField));
        form.add(Box.createVerticalStrut(5));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton genBtn = new JButton("Generate");
        JButton clearBtn = new JButton("Clear");
        genBtn.addActionListener(e -> generateReport());
        clearBtn.addActionListener(e -> reportArea.setText(""));
        btns.add(genBtn); btns.add(clearBtn);

        p.add(title, BorderLayout.NORTH);
        p.add(form, BorderLayout.CENTER);
        p.add(btns, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createDisplayPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        JTabbedPane tabs = new JTabbedPane();
        reportArea = new JTextArea(15, 50);
        reportArea.setEditable(false);
        tabs.addTab("Report", new JScrollPane(reportArea));

        tableModel = new DefaultTableModel(new String[]{"ID", "Type", "Date", "By", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        reportsTable = new JTable(tableModel);
        reportsTable.setRowHeight(25);
        addSampleHistory();
        tabs.addTab("History", new JScrollPane(reportsTable));

        JButton exportBtn = new JButton("Export");
        exportBtn.addActionListener(e -> exportReport());

        p.add(tabs, BorderLayout.CENTER);
        p.add(exportBtn, BorderLayout.SOUTH);
        return p;
    }

    private JPanel row(String label, JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(60, 25));
        row.add(lbl);
        row.add(comp);
        return row;
    }

    private void updateDates() {
        String period = (String) periodCombo.getSelectedItem();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        switch (period) {
            case "Today": startDateField.setText(today); endDateField.setText(today); break;
            case "This Month": startDateField.setText("2024-01-01"); endDateField.setText("2024-01-31"); break;
            case "This Year": startDateField.setText("2024-01-01"); endDateField.setText("2024-12-31"); break;
        }
    }

    private void generateReport() {
        String type = (String) reportTypeCombo.getSelectedItem();
        String period = (String) periodCombo.getSelectedItem();
        String start = startDateField.getText().trim();
        String end = endDateField.getText().trim();

        StringBuilder sb = new StringBuilder();
        sb.append("=== SMARTCART ").append(type.toUpperCase()).append(" REPORT ===\n");
        sb.append("Period: ").append(period).append(" (").append(start).append(" to ").append(end).append(")\n");
        sb.append("Generated: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())).append("\n\n");
        sb.append("Today Sales: $").append(String.format("%.2f", dashboard.getTodaySales())).append("\n");
        sb.append("Total Revenue: $").append(String.format("%.2f", dashboard.getTotalRevenue())).append("\n");
        sb.append("Products: ").append(dashboard.getTotalProducts()).append("\n");
        sb.append("Low Stock: ").append(dashboard.getLowStockItems()).append("\n");
        sb.append("Active Users: ").append(dashboard.getActiveUsers()).append("\n");
        sb.append("Pending Orders: ").append(dashboard.getPendingOrders()).append("\n");

        reportArea.setText(sb.toString());
        tableModel.insertRow(0, new Object[]{"RPT" + String.format("%03d", tableModel.getRowCount() + 1), type, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), "User", "Done"});
    }

    private void exportReport() {
        if (reportArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panel, "No report to export"); return;
        }
        try {
            String name = "report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt";
            FileWriter fw = new FileWriter(name);
            fw.write(reportArea.getText());
            fw.close();
            JOptionPane.showMessageDialog(panel, "Saved: " + name);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panel, "Export failed");
        }
    }

    private void addSampleHistory() {
        String[][] data = {
            {"RPT001", "Sales", "2024-01-15 09:30", "admin", "Done"},
            {"RPT002", "Inventory", "2024-01-15 10:15", "manager", "Done"},
            {"RPT003", "Customer", "2024-01-15 11:00", "admin", "Done"},
            {"RPT004", "Financial", "2024-01-15 14:30", "admin", "Done"}
        };
        for (String[] row : data) tableModel.addRow(row);
    }
}