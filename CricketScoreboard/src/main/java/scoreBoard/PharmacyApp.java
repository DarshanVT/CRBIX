package scoreBoard;

import java.util.*;

class Medicine {
    String name;
    double costPrice;    
    double sellPrice;    
    int qty;

    Medicine(String name, double costPrice, double sellPrice, int qty) {
        this.name = name;
        this.costPrice = costPrice;
        this.sellPrice = sellPrice;
        this.qty = qty;
    }
}
public class PharmacyApp {

    static ArrayList<Medicine> inventory = new ArrayList<>();
    static ArrayList<String> bills = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        int choice;
        do {
            System.out.println("\n====== MINI PHARMACY SYSTEM ======");
            System.out.println("1. Add New Medicine");
            System.out.println("2. Sell Medicine ");
            System.out.println("3. View Stock Report");
            System.out.println("4. Search Medicine");
            System.out.println("5. Update Medicine Quantity");
            System.out.println("6. Delete Medicine");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> sellMedicine();     
                case 3 -> showStock();
                case 4 -> searchMedicine();
                case 5 -> updateQty();
                case 6 -> deleteMedicine();
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }

        } while (choice != 0);
    }
    
    static void addMedicine() {
        sc.nextLine();
        System.out.print("Medicine name: ");
        String name = sc.nextLine();

        System.out.print("Cost Price (your purchase price): ");
        double cost = sc.nextDouble();

        System.out.print("Selling Price (price to customer): ");
        double sell = sc.nextDouble();

        System.out.print("Quantity (packet of 10 pills): ");
        int qty = sc.nextInt();

        inventory.add(new Medicine(name, cost, sell, qty));
        System.out.println("Medicine added successfully!");
    }

    static void sellMedicine() {
        sc.nextLine();
        System.out.print("Enter medicine name: ");
        String name = sc.nextLine();

        Medicine med = find(name);
        if (med == null) {
            System.out.println(" Medicine Not Found!");
            return;
        }

        System.out.print("Enter quantity: ");
        int q = sc.nextInt();

        if (q > med.qty) {
            System.out.println(" Not enough stock!");
            return;
        }

        double total = q * med.sellPrice;
        double profit = (med.sellPrice - med.costPrice) * q;  
        med.qty -= q;

        String bill = "Sold: " + name + 
                      " | Qty: " + q + 
                      " | Total: ₹" + total + 
                      " | Profit: ₹" + profit;

        bills.add(bill);

        System.out.println("\n******** BILL ********");
        System.out.println("Medicine: " + name);
        System.out.println("Quantity: " + q);
        System.out.println("Total Price: ₹" + total);
        System.out.println("Profit Earned: ₹" + profit);    
        System.out.println("************************");
    }

    static void showStock() {
        System.out.println("\n====== STOCK REPORT ======");
        if (inventory.isEmpty()) {
            System.out.println("No medicines available.");
            return;
        }

        for (Medicine m : inventory) {
            System.out.println(
                m.name + 
                " | Cost Price: ₹" + m.costPrice +
                " | Sell Price: ₹" + m.sellPrice +
                " | Qty: " + m.qty
            );

            if (m.qty <= 5) {
                System.out.println("⚠ LOW STOCK WARNING!");
            }
        }
    }


    static void searchMedicine() {
        sc.nextLine();
        System.out.print("Enter name to search: ");
        String name = sc.nextLine();

        Medicine m = find(name);
        if (m == null) {
            System.out.println(" Not found!");
        } else {
            System.out.println("Found: " + m.name + 
                               " | Cost: ₹" + m.costPrice + 
                               " | Sell: ₹" + m.sellPrice + 
                               " | Qty: " + m.qty);
        }
    }

    static void updateQty() {
        sc.nextLine();
        System.out.print("Medicine name: ");
        String name = sc.nextLine();

        Medicine m = find(name);
        if (m == null) {
            System.out.println(" Not found!");
            return;
        }

        System.out.print("Enter new quantity: ");
        m.qty = sc.nextInt();

        System.out.println(" Quantity updated!");
    }

    static void deleteMedicine() {
        sc.nextLine();
        System.out.print("Enter name to delete: ");
        String name = sc.nextLine();

        Medicine m = find(name);
        if (m == null) {
            System.out.println(" Not found!");
            return;
        }

        inventory.remove(m);
        System.out.println(" Medicine deleted!");
    }
   
    static Medicine find(String name) {
        for (Medicine m : inventory) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}