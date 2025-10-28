
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookManager {
    private List<Book> books;
    private static final String BOOK_FILE = "books.txt";

    public BookManager() {
        this.books = new ArrayList<>();
        loadBooks();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }


    public Book createBook(String title, String author, int publishYear) {
        String id = UUID.randomUUID().toString();
        Book b = new Book(id, title, author, publishYear, false);
        addBook(b);
        return b;
    }


    public List<Book> searchBooks(String title, String author, Integer year) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            boolean match = true;
            if (title != null && !title.trim().isEmpty() &&
                    !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                match = false;
            }
            if (author != null && !author.trim().isEmpty() &&
                    !book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                match = false;
            }
            if (year != null && book.getPublishYear() != year) {
                match = false;
            }
            if (match) results.add(book);
        }
        return results;
    }

    public boolean addBook(String id, String title, String author, int year) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return false;
            }
        }
        Book newBook = new Book(id, title, author, year, true);
        books.add(newBook);
        saveBooks();
        return true;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public Book findById(String id) {
        for (Book b : books) {
            if (b.getId().equals(id)) return b;
        }
        return null;
    }

    public void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : books) {
                pw.println(b.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void loadBooks() {
        File file = new File(BOOK_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Book b = Book.fromFileString(line);
                if (b != null) books.add(b);
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }


    public void updateBook(Book book) {
        // in this simple list-backed manager, objects are the same references;
        // just persist to disk
        saveBooks();
    }

    public Book findBookById(String id) {
        for (Book book : books) {
            if (book.getId().equalsIgnoreCase(id)) {
                return book;
            }
        }
        return null;
    }

    public boolean editBook(String id, String newTitle, String newAuthor, int newYear) {
        Book book = findBookById(id);
        if (book != null) {
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setYear(newYear);
            saveBooks();
            return true;
        }
        return false;
    }

}
