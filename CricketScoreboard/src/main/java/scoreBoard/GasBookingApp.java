package scoreBoard;


import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.HashMap;

public class GasBookingApp extends JFrame {

    private final int MAX_CYLINDERS = 12;
    private final int CYLINDER_PRICE = 1500;

    private HashMap<String, Integer> customerCylinders = new HashMap<>();

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField txtName, txtPhone, txtAddress, txtQty;
    private JTextArea homeOutput;

    private JTextField txtCngName, txtAutoNo, txtCngDate, txtSlotTime;
    private JRadioButton rbMorning, rbEvening, rbNight;
    private JTextArea cngOutput;

    public GasBookingApp() {
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
        h.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField(20);
        txtPhone = new JTextField(20);
        txtAddress = new JTextField(20);
        txtQty = new JTextField(20);

        int y = 0;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Customer Name:"), g);
        g.gridx = 1; form.add(txtName, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Phone Number:"), g);
        g.gridx = 1; form.add(txtPhone, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Address:"), g);
        g.gridx = 1; form.add(txtAddress, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("No. of Cylinders:"), g);
        g.gridx = 1; form.add(txtQty, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Price per Cylinder:"), g);
        g.gridx = 1; form.add(new JLabel("Rs. 1500"), g);

        JButton book = new JButton("Book Cylinder");
        JButton back = new JButton("Back");

        y++;
        g.gridx = 0; g.gridy = y; form.add(back, g);
        g.gridx = 1; form.add(book, g);

        homeOutput = new JTextArea(7, 50);
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
            String name = txtName.getText().trim();
            int qty = Integer.parseInt(txtQty.getText());

            if (name.isEmpty()) {
                homeOutput.append("Customer name required!\n");
                return;
            }

            int alreadyUsed = customerCylinders.getOrDefault(name, 0);

            if (alreadyUsed + qty > MAX_CYLINDERS) {
                homeOutput.append("Booking failed! " + name + " already used " + alreadyUsed + "/12 cylinders\n");
                return;
            }

            int newTotal = alreadyUsed + qty;
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

            txtName.setText("");
            txtPhone.setText("");
            txtAddress.setText("");
            txtQty.setText("");

        } catch (Exception ex) {
            homeOutput.append("Invalid input!\n");
        }
    }

    private JPanel cngPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JLabel h = new JLabel("Auto Rikshaw CNG Slot Booking", JLabel.CENTER);
        h.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        txtCngName = new JTextField(20);
        txtAutoNo = new JTextField(20);
        txtCngDate = new JTextField(LocalDate.now().toString());
        txtSlotTime = new JTextField(20);

        rbMorning = new JRadioButton("Morning");
        rbEvening = new JRadioButton("Evening");
        rbNight = new JRadioButton("Night");

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMorning); bg.add(rbEvening); bg.add(rbNight);

        int y = 0;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Customer Name:"), g);
        g.gridx = 1; form.add(txtCngName, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Auto Number:"), g);
        g.gridx = 1; form.add(txtAutoNo, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Booking Date:"), g);
        g.gridx = 1; form.add(txtCngDate, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Select Slot:"), g);
        JPanel slotPanel = new JPanel();
        slotPanel.add(rbMorning); slotPanel.add(rbEvening); slotPanel.add(rbNight);
        g.gridx = 1; form.add(slotPanel, g);

        y++;
        g.gridx = 0; g.gridy = y; form.add(new JLabel("Slot Time :"), g);
        g.gridx = 1; form.add(txtSlotTime, g);

        JButton back = new JButton("Back");
        JButton book = new JButton("Book Slot");

        y++;
        g.gridx = 0; g.gridy = y; form.add(back, g);
        g.gridx = 1; form.add(book, g);

        cngOutput = new JTextArea(6, 50);
        cngOutput.setEditable(false);

        back.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
        book.addActionListener(e -> bookCng());

        p.add(h, BorderLayout.NORTH);
        p.add(form, BorderLayout.CENTER);
        p.add(new JScrollPane(cngOutput), BorderLayout.SOUTH);

        return p;
    }

    private void bookCng() {
        String slot = rbMorning.isSelected() ? "Morning" :
                      rbEvening.isSelected() ? "Evening" :
                      rbNight.isSelected() ? "Night" : "";

        if (slot.isEmpty()) {
            cngOutput.append("Please select slot!\n");
            return;
        }

        cngOutput.append("Customer: " + txtCngName.getText() + "\n");
        cngOutput.append("Auto No: " + txtAutoNo.getText() + "\n");
        cngOutput.append("Date: " + txtCngDate.getText() + "\n");
        cngOutput.append("Slot: " + slot + "\n");
        cngOutput.append("Time: " + txtSlotTime.getText() + "\n");
        cngOutput.append("--------------------------\n");

        txtCngName.setText("");
        txtAutoNo.setText("");
        txtSlotTime.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GasBookingApp().setVisible(true));
    }
}
