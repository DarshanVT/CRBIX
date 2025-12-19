package dairyProducts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;



public class ExamSystem extends JFrame {

    CardLayout layout = new CardLayout();
    JPanel container = new JPanel(layout);

    Font titleFont = new Font("Segoe UI", Font.BOLD, 20);
    Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font btnFont = new Font("Segoe UI", Font.BOLD, 13);

    Color bg = new Color(245, 247, 250);
    Color primary = new Color(33, 150, 243);
    Color danger = new Color(211, 47, 47);

    DefaultTableModel timetableModel =
            new DefaultTableModel(new String[]{"Subject", "Date(dd-mm-yyyy)", "Time", "Duration(in hrs)"}, 0);

    DefaultTableModel studentModel =
            new DefaultTableModel(new String[]{"Roll No", "Student Name"}, 0);

    DefaultTableModel attendanceModel =
            new DefaultTableModel(new String[]{"Roll No", "Name", "Date", "Status", "Seat"}, 0);

    HashMap<String, String> seatingMap = new HashMap<>();

    JTextField txtId = new JTextField();
    JTextField txtName = new JTextField();
    JComboBox<String> cbRole = new JComboBox<>(
            new String[]{"Select Role", "Principal", "Vice Principal", "Subject Teacher"}
    );

    public ExamSystem() {
        setTitle("Exam Timetable Management System");
        setSize(580, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        container.add(loginPanel(), "login");
        container.add(dashboardPanel(), "dash");

        add(container);
        layout.show(container, "login");
        setVisible(true);
    }

    JButton styledButton(String text, Color c) {
        JButton b = new JButton(text);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(btnFont);
        b.setFocusPainted(false);
        return b;
    }

    JPanel loginPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(bg);

        JLabel t = new JLabel("Teacher Login", SwingConstants.CENTER);
        t.setBounds(180, 30, 220, 35);
        t.setFont(titleFont);
        p.add(t);

        addField(p, "Teacher ID:", txtId, 90);
        addField(p, "Name:", txtName, 130);

        JLabel r = new JLabel("Role:");
        r.setBounds(150, 170, 100, 25);
        cbRole.setBounds(250, 170, 180, 25);
        p.add(r);
        p.add(cbRole);

        JButton login = styledButton("Login", primary);
        login.setBounds(230, 220, 120, 35);
        p.add(login);

        login.addActionListener(e -> {
            if (txtId.getText().isEmpty() ||
                    txtName.getText().isEmpty() ||
                    cbRole.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Fill all login details");
                return;
            }
            layout.show(container, "dash");
        });

        return p;
    }

    JPanel dashboardPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(bg);

        JLabel h = new JLabel("Dashboard", SwingConstants.CENTER);
        h.setBounds(180, 20, 220, 35);
        h.setFont(titleFont);
        p.add(h);

        JButton exam = styledButton("Exam / Timetable", primary);
        JButton student = styledButton("Student Management", primary);
        JButton attendance = styledButton("Attendance System", primary);
        JButton seating = styledButton("Seating Arrangement", primary);
        JButton logout = styledButton("Logout", danger);

        exam.setBounds(170, 80, 240, 35);
        student.setBounds(170, 125, 240, 35);
        attendance.setBounds(170, 170, 240, 35);
        seating.setBounds(170, 215, 240, 35);
        logout.setBounds(170, 260, 240, 35);

        p.add(exam); p.add(student); p.add(attendance); p.add(seating); p.add(logout);

        exam.addActionListener(e -> openTimetableWindow());
        student.addActionListener(e -> openStudentWindow());
        attendance.addActionListener(e -> openAttendanceWindow());
        seating.addActionListener(e -> openSeatingWindow());

        logout.addActionListener(e -> {
            txtId.setText("");
            txtName.setText("");
            cbRole.setSelectedIndex(0);
            layout.show(container, "login");
        });

        return p;
    }

    void openTimetableWindow() {
        JFrame f = baseFrame("Exam Timetable");

        JTextField subject = new JTextField();
        JTextField date = new JTextField();
        JTextField time = new JTextField();
        JTextField duration = new JTextField();

        addField(f, "Subject:", subject, 30);
        addField(f, "Date(DD-MM-YYYY):", date, 70);
        addField(f, "Time:", time, 110);
        addField(f, "Duration(in Hrs):", duration, 150);

        JButton add = styledButton("Add Exam", primary);
        JButton gen = styledButton("Generate Timetable", primary);

        add.setBounds(150, 190, 140, 30);
        gen.setBounds(300, 190, 190, 30);

        f.add(add); f.add(gen);

        JTable table = new JTable(timetableModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 240, 460, 140);
        f.add(sp);

        add.addActionListener(e -> {
            timetableModel.addRow(new Object[]{
                    subject.getText(), date.getText(), time.getText(), duration.getText()
            });
            subject.setText(""); date.setText(""); time.setText(""); duration.setText("");
        });

        gen.addActionListener(e -> {
            f.dispose();
            openStudentWindow();
        });

        f.setVisible(true);
    }

    void openStudentWindow() {
        JFrame f = baseFrame("Student Management");

        JTextField roll = new JTextField();
        JTextField name = new JTextField();

        addField(f, "Roll No:", roll, 40);
        addField(f, "Name:", name, 80);

        JButton add = styledButton("Add Student", primary);
        add.setBounds(200, 120, 160, 30);
        f.add(add);

        JTable table = new JTable(studentModel);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(30, 170, 460, 130);
        f.add(sp);

        JTable tt = new JTable(timetableModel);
        JScrollPane tsp = new JScrollPane(tt);
        tsp.setBounds(30, 310, 460, 120);
        f.add(tsp);

        add.addActionListener(e -> {
            studentModel.addRow(new Object[]{roll.getText(), name.getText()});
            roll.setText(""); name.setText("");
        });

        f.setVisible(true);
    }

    void openAttendanceWindow() {
        JFrame f = baseFrame("Student's Attendance");

        JTable st = new JTable(studentModel);
        JScrollPane sp1 = new JScrollPane(st);
        sp1.setBounds(30, 30, 460, 120);
        f.add(sp1);

        JRadioButton p = new JRadioButton("Present");
        JRadioButton a = new JRadioButton("Absent");
        ButtonGroup bgp = new ButtonGroup();
        bgp.add(p); bgp.add(a);

        p.setBounds(200, 160, 80, 25);
        a.setBounds(290, 160, 80, 25);
        f.add(p); f.add(a);

        JButton mark = styledButton("Mark Attendance", primary);
        mark.setBounds(180, 190, 200, 30);
        f.add(mark);

        JTable at = new JTable(attendanceModel);
        JScrollPane sp2 = new JScrollPane(at);
        sp2.setBounds(30, 235, 460, 130);
        f.add(sp2);

        mark.addActionListener(e -> {
            int r = st.getSelectedRow();
            if (r == -1) return;

            String roll = studentModel.getValueAt(r, 0).toString();
            String seat = seatingMap.getOrDefault(roll, "Not Assigned");

            attendanceModel.addRow(new Object[]{
                    roll,
                    studentModel.getValueAt(r, 1),
                    LocalDate.now(),
                    p.isSelected() ? "Present" : "Absent",
                    seat
            });
        });

        f.setVisible(true);
    }

    void openSeatingWindow() {
        JFrame f = baseFrame("Seating Arrangement");

        JPanel grid = new JPanel(new GridLayout(0, 5, 5, 5));
        JScrollPane sp = new JScrollPane(grid);
        sp.setBounds(30, 30, 460, 330);
        f.add(sp);

        grid.removeAll();
        for (int i = 0; i < studentModel.getRowCount(); i++) {
            String roll = studentModel.getValueAt(i, 0).toString();
            String seat = "Room 1 Bench " + (i + 1);
            seatingMap.put(roll, seat);
            grid.add(new JLabel(roll + " → " + seat, SwingConstants.CENTER));
        }

        f.setVisible(true);
    }

    JFrame baseFrame(String title) {
        JFrame f = new JFrame(title);
        f.setSize(540, 430);
        f.setLocationRelativeTo(this);
        f.setLayout(null);
        f.getContentPane().setBackground(bg);
        return f;
    }

    void addField(JPanel p, String lbl, JTextField t, int y) {
        JLabel l = new JLabel(lbl);
        l.setFont(labelFont);
        l.setBounds(150, y, 100, 25);
        t.setBounds(250, y, 180, 25);
        p.add(l); p.add(t);
    }

    void addField(JFrame f, String lbl, JTextField t, int y) {
        JLabel l = new JLabel(lbl);
        l.setFont(labelFont);
        l.setBounds(100, y, 100, 25);
        t.setBounds(200, y, 200, 25);
        f.add(l); f.add(t);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExamSystem::new);
    }
}
