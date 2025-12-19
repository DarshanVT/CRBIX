package dairyProducts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class MobileRechargeApp extends JFrame {

    JTextField txtMobile;
    JComboBox<String> cbOperator, cbCircle, cbRechargeType;
    JLabel lblWallet, lblPlanDetails;
    JTable planTable, historyTable;
    DefaultTableModel planModel, historyModel;

    double walletBalance = 0;

    public MobileRechargeApp() {

        setTitle("Mobile Recharge System");
        setSize(720, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new GradientPanel();
        panel.setLayout(null);
        add(panel);

        JLabel heading = new JLabel("📱 Mobile Recharge Application");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setBounds(180, 15, 400, 30);
        panel.add(heading);

        lblWallet = new JLabel("💼 Wallet: ₹" + walletBalance);
        lblWallet.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblWallet.setBounds(460, 55, 180, 25);
        panel.add(lblWallet);

        JButton btnAddMoney = new JButton("➕ Add Money");
        btnAddMoney.setBounds(460, 85, 140, 30);
        btnAddMoney.setBackground(new Color(0, 153, 76));
        btnAddMoney.setForeground(Color.WHITE);
        panel.add(btnAddMoney);

        panel.add(label("📞 Mobile Number", 40, 70));
        txtMobile = field(220, 70);
        panel.add(txtMobile);

        panel.add(label("📡 Sim Card Operator", 40, 110));
        cbOperator = combo(new String[]{"Select", "Airtel", "Jio", "VI", "BSNL"}, 220, 110);
        panel.add(cbOperator);

        panel.add(label("🌍 City", 40, 150));
        cbCircle = combo(new String[]{"Select", "Delhi", "Maharashtra", "Gujarat", "UP"}, 220, 150);
        panel.add(cbCircle);

        panel.add(label("🔁 Recharge Type", 40, 190));
        cbRechargeType = combo(new String[]{"Select", "Unlimited", "Data", "Cricket Plans"}, 220, 190);
        panel.add(cbRechargeType);

        String[] cols = {"💰 Price", "📊 Data", "⏳ Validity", "📞 Calls", "🎁 Extras"};
        planModel = new DefaultTableModel(cols, 0);
        planTable = new JTable(planModel);
        planTable.setRowHeight(35);
        planTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane planScroll = new JScrollPane(planTable);
        planScroll.setBounds(40, 240, 630, 150);
        panel.add(planScroll);

        lblPlanDetails = new JLabel("✨ Select a plan to see details");
        lblPlanDetails.setOpaque(true);
        lblPlanDetails.setBackground(Color.WHITE);
        lblPlanDetails.setBounds(40, 405, 630, 80);
        lblPlanDetails.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblPlanDetails);

        JButton btnRecharge = new JButton("⚡ Recharge Now");
        btnRecharge.setBounds(100, 500, 180, 45);
        btnRecharge.setBackground(new Color(33, 150, 243));
        btnRecharge.setForeground(Color.WHITE);
        panel.add(btnRecharge);
        
        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(400, 500, 180, 45);
        btnReset.setBackground(new Color(33, 150, 243));
        btnReset.setForeground(Color.WHITE);
        panel.add(btnReset);

        JLabel historyLbl = new JLabel("📜 Recharge History");
        historyLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        historyLbl.setBounds(40, 555, 200, 25);
        panel.add(historyLbl);

        String[] hCols = {"Mobile", "Type", "Amount", "Validity", "Date"};
        historyModel = new DefaultTableModel(hCols, 0);
        historyTable = new JTable(historyModel);
        JScrollPane historyScroll = new JScrollPane(historyTable);
        historyScroll.setBounds(40, 585, 630, 140);
        panel.add(historyScroll);

        cbRechargeType.addActionListener(e -> loadPlans());

        planTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = planTable.getSelectedRow();
                lblPlanDetails.setText("<html><b>✨ Plan Details</b><br>"
                        + planTable.getValueAt(r, 0) + " | "
                        + planTable.getValueAt(r, 1) + " | "
                        + planTable.getValueAt(r, 2) + "<br>"
                        + "📞 " + planTable.getValueAt(r, 3) + " | 🎁 "
                        + planTable.getValueAt(r, 4)
                        + "</html>");
            }
        });

        btnRecharge.addActionListener(e -> doRecharge());
        btnReset.addActionListener(e -> reset());
        btnAddMoney.addActionListener(e -> openAddMoneyWindow());
    }
    void loadPlans() {
        planModel.setRowCount(0);
        String type = cbRechargeType.getSelectedItem().toString();

        if (type.equals("Unlimited")) {
            addPlan("₹379", "2GB/day", "1 Month", "Unlimited", "5G + OTT");
            addPlan("₹349", "2GB/day", "28 Days", "Unlimited", "5G");
            addPlan("₹398", "2GB/day", "28 Days", "Unlimited", "OTT Pack");
        }
        else if (type.equals("Data")) {
            addPlan("₹199", "1.5GB/day", "28 Days", "NA", "Data Only");
            addPlan("₹299", "2GB/day", "28 Days", "NA", "Data Booster");
            addPlan("₹599", "1GB/day", "84 Days", "NA", "Data Only");
        }
        else if (type.equals("Cricket Plans")) {
            addPlan("₹299", "2GB/day", "28 Days", "Unlimited", "Live Cricket");
            addPlan("₹499", "3GB/day", "56 Days", "Unlimited", "World Cup Pack");
            addPlan("₹1099", "3GB/day", "1 Year", "Unlimited", "All Cricket Matches");
        }
    }

    void addPlan(String p, String d, String v, String c, String e) {
        planModel.addRow(new Object[]{p, d, v, c, e});
    }

    void doRecharge() {
        try {
            if (!txtMobile.getText().matches("\\d{10}"))
                throw new Exception("Mobile number must be exactly 10 digits");

            int row = planTable.getSelectedRow();
            if (row == -1) throw new Exception("Select a plan");

            double amount = Double.parseDouble(planTable.getValueAt(row, 0).toString().replace("₹", ""));

            if (walletBalance < amount) throw new Exception("Insufficient balance");

            walletBalance -= amount;
            lblWallet.setText("💼 Wallet: ₹" + walletBalance);

            historyModel.addRow(new Object[]{
                    txtMobile.getText(),
                    cbRechargeType.getSelectedItem(),
                    "₹" + amount,
                    planTable.getValueAt(row, 2),
                    LocalDateTime.now().toString()
            });

            JOptionPane.showMessageDialog(this, "Recharge Successful ✔");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    void reset() {
        txtMobile.setText("");
        cbOperator.setSelectedIndex(0);
        cbCircle.setSelectedIndex(0);
        cbRechargeType.setSelectedIndex(0);
        planModel.setRowCount(0);
        lblPlanDetails.setText("Select a plan to see details");
    }
    void openAddMoneyWindow() {
        JFrame f = new JFrame("Add Money");
        f.setSize(300, 180);
        f.setLayout(null);
        f.setLocationRelativeTo(this);

        JLabel l = new JLabel("Enter Amount:");
        l.setBounds(30, 30, 100, 25);
        f.add(l);

        JTextField t = new JTextField();
        t.setBounds(130, 30, 120, 25);
        f.add(t);

        JButton b = new JButton("Add");
        b.setBounds(90, 80, 100, 30);
        f.add(b);

        b.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(t.getText());
                if (amt <= 0) throw new Exception();
                walletBalance += amt;
                lblWallet.setText("💼 Wallet: ₹" + walletBalance);
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Enter valid amount");
            }
        });

        f.setVisible(true);
    }

    JLabel label(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 160, 25);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    JTextField field(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 200, 25);
        return f;
    }

    JComboBox<String> combo(String[] v, int x, int y) {
        JComboBox<String> c = new JComboBox<>(v);
        c.setBounds(x, y, 200, 25);
        return c;
    }

    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(230, 245, 255),
                    0, getHeight(), Color.WHITE));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        new MobileRechargeApp().setVisible(true);
    }
}
