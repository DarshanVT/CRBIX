package scoreBoard;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class PoultryFarmUI extends JFrame {


int totalBirds = 0;
int deadBirds = 0;
double totalFeedCost = 0;
double totalProfit = 0;


JTextArea output;


public PoultryFarmUI() {
setTitle("Poultry Farm Management System");
setSize(900, 600);
setLayout(null);
setDefaultCloseOperation(EXIT_ON_CLOSE);


JLabel title = new JLabel("🐔 Poultry Farm Management");
title.setFont(new Font("Arial", Font.BOLD, 22));
title.setBounds(300, 10, 400, 30);
add(title);


// Bird Type
JRadioButton broiler = new JRadioButton("Broiler");
JRadioButton layer = new JRadioButton("Layer");
ButtonGroup bg = new ButtonGroup();
bg.add(broiler);
bg.add(layer);
broiler.setBounds(50, 60, 100, 20);
layer.setBounds(160, 60, 100, 20);
add(broiler);
add(layer);


// Add Birds
JLabel qtyLbl = new JLabel("Bird Quantity:");
qtyLbl.setBounds(50, 90, 120, 20);
add(qtyLbl);


JTextField qtyField = new JTextField();
qtyField.setBounds(180, 90, 100, 20);
add(qtyField);


JLabel costLbl = new JLabel("Cost per Bird:");
costLbl.setBounds(50, 120, 120, 20);
add(costLbl);


JTextField costField = new JTextField();
costField.setBounds(180, 120, 100, 20);
add(costField);


JButton addBirdBtn = new JButton("Add Birds");
addBirdBtn.setBounds(50, 150, 150, 25);
add(addBirdBtn);


// Mortality
JLabel deathLbl = new JLabel("Death Count:");
deathLbl.setBounds(50, 190, 120, 20);
add(deathLbl);


JTextField deathField = new JTextField();
deathField.setBounds(180, 190, 100, 20);
add(deathField);


JButton deathBtn = new JButton("Update Mortality");
deathBtn.setBounds(50, 220, 180, 25);
add(deathBtn);


JLabel feedLbl = new JLabel("Feed Cost:");
feedLbl.setBounds(50, 260, 120, 20);
add(feedLbl);


JTextField feedField = new JTextField();
feedField.setBounds(180, 260, 100, 20);
add(feedField);


JComboBox<String> feedTime = new JComboBox<>(new String[]{"Morning","Afternoon","Evening","Night"});
feedTime.setBounds(300, 260, 120, 20);
add(feedTime);


JButton feedBtn = new JButton("Add Feed");
feedBtn.setBounds(50, 290, 150, 25);
add(feedBtn);


// Selling
JLabel sellLbl = new JLabel("Sell Price per Bird:");
sellLbl.setBounds(50, 330, 150, 20);
add(sellLbl);


JTextField sellField = new JTextField();
sellField.setBounds(210, 330, 100, 20);
add(sellField);


JButton sellBtn = new JButton("Sell Birds");
sellBtn.setBounds(50, 360, 150, 25);
add(sellBtn);


// Output Area
output = new JTextArea();
JScrollPane sp = new JScrollPane(output);
sp.setBounds(450, 60, 380, 450);
add(sp);


// Actions
addBirdBtn.addActionListener(e -> {
int qty = Integer.parseInt(qtyField.getText());
double cost = Double.parseDouble(costField.getText());
totalBirds += qty;
totalProfit -= qty * cost;
output.append("Birds Added: " + qty + "\n");
});


deathBtn.addActionListener(e -> {
int d = Integer.parseInt(deathField.getText());
deadBirds += d;
totalBirds -= d;
output.append("Deaths Today: " + d + "\n");
});


feedBtn.addActionListener(e -> {
double feed = Double.parseDouble(feedField.getText());
totalFeedCost += feed;
totalProfit -= feed;
output.append("Feed Added (" + feedTime.getSelectedItem() + "): ₹" + feed + "\n");
});

sellBtn.addActionListener(e -> {
double sell = Double.parseDouble(sellField.getText());
totalProfit += totalBirds * sell;
output.append("Birds Sold. Total Profit: ₹" + totalProfit + "\n");
totalBirds = 0;
});


setVisible(true);
}


public static void main(String[] args) {
new PoultryFarmUI();
}
}
