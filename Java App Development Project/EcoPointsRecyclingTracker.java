import java.io.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Main app to run the Eco-Points Recycling Tracker.
 */
public class EcoPointsRecyclingTracker {
    //This is where the rest of the code will be added
}

    private static Scanner scanner = new Scanner(System.in);

    private static Map<String, Household> households = new HashMap<>(); // Task 2

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Eco-Points Recycling Tracker ===");
            System.out.println("1. Register Household");
            System.out.println("2. Log Recycling Event");
            System.out.println("3. Display Households");
            System.out.println("4. Display Household Recycling Events");
            System.out.println("5. Generate Reports");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            // You will handle the choice entered here using switch(case)
        }
    }

    // Add the methods to handle each choice here

    // Task 3
    private static void registerHousehold() {
        // Prompt the user to enter a unique household ID
        System.out.print("Enter household ID: ");
        String id = scanner.nextLine().trim();  // Read and trim input

        // Check if a household with this ID already exists in the map
        if (households.containsKey(id)) {
            System.out.println("Error: Household ID already exists.");
            return;  // Stop and return early if duplicate found
        }

        // Prompt the user to enter the household's name
        System.out.print("Enter household name: ");
        String name = scanner.nextLine().trim();

        // Prompt the user to enter the household's address
        System.out.print("Enter household address: ");
        String address = scanner.nextLine().trim();

        // Create a new Household object using the provided details
        Household household = new Household(id, name, address);

        // Add the new household to the households map (using ID as the key)
        households.put(id, household);

        // Confirm to the user that the household was registered successfully
        System.out.println("Household registered successfully on " + household.getJoinDate());
    }

        // Task 4
        private static void logRecyclingEvent() {
            // Ask the user for the household ID
            System.out.print("Enter household ID: ");
            String id = scanner.nextLine().trim();
    
            // Look up the household in the map by ID
            Household household = households.get(id);
    
            // If household not found, show error and exit
            if (household == null) {
                // Task 8
                System.out.println("Error: Household ID not found.");
                return;
            }
    
            // Ask the user for the material type they recycled
            System.out.print("Enter material type (plastic/glass/metal/paper): ");
            String material = scanner.nextLine().trim();
    
            double weight = 0.0;
    
            // Loop until a valid weight is entered
            while (true) {
                try {
                    System.out.print("Enter weight in kilograms: ");
                    weight = Double.parseDouble(scanner.nextLine());  // Convert input to double
    
                    // Check that weight is a positive number
                    if (weight <= 0) throw new IllegalArgumentException();
    
                    break;  // Exit loop if input is valid
                }  catch (NumberFormatException e) {
                    System.out.println("Invalid weight. Must be a positive number.");
                }  catch (IllegalArgumentException e) {
                    System.out.println("Invalid weight. Must be a positive number.");
                }
            }
    
            // Create a new RecyclingEvent using the material and weight
            RecyclingEvent event = new RecyclingEvent(material, weight);
    
            // Add the new event to the household and update points
            household.addEvent(event);
    
            // Show success message with points earned
            System.out.println("Recycling event logged! Points earned: " + event.getEcoPoints());
        }

    // Task 6
    private static void displayHouseholds() {
        // Check if the households map is empty
        if (households.isEmpty()) {
            System.out.println("No households registered.");
            return; // Exit early if there's nothing to show
        }

        // If there are households, print a header first
        System.out.println("\nRegistered Households:");

        // Loop through each household in the map and print its details
        for (Household h : households.values()) {
            System.out.println("ID: " + h.getId() +
                               ", Name: " + h.getName() +
                               ", Address: " + h.getAddress() +
                               ", Joined: " + h.getJoinDate());
        }
    }        

    // Task 7
    private static void generateReports() {
        // Check if there are any households registered
        if (households.isEmpty()) {
            System.out.println("No households registered.");
            return; // Exit if there's nothing to report on
        }

        // ------------------------------
        // Find the household with the highest points
        // ------------------------------
        Household top = null; // Start with no top household
        for (Household h : households.values()) {
            // If 'top' is still null, or this household has more points, update 'top'
            if (top == null || h.getTotalPoints() > top.getTotalPoints()) {
                top = h;
            }
        }

        // Print details of the top household
        System.out.println("\nHousehold with Highest Points:");
        System.out.println("ID: " + top.getId() +
                           ", Name: " + top.getName() +
                           ", Points: " + top.getTotalPoints());

        // ------------------------------
        // Calculate total community recycling weight
        // ------------------------------
        double totalWeight = 0.0;

        // Loop through all households to sum up their total weights
        for (Household h : households.values()) {
            totalWeight += h.getTotalWeight();
        }

        // Print total community weight
        System.out.println("Total Community Recycling Weight: " + totalWeight + " kg");
    }