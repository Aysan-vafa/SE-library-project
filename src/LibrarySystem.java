import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibrarySystem {
    private StudentManager studentManager;
    private BookManager bookManager;
    private LoanManager loanManager;
    private MenuHandler menuHandler;
    private EmployeeManager employeeManager;

    public LibrarySystem() {
        this.studentManager = new StudentManager();
        this.bookManager = new BookManager();

        try {
            this.loanManager = new LoanManager(this.bookManager);
        } catch (Exception e) {

            this.loanManager = null;
        }
        this.menuHandler = new MenuHandler(this);
        this.employeeManager = new EmployeeManager();
    }

    public int getStudentCount() {
        return this.studentManager.getStudentCount();
    }

    public void registerStudent(String name, String studentId, String username, String password) {
        studentManager.registerStudent(name, studentId, username, password);
    }

    public Student authenticateStudent(String username, String password) {
        return studentManager.authenticateStudent(username, password);
    }

    // در کلاس LibrarySystem، متد editStudentInformation را کامل کنید:
    public void editStudentInformation(Student student) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Edit My Information ---");
        System.out.println("Current Information:");
        System.out.println(student);

        System.out.print("Enter new name (press Enter to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.trim().isEmpty()) {
            // در این ساختار ساده، برای تغییر نام نیاز به تغییر در StudentManager داریم
            // برای سادگی، فقط پیام نمایش می‌دهیم
            System.out.println("Name change requires administrator assistance.");
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        if (!newPassword.trim().isEmpty()) {
            // این بخش نیاز به پیاده‌سازی setter در کلاس Student و ذخیره دارد
            System.out.println("Password change feature will be implemented in next version.");
        }

        System.out.println("Information update completed.");
    }

    public void borrowBook(Student student) {
        if (loanManager != null) {
            loanManager.createLoanRequest(student);
        } else {
            System.out.println("Loan functionality not available.");
        }
    }

    // LibrarySystem
    // FPR_3-8: بازگرداندن کتاب توسط دانشجو (اصلاح‌شده)
    public void returnBook(Student student) {
        if (loanManager == null) {
            System.out.println("Loan functionality not available.");
            return;
        }

        List<BookLoan> activeLoans = loanManager.getApprovedLoansForStudent(student.getUsername());

        if (activeLoans.isEmpty()) {
            System.out.println("You have no active loans to return.");
            return;
        }

        System.out.println("\n--- Your Active Loans ---");
        for (int i = 0; i < activeLoans.size(); i++) {
            BookLoan loan = activeLoans.get(i);
            System.out.printf("%d. LoanID: %s | BookID: %s | From: %s | To: %s%n",
                    i + 1, loan.getLoanId(), loan.getBookId(), loan.getStartDate(), loan.getEndDate());
        }

        System.out.print("Enter the number of the loan to mark as returned: ");
        int choice;
        try {
            choice = Integer.parseInt(new Scanner(System.in).nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (choice < 0 || choice >= activeLoans.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        BookLoan selectedLoan = activeLoans.get(choice);

        // علامت‌گذاری بازگشت در LoanManager
        loanManager.markLoanReturned(selectedLoan);
        System.out.println("Book returned successfully!");
    }


    public void displayAvailableBooks() {
        System.out.println("\n--- All Books ---");
        for (Book b : bookManager.getAllBooks()) {
            System.out.println(b);
        }
    }

    public void displayGuestStatistics() {
        int totalStudents = studentManager.getStudentCount();
        int totalBooks = bookManager.getAllBooks().size();
        int totalLoans = (loanManager != null) ? loanManager.getLoanCount() : 0;
        int borrowedBooks = (loanManager != null) ? loanManager.getActiveLoanCount() : 0;

        System.out.println("\n--- Library Statistics ---");
        System.out.println("Total registered students: " + totalStudents);
        System.out.println("Total books: " + totalBooks);
        System.out.println("Total loans: " + totalLoans);
        System.out.println("Currently borrowed books: " + borrowedBooks);
    }


    public BookManager getBookManager() {
        return bookManager;
    }

    public StudentManager getStudentManager() {
        return studentManager;
    }

    public LoanManager getLoanManager() {
        return loanManager;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }



    public void start() {
        menuHandler.displayMainMenu();
    }

    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }
}
