import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class LoanManager {
    private List<BookLoan> loans;
    private BookManager bookManager;
    private static final String LOAN_FILE = "loans.txt";
    private Scanner scanner;

    public LoanManager(BookManager bookManager) {
        this.bookManager = bookManager;
        this.loans = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        loadLoans();
    }

    public void createLoanRequest(Student student) {
        System.out.println("\n--- Create Loan Request ---");

        List<Book> allBooks = bookManager.getAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("No books available in the system.");
            return;
        }


        System.out.println("Available Books:");
        for (int i = 0; i < allBooks.size(); i++) {
            System.out.println((i + 1) + ". " + allBooks.get(i));
        }

        System.out.print("Select book number: ");
        int bookIndex = getIntInput(1, allBooks.size()) - 1;
        Book selectedBook = allBooks.get(bookIndex);

        if (selectedBook.isBorrowed()) {
            System.out.println("Sorry, this book is already borrowed.");
            return;
        }

        System.out.print("Start Date (yyyy-MM-dd): ");
        String startDate = scanner.nextLine();

        System.out.print("End Date (yyyy-MM-dd): ");
        String endDate = scanner.nextLine();

        String loanId = UUID.randomUUID().toString();
        BookLoan loan = new BookLoan(loanId, student.getUsername(), selectedBook.getId(), startDate, endDate, false);
        loans.add(loan);
        saveLoans();
        System.out.println("Loan request created successfully and is waiting for approval.");
    }
    public int getLoanCount() {
        return loans.size();
    }

    public int getActiveLoanCount() {
        int count = 0;
        for (BookLoan loan : loans) {
            if (loan.isApproved() && !bookManager.findById(loan.getBookId()).isBorrowed()) {
                count++;
            }
        }
        return count;
    }

    private void loadLoans() {
        File file = new File(LOAN_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                BookLoan loan = BookLoan.fromFileString(line);
                if (loan != null) loans.add(loan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLoans() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOAN_FILE))) {
            for (BookLoan loan : loans) {
                pw.println(loan.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public List<BookLoan> getPendingLoans() {
        List<BookLoan> pending = new ArrayList<>();
        for (BookLoan loan : loans) {
            if (loan.getStatus() == BookLoan.LoanStatus.PENDING) {
                pending.add(loan);
            }
        }
        return pending;
    }


    public void approveLoan(BookLoan loan) {
        loan.setStatus(BookLoan.LoanStatus.APPROVED);

        Book book = bookManager.findBookById(loan.getBookId());
        if (book != null) {
            book.setBorrowed(true);
        }

        saveLoans();
    }

    public void rejectLoan(BookLoan loan) {
        loan.setStatus(BookLoan.LoanStatus.REJECTED);
        saveLoans();
    }

    public void updateLoan(BookLoan loan) {

        saveLoans();
    }
    public List<BookLoan> getPendingLoansForApproval() {
        List<BookLoan> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        for (BookLoan loan : loans) {
            if (loan.getStatus() == BookLoan.LoanStatus.PENDING) {
                try {
                    LocalDate start = LocalDate.parse(loan.getStartDate());
                    if (start.equals(today) || start.equals(yesterday)) {
                        result.add(loan);
                    }
                } catch (Exception e) {
                    // اگر فرمت تاریخ درست نیست، از آن رد می‌شویم
                }
            }
        }
        return result;
    }

    // FPR_3-6: گرفتن امانت‌های تاییدشده (فعال) متعلق به یک دانشجو
    public List<BookLoan> getApprovedLoansForStudent(String studentUsername) {
        List<BookLoan> result = new ArrayList<>();
        for (BookLoan loan : loans) {
            if (loan.getStudentUsername().equals(studentUsername) && loan.getStatus() == BookLoan.LoanStatus.APPROVED) {
                result.add(loan);
            }
        }
        return result;
    }

    // FPR_3-8: علامت‌گذاری بازگشت کتاب
    public void markLoanReturned(BookLoan loan) {
        if (loan == null) return;
        // برای سادگی: وقتی کتاب برگشت، وضعیت را REJECTED قرار می‌دهیم (در ساختار فعلی رمزخورده)
        loan.setStatus(BookLoan.LoanStatus.REJECTED);
        // کتاب را در BookManager آزاد کن
        Book book = bookManager.findBookById(loan.getBookId());
        if (book != null) {
            book.setBorrowed(false);
            bookManager.saveBooks();
        }
        saveLoans();
    }

}
