package fertilizerForPlants;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class GasBookingApp2 extends JFrame {

    Connection con;

    private final int MAX_CYLINDERS = 12;
    private final int CYLINDER_PRICE = 1500;

    private HashMap<String, Integer> customerCylinders = new HashMap<>();
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField txtCngKg;

    private JTextField txtName, txtPhone, txtAddress, txtQty;
    private JTextArea homeOutput;

    private JTextField txtCngName, txtAutoNo, txtCngDate, txtSlotTime;
    private JRadioButton rbMorning, rbEvening, rbNight;
    private JTextArea cngOutput;

    public GasBookingApp2() {
        connectDB();

        setTitle("Gas Cylinder & CNG Booking System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(menuPanel(), "MENU");
        mainPanel.add(homeGasPanel(), "HOME");
        mainPanel.add(cngPanel(), "CNG");

        add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gas_booking_db",
                "root",
                "Darshan@7742"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed");
            e.printStackTrace();
        }
    }
    private int getAvailableCNG() throws Exception {
        PreparedStatement ps = con.prepareStatement(
            "SELECT available_cng FROM cng_stock WHERE id = 1"
        );
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("available_cng");
        }
        return 0;
    }


    private int getTotalCNG() throws Exception {
        PreparedStatement ps = con.prepareStatement(
            "SELECT total_cng FROM cng_stock WHERE id = 1"
        );
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("total_cng");
        }
        return 0;
    }


    private void updateCNGStock(int newAvailable) throws Exception {
        PreparedStatement ps = con.prepareStatement(
            "UPDATE cng_stock SET available_cng = ?, last_updated = CURDATE() WHERE id = 1"
        );
        ps.setInt(1, newAvailable);
        ps.executeUpdate();
    }

    private JPanel menuPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(15, 15, 15, 15);

        JLabel title = new JLabel("Gas Booking Application");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JButton b1 = new JButton("Home Gas Booking");
        JButton b2 = new JButton("Auto Rikshaw CNG Booking");

        b1.setPreferredSize(new Dimension(320, 45));
        b2.setPreferredSize(new Dimension(320, 45));

        b1.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        b2.addActionListener(e -> cardLayout.show(mainPanel, "CNG"));

        g.gridx = 0; g.gridy = 0; p.add(title, g);
        g.gridy = 1; p.add(b1, g);
        g.gridy = 2; p.add(b2, g);

        return p;
    }

    private JPanel homeGasPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Home Gas Cylinder Booking", JLabel.CENTER);
        h.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);

        txtName = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
        txtQty = new JTextField(20);

        int y = 0;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Customer Name:"), g);
        g.gridx=1; form.add(txtName, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Phone Number:"), g);
        g.gridx=1; form.add(txtPhone, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Address:"), g);
        g.gridx=1; form.add(txtAddress, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("No of Cylinders:"), g);
        g.gridx=1; form.add(txtQty, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Price per Cylinder:"), g);
        g.gridx = 1; form.add(new JLabel("Rs. 1500"), g);

        JButton book = new JButton("Book Cylinder");
        JButton back = new JButton("Back");

        y++;
        g.gridx=0; g.gridy=y; form.add(back, g);
        g.gridx=1; form.add(book, g);

        homeOutput = new JTextArea(8,50);
        homeOutput.setEditable(false);

        book.addActionListener(e -> bookHomeGas());
        back.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));

        p.add(h, BorderLayout.NORTH);
        p.add(form, BorderLayout.CENTER);
        p.add(new JScrollPane(homeOutput), BorderLayout.SOUTH);

        return p;
    }

    private void bookHomeGas() {
        try {
            String name = txtName.getText();
            int qty = Integer.parseInt(txtQty.getText());

            int used = customerCylinders.getOrDefault(name, 0);
            if (used + qty > MAX_CYLINDERS) {
                homeOutput.append("Booking failed! Limit exceeded\n");
                return;
            }

            int newTotal = used + qty;
            customerCylinders.put(name, newTotal);
            int remaining = MAX_CYLINDERS - newTotal;
            int totalAmount = qty * CYLINDER_PRICE;

            homeOutput.append("Customer: " + name + "\n");
            homeOutput.append("Cylinders Booked Now: " + qty + "\n");
            homeOutput.append("Total Cylinders Used: " + newTotal + " / 12\n");
            homeOutput.append("Remaining Cylinders: " + remaining + "\n");
            homeOutput.append("Price per Cylinder: Rs.1500\n");
            homeOutput.append("Total Amount: Rs." + totalAmount + "\n");
            homeOutput.append("--------------------------\n");

            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO home_gas_booking (customer_name, phone, address, qty, total_used, total_amount, booking_date) VALUES (?,?,?,?,?,?,?)"
            );
            ps.setString(1, name);
            ps.setString(2, txtPhone.getText());
            ps.setString(3, txtAddress.getText());
            ps.setInt(4, qty);
            ps.setInt(5, newTotal);
            ps.setInt(6, totalAmount);
            ps.setDate(7, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            homeOutput.append("Saved to Database\n");
            homeOutput.append("================================\n");

            txtName.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            txtQty.setText("");

        } catch (Exception e) {
            homeOutput.append("DB Error!\n");
            e.printStackTrace();
        }
    }

    private JPanel cngPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Auto Rikshaw CNG Booking", JLabel.CENTER);
        h.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);

        txtCngName = new JTextField(20);
        txtAutoNo = new JTextField(20);
        txtCngDate = new JTextField(LocalDate.now().toString());
        txtSlotTime = new JTextField(20);

        rbMorning = new JRadioButton("Morning");
        rbEvening = new JRadioButton("Evening");
        rbNight = new JRadioButton("Night");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMorning); bg.add(rbEvening); bg.add(rbNight);

        int y=0;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Customer Name:"), g);
        g.gridx=1; form.add(txtCngName, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Auto Number:"), g);
        g.gridx=1; form.add(txtAutoNo, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Booking Date:"), g);
        g.gridx=1; form.add(txtCngDate, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Select Slot:"), g);
        JPanel sp = new JPanel();
        sp.add(rbMorning); sp.add(rbEvening); sp.add(rbNight);
        g.gridx=1; form.add(sp, g);

        y++;
        g.gridx=0; g.gridy=y; form.add(new JLabel("Slot Time:"), g);
        g.gridx=1; form.add(txtSlotTime, g);
        y++;
        g.gridx = 0; g.gridy = y;
        form.add(new JLabel("CNG Required (kg):"), g);

        txtCngKg = new JTextField(20);
        g.gridx = 1;
        form.add(txtCngKg, g);

        JButton book = new JButton("Book Slot");
        JButton back = new JButton("Back");

        y++;
        g.gridx=0; g.gridy=y; form.add(back, g);
        g.gridx=1; form.add(book, g);

        cngOutput = new JTextArea(7,50);
        cngOutput.setEditable(false);

        book.addActionListener(e -> bookCng());
        back.addActionListener(e -> cardLayout.show(mainPanel,"MENU"));

        p.add(h,BorderLayout.NORTH);
        p.add(form,BorderLayout.CENTER);
        p.add(new JScrollPane(cngOutput),BorderLayout.SOUTH);

        return p;
    }

    private void bookCng() {

        String slot = rbMorning.isSelected() ? "Morning" :
                      rbEvening.isSelected() ? "Evening" :
                      rbNight.isSelected() ? "Night" : "";

        if (slot.isEmpty()) {
            cngOutput.append("Select Slot!\n");
            return;
        }

        int cngUsedKg;
        try {
            cngUsedKg = Integer.parseInt(txtCngKg.getText());
            if (cngUsedKg <= 0) {
                cngOutput.append("Enter valid CNG quantity!\n");
                return;
            }
        } catch (Exception e) {
            cngOutput.append("Enter CNG in numbers!\n");
            return;
        }

        try {
            int availableCNG = getAvailableCNG();
            int totalCNG = getTotalCNG();

            if (cngUsedKg > availableCNG) {
                cngOutput.append("Not enough CNG available!\n");
                return;
            }

            int newAvailable = availableCNG - cngUsedKg;

           
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO cng_booking (customer_name, auto_no, booking_date, slot, slot_time, created_on) VALUES (?,?,?,?,?,?)"
            );
            ps.setString(1, txtCngName.getText());
            ps.setString(2, txtAutoNo.getText());
            ps.setDate(3, Date.valueOf(txtCngDate.getText()));
            ps.setString(4, slot);
            ps.setString(5, txtSlotTime.getText());
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            ps.executeUpdate();

            txtCngKg.setText("");
            updateCNGStock(newAvailable);

            
            cngOutput.append("Customer: " + txtCngName.getText() + "\n");
            cngOutput.append("Auto No: " + txtAutoNo.getText() + "\n");
            cngOutput.append("Date: " + txtCngDate.getText() + "\n");
            cngOutput.append("Slot: " + slot + "\n");
            cngOutput.append("Time: " + txtSlotTime.getText() + "\n");
            cngOutput.append("CNG Used: " + cngUsedKg + " kg\n");
            cngOutput.append("Total CNG: " + totalCNG + " kg\n");
            cngOutput.append("Available CNG: " + newAvailable + " kg\n");
            cngOutput.append("Saved to Database\n");
            cngOutput.append("================================\n");

            txtCngName.setText("");
            txtAutoNo.setText("");
            txtSlotTime.setText("");

        } catch (Exception e) {
            cngOutput.append("DB Error!\n");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GasBookingApp2().setVisible(true));
    }
}
