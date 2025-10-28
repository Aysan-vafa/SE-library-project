public class BookLoan {
    private String loanId;
    private String studentUsername;
    private String bookId;
    private String startDate;
    private String endDate;
    private boolean approved;

    public BookLoan(String loanId, String studentUsername, String bookId, String startDate, String endDate, boolean approved) {
        this.loanId = loanId;
        this.studentUsername = studentUsername;
        this.bookId = bookId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.approved = approved;

        if (approved) {
            this.status = LoanStatus.APPROVED;
        } else {
            this.status = LoanStatus.PENDING;
        }
    }

    public enum LoanStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    private LoanStatus status;

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }


    public String getLoanId() {
        return loanId;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getBookId() {
        return bookId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public boolean isApproved() {
        return status == LoanStatus.APPROVED;
    }


    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String toFileString() {
        return loanId + ";" + studentUsername + ";" + bookId + ";" + startDate + ";" + endDate + ";" + approved;
    }

    public static BookLoan fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 6) return null;
        return new BookLoan(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                Boolean.parseBoolean(parts[5])
        );
    }

    @Override
    public String toString() {
        return "LoanID: " + loanId +
                " | Student: " + studentUsername +
                " | BookID: " + bookId +
                " | From: " + startDate +
                " | To: " + endDate +
                " | Status: " + (approved ? "Approved" : "Pending");
    }
}

