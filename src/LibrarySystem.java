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

    public void editStudentInformation(Student student) {
        System.out.println("Not implemented.");
    }

    public void borrowBook(Student student) {
        if (loanManager != null) {
            loanManager.createLoanRequest(student);
        } else {
            System.out.println("Loan functionality not available.");
        }
    }

    // LibrarySystem
    public void returnBook(Student student) {
        if (loanManager == null) {
            System.out.println("Loan functionality not available.");
            return;
        }


        List<BookLoan> activeLoans = new ArrayList<>();
        for (BookLoan loan : loanManager.getPendingLoans()) {
            if (loan.getStudentUsername().equals(student.getUsername()) && loan.isApproved()) {
                activeLoans.add(loan);
            }
        }

        if (activeLoans.isEmpty()) {
            System.out.println("You have no active loans to return.");
            return;
        }

        System.out.println("\n--- Your Active Loans ---");
        for (int i = 0; i < activeLoans.size(); i++) {
            BookLoan loan = activeLoans.get(i);
            System.out.printf("%d. BookID: %s | From: %s | To: %s%n",
                    i + 1, loan.getBookId(), loan.getStartDate(), loan.getEndDate());
        }

        System.out.print("Enter the number of the loan to return: ");
        int choice = new Scanner(System.in).nextInt() - 1;

        if (choice < 0 || choice >= activeLoans.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        BookLoan selectedLoan = activeLoans.get(choice);


        Book book = bookManager.findBookById(selectedLoan.getBookId());
        if (book != null) {
            book.setBorrowed(false);
        }


        selectedLoan.setStatus(BookLoan.LoanStatus.REJECTED); // می‌تونیم از REJECTED استفاده کنیم یا یک فیلد returned اضافه کنیم
        loanManager.saveLoans();

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
