package task06.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity representing a student event registration record.
 */
public class Registration implements Serializable {
    private int registrationId;
    private String studentId;
    private String studentName;
    private String department;
    private String eventName;
    private String registeredAt;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Registration(int registrationId, String studentId, String studentName, String department, String eventName, String registeredAt) {
        this.registrationId = registrationId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.department = department;
        this.eventName = eventName;
        this.registeredAt = registeredAt;
    }

    public Registration(String studentId, String studentName, String department, String eventName) {
        this(-1, studentId, studentName, department, eventName, LocalDateTime.now().format(FORMATTER));
    }

    public int getRegistrationId() { return registrationId; }
    public void setRegistrationId(int registrationId) { this.registrationId = registrationId; }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getDepartment() { return department; }
    public String getEventName() { return eventName; }
    public String getRegisteredAt() { return registeredAt; }

    public Object[] toTableRow() {
        return new Object[]{registrationId, studentId, studentName, department, eventName, registeredAt};
    }
}
