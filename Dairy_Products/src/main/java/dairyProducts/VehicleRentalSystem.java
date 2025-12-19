package dairyProducts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Vehicle {
    String name, type, average;
    int price;
    boolean available = true;

    Vehicle(String name, String type, int price, String average) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.average = average;
    }
}
class Booking {
    String user, vehicle, city, pickup, ret;
    int days;

    Booking(String user, String vehicle, int days,
            String city, String pickup, String ret) {
        this.user = user;
        this.vehicle = vehicle;
        this.days = days;
        this.city = city;
        this.pickup = pickup;
        this.ret = ret;
    }
}
public class VehicleRentalSystem {

    static ArrayList<Vehicle> vehicles = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static class Login extends JFrame implements ActionListener {

        JTextField user;
        JPasswordField pass;
        JButton login;

        Login() {
            setTitle("Vehicle Rental System - Login");
            setSize(350, 230);
            setLayout(null);
            setLocationRelativeTo(null);

            JLabel title = new JLabel("LOGIN");
            title.setFont(new Font("Segoe UI", Font.BOLD, 22));
            title.setBounds(130, 10, 100, 30);
            add(title);

            JLabel l1 = new JLabel("Username");
            l1.setBounds(40, 60, 100, 25);
            add(l1);

            user = new JTextField();
            user.setBounds(140, 60, 150, 28);
            add(user);

            JLabel l2 = new JLabel("Password");
            l2.setBounds(40, 100, 100, 25);
            add(l2);

            pass = new JPasswordField();
            pass.setBounds(140, 100, 150, 28);
            add(pass);

            login = new JButton("LOGIN");
            login.setBounds(110, 150, 120, 35);
            login.setBackground(new Color(33, 150, 243));
            login.setForeground(Color.WHITE);
            login.setFocusPainted(false);
            add(login);

            login.addActionListener(this);
            setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
            String u = user.getText();
            String p = String.valueOf(pass.getPassword());

            if (u.equals("a") && p.equals("1")) {
                new AdminPanel();
                dispose();
            } else if (u.equals("u") && p.equals("1")) {
                new UserPanel();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login!");
            }
        }
    }
    static class AdminPanel extends JFrame implements ActionListener {

        JTextField name, price, avg;
        JComboBox<String> type;
        JTable table;
        DefaultTableModel model;
        JButton add, clear, logout;

        AdminPanel() {
            setTitle("Admin Panel");
            setSize(900, 450);
            setLayout(null);
            setLocationRelativeTo(null);

            JLabel title = new JLabel("ADMIN - VEHICLE MANAGEMENT");
            title.setFont(new Font("Segoe UI", Font.BOLD, 22));
            title.setBounds(30, 10, 400, 30);
            add(title);

            JPanel form = new JPanel(null);
            form.setBounds(30, 60, 300, 320);
            form.setBackground(new Color(245, 245, 245));
            add(form);

            form.add(label("Vehicle Name", 20, 20));
            name = field(140, 20); form.add(name);

            form.add(label("Type", 20, 70));
            type = new JComboBox<>(new String[]{"Car", "Bike", "Scooter"});
            type.setBounds(140, 70, 140, 28);
            form.add(type);

            form.add(label("Price Per Day", 20, 120));
            price = field(140, 120); form.add(price);

            form.add(label("Average (per KM)", 20, 170));
            avg = field(140, 170); form.add(avg);

            add = button("ADD VEHICLE", 20, 230, new Color(76,175,80));
            clear = button("CLEAR", 160, 230, new Color(255,152,0));
            logout = button("LOGOUT", 90, 270, new Color(244,67,54));

            form.add(add); form.add(clear); form.add(logout);

            model = new DefaultTableModel(
                new String[]{"Name","Type","Price","Average","Status"},0
            );
            table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

            JScrollPane sp = new JScrollPane(table);
            sp.setBounds(360, 60, 500, 320);
            add(sp);

            refreshTable();

            add.addActionListener(this);
            clear.addActionListener(this);
            logout.addActionListener(this);

            setVisible(true);
        }

        JLabel label(String t, int x, int y) {
            JLabel l = new JLabel(t);
            l.setBounds(x, y, 100, 25);
            return l;
        }

        JTextField field(int x, int y) {
            JTextField f = new JTextField();
            f.setBounds(x, y, 140, 28);
            return f;
        }

        JButton button(String t, int x, int y, Color c) {
            JButton b = new JButton(t);
            b.setBounds(x, y, 140, 35);
            b.setBackground(c);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            return b;
        }

        void refreshTable() {
            model.setRowCount(0);
            for (Vehicle v : vehicles) {
                model.addRow(new Object[]{
                    v.name, v.type, v.price, v.average,
                    v.available ? "Available" : "Booked"
                });
            }
        }
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {
                vehicles.add(new Vehicle(
                    name.getText(),
                    type.getSelectedItem().toString(),
                    Integer.parseInt(price.getText()),
                    avg.getText()
                ));
                refreshTable();
            }

            if (e.getSource() == clear) {
                name.setText(""); price.setText(""); avg.setText("");
            }

            if (e.getSource() == logout) {
                new Login();
                dispose();
            }
        }
    }
    static class UserPanel extends JFrame implements ActionListener {

        JTextField vname, days, pickup, ret, city;
        JTable table;
        DefaultTableModel model;
        JButton book, bill, clear, logout;

        Vehicle selectedVehicle;
        Booking lastBooking;

        UserPanel() {
            setTitle("User Panel");
            setSize(1000, 500);
            setLayout(null);
            setLocationRelativeTo(null);

            JLabel title = new JLabel("AVAILABLE VEHICLES");
            title.setFont(new Font("Segoe UI", Font.BOLD, 22));
            title.setBounds(30, 10, 300, 30);
            add(title);

            model = new DefaultTableModel(
                new String[]{"Name","Type","Price","Average"},0
            );
            table = new JTable(model);
            table.setRowHeight(28);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

            JScrollPane sp = new JScrollPane(table);
            sp.setBounds(30, 50, 550, 350);
            add(sp);

            refresh();

            JPanel form = new JPanel(null);
            form.setBounds(620, 50, 330, 350);
            form.setBackground(new Color(245,245,245));
            add(form);

            form.add(label("Vehicle Name",20,20));
            vname = field(150,20); form.add(vname);

            form.add(label("How Many Days",20,60));
            days = field(150,60); form.add(days);

            form.add(label("Pickup Date",20,100));
            pickup = field(150,100); form.add(pickup);

            form.add(label("Return Date",20,140));
            ret = field(150,140); form.add(ret);

            form.add(label("City",20,180));
            city = field(150,180); form.add(city);

            book = button("BOOK VEHICLE",20,230,new Color(33,150,243));
            bill = button("GENERATE BILL",170,230,new Color(76,175,80));
            clear = button("CLEAR",20,280,new Color(255,152,0));
            logout = button("LOGOUT",170,280,new Color(244,67,54));

            form.add(book); form.add(bill);
            form.add(clear); form.add(logout);

            book.addActionListener(this);
            bill.addActionListener(this);
            clear.addActionListener(this);
            logout.addActionListener(this);

            setVisible(true);
        }

        JLabel label(String t, int x, int y) {
            JLabel l = new JLabel(t);
            l.setBounds(x, y, 120, 25);
            return l;
        }

        JTextField field(int x, int y) {
            JTextField f = new JTextField();
            f.setBounds(x, y, 150, 28);
            return f;
        }

        JButton button(String t, int x, int y, Color c) {
            JButton b = new JButton(t);
            b.setBounds(x, y, 140, 35);
            b.setBackground(c);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            return b;
        }

        void refresh() {
            model.setRowCount(0);
            for (Vehicle v : vehicles) {
                if (v.available)
                    model.addRow(new Object[]{
                        v.name, v.type, v.price, v.average
                    });
            }
        }

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == book) {
                for (Vehicle v : vehicles) {
                    if (v.name.equalsIgnoreCase(vname.getText()) && v.available) {
                        v.available = false;
                        selectedVehicle = v;

                        lastBooking = new Booking(
                            "user", v.name,
                            Integer.parseInt(days.getText()),
                            city.getText(),
                            pickup.getText(),
                            ret.getText()
                        );
                        bookings.add(lastBooking);
                        refresh();
                        JOptionPane.showMessageDialog(this,"Vehicle Booked!");
                        return;
                    }
                }
            }

            if (e.getSource() == bill && selectedVehicle != null) {
                int total = selectedVehicle.price * lastBooking.days;
                JOptionPane.showMessageDialog(this,
                    "🧾 VEHICLE RENTAL BILL\n\n" +
                    "Vehicle : " + selectedVehicle.name + "\n" +
                    "Type    : " + selectedVehicle.type + "\n" +
                    "Price   : Rs." + selectedVehicle.price + "/day\n" +
                    "Average : " + selectedVehicle.average + "\n\n" +
                    "Days    : " + lastBooking.days + "\n" +
                    "Pickup  : " + lastBooking.pickup + "\n" +
                    "Return  : " + lastBooking.ret + "\n" +
                    "City    : " + lastBooking.city + "\n" +
                    "---------------------------\n" +
                    "TOTAL   : Rs." + total
                );
            }

            if (e.getSource() == clear) {
                vname.setText(""); days.setText("");
                pickup.setText(""); ret.setText(""); city.setText("");
            }

            if (e.getSource() == logout) {
                new Login();
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
