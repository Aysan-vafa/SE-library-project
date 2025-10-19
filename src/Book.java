
public class Book {
    private String id;
    private String title;
    private String author;
    private int publishYear;
    private boolean isBorrowed;

    public Book(String id, String title, String author, int publishYear, boolean isBorrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
        this.isBorrowed = isBorrowed;
    }


    public Book(String id, String title, String author, int publishYear) {
        this(id, title, author, publishYear, false);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Title: " + title +
                " | Author: " + author +
                " | Year: " + publishYear +
                " | Status: " + (isBorrowed ? "Borrowed" : "Available");
    }

    public String toFileString() {

        return id + ";" + escapeSemi(title) + ";" + escapeSemi(author) + ";" + publishYear + ";" + isBorrowed;
    }

    public static Book fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) return null;
        String[] parts = line.split(";");
        if (parts.length != 5) return null;
        try {
            String id = parts[0];
            String title = unescapeSemi(parts[1]);
            String author = unescapeSemi(parts[2]);
            int year = Integer.parseInt(parts[3]);
            boolean borrowed = Boolean.parseBoolean(parts[4]);
            return new Book(id, title, author, year, borrowed);
        } catch (Exception e) {
            return null;
        }
    }

    private static String escapeSemi(String s) {
        if (s == null) return "";
        return s.replace(";", "\\;");
    }

    private static String unescapeSemi(String s) {
        if (s == null) return "";
        return s.replace("\\;", ";");
    }
}
