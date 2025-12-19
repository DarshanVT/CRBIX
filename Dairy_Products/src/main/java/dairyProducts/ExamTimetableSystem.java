package dairyProducts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExamTimetableSystem extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    JTextField txtTeacher;
    JPasswordField txtPass;
    JComboBox<String> cbRole;

    JTextField txtSubject, txtDate, txtTime, txtDuration;
    JComboBox<String> cbExamType;
    JTable examTable;
    DefaultTableModel examModel;

    JTextField txtRoll, txtStudent;
    JTable studentTable;
    DefaultTableModel studentModel;

    JTable attendanceTable;
    DefaultTableModel attendanceModel;
    
    JTable seatingTable;
    DefaultTableModel seatingModel;

    ArrayList<LocalDate> holidays = new ArrayList<>();

    public ExamTimetableSystem() {
        setTitle("Exam Management System");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(loginPanel(), "LOGIN");
        mainPanel.add(dashboardPanel(), "DASHBOARD");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");

        setVisible(true);
    }

    // ---------------- LOGIN PANEL ----------------
    JPanel loginPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(240, 248, 255));

        JLabel h = new JLabel("🔐 Teacher Login");
        h.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h.setBounds(150, 30, 300, 30);
        p.add(h);

        p.add(label("Name", 80, 100));
        txtTeacher = field(200, 100);
        p.add(txtTeacher);

        p.add(label("Password", 80, 150));
        txtPass = new JPasswordField();
        txtPass.setBounds(200, 150, 180, 25);
        p.add(txtPass);

        p.add(label("Post", 80, 200));
        cbRole = new JComboBox<>(new String[]{
                "Select", "Principal", "Vice Principal", "Subject Teacher"
        });
        cbRole.setBounds(200, 200, 180, 25);
        p.add(cbRole);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(180, 260, 120, 35);
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setForeground(Color.WHITE);
        p.add(btnLogin);

        btnLogin.addActionListener(e -> {
            if (cbRole.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select role");
                return;
            }
            cardLayout.show(mainPanel, "DASHBOARD");
        });

        return p;
    }

    // ---------------- DASHBOARD PANEL ----------------
    JPanel dashboardPanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(245, 250, 255));

        JLabel h = new JLabel("📚 Exam + Attendance + Seating System");
        h.setFont(new Font("Segoe UI", Font.BOLD, 22));
        h.setBounds(350, 10, 600, 30);
        p.add(h);

        // EXAM INPUT
        p.add(label("Subject", 20, 60));
        txtSubject = field(120, 60);
        p.add(txtSubject);

        p.add(label("Date (YYYY-MM-DD)", 20, 95));
        txtDate = field(120, 95);
        p.add(txtDate);

        p.add(label("Time", 20, 130));
        txtTime = field(120, 130);
        p.add(txtTime);

        p.add(label("Duration (hrs)", 20, 165));
        txtDuration = field(120, 165);
        p.add(txtDuration);

        cbExamType = new JComboBox<>(new String[]{"Internal", "Final"});
        cbExamType.setBounds(120, 200, 180, 25);
        p.add(cbExamType);

        JButton addExam = btn("Add Exam", 320, 70);
        p.add(addExam);

        examModel = new DefaultTableModel(
                new String[]{"Subject", "Date", "Time", "Duration", "Type"}, 0);
        examTable = new JTable(examModel);
        JScrollPane exScroll = new JScrollPane(examTable);
        exScroll.setBounds(20, 240, 500, 180);
        p.add(exScroll);

        // STUDENT
        p.add(label("Roll No", 550, 60));
        txtRoll = field(650, 60);
        p.add(txtRoll);

        p.add(label("Student Name", 550, 95));
        txtStudent = field(650, 95);
        p.add(txtStudent);

        JButton addStudent = btn("Add Student", 860, 75);
        p.add(addStudent);

        studentModel = new DefaultTableModel(
                new String[]{"Roll", "Name"}, 0);
        studentTable = new JTable(studentModel);
        JScrollPane stScroll = new JScrollPane(studentTable);
        stScroll.setBounds(550, 140, 500, 130);
        p.add(stScroll);

        // ATTENDANCE
        JButton present = btn("Present", 200, 440);
        JButton absent = btn("Absent", 330, 440);
        p.add(present);
        p.add(absent);

        attendanceModel = new DefaultTableModel(
                new String[]{"Roll", "Name", "Status"}, 0);
        attendanceTable = new JTable(attendanceModel);
        JScrollPane atScroll = new JScrollPane(attendanceTable);
        atScroll.setBounds(20, 480, 500, 150);
        p.add(atScroll);

        // SEATING
        JButton seatBtn = btn("Generate Seating", 700, 300);
        p.add(seatBtn);

        seatingModel = new DefaultTableModel(
                new String[]{"Roll", "Name", "Room", "Bench", "Seat"}, 0);
        seatingTable = new JTable(seatingModel);
        JScrollPane seScroll = new JScrollPane(seatingTable);
        seScroll.setBounds(550, 340, 500, 290);
        p.add(seScroll);

        // EVENTS
        addExam.addActionListener(e -> addExam());
        addStudent.addActionListener(e -> addStudent());
        present.addActionListener(e -> markAttendance("Present"));
        absent.addActionListener(e -> markAttendance("Absent"));
        seatBtn.addActionListener(e -> generateSeating());

        return p;
    }

    // ---------------- LOGIC ----------------
    void addExam() {
        try {
            LocalDate d = LocalDate.parse(txtDate.getText());
            if (d.getDayOfWeek() == DayOfWeek.SUNDAY || holidays.contains(d)) {
                JOptionPane.showMessageDialog(this, "Holiday / Sunday!");
                return;
            }

            examModel.addRow(new Object[]{
                    txtSubject.getText(),
                    d,
                    txtTime.getText(),
                    txtDuration.getText() + " hrs",
                    cbExamType.getSelectedItem()
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format");
        }
    }

    void addStudent() {
        studentModel.addRow(new Object[]{
                txtRoll.getText(),
                txtStudent.getText()
        });
        attendanceModel.addRow(new Object[]{
                txtRoll.getText(),
                txtStudent.getText(),
                "Not Marked"
        });
    }

    void markAttendance(String status) {
        int r = attendanceTable.getSelectedRow();
        if (r != -1)
            attendanceModel.setValueAt(status, r, 2);
    }

    void generateSeating() {
        seatingModel.setRowCount(0);
        int room = 1, bench = 1, seat = 1;

        for (int i = 0; i < studentModel.getRowCount(); i++) {
            seatingModel.addRow(new Object[]{
                    studentModel.getValueAt(i, 0),
                    studentModel.getValueAt(i, 1),
                    "Room " + room,
                    bench,
                    seat
            });
            seat++;
            if (seat > 2) { seat = 1; bench++; }
            if (bench > 10) { bench = 1; room++; }
        }
    }

    // ---------------- HELPERS ----------------
    JLabel label(String t, int x, int y) {
        JLabel l = new JLabel(t);
        l.setBounds(x, y, 200, 25);
        return l;
    }

    JTextField field(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 180, 25);
        return f;
    }

    JButton btn(String t, int x, int y) {
        JButton b = new JButton(t);
        b.setBounds(x, y, 150, 35);
        b.setBackground(new Color(33, 150, 243));
        b.setForeground(Color.WHITE);
        return b;
    }

    public static void main(String[] args) {
        new ExamTimetableSystem();
    }
}
