package scoreBoard;

import java.util.*;

class Transaction {
    String name;
    double amount;
    String type;
    String category;
    String month;

    Transaction(String name, double amount, String type, String category, String month) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.month = month;
    }

    public String toString() {
        return name + "," + amount + "," + type + "," + category + "," + month;
    }
}

public class BudgetPlanner {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Transaction> list = new ArrayList<>();

    public static void main(String[] args) {
        

        while (true) {
            System.out.println("\n ===== Budget Planner =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Balance");
            System.out.println("4. View All Transactions");
            System.out.println("5. Search Transaction");
            System.out.println("6. Monthly Report");
            System.out.println("7. Delete Transaction");
            System.out.println("8. Exit");
            System.out.print("Choose option: ");

            int ch = sc.nextInt(); 
            sc.nextLine();

            switch (ch) {
                case 1: addIncome(); break;
                case 2: addExpense(); break;
                case 3: viewBalance(); break;
                case 4: viewAll(); break;
                case 5: searchTransaction(); break;
                case 6: monthlyFilter(); break;
                case 7: deleteTransaction(); break;
                case 8:
                    System.out.println("Exiting... Bye!");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void addIncome() {
        System.out.print("Enter income name: ");
        String name = sc.nextLine();
        System.out.print("Enter amount: ");
        double amt = sc.nextDouble(); 
        sc.nextLine();

        System.out.print("Enter month (Jan/Feb/...): ");
        String month = sc.nextLine();

        list.add(new Transaction(name, amt, "Income", "-", month));
        System.out.println("Income added!");
    }

    static void addExpense() {
        System.out.print("Enter expense name: ");
        String name = sc.nextLine();
        System.out.print("Enter amount: ");
        double amt = sc.nextDouble(); 
        sc.nextLine();

        System.out.println("Choose Category: Food/Travel/Bills/Shopping/Medical/Other");
        String cat = sc.nextLine();

        System.out.print("Enter month (Jan/Feb/...): ");
        String month = sc.nextLine();

        list.add(new Transaction(name, amt, "Expense", cat, month));
        System.out.println("Expense added!");
    }

    static void viewBalance() {
        double income = 0, expense = 0;
        for (Transaction t : list) {
            if (t.type.equals("Income")) income += t.amount;
            else expense += t.amount;
        }
        System.out.println("Total Income: " + income);
        System.out.println("Total Expense: " + expense);
        System.out.println("Balance: " + (income - expense));
    }

    static void viewAll() {
        if (list.isEmpty()) {
            System.out.println("No transactions"); 
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);
            System.out.println((i+1) + ") " + t.type + " -> " + t.name +
                    " : Rs " + t.amount + " | " + t.category + " | " + t.month);
        }
    }

    static void searchTransaction() {
        System.out.print("Enter name to search: ");
        String key = sc.nextLine().toLowerCase();
        boolean found = false;

        for (Transaction t : list) {
            if (t.name.toLowerCase().contains(key)) {
                System.out.println(t.type + " -> " + t.name + " : Rs " + t.amount + " | " + t.category);
                found = true;
            }
        }
        if (!found) System.out.println("Not found!");
    }

    static void monthlyFilter() {
        System.out.print("Enter month (Jan/Feb/...): ");
        String m = sc.nextLine();

        double income = 0, expense = 0;

        for (Transaction t : list) {
            if (t.month.equalsIgnoreCase(m)) {
                if (t.type.equals("Income")) income += t.amount;
                else expense += t.amount;
            }
        }

        System.out.println("Income in " + m + " = " + income);
        System.out.println("Expense in " + m + " = " + expense);
        System.out.println("Savings = " + (income - expense));
    }

    static void deleteTransaction() {
        viewAll();
        System.out.print("Enter transaction number to delete: ");
        int n = sc.nextInt(); 
        sc.nextLine();

        if (n <= 0 || n > list.size()) {
            System.out.println("Invalid number!"); 
            return;
        }

        list.remove(n - 1);
        System.out.println("Deleted successfully!");
    }
}
