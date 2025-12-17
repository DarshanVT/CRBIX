package fertilizerForPlants;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FertilizerSuggestionApp extends JFrame {

    private JTextField txtFarmerName, txtLandSize;
    private JComboBox<String> cmbCrop, cmbSoil;
    private JRadioButton rbLow, rbMedium, rbHigh;
    private JTextArea resultArea;

    private Connection con;

    private int fertilizerKg = 0;
    private int fertilizerPrice = 0;

    public FertilizerSuggestionApp() {

        setTitle("Fertilizer Suggestion App");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JLabel title = new JLabel("Organic Fertilizer Suggestion System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0,102,51));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Farmer & Crop Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0;
        form.add(new JLabel("Farmer Name:"), g);
        g.gridx=1;
        txtFarmerName = new JTextField();
        form.add(txtFarmerName, g);

        g.gridx=0; g.gridy++;
        form.add(new JLabel("Land Size (Guntha):"), g);
        g.gridx=1;
        txtLandSize = new JTextField();
        form.add(txtLandSize, g);

        g.gridx=0; g.gridy++;
        form.add(new JLabel("Crop Type:"), g);
        g.gridx=1;
        cmbCrop = new JComboBox<>(new String[]{"Rice","Wheat","Vegetables","Sugarcane","Other"});
        form.add(cmbCrop, g);

        g.gridx=0; g.gridy++;
        form.add(new JLabel("Soil Type:"), g);
        g.gridx=1;
        cmbSoil = new JComboBox<>(new String[]{"Black","Red","Alluvial","Sandy"});
        form.add(cmbSoil, g);

        g.gridx=0; g.gridy++;
        form.add(new JLabel("Nitrogen Level:"), g);
        g.gridx=1;
        rbLow = new JRadioButton("Low");
        rbMedium = new JRadioButton("Medium");
        rbHigh = new JRadioButton("High");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbLow); bg.add(rbMedium); bg.add(rbHigh);
        JPanel npkPanel = new JPanel();
        npkPanel.add(rbLow); npkPanel.add(rbMedium); npkPanel.add(rbHigh);
        form.add(npkPanel, g);

        add(form, BorderLayout.WEST);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createTitledBorder("Fertilizer Suggestion"));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton btnSuggest = new JButton("Suggest Fertilizer");
        JButton btnSave = new JButton("Save Record");
        JButton btnReset = new JButton("Reset");

        buttons.add(btnSuggest);
        buttons.add(btnSave);
        buttons.add(btnReset);
        add(buttons, BorderLayout.SOUTH);

        btnSuggest.addActionListener(e -> suggestFertilizer());
        btnSave.addActionListener(e -> saveToDB());
        btnReset.addActionListener(e -> resetForm());

        connectDB();
    }

    private void connectDB() {
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/fertilizer_db","root","Darshan@7742");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database not connected");
        }
    }
    private void suggestFertilizer() {

        String crop = cmbCrop.getSelectedItem().toString();
        String soil = cmbSoil.getSelectedItem().toString();
        String nitrogen = rbLow.isSelected() ? "Low" :
                          rbMedium.isSelected() ? "Medium" : "High";

        int guntha = Integer.parseInt(txtLandSize.getText());

        String fertilizer = "";

        if     (crop.equals("Rice") && nitrogen.equals("Low")) fertilizer = "Manure";
        else if(crop.equals("Rice") && nitrogen.equals("Medium")) fertilizer = "Blood Meal";
        else if(crop.equals("Rice") && nitrogen.equals("High")) fertilizer = "Alfalfa Meal";

        else if(crop.equals("Wheat") && nitrogen.equals("Low")) fertilizer = "Corn Gluten Meal";
        else if(crop.equals("Wheat") && nitrogen.equals("Medium")) fertilizer = "Wood Ash";
        else if(crop.equals("Wheat") && nitrogen.equals("High")) fertilizer = "Worm Castings";

        else if(crop.equals("Sugarcane") && nitrogen.equals("Low")) fertilizer = "Bat Guano";
        else if(crop.equals("Sugarcane") && nitrogen.equals("Medium")) fertilizer = "Green Manure";
        else if(crop.equals("Sugarcane") && nitrogen.equals("High")) fertilizer = "Compost";

        else if(crop.equals("Vegetables") && nitrogen.equals("Low")) fertilizer = "Rock Phosphate";
        else if(crop.equals("Vegetables") && nitrogen.equals("Medium")) fertilizer = "Green Sand";
        else if(crop.equals("Vegetables") && nitrogen.equals("High")) fertilizer = "Seaweed Meal";

        else fertilizer = "Cyanobacterial Fertilizer";

        
        fertilizerKg = guntha / 2;
        fertilizerPrice = fertilizerKg * 50;

        resultArea.setText(
            "Farmer Name: " + txtFarmerName.getText() +
            "\nLand Size: " + guntha + " Guntha" +
            "\nCrop: " + crop +
            "\nSoil: " + soil +
            "\nNitrogen Level: " + nitrogen +
            "\n\nSuggested Fertilizer:\n👉 " + fertilizer +
            "\n\nRequired Quantity: " + fertilizerKg + " kg" +
            "\nEstimated Cost: Rs. " + fertilizerPrice
        );
    }

    private void saveToDB() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO farmer_records " +
                "(farmer_name, crop, soil, land_size, suggestion, record_date, quantity_kg, price_rs) " +
                "VALUES (?,?,?,?,?,?,?,?)"
            );

            ps.setString(1, txtFarmerName.getText());
            ps.setString(2, cmbCrop.getSelectedItem().toString());
            ps.setString(3, cmbSoil.getSelectedItem().toString());
            ps.setString(4, txtLandSize.getText());
            ps.setString(5, resultArea.getText());
            ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            ps.setInt(7, fertilizerKg);
            ps.setInt(8, fertilizerPrice);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record Saved Successfully ✅");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving record: " + e.getMessage());
        }
    }

    private void resetForm() {
        txtFarmerName.setText("");
        txtLandSize.setText("");
        rbLow.setSelected(false);
        rbMedium.setSelected(false);
        rbHigh.setSelected(false);
        resultArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FertilizerSuggestionApp().setVisible(true));
    }
}
