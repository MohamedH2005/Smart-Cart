import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MarketingPanel {
    Dashboard dashboard;
    JPanel panel;
    JTable reportsTable, offersTable;
    DefaultTableModel reportsModel, offersModel;
    JTextArea reportArea;
    JTextField offerTitleField, discountField;

    public MarketingPanel(Dashboard dashboard) {
        this.dashboard = dashboard;
        panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Marketing Module", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Reports", createReportsPanel());
        tabs.addTab("Offers", createOffersPanel());

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);
    }

    private JPanel createReportsPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Sales", "Inventory", "Customer", "Product"});
        typeCombo.setPreferredSize(new Dimension(160, 25));
        JTextField startField = new JTextField("2024-01-01", 16);
        JTextField endField = new JTextField("2024-12-31", 16);

        top.add(Box.createVerticalStrut(5));
        top.add(row("Type:", typeCombo));
        top.add(Box.createVerticalStrut(8));
        top.add(row("From:", startField));
        top.add(Box.createVerticalStrut(8));
        top.add(row("To:", endField));
        top.add(Box.createVerticalStrut(8));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton genBtn = new JButton("Generate");
        JButton clearBtn = new JButton("Clear");
        genBtn.addActionListener(e -> generateReport((String) typeCombo.getSelectedItem(), startField.getText(), endField.getText()));
        clearBtn.addActionListener(e -> reportArea.setText(""));
        btns.add(genBtn); btns.add(clearBtn);
        top.add(btns);

        reportArea = new JTextArea(8, 30);
        reportArea.setEditable(false);

        reportsModel = new DefaultTableModel(new String[]{"ID", "Type", "Date", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        reportsTable = new JTable(reportsModel);
        addSampleReports();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(reportArea), new JScrollPane(reportsTable));
        split.setDividerLocation(150);

        p.add(top, BorderLayout.NORTH);
        p.add(split, BorderLayout.CENTER);
        return p;
    }

    private JPanel createOffersPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        offerTitleField = new JTextField(18);
        discountField = new JTextField(18);

        form.add(Box.createVerticalStrut(5));
        form.add(row("Title:", offerTitleField));
        form.add(Box.createVerticalStrut(8));
        form.add(row("Discount %:", discountField));
        form.add(Box.createVerticalStrut(8));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton createBtn = new JButton("Create");
        JButton sendBtn = new JButton("Send to Inventory");
        createBtn.addActionListener(e -> createOffer());
        sendBtn.addActionListener(e -> sendOffer());
        btns.add(createBtn); btns.add(sendBtn);
        form.add(btns);

        offersModel = new DefaultTableModel(new String[]{"ID", "Title", "Discount", "Valid", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        offersTable = new JTable(offersModel);
        addSampleOffers();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, new JScrollPane(offersTable));
        split.setDividerLocation(120);

        p.add(split, BorderLayout.CENTER);
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

    private void addSampleReports() {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        reportsModel.addRow(new Object[]{"RPT001", "Sales", now, "Done"});
        reportsModel.addRow(new Object[]{"RPT002", "Inventory", now, "Done"});
    }

    private void addSampleOffers() {
        offersModel.addRow(new Object[]{"OFF001", "Summer Sale", "20%", "2024-08-31", "Active"});
        offersModel.addRow(new Object[]{"OFF002", "Weekend", "15%", "2024-07-31", "Active"});
    }

    private void generateReport(String type, String start, String end) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(type.toUpperCase()).append(" REPORT ===\n");
        sb.append("Period: ").append(start).append(" to ").append(end).append("\n");
        sb.append("Generated: ").append(now).append("\n\n");
        sb.append("Today Sales: $").append(String.format("%.2f", dashboard.getTodaySales())).append("\n");
        sb.append("Total Revenue: $").append(String.format("%.2f", dashboard.getTotalRevenue())).append("\n");
        sb.append("Products: ").append(dashboard.getTotalProducts()).append("\n");
        sb.append("Low Stock: ").append(dashboard.getLowStockItems()).append("\n");

        reportArea.setText(sb.toString());
        reportsModel.addRow(new Object[]{"RPT" + String.format("%03d", reportsModel.getRowCount() + 1), type, now, "Done"});
    }

    private void createOffer() {
        String title = offerTitleField.getText().trim();
        String discount = discountField.getText().trim();
        if (title.isEmpty() || discount.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Fill all fields"); return;
        }
        try {
            int d = Integer.parseInt(discount);
            if (d <= 0 || d > 100) { JOptionPane.showMessageDialog(panel, "1-100 only"); return; }
            offersModel.addRow(new Object[]{"OFF" + String.format("%03d", offersModel.getRowCount() + 1), title, d + "%", "2024-12-31", "Draft"});
            offerTitleField.setText("");
            discountField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Invalid discount");
        }
    }

    private void sendOffer() {
        int row = offersTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(panel, "Select an offer"); return; }
        offersModel.setValueAt("Sent", row, 4);
        JOptionPane.showMessageDialog(panel, "Offer sent to Inventory");
    }
}