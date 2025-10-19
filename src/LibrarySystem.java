
public class LibrarySystem {
    private StudentManager studentManager;
    private BookManager bookManager;
    private LoanManager loanManager;
    private MenuHandler menuHandler;

    public LibrarySystem() {
        this.studentManager = new StudentManager();
        this.bookManager = new BookManager();

        try {
            this.loanManager = new LoanManager(this.bookManager);
        } catch (Exception e) {

            this.loanManager = null;
        }
        this.menuHandler = new MenuHandler(this);
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

    public void returnBook(Student student) {
        System.out.println("Not implemented.");
    }

    public void displayAvailableBooks() {
        System.out.println("\n--- All Books ---");
        for (Book b : bookManager.getAllBooks()) {
            System.out.println(b);
        }
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

    public void start() {
        menuHandler.displayMainMenu();
    }

    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.start();
    }
}
