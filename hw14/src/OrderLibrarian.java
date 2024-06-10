import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OrderLibrarian {
    private HashMap<LibraryBookId, ArrayList<Book>> aoBooks;
    private HashMap<LibraryBookId, ArrayList<RegisterBook>> registerTable;
    private Library library;

    public OrderLibrarian(Library library) {
        this.aoBooks = new HashMap<>();
        this.library = library;
        this.registerTable = new HashMap<>();
    }

    public boolean orderBook(LibraryRequest request) {
        if (request.getBookId().isTypeAU() || request.getBookId().isTypeBU()
                || request.getBookId().isTypeCU()) {
            return false;
        }
        Reader reader = library.getReader(request.getStudentId());
        LibraryBookId libraryBookId = request.getBookId();
        String readerId = request.getStudentId();
        if (reader.canOrderBook(libraryBookId)) {
            if (registerTable.containsKey(libraryBookId)) {
                registerTable.get(libraryBookId).add(new RegisterBook(libraryBookId, readerId));
            } else {
                ArrayList<RegisterBook> registerBooks = new ArrayList<>();
                registerBooks.add(new RegisterBook(libraryBookId, readerId));
                registerTable.put(libraryBookId, registerBooks);
            }
            return true;
        } else {
            return false;
        }
    }

    public HashMap<LibraryBookId, ArrayList<Book>> getAoBooks() {
        return aoBooks;
    }

    public HashMap<LibraryBookId, ArrayList<RegisterBook>> getRegisterTable() {
        return registerTable;
    }

    public void addBook(LibraryBookId libraryBookId, String readerId,
                        LocalDate date, Boolean isAfterOpen) {
        if (!aoBooks.containsKey(libraryBookId)) {
            ArrayList<Book> books = new ArrayList<>();
            books.add(new Book(libraryBookId, readerId, date, isAfterOpen));
            aoBooks.put(libraryBookId, books);
        } else {
            aoBooks.get(libraryBookId).add(new Book(libraryBookId, readerId, date, isAfterOpen));
        }
    }

    public boolean haveRegister(LibraryBookId libraryBookId) {
        if ((registerTable.containsKey(libraryBookId) &&
                !registerTable.get(libraryBookId).isEmpty()) ||
                (aoBooks.containsKey(libraryBookId) && !aoBooks.get(libraryBookId).isEmpty())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean haveBookForReader(LibraryBookId libraryBookId, String readerId) {
        if (aoBooks.containsKey(libraryBookId)) {
            ArrayList<Book> books = aoBooks.get(libraryBookId);
            for (int i = 0; i < books.size(); i++) {
                if (Objects.equals(books.get(i).getReaderId(), readerId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void subBook(LibraryBookId libraryBookId, String readerId) {
        if (aoBooks.containsKey(libraryBookId)) {
            ArrayList<Book> books = aoBooks.get(libraryBookId);
            int flag = 0;
            for (int i = 0; i < books.size(); i++) {
                if (Objects.equals(books.get(i).getReaderId(), readerId)) {
                    flag = i;
                    break;
                }
            }
            books.remove(flag);
            if (books.isEmpty()) {
                aoBooks.remove(libraryBookId);
            }
        }
    }

    public void subRegister(LibraryBookId libraryBookId, String readerId) {
        if (registerTable.containsKey(libraryBookId)) {
            ArrayList<RegisterBook> registerBooks = registerTable.get(libraryBookId);
            int flag = 0;
            for (int i = 0; i < registerBooks.size(); i++) {
                if (Objects.equals(registerBooks.get(i).getReaderId(), readerId)) {
                    flag = i;
                    break;
                }
            }
            registerBooks.remove(flag);
            if (registerBooks.isEmpty()) {
                registerTable.remove(libraryBookId);
            }
        }
    }
}
