package task02;

import java.util.*;

/**
 * Service providing cached doctor listings and appointment booking validation.
 */
public class DoctorService {
    // In-memory cache: Department -> List of Doctors
    private final Map<String, List<String>> departmentDoctors = new LinkedHashMap<>();
    private final Set<String> bookedSlots = new HashSet<>();

    public DoctorService() {
        departmentDoctors.put("Cardiology", Arrays.asList("Dr. Sarah Jenkins", "Dr. Rajesh Sharma", "Dr. David Chen"));
        departmentDoctors.put("Neurology", Arrays.asList("Dr. Alice Walker", "Dr. Vikram Patel"));
        departmentDoctors.put("Orthopedics", Arrays.asList("Dr. Michael Brown", "Dr. Sunita Rao", "Dr. James Miller"));
        departmentDoctors.put("Pediatrics", Arrays.asList("Dr. Emily Davis", "Dr. Ananya Reddy"));
        departmentDoctors.put("Dermatology", Arrays.asList("Dr. Robert Wilson", "Dr. Meera Nair"));
    }

    public List<String> getDepartments() {
        return new ArrayList<>(departmentDoctors.keySet());
    }

    public List<String> getDoctorsForDepartment(String department) {
        if (department == null) return Collections.emptyList();
        return departmentDoctors.getOrDefault(department, Collections.emptyList());
    }

    public synchronized BookingResult bookAppointment(String patientId, String department, String doctor, String timeSlot) {
        // Validation
        if (patientId == null || patientId.trim().isEmpty()) {
            return new BookingResult(false, "Patient ID cannot be blank.", null);
        }
        if (!patientId.trim().matches("^[A-Za-z0-9]{3,10}$")) {
            return new BookingResult(false, "Patient ID must be 3-10 alphanumeric characters (e.g. PAT101).", null);
        }
        if (department == null || department.isEmpty() || "-- Select Department --".equals(department)) {
            return new BookingResult(false, "Please select a valid department.", null);
        }
        if (doctor == null || doctor.isEmpty() || "-- Select Doctor --".equals(doctor)) {
            return new BookingResult(false, "Please select a valid doctor.", null);
        }
        if (timeSlot == null || timeSlot.isEmpty() || "-- Select Time Slot --".equals(timeSlot)) {
            return new BookingResult(false, "Please select an appointment time slot.", null);
        }

        // Duplicate / Slot availability check
        String slotKey = doctor + "|" + timeSlot;
        if (bookedSlots.contains(slotKey)) {
            return new BookingResult(false, "Selected slot (" + timeSlot + ") with " + doctor + " is already booked! Please choose another slot.", null);
        }

        // Register booking
        bookedSlots.add(slotKey);
        String bookingRef = "APT-" + (1000 + bookedSlots.size());
        String msg = "Appointment successfully confirmed!\n"
                   + "Booking Ref: " + bookingRef + "\n"
                   + "Patient: " + patientId.trim().toUpperCase() + "\n"
                   + "Doctor: " + doctor + " (" + department + ")\n"
                   + "Time: " + timeSlot;
        return new BookingResult(true, msg, bookingRef);
    }

    public static class BookingResult {
        private final boolean success;
        private final String message;
        private final String bookingRef;

        public BookingResult(boolean success, String message, String bookingRef) {
            this.success = success;
            this.message = message;
            this.bookingRef = bookingRef;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getBookingRef() { return bookingRef; }
    }
}
