import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused")
public class PetCareScheduler {
    private static final String DATA_FILE = "pets.dat";
    private List<Pet> pets;
    private Scanner scanner;

    public PetCareScheduler() {
        pets = loadData();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        PetCareScheduler app = new PetCareScheduler();
        app.run();
    }

    private void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Paws & Whiskers Pet Care Scheduler ===");
            System.out.println("1. Register a pet");
            System.out.println("2. Schedule an appointment");
            System.out.println("3. Display all pets");
            System.out.println("4. Display appointments for a specific pet");
            System.out.println("5. Display upcoming appointments for all pets");
            System.out.println("6. Display past appointment history for each pet");
            System.out.println("7. Generate reports");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    registerPet();
                    break;
                case "2":
                    scheduleAppointment();
                    break;
                case "3":
                    displayPets();
                    break;
                case "4":
                    displayAppointmentsForPet();
                    break;
                case "5":
                    displayUpcomingAppointments();
                    break;
                case "6":
                    displayPastAppointments();
                    break;
                case "7":
                    generateReports();
                    break;
                case "8":
                    saveData();
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // ---------------------- TASK 3 METHODS ----------------------

    private void registerPet() {
        System.out.print("Enter pet name: ");
        String name = scanner.nextLine();

        System.out.print("Enter species/breed: ");
        String species = scanner.nextLine();

        int age = -1;
        while (age < 0) {
            try {
                System.out.print("Enter age (in years): ");
                age = Integer.parseInt(scanner.nextLine());
                if (age < 0) {
                    System.out.println("Age cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a valid number.");
            }
        }

        System.out.print("Enter owner name: ");
        String ownerName = scanner.nextLine();

        System.out.print("Enter contact info: ");
        String contactInfo = scanner.nextLine();

        Pet pet = new Pet(name, species, age, ownerName, contactInfo);

        // Ensure unique ID (UUID already ensures uniqueness, but check anyway)
        for (Pet existingPet : pets) {
            if (existingPet.getPetId().equals(pet.getPetId())) {
                System.out.println("Duplicate Pet ID detected. Registration failed.");
                return;
            }
        }

        pets.add(pet);
        System.out.println("Pet registered successfully! Pet ID: " + pet.getPetId());
    }

    private void scheduleAppointment() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered. Please register a pet first.");
            return;
        }

        displayPets();
        System.out.print("Enter Pet ID to schedule an appointment: ");
        String petId = scanner.nextLine();

        Pet pet = findPetById(petId);
        if (pet == null) {
            System.out.println("Pet not found!");
            return;
        }

        String type;
        while (true) {
            System.out.print("Enter appointment type (Vet Visit, Vaccination, Grooming): ");
            type = scanner.nextLine();
            if (type.equalsIgnoreCase("Vet Visit") ||
                type.equalsIgnoreCase("Vaccination") ||
                type.equalsIgnoreCase("Grooming")) {
                break;
            } else {
                System.out.println("Invalid appointment type. Try again.");
            }
        }

        LocalDateTime dateTime = null;
        while (dateTime == null) {
            try {
                System.out.print("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
                String dateTimeStr = scanner.nextLine();
                dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (dateTime.isBefore(LocalDateTime.now())) {
                    System.out.println("Appointment must be in the future.");
                    dateTime = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date/time format. Please try again.");
            }
        }

        System.out.print("Enter notes (optional, press Enter to skip): ");
        String notes = scanner.nextLine();

        Appointment appointment = new Appointment(type, dateTime, notes);
        pet.addAppointment(appointment);
        System.out.println("Appointment scheduled successfully!");
    }

    private void displayPets() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
            return;
        }
        for (Pet pet : pets) {
            System.out.println("\n" + pet);
        }
    }

    private void displayAppointmentsForPet() {
        if (pets.isEmpty()) {
            System.out.println("No pets registered.");
            return;
        }
        System.out.print("Enter Pet ID: ");
        String petId = scanner.nextLine();

        Pet pet = findPetById(petId);
        if (pet == null) {
            System.out.println("Pet not found!");
            return;
        }
        System.out.println("\nAppointments for " + pet.getName() + ":");
        for (Appointment app : pet.getAppointments()) {
            System.out.println(app);
        }
    }

    private void displayUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        for (Pet pet : pets) {
            for (Appointment app : pet.getAppointments()) {
                if (app.getDateTime().isAfter(now)) {
                    System.out.println(pet.getName() + " -> " + app);
                }
            }
        }
    }

    private void displayPastAppointments() {
        LocalDateTime now = LocalDateTime.now();
        for (Pet pet : pets) {
            for (Appointment app : pet.getAppointments()) {
                if (app.getDateTime().isBefore(now)) {
                    System.out.println(pet.getName() + " -> " + app);
                }
            }
        }
    }

    private void generateReports() {
        LocalDateTime now = LocalDateTime.now();

        System.out.println("\n=== Pets with Upcoming Appointments in Next Week ===");
        for (Pet pet : pets) {
            for (Appointment app : pet.getAppointments()) {
                if (app.getDateTime().isAfter(now) &&
                    app.getDateTime().isBefore(now.plusDays(7))) {
                    System.out.println(pet.getName() + " -> " + app);
                }
            }
}
System.out.println("\n=== Pets Overdue for a Vet Visit (6+ Months) ===");
LocalDateTime sixMonthsAgo = now.minusMonths(6);
        for (Pet pet : pets) {
            boolean hadRecentVetVisit = false;
            for (Appointment app : pet.getAppointments()) {
                if (app.getType().equalsIgnoreCase("Vet Visit") &&
                    app.getDateTime().isAfter(sixMonthsAgo)) {
                    hadRecentVetVisit = true;
                    break;
                }
            }
            if (!hadRecentVetVisit) {
                System.out.println(pet.getName() + " (" + pet.getPetId() + ")");
            }
        }
    }

    // ---------------------- HELPER METHODS ----------------------

    private Pet findPetById(String petId) {
        for (Pet pet : pets) {
            if (pet.getPetId().equals(petId)) {
                return pet;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Pet> loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Pet>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(pets);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}
