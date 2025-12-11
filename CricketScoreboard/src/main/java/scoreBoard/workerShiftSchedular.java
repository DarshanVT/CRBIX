package scoreBoard;
import java.util.*;

class Worker {
    int id;
    String name;
    String shift;
    String timing;

    
    HashMap<String, Integer> monthlyAttendance = new HashMap<>();

    
    HashMap<String, Integer> dailyOvertime = new HashMap<>();

    public Worker(int id, String name) {
        this.id = id;
        this.name = name;
        this.shift = "Not Assigned";
        this.timing = "-";
    }
}

public class workerShiftSchedular {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Worker> workers = new ArrayList<>();

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== WORKER SHIFT SCHEDULER =====");
            System.out.println("1. Add Worker");
            System.out.println("2. View Workers");
            System.out.println("3. Assign Shift");
            System.out.println("4. Update Month Attendance");
            System.out.println("5. Add Daily Overtime");
            System.out.println("6. Generate Full Report");
            System.out.println("7. Exit");
            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1: addWorker(); break;
                case 2: viewWorkers(); break;
                case 3: assignShift(); break;
                case 4: updateAttendance(); break;
                case 5: addDailyOvertime(); break;
                case 6: generateReport(); break;
                case 7: System.out.println("Exiting..."); break;
                default: System.out.println("Invalid Choice!");
            }

        } while (choice != 7);
    }

   
    public static void addWorker() {
        System.out.print("Enter Worker ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Worker Name: ");
        String name = sc.nextLine();

        workers.add(new Worker(id, name));
        System.out.println("Worker Added Successfully!");
    }

   
    public static void viewWorkers() {
        if (workers.isEmpty()) {
            System.out.println("No Workers Available.");
            return;
        }

        for (Worker w : workers) {
            System.out.println("ID: " + w.id + ", Name: " + w.name + 
                               ", Shift: " + w.shift + ", Timing: " + w.timing);
        }
    }

    
    public static void assignShift() {
        System.out.print("Enter Worker ID: ");
        int id = sc.nextInt();

        Worker w = findWorker(id);
        if (w == null) return;

        System.out.println("\nSelect Shift:");
        System.out.println("1. Morning (9AM - 6PM)");
        System.out.println("2. Evening (3PM - 12AM)");
        System.out.println("3. Night (9PM - 6AM)");
        System.out.print("Enter Choice: ");
        int s = sc.nextInt();

        switch (s) {
            case 1: w.shift = "Morning"; w.timing = "9AM - 6PM"; break;
            case 2: w.shift = "Evening"; w.timing = "3PM - 12AM"; break;
            case 3: w.shift = "Night"; w.timing = "9PM - 6AM"; break;
            default: System.out.println("Invalid Shift!"); return;
        }

        System.out.println("Shift Assigned!");
    }

    
    public static void updateAttendance() {
        System.out.print("Enter Worker ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Worker w = findWorker(id);
        if (w == null) return;

        System.out.print("Enter Month Name (Ex: january, february): ");
        String month = sc.nextLine();

        System.out.print("Total Days Worker Came (Out of 30): ");
        int days = sc.nextInt();

        w.monthlyAttendance.put(month, days);
        System.out.println("Attendance Updated for " + month + "!");
    }

    
    public static void addDailyOvertime() {
        System.out.print("Enter Worker ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Worker w = findWorker(id);
        if (w == null) return;

        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = sc.nextLine();

        System.out.print("Enter Overtime Hours for this day: ");
        int hrs = sc.nextInt();

        w.dailyOvertime.put(date, hrs);
        System.out.println("Overtime Added for " + date + "!");
    }

    
    public static void generateReport() {
        if (workers.isEmpty()) {
            System.out.println("No Workers Available.");
            return;
        }

        for (Worker w : workers) {
            System.out.println("\n===== Worker Report =====");
            System.out.println("ID: " + w.id);
            System.out.println("Name: " + w.name);
            System.out.println("Shift: " + w.shift + " (" + w.timing + ")");

            // Attendance Summary
            System.out.println("\n--- Monthly Attendance ---");
            if (w.monthlyAttendance.isEmpty()) {
                System.out.println("No attendance data.");
            } else {
                for (String month : w.monthlyAttendance.keySet()) {
                    System.out.println(month + ": " + w.monthlyAttendance.get(month) + " / 30 days");
                }
            }

            
            System.out.println("\n--- Daily Overtime ---");
            int totalOT = 0;

            if (w.dailyOvertime.isEmpty()) {
                System.out.println("No overtime data.");
            } else {
                for (String date : w.dailyOvertime.keySet()) {
                    int hrs = w.dailyOvertime.get(date);
                    totalOT += hrs;
                    System.out.println(date + ": " + hrs + " hrs");
                }
            }

            System.out.println("\nTotal Monthly Overtime: " + totalOT + " hrs");
            System.out.println("-----------------------------------");
        }
    }

    
    public static Worker findWorker(int id) {
        for (Worker w : workers) {
            if (w.id == id) return w;
        }
        System.out.println("Worker Not Found!");
        return null;
    }
}
