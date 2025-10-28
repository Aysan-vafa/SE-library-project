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

    // FPR_3-6: گزارش تاریخچه امانات یک دانشجو
    public void displayStudentLoanHistory(String studentUsername) {
        List<BookLoan> studentLoans = new ArrayList<>();
        int totalLoans = 0;
        int notReturnedCount = 0;
        int delayedReturnCount = 0;

        for (BookLoan loan : loans) {
            if (loan.getStudentUsername().equals(studentUsername)) {
                studentLoans.add(loan);
                totalLoans++;

                // منطق ساده برای تشخیص تحویل‌های با تاخیر
                if (loan.getStatus() == BookLoan.LoanStatus.APPROVED) {
                    notReturnedCount++;
                    // بررسی تاخیر (فرض: اگر endDate گذشته باشد ولی هنوز APPROVED است = تاخیر)
                    try {
                        LocalDate endDate = LocalDate.parse(loan.getEndDate());
                        if (LocalDate.now().isAfter(endDate)) {
                            delayedReturnCount++;
                        }
                    } catch (Exception e) {
                        // ignore date parsing errors
                    }
                }
            }
        }

        System.out.println("\n--- Loan History for Student: " + studentUsername + " ---");
        System.out.println("Total loans: " + totalLoans);
        System.out.println("Not returned books: " + notReturnedCount);
        System.out.println("Delayed returns: " + delayedReturnCount);

        if (!studentLoans.isEmpty()) {
            System.out.println("\nDetailed Loan History:");
            for (BookLoan loan : studentLoans) {
                System.out.println(loan);
            }
        }
    }

    // FPR_4-3: آمار امانات کتاب
    public void displayLoanStatistics() {
        int totalRequests = loans.size();
        int approvedLoans = 0;
        long totalDays = 0;
        int countedLoans = 0;

        for (BookLoan loan : loans) {
            if (loan.getStatus() == BookLoan.LoanStatus.APPROVED) {
                approvedLoans++;
                try {
                    LocalDate start = LocalDate.parse(loan.getStartDate());
                    LocalDate end = LocalDate.parse(loan.getEndDate());
                    long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
                    totalDays += days;
                    countedLoans++;
                } catch (Exception e) {
                    // ignore date parsing errors
                }
            }
        }

        double averageDays = countedLoans > 0 ? (double) totalDays / countedLoans : 0;

        System.out.println("\n--- Loan Statistics ---");
        System.out.println("Total loan requests: " + totalRequests);
        System.out.println("Total approved loans: " + approvedLoans);
        System.out.printf("Average loan duration: %.2f days%n", averageDays);
    }

    // متد جدید برای گرفتن لیست دانشجویان با بیشترین تاخیر
    public Map<String, Integer> getStudentsWithMostDelays() {
        Map<String, Integer> studentDelays = new HashMap<>();

        for (BookLoan loan : loans) {
            if (loan.getStatus() == BookLoan.LoanStatus.APPROVED) {
                try {
                    LocalDate endDate = LocalDate.parse(loan.getEndDate());
                    if (LocalDate.now().isAfter(endDate)) {
                        String username = loan.getStudentUsername();
                        studentDelays.put(username, studentDelays.getOrDefault(username, 0) + 1);
                    }
                } catch (Exception e) {
                    // ignore date parsing errors
                }
            }
        }

        return studentDelays;
    }

    // متد کمکی برای گرفتن همه وام‌ها
    public List<BookLoan> getAllLoans() {
        return new ArrayList<>(loans);
    }
}