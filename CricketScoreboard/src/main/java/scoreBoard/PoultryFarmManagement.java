package scoreBoard;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class PoultryFarmManagement extends JFrame {

    private JTextField txtBirdsAdded, txtBuyPrice, txtSellPrice;
    private JTextField txtMortality, txtFeedKg, txtSellQty;
    private JTextField txtTotalEggs, txtBrokenEggs;
    private JComboBox<String> cmbFeedTime, cmbBirdType;
    private JLabel lblAliveBirds, lblDailyPL, lblMonthlyPL;
    private JTextArea logArea;

    private int aliveBirds = 0;
    private int totalCost = 0;
    private int totalIncome = 0;
    private int monthlyProfit = 0;
    private LocalDate currentMonth = LocalDate.now();

    public PoultryFarmManagement() {
        setTitle(" Poultry Farm Management System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        JLabel title = new JLabel("Poultry Farm Management", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(0,102,51));
        add(title, BorderLayout.NORTH);

        JPanel left = new JPanel(new GridBagLayout());
        left.setBorder(BorderFactory.createTitledBorder("Farm Operations"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8,8,8,8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx=0; g.gridy=0;
        left.add(new JLabel("Bird Type:"), g);
        g.gridx=1;
        cmbBirdType = new JComboBox<>(new String[]{"Broiler","Layer"});
        left.add(cmbBirdType, g);
        
        g.gridx=0; g.gridy++;
        left.add(new JLabel("Birds Added:"), g);
        g.gridx=1;
        txtBirdsAdded = new JTextField();
        left.add(txtBirdsAdded, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Buy Bird:"), g);
        g.gridx=1;
        txtBuyPrice = new JTextField();
        left.add(txtBuyPrice, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Mortality (Deaths):"), g);
        g.gridx=1;
        txtMortality = new JTextField();
        left.add(txtMortality, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Feed Used (kg):"), g);
        g.gridx=1;
        txtFeedKg = new JTextField();
        left.add(txtFeedKg, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Feed Time:"), g);
        g.gridx=1;
        cmbFeedTime = new JComboBox<>(new String[]{"Morning","Afternoon","Evening","Night"});
        left.add(cmbFeedTime, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Sell Quantity:"), g);
        g.gridx=1;
        txtSellQty = new JTextField();
        left.add(txtSellQty, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Sell Price / Bird:"), g);
        g.gridx=1;
        txtSellPrice = new JTextField();
        left.add(txtSellPrice, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Total Eggs:"), g);
        g.gridx=1;
        txtTotalEggs = new JTextField();
        left.add(txtTotalEggs, g);

        g.gridx=0; g.gridy++;
        left.add(new JLabel("Broken Eggs:"), g);
        g.gridx=1;
        txtBrokenEggs = new JTextField();
        left.add(txtBrokenEggs, g);

        JButton btnAddBirds = new JButton("Add Birds");
        JButton btnFeed = new JButton("Add Feed");
        JButton btnSell = new JButton("Sell Birds");
        JButton btnEggs = new JButton("Calculate Eggs");
        JButton btnResetDay = new JButton("Reset Day");

        g.gridx=0; g.gridy++; g.gridwidth=2;
        left.add(btnAddBirds, g);
        g.gridy++; left.add(btnFeed, g);
        g.gridy++; left.add(btnSell, g);
        g.gridy++; left.add(btnEggs, g);
        g.gridy++; left.add(btnResetDay, g);

        add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Farm Status"));

        lblAliveBirds = new JLabel("Alive Birds: 0");
        lblAliveBirds.setFont(new Font("Arial", Font.BOLD, 18));

        lblDailyPL = new JLabel("Daily Profit/Loss: Rs. 0");
        lblDailyPL.setFont(new Font("Arial", Font.BOLD, 16));

        lblMonthlyPL = new JLabel("Monthly Profit/Loss: Rs. 0");
        lblMonthlyPL.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel stats = new JPanel(new GridLayout(3,1,5,5));
        stats.add(lblAliveBirds);
        stats.add(lblDailyPL);
        stats.add(lblMonthlyPL);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createTitledBorder("Daily Log"));

        right.add(stats, BorderLayout.NORTH);
        right.add(new JScrollPane(logArea), BorderLayout.CENTER);

        add(right, BorderLayout.CENTER);

        btnAddBirds.addActionListener(e -> addBirds());
        btnFeed.addActionListener(e -> addFeed());
        btnSell.addActionListener(e -> sellBirds());
        btnEggs.addActionListener(e -> calculateEggs());
        btnResetDay.addActionListener(e -> resetDay());
    }

    private void addBirds() {
        int birds = Integer.parseInt(txtBirdsAdded.getText());
        int buy = Integer.parseInt(txtBuyPrice.getText());
        int dead = txtMortality.getText().isEmpty()?0:Integer.parseInt(txtMortality.getText());

        aliveBirds += birds - dead;
        int cost = birds * buy;
        int mortalityLoss = dead * buy;
        totalCost += cost;

        logArea.append("Added "+birds+" | Dead "+dead+" | Loss Rs."+mortalityLoss+"\n");
        updatePL();
    }

    private void addFeed() {
        int kg = Integer.parseInt(txtFeedKg.getText());
        int cost = kg * 30;
        totalCost += cost;
        logArea.append("Feed "+kg+"kg ("+cmbFeedTime.getSelectedItem()+") Cost Rs."+cost+"\n");
        updatePL();
    }

    private void sellBirds() {
        int qty = Integer.parseInt(txtSellQty.getText());
        int price = Integer.parseInt(txtSellPrice.getText());

        if(qty > aliveBirds){
            JOptionPane.showMessageDialog(this,"Not enough birds to sell");
            return;
        }

        int income = qty * price;
        totalIncome += income;
        aliveBirds -= qty;

        logArea.append("Sold "+qty+" birds Rs."+price+" Income Rs."+income+"\n");
        updatePL();
    }

    private void calculateEggs() {
        if(!cmbBirdType.getSelectedItem().equals("Layer")) return;

        int total = Integer.parseInt(txtTotalEggs.getText());
        int broken = Integer.parseInt(txtBrokenEggs.getText());
        int sellable = total - broken;
        int income = sellable * 6;

        totalIncome += income;
        logArea.append("Eggs: "+sellable+" sold | Income Rs."+income+"\n");
        updatePL();
    }

    private void updatePL() {
        int dailyPL = totalIncome - totalCost;
        lblAliveBirds.setText("Alive Birds: " + aliveBirds);
        lblDailyPL.setText("Daily Profit/Loss: Rs. " + dailyPL);
        lblDailyPL.setForeground(dailyPL>=0?new Color(0,128,0):Color.RED);
    }

    private void resetDay() {
        monthlyProfit += totalIncome - totalCost;
        lblMonthlyPL.setText("Monthly Profit/Loss: Rs. " + monthlyProfit);
        totalCost = 0;
        totalIncome = 0;
        logArea.append("---- Day Closed ----\n");
        txtBirdsAdded.setText("");
        txtBuyPrice.setText("");
        txtSellPrice.setText("");
        txtMortality.setText("");
        txtFeedKg.setText("");
        txtSellQty.setText("");
        txtTotalEggs.setText("");
        txtBrokenEggs.setText("");
        updatePL();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PoultryFarmManagement().setVisible(true));
    }
}
