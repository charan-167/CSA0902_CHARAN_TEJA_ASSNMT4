package task06.dao;

import task06.model.Registration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Data Access Object (DAO) for Event Registrations.
 * Executes parameterized queries using JDBC PreparedStatement and handles pagination.
 * If live MySQL is not configured or offline, falls back seamlessly to an in-memory SQL mock.
 */
public class RegistrationDAO {
    private final String jdbcUrl;
    private final String dbUser;
    private final String dbPassword;
    private boolean isLiveJdbcConnected = false;

    // In-memory data store for standalone execution / fallback
    private final List<Registration> fallbackStore = new CopyOnWriteArrayList<>();
    private int nextId = 101;

    public RegistrationDAO(String jdbcUrl, String dbUser, String dbPassword) {
        this.jdbcUrl = jdbcUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        testConnection();
        initDefaultSeedData();
    }

    private void testConnection() {
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            isLiveJdbcConnected = false;
            return;
        }
        try (Connection c = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            isLiveJdbcConnected = true;
            System.out.println("[DAO] Connected to live MySQL database successfully.");
        } catch (SQLException e) {
            isLiveJdbcConnected = false;
            System.out.println("[DAO] MySQL server unreachable; using resilient in-memory database simulation.");
        }
    }

    private void initDefaultSeedData() {
        insertFallback(new Registration("2024CS101", "Aarav Sharma", "Computer Science", "Codeathon"));
        insertFallback(new Registration("2024CS105", "Bhavna Verma", "Computer Science", "Web Design Hackathon"));
        insertFallback(new Registration("2024EC201", "Chetan Kumar", "Electronics", "RoboWars"));
        insertFallback(new Registration("2024ME304", "Divya Nair", "Mechanical", "AutoCAD Design Expo"));
        insertFallback(new Registration("2024CS109", "Eshan Gupta", "Computer Science", "Codeathon"));
        insertFallback(new Registration("2024IT112", "Farhan Ali", "Information Tech", "Quiz Bowl"));
        insertFallback(new Registration("2024EE205", "Gitanjali Rao", "Electrical", "RoboWars"));
    }

    public synchronized void insertRegistration(Registration reg) throws SQLException {
        if (isLiveJdbcConnected) {
            String sql = "INSERT INTO event_registrations (student_id, student_name, department, event_name) VALUES (?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, reg.getStudentId());
                ps.setString(2, reg.getStudentName());
                ps.setString(3, reg.getDepartment());
                ps.setString(4, reg.getEventName());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        reg.setRegistrationId(rs.getInt(1));
                    }
                }
                return;
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062 || e.getMessage().toLowerCase().contains("duplicate")) {
                    throw new SQLException("Student " + reg.getStudentId() + " is already registered for event '" + reg.getEventName() + "'!", "23000", 1062);
                }
                throw e;
            }
        }

        // Fallback / Standalone simulation (enforces duplicate constraint)
        for (Registration existing : fallbackStore) {
            if (existing.getStudentId().equalsIgnoreCase(reg.getStudentId()) &&
                existing.getEventName().equalsIgnoreCase(reg.getEventName())) {
                throw new SQLException("Duplicate Entry: Student " + reg.getStudentId() + " is already registered for '" + reg.getEventName() + "'!", "23000", 1062);
            }
        }
        reg.setRegistrationId(nextId++);
        fallbackStore.add(0, reg); // add to top
    }

    private synchronized void insertFallback(Registration reg) {
        reg.setRegistrationId(nextId++);
        fallbackStore.add(reg);
    }

    public List<Registration> findRegistrations(String eventFilter, int page, int pageSize) throws SQLException {
        // Mock a slight network/query delay to demonstrate background thread execution
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        if (isLiveJdbcConnected) {
            List<Registration> list = new ArrayList<>();
            boolean hasFilter = (eventFilter != null && !eventFilter.isEmpty() && !"All Events".equalsIgnoreCase(eventFilter));
            String sql = hasFilter
                ? "SELECT registration_id, student_id, student_name, department, event_name, registered_at FROM event_registrations WHERE event_name = ? ORDER BY registration_id DESC LIMIT ? OFFSET ?"
                : "SELECT registration_id, student_id, student_name, department, event_name, registered_at FROM event_registrations ORDER BY registration_id DESC LIMIT ? OFFSET ?";

            try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                int paramIdx = 1;
                if (hasFilter) {
                    ps.setString(paramIdx++, eventFilter);
                }
                ps.setInt(paramIdx++, pageSize);
                ps.setInt(paramIdx, page * pageSize);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        list.add(new Registration(
                            rs.getInt("registration_id"),
                            rs.getString("student_id"),
                            rs.getString("student_name"),
                            rs.getString("department"),
                            rs.getString("event_name"),
                            rs.getString("registered_at")
                        ));
                    }
                }
            }
            return list;
        }

        // Fallback filtering & pagination simulation
        List<Registration> filtered = new ArrayList<>();
        boolean hasFilter = (eventFilter != null && !eventFilter.isEmpty() && !"All Events".equalsIgnoreCase(eventFilter));
        for (Registration r : fallbackStore) {
            if (!hasFilter || r.getEventName().equalsIgnoreCase(eventFilter)) {
                filtered.add(r);
            }
        }

        int fromIndex = Math.min(page * pageSize, filtered.size());
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());
        return new ArrayList<>(filtered.subList(fromIndex, toIndex));
    }

    public void exportToCSV(File targetFile, String eventFilter) throws IOException, SQLException {
        List<Registration> records = findRegistrations(eventFilter, 0, 10000);
        try (PrintWriter pw = new PrintWriter(new FileWriter(targetFile))) {
            pw.println("RegistrationID,StudentID,StudentName,Department,EventName,RegistrationDate");
            for (Registration r : records) {
                pw.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                    r.getRegistrationId(),
                    r.getStudentId().replace("\"", "\"\""),
                    r.getStudentName().replace("\"", "\"\""),
                    r.getDepartment().replace("\"", "\"\""),
                    r.getEventName().replace("\"", "\"\""),
                    r.getRegisteredAt()
                );
            }
        }
    }

    public boolean isLiveJdbcConnected() {
        return isLiveJdbcConnected;
    }
}
