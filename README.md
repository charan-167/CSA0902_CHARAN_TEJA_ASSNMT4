# CSA0902_CHARAN_TEJA_ASSNMT4
# CSA09 Programming in Java — Assignment 4 (Course Outcome 4: Event-Driven GUI Design)

**Course:** Programming in Java (CSA09)  
**Course Outcome:** CO4 – Applets, Event Delegation Model, Listeners and Adapters, Inner and Anonymous Classes, AWT/Swing Components, Layout Managers, Menus/Dialogs, and GUI–JDBC Integration  
**Student Name:** Kola VENKATA Charan  Tejs
**Register Number:** [192424167]  
**Java Version:** OpenJDK 17.0.21 (LTS)  
**Repository Name:** `CSA09-<RegisterNumber>-Assignment4`  

---

## Repository Structure

```
├── Task01/                     # Task 1: GUI Design Review (Diagnose & Repair)
│   ├── FeeService.java         # Separated business & JDBC persistence service
│   ├── FeeWindowBad.java       # Original defective code for reference
│   ├── FeeWindowRepaired.java  # Repaired responsive GUI with SwingWorker
│   ├── PaymentResult.java      # Model/Value object
│   ├── Task1_Test.java         # Test harness
│   └── screenshots/
│       └── fee_payment_repaired.png
├── Task02/                     # Task 2: Hospital Outpatient Appointment Desk
│   ├── AppointmentDeskFrame.java # Cascading ComboBox, validation & exit confirm
│   ├── DoctorService.java      # Service layer with cached department-doctor mappings
│   ├── Task2_Test.java         # Test harness
│   └── screenshots/
│       ├── appointment_desk_main.png
│       └── appointment_booked_confirm.png
├── Task03/                     # Task 3: Campus Library Search Screen (Layouts)
│   ├── LibrarySearchFrame.java # Proper nested BorderLayout + FlowLayout design
│   ├── Task3_Test.java         # Automated multi-size resizing tester
│   └── screenshots/
│       ├── library_search_small.png    (500x350)
│       ├── library_search_default.png  (750x450)
│       └── library_search_large.png    (1000x650)
├── Task04/                     # Task 4: Event-Handling Forensics (DrawPad)
│   ├── DrawPad.java            # Repaired DrawPad with completed TODO 1 & TODO 2
│   ├── DrawPadDefective.java   # Original flawed implementation
│   ├── DrawPadOffscreenImage.java # Alternative concept using BufferedImage
│   ├── Task4_Test.java         # Clicks and minimize/restore test harness
│   ├── compiler_override_error.txt # Recorded javac @Override error output
│   └── screenshots/
│       ├── drawpad_after_clicks.png
│       └── drawpad_after_restore.png
├── Task05/                     # Task 5: Event-Handling Styles (Currency Converter)
│   ├── Converter.java          # Completed code (Lambda style d with TODOs solved)
│   ├── ConverterStyleA_FrameListener.java     # Style (a): Frame implements ActionListener
│   ├── ConverterStyleB_NamedInnerClass.java    # Style (b): Named Inner Class
│   ├── ConverterStyleC_AnonymousInnerClass.java # Style (c): Anonymous Inner Class
│   ├── ConverterEnhanced.java  # Requirement change: Enter key & JOptionPane dialog
│   ├── ConverterActionRef.java # Alternative concept: Shared Action & Method Ref
│   ├── Task5_Test.java         # Test harness
│   └── screenshots/
│       ├── converter_valid.png
│       └── converter_invalid.png
├── Task06/                     # Task 6: Design Challenge (Campus Event Registration)
│   ├── schema.sql              # MySQL DDL and DML script with UNIQUE constraint
│   ├── model/Registration.java # Registration entity model
│   ├── dao/RegistrationDAO.java# JDBC Data Access Layer (PreparedStatement, pagination)
│   ├── service/RegistrationService.java # Business validation & duplicate handling
│   ├── ui/EventRegistrationFrame.java   # Swing GUI (JMenuBar, JSplitPane, JTable, SwingWorker)
│   ├── Task6_App.java          # Main application launcher
│   ├── Task6_Test.java         # Automated verification harness
│   └── screenshots/
│       ├── event_reg_normal.png
│       ├── event_reg_invalid_error.png
│       ├── event_reg_duplicate_error.png
│       └── event_reg_filtered.png
├── ASSIGNMENT_REPORT.md        # Complete academic report with all analyses & pseudocode
├── ASSIGNMENT_REPORT.docx      # Formatted Word Document ready for submission
└── README.md                   # Setup, execution steps, and verification summary
```

---

## Compilation & Execution Instructions

Ensure you are in the project root directory (`JAVA ASSNMT 4`).

### Task 1: Fee Payment Window
```bash
# Compile
javac -d bin Task01/*.java

# Run Interactive Application
java -cp bin task01.FeeWindowRepaired

# Run Test Harness & Generate Screenshot
java -cp bin task01.Task1_Test
```

### Task 2: Hospital Outpatient Appointment Desk
```bash
# Compile
javac -d bin Task02/*.java

# Run Interactive Application
java -cp bin task02.AppointmentDeskFrame

# Run Test Harness & Generate Screenshots
java -cp bin task02.Task2_Test
```

### Task 3: Campus Library Search Screen (Layout Resizing)
```bash
# Compile
javac -d bin Task03/*.java

# Run Interactive Application
java -cp bin task03.LibrarySearchFrame

# Run Test Harness & Generate 3 Resizing Screenshots
java -cp bin task03.Task3_Test
```

### Task 4: DrawPad Event Forensics
```bash
# Compile
javac -d bin Task04/*.java

# Run Interactive Application
java -cp bin task04.DrawPad

# Run Alternative Concept (BufferedImage Offscreen)
java -cp bin task04.DrawPadOffscreenImage

# Run Test Harness (Click simulation & minimize/restore verification)
java -cp bin task04.Task4_Test
```

### Task 5: Currency Converter (Event Styles)
```bash
# Compile
javac -d bin Task05/*.java

# Run Completed Lambda Converter (Style d)
java -cp bin task05.Converter

# Run Enhanced Version (Enter key & Dialog)
java -cp bin task05.ConverterEnhanced

# Run Test Harness & Generate Screenshots
java -cp bin task05.Task5_Test
```

### Task 6: Campus Event Registration System (Swing + JDBC)
```bash
# Compile all layers
javac -d bin Task06/model/*.java Task06/dao/*.java Task06/service/*.java Task06/ui/*.java Task06/*.java

# Run Full Desktop Application
java -cp bin task06.Task6_App

# Run Automated Test Harness (Generates Normal, Invalid, Duplicate & Filtered Screenshots)
java -cp bin task06.Task6_Test
```

---

## Summary of Results

| Task | Component Assessed | Outcome & Result Summary |
|---|---|---|
| **Task 1** | GUI Design Review | Identified 8 critical design flaws in junior developer's code. Repaired architecture by decoupling UI from `FeeService`, replacing string `==` with command dispatch, using `PreparedStatement` to eliminate SQL injection, and executing bank confirmation asynchronously via `SwingWorker`. |
| **Task 2** | Event-Driven Design | Modeled event table and component tree for hospital appointment desk. Implemented dynamic cascading ComboBox for Doctor list based on Department. Enforced booking state tracking to warn before closing. |
| **Task 3** | Layout Managers & Resizing | Designed nested `BorderLayout` and `FlowLayout` hierarchy. Verified across 3 window sizes (500x350, 750x450, 1000x650); the results table in `BorderLayout.CENTER` grows smoothly while controls in North and South retain preferred heights. |
| **Task 4** | Event-Handling Forensics | Detected `mouseclicked` lowercase typo which bypassed `MouseAdapter.mouseClicked()`. Proved that transient `getGraphics()` calls vanish on window minimize/restore. Repaired by overriding `paint(Graphics)` and storing points in `List<Point>`. Captured compile-time error when `@Override` is applied. |
| **Task 5** | Event-Handling Styles | Implemented 4 distinct styles: Frame listener, Named inner class, Anonymous inner class, and Lambda. Evaluated change effort when adding Enter key support and Dialog display. Compared shared `Action` objects and method references. |
| **Task 6** | Layered Swing–JDBC App | Designed full tiered application (Presentation, Service, DAO, Model). Implemented MySQL DDL with `UNIQUE (student_id, event_name)` constraint. Ensured EDT remains responsive using `SwingWorker` for background database queries and pagination. |

---

## GitHub Progressive Commit History

The repository was built with progressive commits per task:
1. `e47c6b4`: Task 1: GUI Design Review - Diagnose and repair poor event-handling design
2. `71e25c6`: Task 2: From Requirement to Event-Driven Design - Hospital Appointment Desk
3. `8c9474f`: Task 3: From Sketch to Layout - Campus Library Search Screen
4. `acad330`: Task 4: Event-Handling Forensics - Repaired DrawPad and @Override analysis
5. `9c56709`: Task 5: Same Screen, Different Event-Handling Styles - Currency Converter
6. `4856907`: Task 6: Design Challenge - Layered Campus Event Registration Swing-JDBC Application
