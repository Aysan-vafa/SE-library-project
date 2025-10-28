public class Employee {
    private String name;
    private String username;
    private String password;
    private int booksAdded;
    private int loansApproved;
    private int booksReceived;

    public Employee(String name, String username, String password) {
        this(name, username, password, 0, 0, 0);
    }

    public Employee(String name, String username, String password,
                    int booksAdded, int loansApproved, int booksReceived) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.booksAdded = booksAdded;
        this.loansApproved = loansApproved;
        this.booksReceived = booksReceived;
    }


    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public int getBooksAdded() { return booksAdded; }
    public int getLoansApproved() { return loansApproved; }
    public int getBooksReceived() { return booksReceived; }

    public void incBooksAdded() { this.booksAdded++; }
    public void incLoansApproved() { this.loansApproved++; }
    public void incBooksReceived() { this.booksReceived++; }

    @Override
    public String toString() {
        return "Name: " + name + " | Username: " + username +
                " | booksAdded: " + booksAdded +
                " | loansApproved: " + loansApproved +
                " | booksReceived: " + booksReceived;
    }

    // ذخیره به فایل — قالب: name,username,password,booksAdded,loansApproved,booksReceived
    public String toFileString() {
        return name + "," + username + "," + password + "," +
                booksAdded + "," + loansApproved + "," + booksReceived;
    }

    // خواندن از فایل: هم ورژن قدیمی (3 فیلد) و هم ورژن جدید (6 فیلد) پشتیبانی می‌شود
    public static Employee fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            // قدیمی: تنها name,username,password
            return new Employee(parts[0], parts[1], parts[2]);
        } else if (parts.length >= 6) {
            try {
                int bAdded = Integer.parseInt(parts[3]);
                int lApp = Integer.parseInt(parts[4]);
                int bRec = Integer.parseInt(parts[5]);
                return new Employee(parts[0], parts[1], parts[2], bAdded, lApp, bRec);
            } catch (Exception e) {
                // اگر خطا در parse شد، باز هم یک نمونه با مقادیر صفر برگردان
                return new Employee(parts[0], parts[1], parts[2]);
            }
        } else {
            return null;
        }
    }
}