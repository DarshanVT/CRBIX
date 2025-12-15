package scoreBoard;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AmbulanceBookingApp extends JFrame {

    private JTextField txtName, txtPhone, txtAddress;
    private JRadioButton rbEmergency, rbNormal;
    private JComboBox<String> cmbAmbulanceType, cmbHospital;
    private JLabel lblCost;
    private JTextArea bookingInfo;

    private static int bookingCounter = 1; 

    private PriorityQueue<Booking> bookingQueue = new PriorityQueue<>();

    public AmbulanceBookingApp() {
        setTitle("Ambulance Booking System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Ambulance Booking App", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(0, 102, 204));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient & Booking Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Patient Name:"), gbc);
        gbc.gridx = 1;
        txtName = new JTextField(15);
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField();
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Pickup Address:"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextField();
        formPanel.add(txtAddress, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Hospital:"), gbc);
        gbc.gridx = 1;
        cmbHospital = new JComboBox<>(new String[]{"Sasoon Hospital", "Sahyadri Hospital", "Sanchiti Hospital", "Poona Hospital", "Jahangir Hospital", "None of These"});
        formPanel.add(cmbHospital, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Case Type:"), gbc);
        gbc.gridx = 1;
        rbEmergency = new JRadioButton("Emergency");
        rbNormal = new JRadioButton("Normal");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbEmergency);
        bg.add(rbNormal);
        JPanel casePanel = new JPanel();
        casePanel.add(rbEmergency);
        casePanel.add(rbNormal);
        formPanel.add(casePanel, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Ambulance Type:"), gbc);
        gbc.gridx = 1;
        cmbAmbulanceType = new JComboBox<>(new String[]{"Basic", "ICU", "Advanced Life Support"});
        formPanel.add(cmbAmbulanceType, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Estimated Cost:"), gbc);
        gbc.gridx = 1;
        lblCost = new JLabel("Rs. 0");
        lblCost.setFont(new Font("Arial", Font.BOLD, 14));
        lblCost.setForeground(Color.RED);
        formPanel.add(lblCost, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton btnBook = new JButton("Book Ambulance");
        JButton btnReset = new JButton("Reset");

        buttonPanel.add(btnBook);
        buttonPanel.add(btnReset);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        bookingInfo = new JTextArea(10, 35);
        bookingInfo.setEditable(false);
        bookingInfo.setBorder(BorderFactory.createTitledBorder("Booking Queue "));
        mainPanel.add(new JScrollPane(bookingInfo), BorderLayout.EAST);

        add(mainPanel);

        btnBook.addActionListener(e -> bookAmbulance());
        btnReset.addActionListener(e -> resetForm());
    }

    private void calculateCost() {
        int cost = 1500;
        if (cmbAmbulanceType.getSelectedIndex() == 1) cost += 1000;
        if (cmbAmbulanceType.getSelectedIndex() == 2) cost += 2000;
        lblCost.setText("Rs. " + cost);
    }

    private void bookAmbulance() {
        if (txtName.getText().isEmpty() || txtPhone.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all details");
            return;
        }

        calculateCost();

        String bookingId = String.format("AMB2025_%03d", bookingCounter++);
        boolean emergency = rbEmergency.isSelected();

        Booking booking = new Booking(
                bookingId,
                txtName.getText(),
                txtPhone.getText(),
                txtAddress.getText(),
                (String) cmbHospital.getSelectedItem(),
                (String) cmbAmbulanceType.getSelectedItem(),
                emergency,
                lblCost.getText()
        );

        bookingQueue.add(booking);
        refreshBookingQueue();

        JOptionPane.showMessageDialog(this,
                "Booking Confirmed!\nBooking ID: " + bookingId +
                "\nPriority: " + (emergency ? "Emergency" : "Normal "));

        resetForm();
    }

    private void refreshBookingQueue() {
        bookingInfo.setText("");
        for (Booking b : bookingQueue) {
            bookingInfo.append(b + "\n------------------\n");
        }
    }

    private void resetForm() {
        txtName.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        rbEmergency.setSelected(false);
        rbNormal.setSelected(false);
        cmbAmbulanceType.setSelectedIndex(0);
        cmbHospital.setSelectedIndex(0);
        lblCost.setText("Rs. 0");
    }

    static class Booking implements Comparable<Booking> {
        String bookingId, name, phone, address, hospital, ambulanceType, cost;
        boolean emergency;

        Booking(String bookingId, String name, String phone, String address,
                String hospital, String ambulanceType, boolean emergency, String cost) {
            this.bookingId = bookingId;
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.hospital = hospital;
            this.ambulanceType = ambulanceType;
            this.emergency = emergency;
            this.cost = cost;
        }

        @Override
        public int compareTo(Booking o) {
            return Boolean.compare(o.emergency, this.emergency);
        }

        @Override
        public String toString() {
            return "Booking ID: " + bookingId +
                    "\nName: " + name +
                    "\nHospital: " + hospital +
                    "\nCase: " + (emergency ? "Emergency" : "Normal") +
                    "\nAmbulance: " + ambulanceType +
                    "\nCost: " + cost;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AmbulanceBookingApp().setVisible(true));
    }
}
