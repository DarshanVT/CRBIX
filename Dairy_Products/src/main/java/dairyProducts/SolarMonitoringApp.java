package dairyProducts;

import javax.swing.*;
import java.awt.*;

public class SolarMonitoringApp extends JFrame {

    JCheckBox cbLight, cbFan, cbFridge, cbTV, cbWashing, cbAC;
    JLabel lblStatus, lblSource, lblBattery;
    JProgressBar solarBar, batteryBar;
    JRadioButton rbDay, rbNight;

    int SOLAR_CAPACITY = 3000;
    int BATTERY_CAPACITY = 2000;
    int batteryLevel = 2000;

    public SolarMonitoringApp() {

        setTitle("Smart Home Solar Energy Management System");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(null);
        p.setBackground(new Color(240, 248, 255));

        JLabel heading = new JLabel("SMART SOLAR HOME ENERGY SYSTEM");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(220, 20, 500, 30);
        p.add(heading);

        rbDay = new JRadioButton("Day Mode (Solar ON)", true);
        rbNight = new JRadioButton("Night Mode (Solar OFF)");
        rbDay.setBounds(50, 80, 200, 30);
        rbNight.setBounds(260, 80, 220, 30);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbDay);
        bg.add(rbNight);

        p.add(rbDay);
        p.add(rbNight);

        cbLight = new JCheckBox("Lights (100W)");
        cbFan = new JCheckBox("Fan (80W)");
        cbFridge = new JCheckBox("Fridge (200W)");
        cbTV = new JCheckBox("TV (150W)");
        cbWashing = new JCheckBox("Washing Machine (500W)");
        cbAC = new JCheckBox("AC (1500W)");

        JCheckBox[] arr = {cbLight, cbFan, cbFridge, cbTV, cbWashing, cbAC};
        int y = 130;
        for (JCheckBox cb : arr) {
            cb.setBounds(50, y, 250, 30);
            cb.setBackground(p.getBackground());
            p.add(cb);
            y += 35;
        }

        JButton btnCheck = new JButton("Check Power Status");
        btnCheck.setBounds(350, 160, 200, 40);
        p.add(btnCheck);

        solarBar = new JProgressBar(0, 100);
        solarBar.setBounds(50, 350, 750, 30);
        solarBar.setStringPainted(true);
        p.add(solarBar);

        batteryBar = new JProgressBar(0, 100);
        batteryBar.setBounds(50, 400, 750, 30);
        batteryBar.setStringPainted(true);
        p.add(batteryBar);

        lblSource = new JLabel("Power Source: ---");
        lblSource.setBounds(50, 450, 500, 25);
        lblSource.setFont(new Font("Arial", Font.BOLD, 15));
        p.add(lblSource);

        lblBattery = new JLabel("Battery Level: 100%");
        lblBattery.setBounds(50, 480, 500, 25);
        lblBattery.setFont(new Font("Arial", Font.BOLD, 15));
        p.add(lblBattery);

        lblStatus = new JLabel("System Status: Waiting...");
        lblStatus.setBounds(50, 520, 800, 30);
        lblStatus.setFont(new Font("Arial", Font.BOLD, 16));
        p.add(lblStatus);

        add(p);

        btnCheck.addActionListener(e -> calculate());
    }

    void calculate() {
        int load = 0;

        if (cbLight.isSelected()) load += 100;
        if (cbFan.isSelected()) load += 80;
        if (cbFridge.isSelected()) load += 200;
        if (cbTV.isSelected()) load += 150;
        if (cbWashing.isSelected()) load += 500;
        if (cbAC.isSelected()) load += 1500;

        boolean isDay = rbDay.isSelected();

        int availableSolar = isDay ? SOLAR_CAPACITY : 0;

        if (load <= availableSolar) {
            lblSource.setText("Power Source:  Solar Panel");
            solarBar.setValue((load * 100) / SOLAR_CAPACITY);
            lblStatus.setText(" All appliances running on Solar");
            lblStatus.setForeground(Color.GREEN.darker());

            if (batteryLevel < BATTERY_CAPACITY && isDay) {
                batteryLevel += 200;
                if (batteryLevel > BATTERY_CAPACITY)
                    batteryLevel = BATTERY_CAPACITY;
            }

        } else if (load <= availableSolar + batteryLevel) {
            lblSource.setText("Power Source: ☀ Solar + 🔋 Battery");
            batteryLevel -= (load - availableSolar);
            lblStatus.setText(" Running on Battery Backup");
            lblStatus.setForeground(Color.ORANGE);

        } else {
            autoDisable();
            lblSource.setText(" Power Limit Exceeded");
            lblStatus.setText(" Heavy appliances auto-disabled");
            lblStatus.setForeground(Color.RED);
        }

        batteryBar.setValue((batteryLevel * 100) / BATTERY_CAPACITY);
        lblBattery.setText("Battery Level: " + (batteryLevel * 100 / BATTERY_CAPACITY) + "%");
    }

    void autoDisable() {
        cbAC.setSelected(false);
        cbWashing.setSelected(false);
        cbTV.setSelected(false);
    }

    public static void main(String[] args) {
        new SolarMonitoringApp().setVisible(true);
    }
}
