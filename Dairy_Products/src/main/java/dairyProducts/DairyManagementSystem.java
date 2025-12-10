package dairyProducts;

import java.util.Scanner;

public class DairyManagementSystem {
	
	Supplier supplier = new Supplier();
	Farmer farmer = new Farmer();
	Distributor distributor = new Distributor();
	
	Scanner sc = new Scanner(System.in);
	
	public void menu() {
		while(true) {
			System.out.println("Dairy Products Management System");
			System.out.println(" 1. Farmer Module");
			System.out.println(" 2. Distributor Module");
			System.out.println(" 3. Supplier Module");
			System.out.println(" 4. Exit");
			System.out.println("Enter Your Choice : ");
			int choice = sc.nextInt();
			
			switch (choice) {
			
			case 1:
				farmerMenu();
				break;
				
			case 2: 
				distributorMenu(); 
				break;
				
            case 3: 
            	supplierMenu(); 
            	break;
            	
            case 4: 
            	System.out.println("Thank you!"); 
            	return;
            	
            default: 
            	System.out.println("Invalid Choice!");
			}			
		}
	}
	
	private void farmerMenu() {
        System.out.println("\n--- Farmer Module ---");
        System.out.print("Milk Name: ");
        String name = sc.next();

        System.out.print("Quantity (in Litres): ");
        while (!sc.hasNextDouble()) {
            System.out.println("Please enter number only!");
            sc.next();  
        }
        double quantity = sc.nextDouble();
        sc.nextLine();

        System.out.print("Price per Litre: ");
        double price = sc.nextDouble();

        farmer.addMilk(name, quantity, price);
        System.out.println("Milk Added Successfully!");
    }
	
	private void distributorMenu() {
        System.out.println("\n--- Distributor Module ---");

        System.out.println("Farmer Stock:");
        for (Product p : farmer.getProducts()) {
            System.out.println("→ " + p);
        }

        System.out.print("Enter index of product to buy (0,1,2..): ");
        int idx = sc.nextInt();

        if (idx >= 0 && idx < farmer.getProducts().size()) {
            Product p = farmer.getProducts().get(idx);
            distributor.buyFromFarmer(p);
            System.out.println("Distributor purchased: " + p);
        } 
        else {
            System.out.println("Invalid index!");
        }
    }
    
    private void supplierMenu() {
        System.out.println("\n--- Supplier Module ---");

        System.out.println("Distributor Stock:");
        for (Product p : distributor.getStocks()) {
            System.out.println("→ " + p);
        }

        System.out.print("Enter index of product to supply: ");
        int idx = sc.nextInt();

        if (idx >= 0 && idx < distributor.getStocks().size()) {
            Product p = distributor.getStocks().get(idx);
            supplier.supplyToShop(p);
            System.out.println("Supplied to shop: " + p);
        } else {
            System.out.println("Invalid index!");
        }
    }
}