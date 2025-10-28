
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManager {
    private List<Student> students;
    private static final String FILE_NAME = "students";

    public StudentManager() {
        this.students = new ArrayList<>();
        loadStudents();
    }

    public void registerStudent(String name, String studentId, String username, String password) {
        if (isUsernameTaken(username)) {
            System.out.println("This username already exists. Please choose a different username.");
            return;
        }

        Student newStudent = new Student(name, studentId, username, password, true);
        students.add(newStudent);
        saveStudents();
        System.out.println("Student registration completed successfully.");
    }

    public Student authenticateStudent(String username, String password) {
        for (Student s : students) {
            if (s.getUsername().equals(username) && s.getPassword().equals(password)) {
                if (!s.isActive()) {
                    System.out.println("Your account is deactivated. Please contact the library.");
                    return null;
                }
                return s;
            }
        }
        return null;
    }

    public void displayStudents() {
        System.out.println("\n--- List of Registered Students ---");

        if (students.isEmpty()) {
            System.out.println("No students have registered yet.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private boolean isUsernameTaken(String username) {
        return students.stream().anyMatch(s -> s.getUsername().equals(username));
    }

    public int getStudentCount() {
        return students.size();
    }

    public Student findByUsername(String username) {
        return students.stream()
                .filter(s -> s.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    public void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.write(s.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    private void loadStudents() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Student s = Student.fromFileString(line);
                if (s != null) {
                    students.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
    // FPR_3-7: interactive toggle student active status
    public void toggleStudentStatusInteractive() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter student's username to toggle status: ");
        String username = sc.nextLine();
        Student s = findByUsername(username);
        if (s == null) {
            System.out.println("Student not found.");
            return;
        }
        s.setActive(!s.isActive());
        saveStudents();
        System.out.println("Student " + username + " active status is now: " + (s.isActive() ? "active" : "inactive"));
    }

}
