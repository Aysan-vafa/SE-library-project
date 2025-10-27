import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private List<Employee> employees;
    private static final String FILE_NAME = "employees.txt";

    public EmployeeManager() {
        employees = new ArrayList<>();
        employees.add(new Employee("Ali", "ali123", "1234"));
        employees.add(new Employee("Sara", "sara", "abcd"));
        loadEmployees();
    }

    public void changePassword(Employee emp, String newPassword) {
        emp.setPassword(newPassword);
        System.out.println("Password changed successfully for employee: " + emp.getUsername());
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee emp) {
        employees.add(emp);
        saveEmployees();
    }

    public Employee authenticate(String username, String password) {
        for (Employee e : employees) {
            if (e.getUsername().equals(username) && e.getPassword().equals(password)) {
                return e;
            }
        }
        return null;
    }

    private void saveEmployees() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Employee e : employees) {
                writer.write(e.toFileString());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Error saving employees: " + ex.getMessage());
        }
    }

    private void loadEmployees() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Employee e = Employee.fromFileString(line);
                if (e != null) employees.add(e);
            }
        } catch (IOException ex) {
            System.out.println("Error loading employees: " + ex.getMessage());
        }
    }
}

