import java.io.*;
import java.util.*;
import java.time.LocalDate;

/**
 * Main app to run the Eco-Points Recycling Tracker.
 */
public class EcoPointsRecyclingTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Household> households = new HashMap<>(); // Task 2
    public static void main(String[] args) {
        loadHouseholdsFromFile();
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

            switch (choice) {
                case "1":
                    registerHousehold();
                    break;
                case "2":
                    logRecyclingEvent();
                    break;
                case "3":
                    displayHouseholds();
                    break;
                case "4":
                    displayHouseholdEvents();
                    break;
                case "5":
                    generateReports();
                    break;
                case "6":
                    saveHouseholdsToFile();
                    running = false;
                    System.out.println("Data saved. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        }
    }

    // Add the methods to handle each choice provided here
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

    // Task 6
    private static void displayHouseholdEvents() {
        // Prompt the user to enter the household ID
        System.out.print("Enter household ID: ");
        String id = scanner.nextLine().trim();

        // Look up the household in the households map using the ID
        Household household = households.get(id);

        // If household is not found, show an error and exit
        if (household == null) {
            System.out.println("Household not found.");
            return;
        }

        // Print a header with the household's name
        System.out.println("\nRecycling Events for " + household.getName() + ":");

        // Check if the household has any recycling events
        if (household.getEvents().isEmpty()) {
            System.out.println("No events logged.");
        } else {
            // Loop through all recycling events and print each one
            for (RecyclingEvent e : household.getEvents()) {
                //Print the stringified version of the event
                System.out.println(e);
            }

            // After listing events, show the total weight recycled by this household
            System.out.println("Total Weight: " + household.getTotalWeight() + " kg");

            // Show the total eco points earned by this household
            System.out.println("Total Points: " + household.getTotalPoints() + " pts");
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

    // Task 5
    private static void saveHouseholdsToFile() {
        try {
            // Create a FileOutputStream to write to the file named "households.ser"
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("households.ser")
            );
            // Write the entire households map to the file
            out.writeObject(households);
            out.close();
            // If successful, no message is printed here — could add confirmation if you like
        } catch (IOException e) {
            // Task 8
            // If something goes wrong while saving, print an error message
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked") // Suppresses unchecked cast warning when reading the object
    private static void loadHouseholdsFromFile() {
        // Use a try-with-resources block to automatically close the input stream
        try (
            // Open an ObjectInputStream to read from the file "households.ser"
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("households.ser"))
        ) {
            // Read the object from the file and cast it back to the correct type
            households = (Map<String, Household>) in.readObject();

            // Confirmation message to let the user know data was loaded
            System.out.println("Household data loaded.");
        } catch (FileNotFoundException e) {
            // Task 8
            // If the file doesn't exist yet, that's okay — start with empty data
            System.out.println("No saved data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            // Handle other errors, like if the file is corrupted or unreadable
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}