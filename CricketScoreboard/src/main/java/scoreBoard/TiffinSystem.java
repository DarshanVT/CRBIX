package scoreBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TiffinSystem extends JFrame { 
	
    static class Order {
        String customerName;
        String phone;
        String address;
        String planType;      
        boolean breakfast;
        boolean lunch;
        boolean dinner;
        int durationDays;     
        double totalPrice;
        String startDate;     
        String payment;       

        Order() {}

        String toLine() {
            return escape(customerName) + "|" + escape(phone) + "|" + escape(address) + "|" + planType + "|"
                    + (breakfast ? "1" : "0") + "|" + (lunch ? "1" : "0") + "|" + (dinner ? "1" : "0") + "|"
                    + durationDays + "|" + totalPrice + "|" + escape(startDate) + "|" + payment;
        }
        
        static Order fromLine(String line) {
            try {
                String[] p = line.split("\\|", 11);
                Order o = new Order();
                o.customerName = unescape(p[0]);
                o.phone = unescape(p[1]);
                o.address = unescape(p[2]);
                o.planType = p[3];
                o.breakfast = p[4].equals("1");
                o.lunch = p[5].equals("1");
                o.dinner = p[6].equals("1");
                o.durationDays = Integer.parseInt(p[7]);
                o.totalPrice = Double.parseDouble(p[8]);
                o.startDate = unescape(p[9]);
                o.payment = p[10];
                return o;
            } catch (Exception ex) {
                return null;
            }
        }

        static String escape(String s) {
            if (s == null) return "";
            return s.replace("|", " ");
        }

        static String unescape(String s) {
            if (s == null) return "";
            return s;
        }

        @Override
        public String toString() {
            String meals = (breakfast ? "B " : "") + (lunch ? "L " : "") + (dinner ? "D " : "");
            return String.format("%s | %s | %s | %s | %s | %dd | Rs %.0f | %s",
                    customerName, phone, planType, meals.trim(), startDate, durationDays, totalPrice, payment);
        }
    }
    
    private static final double PRICE_BREAKFAST = 40; 
    private static final double PRICE_LUNCH = 60;
    private static final double PRICE_DINNER = 60;
    
    java.util.List<Order> orders = new ArrayList<>();
    Map<String, Integer> customPlans = new LinkedHashMap<>(); 
    
    private final File ordersFile = new File("tiffin_orders.txt");
    private final File plansFile = new File("tiffin_plans.txt");

    
    JPanel leftMenu, mainPanel;

    
    JComboBox<String> planComboGlobal; 

    JTextArea viewArea;

    public TiffinSystem() {
        setTitle("Online Tiffin Service");
        setSize(1000, 640);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        loadPlans();
        loadOrders();

        initUI();
    }

    private void initUI() {
        
        leftMenu = new JPanel();
        leftMenu.setLayout(new GridLayout(12, 1, 8, 8));
        leftMenu.setBounds(0, 0, 220, 640);
        leftMenu.setBackground(new Color(50, 120, 60));
        leftMenu.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        String[] items = {
                "New Order",
                "View Orders",
                "Search Order",
                "Update Order",
                "Delete / Management",
                "Monthly Filter",
                "Open Orders File",
                "Add Custom Plan",
                "View Plans",
                "Save All",
                "Exit"
        };

        for (String it : items) {
            JButton b = new JButton(it);
            b.setFont(new Font("SansSerif", Font.BOLD, 13));
            b.setBackground(new Color(240, 255, 240));
            b.addActionListener(e -> onMenuClick(it));
            leftMenu.add(b);
        }

        add(leftMenu);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBounds(220, 0, 760, 640);
        mainPanel.setBackground(new Color(245, 255, 245));
        add(mainPanel);

        showWelcome();
    }

    private void onMenuClick(String item) {
        mainPanel.removeAll();
        switch (item) {
            case "New Order": showCustomerBooking(); break;
            case "View Orders": showOrders(); break;
            case "Search Order": showSearch(); break;
            case "Update Order": showUpdateOrder(); break;
            case "Delete Order/Customer": showDeleteManagement(); break;
            case "Monthly Filter": showMonthlyFilter(); break;
            case "Open Orders File": openOrdersFile(); break;
            case "Add Custom Plan": showAddCustomPlan(); break;
            case "View Plans": showPlans(); break;
            case "Save All": saveAll(); JOptionPane.showMessageDialog(this, "Saved!"); break;
            case "Exit": saveAll(); System.exit(0); break;
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void showWelcome() {
        JLabel title = new JLabel("Welcome to Tiffin Service", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setBounds(80, 30, 600, 40);
        mainPanel.add(title);

        JTextArea info = new JTextArea();
        info.setEditable(false);
        info.setFont(new Font("Monospaced", Font.PLAIN, 14));
        info.setText("Instructions:\n\n- Create new order with plan (Daily / Weekly / Monthly / Custom)\n"
                + "- Choose meal shifts: Breakfast (₹40/day), Lunch (₹60/day), Dinner (₹60/day)\n"
                + "- Price auto-calculated based on plan duration and selected shifts\n"
                + "- Manage orders: view, search, update, delete\n"
                + "- Delete customer (removes all their orders) or delete custom plan\n"
                + "- Data auto-saved to tiffin_orders.txt and tiffin_plans.txt\n");
        info.setBounds(60, 90, 640, 300);
        mainPanel.add(info);
    }

    private String[] defaultPlanOptions() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Daily (1 day)");
        list.add("Weekly (7 days)");
        list.add("Monthly (30 days)");
        for (String k : customPlans.keySet()) list.add(k + " (" + customPlans.get(k) + " days)");
        return list.toArray(new String[0]);
    }

    private int planDurationDays(String planText) {
        if (planText.startsWith("Daily")) return 1;
        if (planText.startsWith("Weekly")) return 7;
        if (planText.startsWith("Monthly")) return 30;
        int days = 0;
        int idx1 = planText.indexOf("(");
        int idx2 = planText.indexOf(")");
        if (idx1 >= 0 && idx2 > idx1) {
            String inside = planText.substring(idx1+1, idx2);
            inside = inside.replaceAll("\\D+", ""); // digits only
            try { days = Integer.parseInt(inside); } catch (Exception ex) { days = 0; }
        }
        return days;
    }
    private void showCustomerBooking() {
        int leftX = 40;
        JLabel head = new JLabel("Place New Tiffin Order");
        head.setFont(new Font("SansSerif", Font.BOLD, 20));
        head.setBounds(40, 12, 420, 32);
        mainPanel.add(head);

        JLabel lName = new JLabel("Customer Name:");
        lName.setBounds(leftX, 70, 140, 24);
        mainPanel.add(lName);
        JTextField tfName = new JTextField();
        tfName.setBounds(leftX+150, 70, 280, 26);
        mainPanel.add(tfName);

        JLabel lPhone = new JLabel("Phone:");
        lPhone.setBounds(leftX, 110, 140, 24);
        mainPanel.add(lPhone);
        JTextField tfPhone = new JTextField();
        tfPhone.setBounds(leftX+150, 110, 200, 26);
        mainPanel.add(tfPhone);

        JLabel lAddr = new JLabel("Address:");
        lAddr.setBounds(leftX, 150, 140, 24);
        mainPanel.add(lAddr);
        JTextField tfAddr = new JTextField();
        tfAddr.setBounds(leftX+150, 150, 420, 26);
        mainPanel.add(tfAddr);

        JLabel lPlan = new JLabel("Plan:");
        lPlan.setBounds(leftX, 190, 140, 24);
        mainPanel.add(lPlan);
        JComboBox<String> cbPlan = new JComboBox<>(defaultPlanOptions());
        cbPlan.setBounds(leftX+150, 190, 280, 26);
        mainPanel.add(cbPlan);

        JLabel lStart = new JLabel("Date (DD/MM/YYYY):");
        lStart.setBounds(leftX, 230, 180, 24);
        mainPanel.add(lStart);
        JTextField tfStart = new JTextField();
        tfStart.setBounds(leftX+180, 230, 150, 26);
        mainPanel.add(tfStart);
        
        JLabel lMeals = new JLabel("Meals (choose one or more):");
        lMeals.setBounds(leftX, 270, 220, 24);
        mainPanel.add(lMeals);

        JCheckBox cbBreakfast = new JCheckBox("Breakfast (₹40/day)");
        cbBreakfast.setBounds(leftX+150, 270, 170, 24);
        mainPanel.add(cbBreakfast);

        JCheckBox cbLunch = new JCheckBox("Lunch (₹60/day)");
        cbLunch.setBounds(leftX+330, 270, 140, 24);
        mainPanel.add(cbLunch);

        JCheckBox cbDinner = new JCheckBox("Dinner (₹60/day)");
        cbDinner.setBounds(leftX+470, 270, 140, 24);
        mainPanel.add(cbDinner);

        JLabel lPayment = new JLabel("Payment:");
        lPayment.setBounds(leftX, 310, 120, 24);
        mainPanel.add(lPayment);
        JComboBox<String> cbPayment = new JComboBox<>(new String[]{"Pending", "Paid"});
        cbPayment.setBounds(leftX+150, 310, 140, 26);
        mainPanel.add(cbPayment);

        JLabel lblPrice = new JLabel("Total: Rs. 0");
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPrice.setBounds(leftX+150, 350, 240, 28);
        mainPanel.add(lblPrice);

        JButton btnPlace = new JButton("Place Order");
        btnPlace.setBounds(leftX+350, 350, 150, 34);
        mainPanel.add(btnPlace);

        ActionListener recalc = ev -> {
            String planText = (String) cbPlan.getSelectedItem();
            int days = planDurationDays(planText);
            int mealsPerDay = 0;
            double perDay = 0;
            if (cbBreakfast.isSelected()) { mealsPerDay++; perDay += PRICE_BREAKFAST; }
            if (cbLunch.isSelected()) { mealsPerDay++; perDay += PRICE_LUNCH; }
            if (cbDinner.isSelected()) { mealsPerDay++; perDay += PRICE_DINNER; }
            double total = perDay * days;
            lblPrice.setText(String.format("Total: Rs. %.0f", total));
        };

        cbPlan.addActionListener(recalc);
        cbBreakfast.addActionListener(recalc);
        cbLunch.addActionListener(recalc);
        cbDinner.addActionListener(recalc);

        btnPlace.addActionListener(e -> {
            String name = tfName.getText().trim();
            String phone = tfPhone.getText().trim();
            String addr = tfAddr.getText().trim();
            String planText = (String) cbPlan.getSelectedItem();
            String start = tfStart.getText().trim();
            boolean bk = cbBreakfast.isSelected();
            boolean ln = cbLunch.isSelected();
            boolean dn = cbDinner.isSelected();
            String pay = (String) cbPayment.getSelectedItem();

            if (name.isEmpty() || phone.isEmpty() || addr.isEmpty() || start.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Name, Phone, Address and Start Date.");
                return;
            }
            if (!bk && !ln && !dn) {
                JOptionPane.showMessageDialog(this, "Choose at least one meal (Breakfast/Lunch/Dinner).");
                return;
            }

            int days = planDurationDays(planText);
            double perDay = 0;
            if (bk) perDay += PRICE_BREAKFAST;
            if (ln) perDay += PRICE_LUNCH;
            if (dn) perDay += PRICE_DINNER;
            double total = perDay * days;

            Order o = new Order();
            o.customerName = name;
            o.phone = phone;
            o.address = addr;
            o.planType = planText;
            o.breakfast = bk;
            o.lunch = ln;
            o.dinner = dn;
            o.durationDays = days;
            o.totalPrice = total;
            o.startDate = start;
            o.payment = pay;

            orders.add(o);
            saveOrders();
            JOptionPane.showMessageDialog(this, "Order placed! Total: Rs. " + (int) total);
            showOrders();
        });
    }
    private void showOrders() {
        JLabel head = new JLabel("All Orders");
        head.setFont(new Font("SansSerif", Font.BOLD, 20));
        head.setBounds(20, 8, 300, 30);
        mainPanel.add(head);

        viewArea = new JTextArea();
        viewArea.setEditable(false);
        viewArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane sp = new JScrollPane(viewArea);
        sp.setBounds(20, 50, 700, 520);
        mainPanel.add(sp);

        refreshOrdersView();
    }

    private void refreshOrdersView() {
        if (viewArea == null) return;
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Order o : orders) {
            sb.append(String.format("%02d) %s\n", i++, o.toString()));
        }
        if (sb.length() == 0) sb.append("No orders yet.\n");
        viewArea.setText(sb.toString());
    }

    private void showSearch() {
        JLabel head = new JLabel("Search Orders by Customer Name");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JTextField tf = new JTextField();
        tf.setBounds(20, 50, 420, 30);
        mainPanel.add(tf);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBounds(20, 100, 700, 470);
        mainPanel.add(sp);

        JButton btn = new JButton("Search");
        btn.setBounds(460, 50, 100, 30);
        mainPanel.add(btn);

        btn.addActionListener(e -> {
            String key = tf.getText().trim().toLowerCase();
            StringBuilder sb = new StringBuilder();
            int idx = 1;
            for (Order o : orders) {
                if (o.customerName.toLowerCase().contains(key)) {
                    sb.append(String.format("%02d) %s\n", idx, o.toString()));
                }
                idx++;
            }
            if (sb.length() == 0) sb.append("No match found.\n");
            ta.setText(sb.toString());
        });
    }

    private void showUpdateOrder() {
        JLabel head = new JLabel("Update Order (by order number)");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JTextField tfNum = new JTextField();
        tfNum.setBounds(20, 50, 200, 28);
        mainPanel.add(tfNum);
        JButton btnLoad = new JButton("Load");
        btnLoad.setBounds(230, 50, 100, 28);
        mainPanel.add(btnLoad);

        JPanel form = new JPanel(null);
        form.setBounds(20, 90, 700, 450);
        mainPanel.add(form);

        btnLoad.addActionListener(e -> {
            form.removeAll();
            try {
                int idx = Integer.parseInt(tfNum.getText().trim()) - 1;
                if (idx < 0 || idx >= orders.size()) {
                    JOptionPane.showMessageDialog(this, "Invalid order number.");
                    return;
                }
                Order o = orders.get(idx);

                JLabel lName = new JLabel("Customer:");
                lName.setBounds(10, 10, 120, 24);
                form.add(lName);
                JTextField tfName = new JTextField(o.customerName);
                tfName.setBounds(130, 10, 240, 26);
                form.add(tfName);

                JLabel lPhone = new JLabel("Phone:");
                lPhone.setBounds(10, 50, 120, 24);
                form.add(lPhone);
                JTextField tfPhone = new JTextField(o.phone);
                tfPhone.setBounds(130, 50, 180, 26);
                form.add(tfPhone);

                JLabel lAddr = new JLabel("Address:");
                lAddr.setBounds(10, 90, 120, 24);
                form.add(lAddr);
                JTextField tfAddr = new JTextField(o.address);
                tfAddr.setBounds(130, 90, 420, 26);
                form.add(tfAddr);

                JLabel lPlan = new JLabel("Plan:");
                lPlan.setBounds(10, 130, 120, 24);
                form.add(lPlan);
                JComboBox<String> cbPlan = new JComboBox<>(defaultPlanOptions());
                cbPlan.setBounds(130, 130, 240, 26);
                cbPlan.setSelectedItem(o.planType);
                form.add(cbPlan);

                JCheckBox cbB = new JCheckBox("Breakfast");
                cbB.setBounds(10, 170, 140, 24); cbB.setSelected(o.breakfast); form.add(cbB);
                JCheckBox cbL = new JCheckBox("Lunch");
                cbL.setBounds(160, 170, 120, 24); cbL.setSelected(o.lunch); form.add(cbL);
                JCheckBox cbD = new JCheckBox("Dinner");
                cbD.setBounds(300, 170, 120, 24); cbD.setSelected(o.dinner); form.add(cbD);

                JLabel lStart = new JLabel("Start Date:");
                lStart.setBounds(10, 210, 120, 24);
                form.add(lStart);
                JTextField tfStart = new JTextField(o.startDate);
                tfStart.setBounds(130, 210, 150, 26);
                form.add(tfStart);

                JLabel lPay = new JLabel("Payment:");
                lPay.setBounds(10, 250, 120, 24);
                form.add(lPay);
                JComboBox<String> cbPay = new JComboBox<>(new String[]{"Pending", "Paid"});
                cbPay.setBounds(130, 250, 140, 26); cbPay.setSelectedItem(o.payment); form.add(cbPay);

                JLabel lblPrice = new JLabel("Total: Rs. " + (int)o.totalPrice);
                lblPrice.setBounds(10, 290, 200, 26);
                lblPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
                form.add(lblPrice);

                JButton btnRecalc = new JButton("Recalculate");
                btnRecalc.setBounds(220, 290, 140, 28);
                form.add(btnRecalc);
                JButton btnSave = new JButton("Save Changes");
                btnSave.setBounds(380, 290, 140, 28);
                form.add(btnSave);

                btnRecalc.addActionListener(ev -> {
                    String planSel = (String) cbPlan.getSelectedItem();
                    int days = planDurationDays(planSel);
                    double perDay = 0;
                    if (cbB.isSelected()) perDay += PRICE_BREAKFAST;
                    if (cbL.isSelected()) perDay += PRICE_LUNCH;
                    if (cbD.isSelected()) perDay += PRICE_DINNER;
                    double tot = perDay * days;
                    lblPrice.setText("Total: Rs. " + (int)tot);
                });

                btnSave.addActionListener(ev -> {
                    String newName = tfName.getText().trim();
                    String newPhone = tfPhone.getText().trim();
                    String newAddr = tfAddr.getText().trim();
                    String planSel = (String) cbPlan.getSelectedItem();
                    boolean nb = cbB.isSelected(), nl = cbL.isSelected(), nd = cbD.isSelected();
                    int days = planDurationDays(planSel);
                    double perDay = 0;
                    if (nb) perDay += PRICE_BREAKFAST;
                    if (nl) perDay += PRICE_LUNCH;
                    if (nd) perDay += PRICE_DINNER;
                    double tot = perDay * days;

                    o.customerName = newName;
                    o.phone = newPhone;
                    o.address = newAddr;
                    o.planType = planSel;
                    o.breakfast = nb; o.lunch = nl; o.dinner = nd;
                    o.durationDays = days;
                    o.totalPrice = tot;
                    o.startDate = tfStart.getText().trim();
                    o.payment = (String) cbPay.getSelectedItem();

                    saveOrders();
                    JOptionPane.showMessageDialog(this, "Order updated.");
                    refreshOrdersView();
                });

                form.revalidate();
                form.repaint();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid order number.");
            }
        });
    }

    private void showDeleteManagement() {
        JLabel head = new JLabel("Delete Options");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JButton delOrder = new JButton("Delete Single Order");
        delOrder.setBounds(40, 60, 240, 40);
        mainPanel.add(delOrder);

        JButton delCustomer = new JButton("Delete  a Customer");
        delCustomer.setBounds(40, 120, 300, 40);
        mainPanel.add(delCustomer);

        JButton delPlan = new JButton("Delete Custom Plan");
        delPlan.setBounds(40, 180, 240, 40);
        mainPanel.add(delPlan);

        delOrder.addActionListener(e -> {
            String num = JOptionPane.showInputDialog(this, "Enter order # to delete:");
            if (num == null) return;
            try {
                int idx = Integer.parseInt(num.trim()) - 1;
                if (idx < 0 || idx >= orders.size()) { JOptionPane.showMessageDialog(this, "Invalid order"); return; }
                int confirm = JOptionPane.showConfirmDialog(this, "Delete order #" + (idx+1) + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    orders.remove(idx);
                    saveOrders();
                    JOptionPane.showMessageDialog(this, "Deleted.");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid input"); }
        });

        delCustomer.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter customer name :");
            if (name == null || name.trim().isEmpty()) return;
            String key = name.trim().toLowerCase();
            int before = orders.size();
            orders.removeIf(o -> o.customerName.toLowerCase().contains(key));
            int removed = before - orders.size();
            saveOrders();
            JOptionPane.showMessageDialog(this, "Removed " + removed + " orders for '" + name + "'");
        });

        delPlan.addActionListener(e -> {
            if (customPlans.isEmpty()) { JOptionPane.showMessageDialog(this, "No custom plans to delete."); return; }
            String[] arr = customPlans.keySet().toArray(new String[0]);
            String sel = (String) JOptionPane.showInputDialog(this, "Select plan to remove:", "Delete Plan", JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
            if (sel == null) return;
            customPlans.remove(sel);
            savePlans();
            JOptionPane.showMessageDialog(this, "Plan removed: " + sel);
        });
    }

    private void showMonthlyFilter() {
        JLabel head = new JLabel("Filter Orders by Month (MM)");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JTextField tf = new JTextField();
        tf.setBounds(20, 60, 120, 28);
        mainPanel.add(tf);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        JScrollPane sp = new JScrollPane(ta);
        sp.setBounds(20, 100, 700, 470);
        mainPanel.add(sp);

        JButton btn = new JButton("Filter");
        btn.setBounds(150, 60, 100, 28);
        mainPanel.add(btn);

        btn.addActionListener(e -> {
            String m = tf.getText().trim();
            if (m.length() == 1) m = "0" + m;
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int i = 0; i < orders.size(); i++) {
                Order o = orders.get(i);
                if (o.startDate != null && o.startDate.length() >= 5) {
                    String[] parts = o.startDate.split("/");
                    if (parts.length >= 2 && parts[1].equals(m)) {
                        sb.append(String.format("%02d) %s\n", i+1, o.toString()));
                        count++;
                    }
                }
            }
            if (count == 0) ta.setText("No orders found for month " + m);
            else ta.setText(sb.toString());
        });
    }

    private void showAddCustomPlan() {
        JLabel head = new JLabel("Add Custom Plan (name & days)");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JLabel l1 = new JLabel("Special Plan:");
        l1.setBounds(20, 60, 120, 24); mainPanel.add(l1);
        JTextField tfName = new JTextField(); tfName.setBounds(150, 60, 220, 26); mainPanel.add(tfName);

        JLabel l2 = new JLabel("Days:"); l2.setBounds(20, 100, 80, 24); mainPanel.add(l2);
        JTextField tfDays = new JTextField(); tfDays.setBounds(150, 100, 80, 26); mainPanel.add(tfDays);

        JButton btn = new JButton("Add Plan"); btn.setBounds(150, 140, 120, 30); mainPanel.add(btn);

        btn.addActionListener(e -> {
            String name = tfName.getText().trim();
            String ds = tfDays.getText().trim();
            if (name.isEmpty() || ds.isEmpty()) { JOptionPane.showMessageDialog(this, "Fill both"); return; }
            try {
                int days = Integer.parseInt(ds);
                customPlans.put(name, days);
                savePlans();
                JOptionPane.showMessageDialog(this, "Plan added: " + name + " (" + days + " days)");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Days must be number");
            }
        });
    }

    private void showPlans() {
        JLabel head = new JLabel("Available Plans");
        head.setFont(new Font("SansSerif", Font.BOLD, 18));
        head.setBounds(20, 8, 420, 30);
        mainPanel.add(head);

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        StringBuilder sb = new StringBuilder();
        sb.append("Standard Plans:\n");
        sb.append(" - Daily (1 day)\n - Weekly (7 days)\n - Monthly (30 days)\n\n");
        if (!customPlans.isEmpty()) {
            sb.append("Custom Plans:\n");
            for (Map.Entry<String,Integer> e : customPlans.entrySet())
                sb.append(" - ").append(e.getKey()).append(" (").append(e.getValue()).append(" days)\n");
        } else sb.append("No custom plans.\n");
        ta.setText(sb.toString());
        JScrollPane sp = new JScrollPane(ta);
        sp.setBounds(20, 60, 700, 520);
        mainPanel.add(sp);
    }

    private void saveOrders() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ordersFile))) {
            for (Order o : orders) bw.write(o.toLine() + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save orders: " + ex.getMessage());
        }
    }

    private void loadOrders() {
        orders.clear();
        if (!ordersFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(ordersFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                Order o = Order.fromLine(line);
                if (o != null) orders.add(o);
            }
        } catch (IOException ex) {
            System.err.println("loadOrders: " + ex.getMessage());
        }
    }

    private void savePlans() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(plansFile))) {
            for (Map.Entry<String,Integer> e : customPlans.entrySet()) {
                bw.write(e.getKey() + "|" + e.getValue() + "\n");
            }
        } catch (IOException ex) {
            System.err.println("savePlans: " + ex.getMessage());
        }
    }

    private void loadPlans() {
        customPlans.clear();
        if (!plansFile.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(plansFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|",2);
                if (p.length == 2) {
                    try { customPlans.put(p[0], Integer.parseInt(p[1])); } catch (Exception ignored) {}
                }
            }
        } catch (IOException ex) {
            System.err.println("loadPlans: " + ex.getMessage());
        }
    }

    private void saveAll() {
        saveOrders();
        savePlans();
    }

    private void openOrdersFile() {
        try {
            if (!ordersFile.exists()) {
                JOptionPane.showMessageDialog(this, "orders file not found.");
                return;
            }
            Desktop.getDesktop().open(ordersFile);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cannot open orders file: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TiffinSystem app = new TiffinSystem();
            app.setVisible(true);
        });
    }
}