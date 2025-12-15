package scoreBoard;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CarWashBookingApp extends JFrame {

    static class Booking {
        String name, carModel, serviceType, date, time;
        double price;
        String paymentStatus;

        Booking(String name, String carModel, String serviceType, String date, String time, double price, String paymentStatus) {
            this.name = name;
            this.carModel = carModel;
            this.serviceType = serviceType;
            this.date = date;
            this.time = time;
            this.price = price;
            this.paymentStatus = paymentStatus;
        }

        public String toString() {
            return name + " | " + carModel + " | " + serviceType + " | " + date + " | " + time + " | Rs." + price + " | " + paymentStatus;
        }

        public String toLine() {
            return escape(name) + "|" + escape(carModel) + "|" + escape(serviceType) + "|" + escape(date) + "|" + escape(time) + "|" + price + "|" + paymentStatus;
        }

        public static Booking fromLine(String line) {
            try {
                String[] parts = line.split("\\|", 7);
                if (parts.length < 7) return null;
                String name = unescape(parts[0]);
                String car = unescape(parts[1]);
                String serv = unescape(parts[2]);
                String date = unescape(parts[3]);
                String time = unescape(parts[4]);
                double price = Double.parseDouble(parts[5]);
                String status = parts[6];
                return new Booking(name, car, serv, date, time, price, status);
            } catch (Exception e) {
                return null;
            }
        }

        private static String escape(String s) { return s.replace("|", " "); }
        private static String unescape(String s) { return s; }
    }

    ArrayList<Booking> bookings = new ArrayList<>();

    JTextField tfName, tfCarModel, tfDate, tfTime;
    JComboBox<String> cbService;
    JComboBox<String> cbPaymentStatus;
    JTextArea display;

    private final File bookingsFile = new File("bookings.txt");

    public CarWashBookingApp() {
        setTitle("Car Wash Booking System");
        setSize(740, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.lightGray);

        loadBookingsFromFile();

        JLabel title = new JLabel("Car Wash Booking System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(150, 10, 400, 40);
        add(title);

        JLabel l1 = new JLabel("Customer Name:");
        l1.setBounds(50, 80, 150, 25);
        add(l1);

        tfName = new JTextField();
        tfName.setBounds(200, 80, 200, 25);
        add(tfName);

        JLabel l2 = new JLabel("Car Model:");
        l2.setBounds(50, 120, 150, 25);
        add(l2);

        tfCarModel = new JTextField();
        tfCarModel.setBounds(200, 120, 200, 25);
        add(tfCarModel);

        JLabel l3 = new JLabel("Service Type:");
        l3.setBounds(50, 160, 150, 25);
        add(l3);

        String services[] = {
                "Basic Wash (300 rs)",
                "Deep Cleaning (800 rs)",
                "Interior Detailing (1200 rs)",
                "Full Body Polish (2000 rs)"
        };
        cbService = new JComboBox<>(services);
        cbService.setBounds(200, 160, 300, 25);
        add(cbService);

        JLabel l4 = new JLabel("Date (DD/MM/YYYY):");
        l4.setBounds(50, 200, 150, 25);
        add(l4);

        tfDate = new JTextField();
        tfDate.setBounds(200, 200, 200, 25);
        add(tfDate);

        JLabel l5 = new JLabel("Time Slot:");
        l5.setBounds(50, 240, 150, 25);
        add(l5);

        tfTime = new JTextField();
        tfTime.setBounds(200, 240, 200, 25);
        add(tfTime);

        JLabel l6 = new JLabel("Payment Status:");
        l6.setBounds(420, 200, 110, 25);
        add(l6);

        cbPaymentStatus = new JComboBox<>(new String[]{"Pending", "Paid"});
        cbPaymentStatus.setBounds(520, 200, 120, 25);
        add(cbPaymentStatus);

        JButton btnViewFile = new JButton("View Bookings File");
        btnViewFile.setBounds(420, 400, 180, 35);
        btnViewFile.setBackground(new Color(180, 180, 255));
        add(btnViewFile);

        btnViewFile.addActionListener(e -> openBookingsFile());


        JButton btnAdd = new JButton("Book Now");
        btnAdd.setBounds(50, 300, 150, 35);
        btnAdd.setBackground(new Color(100, 180, 255));
        add(btnAdd);

        JButton btnView = new JButton("View All Bookings");
        btnView.setBounds(220, 300, 160, 35);
        btnView.setBackground(new Color(140, 200, 255));
        add(btnView);

        JButton btnSearch = new JButton("Search Booking");
        btnSearch.setBounds(420, 300, 180, 35);
        btnSearch.setBackground(new Color(140, 220, 255));
        add(btnSearch);

        JButton btnUpdate = new JButton("Update Booking");
        btnUpdate.setBounds(50, 350, 150, 35);
        btnUpdate.setBackground(new Color(255, 215, 120));
        add(btnUpdate);

        JButton btnDelete = new JButton("Delete Booking");
        btnDelete.setBounds(220, 350, 160, 35);
        btnDelete.setBackground(new Color(255, 150, 150));
        add(btnDelete);

        JButton btnMonthly = new JButton("Monthly Filter");
        btnMonthly.setBounds(420, 350, 180, 35);
        btnMonthly.setBackground(new Color(200, 220, 255));
        add(btnMonthly);

        JButton btnExit = new JButton("Exit");
        btnExit.setBounds(220, 400, 160, 35);
        btnExit.setBackground(new Color(255, 120, 120));
        add(btnExit);

        display = new JTextArea();
        display.setEditable(false);
        JScrollPane pane = new JScrollPane(display);
        pane.setBounds(50, 450, 650, 180);
        add(pane);

        btnAdd.addActionListener(e -> addBooking());
        btnView.addActionListener(e -> viewBookings());
        btnSearch.addActionListener(e -> searchBooking());
        btnUpdate.addActionListener(e -> updateBooking());
        btnDelete.addActionListener(e -> deleteBooking());
        btnExit.addActionListener(e -> {
            saveBookingsToFile();
            System.exit(0);
        });
        btnMonthly.addActionListener(e -> monthlyFilter());

        setVisible(true);
    }
    
    void openBookingsFile() {
        try {
            if (!bookingsFile.exists()) {
                JOptionPane.showMessageDialog(this, "bookings.txt not found!");
                return;
            }
            Desktop.getDesktop().open(bookingsFile); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot open bookings file!");
        }
    }

    
    double getPrice(String service) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(service);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group(1));
            } catch (Exception ignored) {}
        }

        service = service.toLowerCase();
        if (service.contains("basic")) return 300;
        if (service.contains("deep")) return 800;
        if (service.contains("interior")) return 1200;
        if (service.contains("full")) return 2000;
        return 0;
    }

    boolean isSlotAvailable(String date, String time, Integer ignoreIndex) {
        for (int i = 0; i < bookings.size(); i++) {
            if (ignoreIndex != null && i == ignoreIndex) continue;
            Booking b = bookings.get(i);
            if (b.date.equals(date) && b.time.equalsIgnoreCase(time)) {
                return false;
            }
        }
        return true;
    }

    void addBooking() {
        String name = tfName.getText().trim();
        String car = tfCarModel.getText().trim();
        String service = cbService.getSelectedItem().toString();
        String date = tfDate.getText().trim();
        String time = tfTime.getText().trim();
        String paymentStatus = cbPaymentStatus.getSelectedItem().toString();

        if (name.isEmpty() || car.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (!isSlotAvailable(date, time, null)) {
            JOptionPane.showMessageDialog(this, "Slot already booked for " + date + " " + time + "!");
            return;
        }

        double price = getPrice(service);
        Booking b = new Booking(name, car, service, date, time, price, paymentStatus);
        bookings.add(b);
        saveBookingsToFile();
        JOptionPane.showMessageDialog(this, "Booking Added Successfully!");

        clearFields();
    }

    void viewBookings() {
        if (bookings.isEmpty()) {
            display.setText("No bookings available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;
        double totalEarnings = 0;
        for (Booking b : bookings) {
            sb.append(i++).append(") ").append(b.toString()).append("\n");
            totalEarnings += b.price;
        }
        sb.append("\nTotal Bookings: ").append(bookings.size()).append("\n");
        sb.append("Total Earnings: Rs. ").append(totalEarnings);

        display.setText(sb.toString());
    }

    void searchBooking() {
        String key = JOptionPane.showInputDialog(this, "Enter customer name:");
        if (key == null) return;

        StringBuilder sb = new StringBuilder();
        key = key.toLowerCase();

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            if (b.name.toLowerCase().contains(key)) {
                sb.append((i + 1)).append(") ").append(b.toString()).append("\n");
            }
        }

        if (sb.length() == 0) display.setText("No results found.");
        else display.setText(sb.toString());
    }

    void updateBooking() {
        String num = JOptionPane.showInputDialog(this, "Enter booking number to update:");
        if (num == null) return;

        try {
            int index = Integer.parseInt(num) - 1;
            if (index < 0 || index >= bookings.size()) {
                JOptionPane.showMessageDialog(this, "Invalid booking number!");
                return;
            }

            Booking old = bookings.get(index);

            JTextField nameF = new JTextField(old.name);
            JTextField carF = new JTextField(old.carModel);
            JTextField dateF = new JTextField(old.date);
            JTextField timeF = new JTextField(old.time);

            JComboBox<String> servF = new JComboBox<>(new String[]{
                    "Basic Wash (300 rs)",
                    "Deep Cleaning (800 rs)",
                    "Interior Detailing (1200 rs)",
                    "Full Body Polish (2000 rs)"
            });
            servF.setSelectedItem(old.serviceType);

            JComboBox<String> payF = new JComboBox<>(new String[]{"Pending", "Paid"});
            payF.setSelectedItem(old.paymentStatus);

            Object[] fields = {
                    "Name:", nameF,
                    "Car Model:", carF,
                    "Service:", servF,
                    "Date:", dateF,
                    "Time:", timeF,
                    "Payment:", payF
            };

            int result = JOptionPane.showConfirmDialog(this, fields, "Edit Booking", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {

                String newDate = dateF.getText();
                String newTime = timeF.getText();

                if (!isSlotAvailable(newDate, newTime, index)) {
                    JOptionPane.showMessageDialog(this, "Slot Not Available!");
                    return;
                }

                Booking updated = new Booking(
                        nameF.getText(),
                        carF.getText(),
                        servF.getSelectedItem().toString(),
                        dateF.getText(),
                        timeF.getText(),
                        getPrice(servF.getSelectedItem().toString()),
                        payF.getSelectedItem().toString()
                );

                bookings.set(index, updated);
                saveBookingsToFile();
                JOptionPane.showMessageDialog(this, "Updated Successfully!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    void deleteBooking() {
        String num = JOptionPane.showInputDialog(this, "Enter booking number:");
        if (num == null) return;

        try {
            int index = Integer.parseInt(num) - 1;
            if (index < 0 || index >= bookings.size()) {
                JOptionPane.showMessageDialog(this, "Invalid booking!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Delete booking?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookings.remove(index);
                saveBookingsToFile();
                JOptionPane.showMessageDialog(this, "Deleted!");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    void monthlyFilter() {
        String month = JOptionPane.showInputDialog(this, "Enter month (01–12):");
        if (month == null) return;

        if (month.length() == 1) month = "0" + month;

        StringBuilder sb = new StringBuilder();
        int count = 0;
        double total = 0;

        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);

            if (b.date.startsWith("00")) continue;

            String[] parts = b.date.split("/");
            if (parts.length >= 2 && parts[1].equals(month)) {
                sb.append(i + 1).append(") ").append(b.toString()).append("\n");
                count++;
                total += b.price;
            }
        }

        if (count == 0) display.setText("No bookings for this month.");
        else {
            sb.append("\nTotal: ").append(count).append(" bookings");
            sb.append("\nEarnings: Rs. ").append(total);
            display.setText(sb.toString());
        }
    }

    void saveBookingsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bookingsFile))) {
            for (Booking b : bookings) {
                bw.write(b.toLine());
                bw.newLine();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Cannot save file!");
        }
    }

    void loadBookingsFromFile() {
        bookings.clear();
        if (!bookingsFile.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(bookingsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                Booking b = Booking.fromLine(line);
                if (b != null) bookings.add(b);
            }
        } catch (Exception ignored) {}
    }

    void clearFields() {
        tfName.setText("");
        tfCarModel.setText("");
        tfDate.setText("");
        tfTime.setText("");
        cbService.setSelectedIndex(0);
        cbPaymentStatus.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        SwingUtilities.invokeLater(CarWashBookingApp::new);
    }
}
