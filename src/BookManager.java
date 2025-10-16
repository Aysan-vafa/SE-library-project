import java.io.*;
import java.util.*;

public class BookManager {
    private List<Book> books;
    private static final String BOOK_FILE = "books";

    public BookManager() {
        books = new ArrayList<>();
        loadBooks();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooks();
    }

    public List<Book> searchBooks(String title, String author, Integer year) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            boolean match = true;
            if (title != null && !title.isEmpty() && !book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                match = false;
            }
            if (author != null && !author.isEmpty() && !book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                match = false;
            }
            if (year != null && book.getPublishYear() != year) {
                match = false;
            }
            if (match) results.add(book);
        }
        return results;
    }

    private void loadBooks() {
        File file = new File(BOOK_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Book book = Book.fromFileString(line);
                if (book != null) books.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOK_FILE))) {
            for (Book book : books) {
                pw.println(book.toFileString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        return books;
    }
}