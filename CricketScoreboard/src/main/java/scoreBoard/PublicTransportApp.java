package scoreBoard;

import java.util.*;

class TransportRoute {
    String routeName;
    List<String> timings = new ArrayList<>();

    public TransportRoute(String routeName) {
        this.routeName = routeName;
    }

    public void addTiming(String time) {
        timings.add(time);
    }

    public void displayTimings() {
        if (timings.isEmpty()) {
            System.out.println("No timings added yet.");
        } else {
            System.out.println("Timings:");
            for (String t : timings) {
                System.out.println(" - " + t);
            }
        }
    }
}

public class PublicTransportApp {

    static Scanner sc = new Scanner(System.in);
    static Map<Integer, TransportRoute> routes = new HashMap<>();
    static int routeIdCounter = 1;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== PUBLIC TRANSPORT TIMING SYSTEM =====");
            System.out.println("1. Add Route");
            System.out.println("2. View All Routes and its ID");
            System.out.println("3. Search Route by Name");
            System.out.println("4. Add Timings to Route");
            System.out.println("5. View Route Timings");
            System.out.println("6. Delete Route");
            System.out.println("7. Edit Route Name");
            System.out.println("8. Exit");
            System.out.print("Enter option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Handle newline

            switch (choice) {
                case 1:
                    addRoute();
                    break;
                case 2:
                    viewRoutes();
                    break;
                case 3:
                    searchRoute();
                    break;
                case 4:
                    addTiming();
                    break;
                case 5:
                    viewRouteTimings();
                    break;
                case 6:
                    deleteRoute();
                    break;
                case 7:
                    editRoute();
                    break;
                case 8:
                    System.out.println("Exiting... Thank you!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    static void addRoute() {
        System.out.print("Enter Route Name: ");
        String name = sc.nextLine();

        TransportRoute route = new TransportRoute(name);
        routes.put(routeIdCounter++, route);

        System.out.println("Route added successfully!");
    }

    static void viewRoutes() {
        if (routes.isEmpty()) {
            System.out.println("No routes available.");
            return;
        }

        System.out.println("\n--- Available Routes ---");
        for (Integer id : routes.keySet()) {
            System.out.println(id + ". " + routes.get(id).routeName);
        }
    }

    static void searchRoute() {
        System.out.print("Enter route name to search: ");
        String name = sc.nextLine();

        boolean found = false;
        for (Map.Entry<Integer, TransportRoute> entry : routes.entrySet()) {
            if (entry.getValue().routeName.equalsIgnoreCase(name)) {
                System.out.println("Route Found: ID = " + entry.getKey());
                found = true;
            }
        }

        if (!found) System.out.println("No route found with this name.");
    }

    static void addTiming() {
        System.out.print("Enter Route ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!routes.containsKey(id)) {
            System.out.println("Invalid route ID.");
            return;
        }

        System.out.print("Enter timing (e.g., 08:30 AM): ");
        String time = sc.nextLine();

        routes.get(id).addTiming(time);

        System.out.println("Timing added successfully!");
    }

    static void viewRouteTimings() {
        System.out.print("Enter Route ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!routes.containsKey(id)) {
            System.out.println("Invalid route ID.");
            return;
        }

        System.out.println("\nRoute: " + routes.get(id).routeName);
        routes.get(id).displayTimings();
    }

    static void deleteRoute() {
        System.out.print("Enter Route ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (routes.remove(id) != null) {
            System.out.println("Route deleted successfully!");
        } else {
            System.out.println("Route not found.");
        }
    }

    static void editRoute() {
        System.out.print("Enter Route ID to edit: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (!routes.containsKey(id)) {
            System.out.println("Invalid route ID.");
            return;
        }

        System.out.print("Enter new route name: ");
        String newName = sc.nextLine();
        routes.get(id).routeName = newName;

        System.out.println("Route updated successfully!");
    }
}
