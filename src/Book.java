
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
        return id + ";" + title + ";" + author + ";" + publishYear + ";" + isBorrowed;
    }

    public static Book fromFileString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 5) return null;
        return new Book(
                parts[0],
                parts[1],
                parts[2],
                Integer.parseInt(parts[3]),
                Boolean.parseBoolean(parts[4])
        );
    }
}
