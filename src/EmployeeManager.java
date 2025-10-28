import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public void recordBookAdded(Employee emp) {
        if (emp == null) return;
        emp.incBooksAdded();
        saveEmployees();
    }

    public void recordLoanApproved(Employee emp) {
        if (emp == null) return;
        emp.incLoansApproved();
        saveEmployees();
    }

    public void recordBookReceived(Employee emp) {
        if (emp == null) return;
        emp.incBooksReceived();
        saveEmployees();
    }
    public void addEmployeeInteractive() { // FPR_4-1
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Add New Employee ---");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        Employee emp = new Employee(name, username, password);
        employees.add(emp);
        saveEmployees();
        System.out.println("Employee added successfully.");
    }
    // نمایش عملکرد ساده (FPR_4-2)
    public void printEmployeePerformance() {
        System.out.println("\n--- Employee Performance ---");
        for (Employee e : employees) {
            System.out.printf("Username: %s | Name: %s | booksAdded: %d | loansApproved: %d | booksReceived: %d%n",
                    e.getUsername(), e.getName(), e.getBooksAdded(), e.getLoansApproved(), e.getBooksReceived());
        }
    }
    public void removeEmployeeInteractive() { // FPR_4-4
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter employee username to remove: ");
        String username = sc.nextLine();
        boolean removed = employees.removeIf(e -> e.getUsername().equals(username));
        if (removed) {
            saveEmployees();
            System.out.println("Employee removed.");
        } else {
            System.out.println("Employee not found.");
        }
    }

}

