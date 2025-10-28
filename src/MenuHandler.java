
import java.util.List;
import java.util.Scanner;

public class MenuHandler {
    private Scanner scanner;
    private LibrarySystem librarySystem;
    private Student currentUser;

    public MenuHandler(LibrarySystem librarySystem) {
        this.scanner = new Scanner(System.in);
        this.librarySystem = librarySystem;
        this.currentUser = null;
    }

    public void displayMainMenu() {
        while (true) {
            System.out.println("\n=== University Library Management System ===");
            System.out.println("1. Student Registration");
            System.out.println("2. Student Login");
            System.out.println("3. View Registered Student Count");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");

            int choice = getIntInput(1, 4);

            switch (choice) {
                case 1:
                    handleStudentRegistration();
                    break;
                case 2:
                    handleStudentLogin();
                    break;
                case 3:
                    displayStudentCount();
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
            System.out.println("___________________________");
        }
    }

    private void displayStudentCount() {
        int studentCount = librarySystem.getStudentCount();
        System.out.println("\nTotal registered students: " + studentCount);
    }

    private void handleStudentRegistration() {
        System.out.println("\n--- New Student Registration ---");

        System.out.print("Student name: ");
        String name = scanner.nextLine();

        System.out.print("Student ID: ");
        String studentId = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        librarySystem.registerStudent(name, studentId, username, password);
    }

    private void handleStudentLogin() {
        System.out.println("\n--- Student Login ---");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = librarySystem.authenticateStudent(username, password);

        if (currentUser != null) {
            System.out.println("Login successful! Welcome, " + currentUser.getName());
            displayLoggedInStudentMenu();
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private void displayLoggedInStudentMenu() {
        while (currentUser != null) {
            System.out.println("\n=== Student Dashboard ===");
            System.out.println("1. View My Information");
            System.out.println("2. Edit My Information");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. View Available Books");
            System.out.println("6. Search Books"); // added in FPR_1-3
            System.out.println("7. Logout");
            System.out.print("Please enter your choice: ");

            int choice = getIntInput(1, 7);

            switch (choice) {
                case 1:
                    System.out.println("\n--- My Information ---");
                    System.out.println(currentUser);
                    break;
                case 2:
                    librarySystem.editStudentInformation(currentUser);
                    break;
                case 3:
                    librarySystem.borrowBook(currentUser);
                    break;
                case 4:
                    librarySystem.returnBook(currentUser);
                    break;
                case 5:
                    librarySystem.displayAvailableBooks();
                    break;
                case 6:
                    handleSearchBooks();
                    break;
                case 7:
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private void handleSearchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.print("Title (press Enter to skip): ");
        String title = scanner.nextLine();

        System.out.print("Author (press Enter to skip): ");
        String author = scanner.nextLine();

        System.out.print("Publish Year (press Enter to skip): ");
        String yearInput = scanner.nextLine();
        Integer year = null;
        if (!yearInput.isEmpty()) {
            try {
                year = Integer.parseInt(yearInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year, ignored.");
            }
        }

        List<Book> results = librarySystem.getBookManager().searchBooks(title, author, year);
        if (results == null || results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\n--- Search Results ---");
            for (Book b : results) {
                System.out.println(b);
            }
        }
    }

    private int getIntInput(int min, int max) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.printf("Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    private void displayGuestMenu() {
        System.out.println("\n=== Guest Menu ===");
        System.out.println("1. View total registered students");
        System.out.println("2. View library statistics");
        System.out.println("3. Search books by title");
        System.out.println("4. Back to main menu");
        System.out.print("Please enter your choice: ");

        int choice = getIntInput(1, 4);
        switch (choice) {
            case 1:
                System.out.println("Total registered students: " + librarySystem.getStudentCount());
                break;
            case 2:
                librarySystem.displayGuestStatistics();
                break;
            case 3:
                handleSearchBooks();
                break;
            case 4:
                return;
        }
    }


    private void handleEmployeeLogin() {
        System.out.println("\n--- Employee Login ---");

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Employee emp = librarySystem.getEmployeeManager().authenticate(username, password);
        if (emp != null) {
            System.out.println("Login successful! Welcome, " + emp.getName());
            showEmployeeMenu(emp);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private void handleChangeEmployeePassword(Employee emp) {
        System.out.println("\n--- Change Password ---");
        System.out.print("Enter your current password: ");
        String current = scanner.nextLine();

        if (!emp.getPassword().equals(current)) {
            System.out.println("Incorrect current password. Try again.");
            return;
        }

        System.out.print("Enter new password: ");
        String newPass = scanner.nextLine();

        System.out.print("Confirm new password: ");
        String confirm = scanner.nextLine();

        if (!newPass.equals(confirm)) {
            System.out.println("Passwords do not match. Try again.");
            return;
        }

        librarySystem.getEmployeeManager().changePassword(emp, newPass);
    }

    private void showEmployeeMenu(Employee employee) {
        int choice;
        do {
            System.out.println("\n===== Employee Menu =====");
            System.out.println("1. Change Password");
            System.out.println("2. Add New Book");
            System.out.println("3. Edit Book Information"); // 🆕 جدید
            System.out.println("4. Approve Loan Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleChangeEmployeePassword(employee);
                    break;
                case 2:
                    handleAddNewBook();
                    break;
                case 3:
                    handleEditBook();
                    break;
                case 4:
                    handleLoanApprovalMenu(employee);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 5);
    }


    private void handleAddNewBook() {
        System.out.println("\n=== Add New Book ===");
        System.out.print("Enter Book ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Publication Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        boolean success = librarySystem.getBookManager().addBook(id, title, author, year);


        if (success) {
            System.out.println("✅ Book added successfully!");
        } else {
            System.out.println("⚠️ Book with this ID already exists!");
        }
    }
    private void handleEditBook() {
        System.out.println("\n=== Edit Book Information ===");
        System.out.print("Enter Book ID to edit: ");
        String id = scanner.nextLine();

        BookManager bookManager = librarySystem.getBookManager();
        Book book = bookManager.findBookById(id);

        if (book == null) {
            System.out.println("❌ No book found with this ID.");
            return;
        }

        System.out.println("\nCurrent Book Information:");
        System.out.println(book);

        System.out.print("New Title (press Enter to keep current): ");
        String newTitle = scanner.nextLine();
        if (newTitle.isEmpty()) newTitle = book.getTitle();

        System.out.print("New Author (press Enter to keep current): ");
        String newAuthor = scanner.nextLine();
        if (newAuthor.isEmpty()) newAuthor = book.getAuthor();

        System.out.print("New Publication Year (press Enter to keep current): ");
        String yearInput = scanner.nextLine();
        int newYear = book.getPublishYear();
        if (!yearInput.isEmpty()) {
            try {
                newYear = Integer.parseInt(yearInput);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid year entered, keeping old value.");
            }
        }

        boolean success = bookManager.editBook(id, newTitle, newAuthor, newYear);

        if (success) {
            System.out.println("✅ Book information updated successfully!");
        } else {
            System.out.println("⚠️ Failed to update book information.");
        }
    }


}
