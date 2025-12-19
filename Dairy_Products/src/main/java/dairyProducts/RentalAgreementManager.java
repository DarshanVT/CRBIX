package dairyProducts;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class RentalAgreementManager extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    Map<String, String> data = new LinkedHashMap<>();

    JTextField ownerName, tenantName, location, sqft, rent, deposit, startDate, endDate;
    JComboBox<String> propertyType, duration;
    JRadioButton depYes, depNo, maintOwner, maintTenant, lightOwner, lightTenant,
            stampOwner, stampTenant, regOwner, regTenant;

    JTextArea propertyAddress;
    JRadioButton parkingYes, parkingNo;

    JTextField oName, oPhone, oAadhar, oPan, oEmail, oDob;
    JTextArea oAddress;

    JTextField tName, tPhone, tAadhar, tPan, tEmail, tDob;
    JTextArea tAddress;

    JTextArea summaryArea;

    public RentalAgreementManager() {
        setTitle("Rental Agreement Manager");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopDashboard(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(agreementPanel(), "1");
        mainPanel.add(propertyPanel(), "2");
        mainPanel.add(ownerPanel(), "3");
        mainPanel.add(tenantPanel(), "4");
        mainPanel.add(summaryPanel(), "5");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    JPanel createTopDashboard() {
        JPanel p = new JPanel(new GridLayout(1,5,15,0));
        p.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        p.setBackground(new Color(28,32,68));

        String[] steps = {
                "1 Agreement Details","2 Property Details",
                "3 Owner Details","4 Tenant Details","5 Summary"
        };

        for(int i=0;i<steps.length;i++){
            JButton b = new JButton(steps[i]);
            b.setFocusPainted(false);
            b.setBackground(new Color(52,57,110));
            b.setForeground(Color.WHITE);
            int idx = i+1;
            b.addActionListener(e -> cardLayout.show(mainPanel, String.valueOf(idx)));
            p.add(b);
        }
        return p;
    }

    JPanel agreementPanel() {
        JPanel p = basePanel();
        JLabel title = new JLabel("AGREEMENT DETAILS", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 10, 1200, 40);
        p.add(title);

        ownerName = field(p,"Owner Name",20,50);
        tenantName = field(p,"Tenant Name",400,50);

        propertyType = combo(p,"Property Type",20,100,new String[]{"Flat","Building","Shop","Banglow"});
        location = field(p,"Property Location",400,100);

        sqft = field(p,"Square Feet",20,150);
        duration = combo(p,"Duration",400,150,new String[]{"6 Months","11 Months","23 Months","Other"});

        rent = field(p,"Monthly Rent",20,200);
        deposit = field(p,"Deposit Amount",400,200);

        depYes = radio(p,"Deposit Refundable : Yes",20,260);
        depNo  = radio(p,"Deposit Refundable : No",250,260);
        group(depYes,depNo);

        maintOwner = radio(p,"Maintenance paid  : Owner",20,300);
        maintTenant= radio(p,"Maintenance paid  : Tenant",250,300);
        group(maintOwner,maintTenant);

        lightOwner = radio(p,"Light Bill paid by : Owner",20,340);
        lightTenant= radio(p,"Light Bill paid by : Tenant",250,340);
        group(lightOwner,lightTenant);

        stampOwner = radio(p,"Stamp paid by : Owner",20,380);
        stampTenant= radio(p,"Stamp paid by : Tenant",250,380);
        group(stampOwner,stampTenant);

        regOwner = radio(p,"Registration by : Owner",20,420);
        regTenant= radio(p,"Registration by : Tenant",250,420);
        group(regOwner,regTenant);

        startDate = field(p,"Agreement Start Date",20,470);
        endDate   = field(p,"Agreement End Date",450,470);

        p.add(nextBtn("Next →", e -> cardLayout.show(mainPanel,"2")));
        return p;
    }

    JPanel propertyPanel() {
        JPanel p = basePanel();
        JLabel title = new JLabel("PROPERTY DETAILS", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 10, 1200, 40);
        p.add(title);
        propertyAddress = area(p,"Property's Full Address",20,50);

        parkingYes = radio(p,"Parking Included : Yes",20,220);
        parkingNo  = radio(p,"Parking Included : No",200,220);
        group(parkingYes,parkingNo);

        p.add(backBtn(e->cardLayout.show(mainPanel,"1")));
        p.add(nextBtn("Next →", e->cardLayout.show(mainPanel,"3")));
        return p;
    }

    JPanel ownerPanel() {
        JPanel p = basePanel();
        JLabel title = new JLabel("OWNER'S DETAILS", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 10, 1200, 40);
        p.add(title);

        oName = field(p,"Owner Name",20,60);
        oPhone = field(p,"Phone NO",400,60);
        oAadhar = field(p,"Aadhar NO",20,110);
        oPan = field(p,"PAN NO",400,110);
        oEmail = field(p,"Email",20,160);
        oDob = field(p,"DOB",400,160);
        oAddress = area(p,"Owner's Full Address",20,220);

        p.add(backBtn(e->cardLayout.show(mainPanel,"2")));
        p.add(nextBtn("Next →", e->cardLayout.show(mainPanel,"4")));
        return p;
    }

    JPanel tenantPanel() {
        JPanel p = basePanel();
        JLabel title = new JLabel("TENANT'S DETAILS", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 10, 1200, 40);
        p.add(title);

        tName = field(p,"Tenant Name",20,60);
        tPhone = field(p,"Phone NO",400,60);
        tAadhar = field(p,"Aadhar NO",20,110);
        tPan = field(p,"PAN No",400,110);
        tEmail = field(p,"Email",20,160);
        tDob = field(p,"DOB",400,160);
        tAddress = area(p,"Tenant's Native Address",20,220);

        p.add(backBtn(e->cardLayout.show(mainPanel,"3")));
        p.add(nextBtn("Generate Summary →", e->generateSummary()));
        return p;
    }

    JPanel summaryPanel() {
        JPanel p = basePanel();
        JLabel title = new JLabel("AGREEMENT SUMMURY", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setBounds(0, 10, 1200, 40);
        p.add(title);
        summaryArea = new JTextArea();
        summaryArea.setEditable(false);    
        summaryArea.setFocusable(false); 
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        summaryArea.setBackground(Color.WHITE);
        summaryArea.setFont(new Font("Serif",Font.PLAIN,15));
        JScrollPane sp = new JScrollPane(summaryArea);
        sp.setBounds(20,50,1100,450);
        p.add(sp);

        JButton save = new JButton("SAVE AGREEMENT");
        save.setBounds(500,510,200,40);
        p.add(save);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(
                        RentalAgreementManager.this,
                        "Agreement Saved Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new javax.swing.Timer(2000, new ActionListener() {
                    public void actionPerformed(ActionEvent ev) {
                        cardLayout.show(mainPanel, "1");
                        ((Timer) ev.getSource()).stop();
                    }
                }).start();
            }
        });

        return p;
    }

    void generateSummary() {
  
    	ownerName.setText("");
    	tenantName.setText("");
    	location.setText("");
    	sqft.setText("");
    	rent.setText("");
    	deposit.setText("");
    	startDate.setText("");
    	endDate.setText("");
    	duration.setSelectedIndex(0);
    	propertyType.setSelectedIndex(0);

    	depYes.setSelected(false);
    	depNo.setSelected(false);
    	maintOwner.setSelected(false);
    	maintTenant.setSelected(false);
    	lightOwner.setSelected(false);
    	lightTenant.setSelected(false);
    	stampOwner.setSelected(false);
    	stampTenant.setSelected(false);
    	regOwner.setSelected(false);
    	regTenant.setSelected(false);

    	propertyAddress.setText("");
    	parkingYes.setSelected(false);
    	parkingNo.setSelected(false);

    	oName.setText("");
    	oPhone.setText("");
    	oAadhar.setText("");
    	oPan.setText("");
    	oEmail.setText("");
    	oDob.setText("");
    	oAddress.setText("");

    	tName.setText("");
    	tPhone.setText("");
    	tAadhar.setText("");
    	tPan.setText("");
    	tEmail.setText("");
    	tDob.setText("");
    	tAddress.setText("");

    	
    	String refundable = depYes.isSelected() ? "Yes" : "No";
    	String maintenance = maintOwner.isSelected() ? "Owner" : "Tenant";
    	String lightBill = lightOwner.isSelected() ? "Owner" : "Tenant";
    	String stamp = stampOwner.isSelected() ? "Owner" : "Tenant";
    	String registration = regOwner.isSelected() ? "Owner" : "Tenant";
        summaryArea.setText(
                "RENTAL AGREEMENT\n\n" +
                "Owner Name: "+ownerName.getText()+"\n" +
                "Tenant Name: "+tenantName.getText()+"\n\n" +
                "Property Type: "+propertyType.getSelectedItem()+"\n" +
                "Property Location: "+location.getText()+"\n" +
                "Property Area: "+sqft.getText()+" sq.ft\n\n" +
                "Property Address:\n" + propertyAddress.getText() + "\n\n"+
                "Rent: Rs."+rent.getText()+"\n" +
                "Deposit: Rs."+deposit.getText()+"\n" +
                "Duration: "+duration.getSelectedItem()+"\n\n" +
                "Start Date: "+startDate.getText()+"\n" +
                "End Date: "+endDate.getText()+"\n\n" +
                "Deposit Refundable : " + refundable + "\n" +
                "Maintenance Paid By : " + maintenance + "\n" +
                "Light Bill Paid By : " + lightBill + "\n" +
                "Stamp Duty Paid By : " + stamp + "\n" +
                "Registration Fees Paid By : " + registration + "\n\n"+
                "Owner Signature: ____________\nTenant Signature: ____________"
        );
        cardLayout.show(mainPanel,"5");
    }

    JPanel basePanel() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(245,246,250));
        return p;
    }

    JTextField field(JPanel p,String l,int x,int y){
        JLabel lab = new JLabel(l);
        lab.setBounds(x,y,150,25);
        JTextField f = new JTextField();
        f.setBounds(x,y+25,300,28);
        p.add(lab); p.add(f);
        return f;
    }

    JComboBox<String> combo(JPanel p,String l,int x,int y,String[] d){
        JLabel lab = new JLabel(l);
        lab.setBounds(x,y,150,25);
        JComboBox<String> c = new JComboBox<>(d);
        c.setBounds(x,y+25,300,28);
        p.add(lab); p.add(c);
        return c;
    }

    JTextArea area(JPanel p,String l,int x,int y){
        JLabel lab = new JLabel(l);
        lab.setBounds(x,y,150,25);
        JTextArea a = new JTextArea();
        JScrollPane sp = new JScrollPane(a);
        sp.setBounds(x,y+25,680,100);
        p.add(lab); p.add(sp);
        return a;
    }

    JRadioButton radio(JPanel p,String t,int x,int y){
        JRadioButton r = new JRadioButton(t);
        r.setBounds(x,y,180,25);
        p.add(r);
        return r;
    }

    void group(AbstractButton... b){
        ButtonGroup g = new ButtonGroup();
        for(AbstractButton x:b) g.add(x);
    }

    JButton nextBtn(String t,ActionListener a){
        JButton b = new JButton(t);
        b.setBounds(950,520,200,40);
        b.addActionListener(a);
        return b;
    }

    JButton backBtn(ActionListener a){
        JButton b = new JButton("← Back");
        b.setBounds(20,520,150,40);
        b.addActionListener(a);
        return b;
    }

    public static void main(String[] args) {
        new RentalAgreementManager();
    }
}