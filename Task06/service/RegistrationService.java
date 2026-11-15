package task06.service;

import task06.dao.RegistrationDAO;
import task06.model.Registration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer for Event Registration.
 * Handles business validation rules, duplicate handling, and coordinates with DAO.
 */
public class RegistrationService {
    private final RegistrationDAO dao;

    public RegistrationService(RegistrationDAO dao) {
        this.dao = dao;
    }

    public ServiceResult registerStudent(String studentId, String name, String department, String event) {
        // Rule 1: Student ID validation
        if (studentId == null || studentId.trim().isEmpty()) {
            return new ServiceResult(false, "Validation Error: Student ID cannot be empty.");
        }
        String cleanStudentId = studentId.trim().toUpperCase();
        if (!cleanStudentId.matches("^[0-9A-Z]{5,15}$")) {
            return new ServiceResult(false, "Validation Error: Student ID must be 5-15 alphanumeric characters (e.g. 2024CS101).");
        }

        // Rule 2: Student Name validation
        if (name == null || name.trim().isEmpty()) {
            return new ServiceResult(false, "Validation Error: Student Name cannot be empty.");
        }
        String cleanName = name.trim();
        if (cleanName.length() < 3 || !cleanName.matches("^[a-zA-Z\\s.']+$")) {
            return new ServiceResult(false, "Validation Error: Student Name must contain only letters and spaces (min 3 chars).");
        }

        // Rule 3: Department validation
        if (department == null || department.trim().isEmpty() || "-- Select Department --".equals(department)) {
            return new ServiceResult(false, "Validation Error: Please select a valid Department.");
        }

        // Rule 4: Event validation
        if (event == null || event.trim().isEmpty() || "-- Select Event --".equals(event)) {
            return new ServiceResult(false, "Validation Error: Please select an Event to register.");
        }

        // Create entity
        Registration reg = new Registration(cleanStudentId, cleanName, department.trim(), event.trim());

        // Attempt persistence through DAO
        try {
            dao.insertRegistration(reg);
            return new ServiceResult(true, "Successfully registered " + cleanName + " (" + cleanStudentId + ") for '" + event + "'!\nReg ID: #" + reg.getRegistrationId());
        } catch (SQLException e) {
            return new ServiceResult(false, e.getMessage());
        }
    }

    public List<Registration> getRegistrations(String eventFilter, int page, int pageSize) throws SQLException {
        return dao.findRegistrations(eventFilter, page, pageSize);
    }

    public void exportRegistrations(File file, String eventFilter) throws IOException, SQLException {
        dao.exportToCSV(file, eventFilter);
    }

    public static class ServiceResult {
        private final boolean success;
        private final String message;

        public ServiceResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}
