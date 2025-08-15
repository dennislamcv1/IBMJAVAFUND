import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

    private String petId;                  // Unique Pet ID
    private String name;
    private String speciesBreed;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate;
    private List<Appointment> appointments;

    // Constructor
    public Pet(String name, String speciesBreed, int age, String ownerName, String contactInfo) {
        this.petId = UUID.randomUUID().toString(); // Automatically generate unique ID
        this.name = name;
        this.speciesBreed = speciesBreed;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.registrationDate = LocalDate.now();
        this.appointments = new ArrayList<>();
    }

    // Getters
    public String getPetId() {
        return petId;
    }

    public String getName() {
        return name;
    }

    public String getSpeciesBreed() {
        return speciesBreed;
    }

    public int getAge() {
        return age;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciesBreed(String speciesBreed) {
        this.speciesBreed = speciesBreed;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    // Appointment management
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    // Readable string output
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        String regDateFormatted = registrationDate.format(formatter);

        return String.format(
                "Pet ID: %s%nName: %s%nSpecies/Breed: %s%nAge: %d%nOwner: %s%nContact: %s%nRegistered on: %s%nAppointments: %d",
                petId, name, speciesBreed, age, ownerName, contactInfo, regDateFormatted, appointments.size()
        );
    }
}

