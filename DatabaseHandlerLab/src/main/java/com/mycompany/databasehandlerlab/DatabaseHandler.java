package com.mycompany.databasehandlerlab;

import java.sql.*;
import java.util.*;

/**
 * PINEZA, MARIAN THERESE J. 2CSD PLEASE MAKE NECESSARY ADJUSTMENTS PRIOR TO
 * RUNNING THE CODE SUCH AS CHANGING THE PATH AND SUCH. THANK YOU!
 */

public class DatabaseHandler {

    private Connection connection;
    private static Scanner in = new Scanner(System.in);

    public DatabaseHandler(String dbPath) {
        try {
            String connectionString = "jdbc:sqlite:" + dbPath;
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    public void checkDatabase() {
        String query = "SELECT name FROM sqlite_master WHERE type=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "table"); // Setting parameter for `type`
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println("Table: " + rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        String databasePath = "C:\\Users\\cjpin\\OneDrive\\Documents\\NetBeansProjects\\DatabaseHandlerLab\\students";
        DatabaseHandler dbHandler = new DatabaseHandler(databasePath);

        System.out.println("Do you want to initialize the Student table? (Note: existing records will be erased if the table exists.)\nPress Y for Yes or N for No.");
        String userInput = in.next();
        char choice = userInput.length() > 0 ? Character.toUpperCase(userInput.charAt(0)) : ' ';

        switch (choice) {
            case 'Y':
                dbHandler.checkDatabase();
                dbHandler.initializeStudents();
                break;
            case 'N':
                System.out.println("You chose not to initialize the Student table.");
                break;
            default:
                System.out.println("Invalid input. The program assumes you do not wish to initialize the table.");
                break;
        }

        while (true) {
            System.out.println("\n\nCHOOSE AN OPTION: ");
            System.out.println("A | ADD STUDENT");
            System.out.println("B | VIEW STUDENT BY ID");
            System.out.println("C | VIEW STUDENT BY NAME");
            System.out.println("D | VIEW ALL STUDENTS");
            System.out.println("E | VIEW STUDENTS BY YEAR");
            System.out.println("F | UPDATE STUDENT INFORMATION");
            System.out.println("G | UPDATE STUDENT UNITS");
            System.out.println("H | REMOVE A STUDENT");
            System.out.println("I | EXIT");

            char option = in.next().charAt(0);
            in.nextLine();
            option = Character.toUpperCase(option);

            switch (option) {
                case 'A':
                    System.out.println("ENTER STUDENT DETAILS:");
                    System.out.print("ID (XXXX010YYYY): ");
                    String id = in.nextLine();
                    System.out.print("First Name: ");
                    String firstName = in.nextLine();
                    System.out.print("Middle Name: ");
                    String middleName = in.nextLine();
                    System.out.print("Last Name: ");
                    String lastName = in.nextLine();
                    System.out.print("Gender (M/F): ");
                    String gender = in.nextLine();
                    System.out.print("Birth Date (YYYY-MM-DD): ");
                    String birthDate = in.nextLine();
                    System.out.print("Enrollment Year: ");
                    int enrollmentYear = in.nextInt();
                    in.nextLine();
                    System.out.print("Department: ");
                    String department = in.nextLine();
                    System.out.print("Credit Units: ");
                    int creditUnits = in.nextInt();
                    in.nextLine();
                    System.out.print("Address: ");
                    String address = in.nextLine();

                    Student newStudent = new Student(id, firstName, middleName, lastName, gender, birthDate, enrollmentYear, department, creditUnits, address);
                    if (dbHandler.insertStudent(newStudent)) {
                        System.out.println("Student added successfully.");
                    } else {
                        System.out.println("Failed to add student.");
                    }
                    break;

                case 'B':
                    System.out.print("ENTER STUDENT ID (XXXX010YYYY): ");
                    String studentId = in.nextLine();
                    Student student = dbHandler.getStudent(studentId);
                    if (student != null) {
                        System.out.println("Student Details: " + student);
                    } else {
                        System.out.println("Student not found.");
                    }
                    break;

                case 'C':
                    System.out.println("ENTER STUDENT DETAILS:");
                    System.out.print("Enter First Name: ");
                    String FN = in.nextLine();

                    System.out.print("Enter Middle Name: ");
                    String MN = in.nextLine();

                    System.out.print("Enter Last Name: ");
                    String LN = in.nextLine();

                    Student studentResult = dbHandler.getStudent(FN, MN, LN);

                    if (studentResult != null) {
                        System.out.println("Student details found:");
                        System.out.println("Full Name: " + studentResult.getFirstName() + " " + studentResult.getMiddleName() + " " + studentResult.getLastName());
                        System.out.println("Additional Information: " + studentResult.toString());
                    } else {
                        System.out.println("Sorry, no student matches the information you provided.");
                    }
                    break;
                case 'D':
                    List<Student> students = dbHandler.getStudents();
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        students.forEach(System.out::println);
                    }
                    break;
                    
                case 'E':
                    System.out.print("ENTER ENROLLMENT YEAR: ");
                    int year = in.nextInt();
                    in.nextLine();

                    List<Student> studentsByYear = dbHandler.getStudentsByYear(year);

                    if (!studentsByYear.isEmpty()) {
                        for (Student s : studentsByYear) {
                            System.out.println(s);
                        }
                    } else {
                        System.out.println("No students found for the given year.");
                    }
                    break;

                case 'F':
                    System.out.print("ENTER STUDENT ID (XXXX010YYYY): ");
                    String idToUpdate = in.nextLine();
                    System.out.print("Enter new first name: ");
                    String newFirstName = in.nextLine();
                    System.out.print("Enter new middle name: ");
                    String newMiddleName = in.nextLine();
                    System.out.print("Enter new last name: ");
                    String newLastName = in.nextLine();
                    System.out.print("Enter new department: ");
                    String newDepartment = in.nextLine();
                    System.out.print("Enter new address: ");
                    String newAddress = in.nextLine();

                    Student updatedStudent = new Student(idToUpdate, newFirstName, newMiddleName, newLastName, "", "", 0, newDepartment, 0, newAddress);

                    boolean isUpdated = dbHandler.updateStudentInfo(idToUpdate, updatedStudent);

                    if (isUpdated) {
                        System.out.println("Student updated successfully.");
                    } else {
                        System.out.println("Failed to update student. Please check the student ID and try again.");
                    }
                    break;
                    
                case 'G':
                    System.out.println("ENTER STUDENT ID (XXXX010YYYY): ");
                    String studentID = in.nextLine();
                    System.out.print("New Units: ");
                    int subtractedUnits = in.nextInt();
                    in.nextLine();
                    if (dbHandler.updateStudentUnits(subtractedUnits, studentID)) {
                        System.out.println("Student " + studentID + "'s units have been updated.");
                    } else {
                        System.out.println("Student with ID " + studentID + " not found or update failed.");
                    }
                    break;

                case 'H':
                    System.out.print("ENTER STUDENT ID (XXXX010YYYY): ");
                    String idToDelete = in.nextLine();
                    if (dbHandler.removeStudent(idToDelete)) {
                        System.out.println("Student deleted successfully.");
                    } else {
                        System.out.println("Failed to delete student.");
                    }
                    break;

                case 'I':
                    System.out.println("EXITING...\nGOODBYE");
                    return;

                default:
                    System.out.println("INVALID OPTION | PLEASE TRY AGAIN");
            }
        }
    }

    public void initializeStudents() {
        String dropTableQuery = "DROP TABLE IF EXISTS Students";
        String createTableQuery = "CREATE TABLE Students (\n"
                + "student_id TEXT NOT NULL CHECK(student_id GLOB '[0-9][0-9][0-9][0-9]010[0-9][0-9][0-9][0-9]'),\n"
                + "student_fname TEXT NOT NULL,\n"
                + "student_mname TEXT NOT NULL,\n"
                + "student_lname TEXT NOT NULL,\n"
                + "student_sex TEXT NOT NULL CHECK (student_sex IN ('M', 'F')),\n"
                + "student_birth TEXT NOT NULL CHECK (student_birth GLOB '[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]'),\n"
                + "student_start INTEGER NOT NULL CHECK (student_start BETWEEN 0 AND 2025),\n"
                + "student_department TEXT NOT NULL,\n"
                + "student_units INTEGER NOT NULL,\n"
                + "student_address TEXT,\n"
                + "CONSTRAINT Students_PK PRIMARY KEY (student_id)\n"
                + ");";

        try {
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is not established.");
                return;
            }

            boolean tableDropped = false;
            int retryCount = 0;
            while (!tableDropped && retryCount < 5) {
                try (PreparedStatement dropStmt = connection.prepareStatement(dropTableQuery)) {
                    dropStmt.executeUpdate();
                    tableDropped = true;
                } catch (SQLException e) {
                    retryCount++;
                    System.err.println("Retry " + retryCount + ": Failed to drop table. Waiting...");
                    Thread.sleep(1000);
                }
            }

            if (!tableDropped) {
                System.err.println("Failed to drop Students table after multiple retries.");
                return;
            }

            try (PreparedStatement createStmt = connection.prepareStatement(createTableQuery)) {
                createStmt.executeUpdate();
                System.out.println("Students table created successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error creating Students table: " + e.getMessage());
            }

        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Error with database connection: " + e.getMessage());
        }
    }

    public Student getStudent(String studentNumber) {
        String query = "SELECT * FROM Students s WHERE s.student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getString("student_id"),
                        rs.getString("student_fname"),
                        rs.getString("student_mname"),
                        rs.getString("student_lname"),
                        rs.getString("student_sex"),
                        rs.getString("student_birth"),
                        rs.getInt("student_start"),
                        rs.getString("student_department"),
                        rs.getInt("student_units"),
                        rs.getString("student_address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Student getStudent(String studentFname, String studentMname, String studentLname) {
        String query = "SELECT * FROM Students s WHERE s.student_fname = ? AND s.student_mname = ? AND s.student_lname = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentFname);
            stmt.setString(2, studentMname);
            stmt.setString(3, studentLname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getString("student_id"),
                        rs.getString("student_fname"),
                        rs.getString("student_mname"),
                        rs.getString("student_lname"),
                        rs.getString("student_sex"),
                        rs.getString("student_birth"),
                        rs.getInt("student_start"),
                        rs.getString("student_department"),
                        rs.getInt("student_units"),
                        rs.getString("student_address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("student_fname"),
                        rs.getString("student_mname"),
                        rs.getString("student_lname"),
                        rs.getString("student_sex"),
                        rs.getString("student_birth"),
                        rs.getInt("student_start"),
                        rs.getString("student_department"),
                        rs.getInt("student_units"),
                        rs.getString("student_address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    

    public boolean removeStudent(String studentNumber) {
        String query = "DELETE FROM Students WHERE student_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, studentNumber);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getStudentsByYear(int year) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students WHERE student_start = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(new Student(
                        rs.getString("student_id"),
                        rs.getString("student_fname"),
                        rs.getString("student_mname"),
                        rs.getString("student_lname"),
                        rs.getString("student_sex"),
                        rs.getString("student_birth"),
                        rs.getInt("student_start"),
                        rs.getString("student_department"),
                        rs.getInt("student_units"),
                        rs.getString("student_address")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving students by year: " + e.getMessage());
        }

        return students;
    }


    public boolean updateStudentInfo(String studentNumber, Student info) {
        String updateInfoQuery = "UPDATE Students\n"
                + "SET student_fname = ?\n"
                + ", student_mname = ?\n"
                + ", student_lname = ?\n"
                + ", student_department = ?\n"
                + ", student_address = ?\n"
                + "WHERE student_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(updateInfoQuery);
            stmt.setString(1, info.getFirstName());
            stmt.setString(2, info.getMiddleName());
            stmt.setString(3, info.getLastName());
            stmt.setString(4, info.getDepartment());
            stmt.setString(5, info.getAddress());
            stmt.setString(6, studentNumber);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                return true;
            } else {
                System.out.println("No student found with ID: " + studentNumber);
                return false;
            }
        } catch (SQLException s) {
            System.err.println("SQL Error: " + s.getMessage());
            s.printStackTrace();
        }

        return false;

    }

    public boolean updateStudentUnits(int subtractedUnits, String studentNumber) {
        String updateUnitsQuery = "UPDATE Students\n"
                + "SET student_units = ?\n"
                + "WHERE student_id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(updateUnitsQuery);
            stmt.setInt(1, subtractedUnits);
            stmt.setString(2, studentNumber);
            int rows = stmt.executeUpdate();

            return rows > 0;
        } catch (SQLException s) {
            s.printStackTrace();
            System.err.println(s.toString());
            System.err.println("");
        }

        return false;
    }

    public boolean insertStudent(Student newStudent) {
        String query = "INSERT INTO Students (student_id, student_fname, student_mname, student_lname, student_sex, student_birth, student_start, student_department, student_units, student_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false); 
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, newStudent.getStudentID());
            stmt.setString(2, newStudent.getFirstName());
            stmt.setString(3, newStudent.getMiddleName());
            stmt.setString(4, newStudent.getLastName());
            stmt.setString(5, newStudent.getSex());
            stmt.setString(6, newStudent.getBirthDate());
            stmt.setInt(7, newStudent.getStartYear());
            stmt.setString(8, newStudent.getDepartment());
            stmt.setInt(9, newStudent.getUnits());
            stmt.setString(10, newStudent.getAddress());

            int rows = stmt.executeUpdate();
            connection.commit(); 
            return rows > 0;
        } catch (SQLException s) {
            try {
                connection.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            s.printStackTrace();
            System.err.println("Error occurred while adding the student: " + s.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }
}
