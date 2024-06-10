import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryRequest;

import java.util.HashMap;

public class BorrowAndReturnLibrarian {
    private HashMap<LibraryBookId, Integer> broBooks;
    private Library library;

    public BorrowAndReturnLibrarian(Library library) {
        this.broBooks = new HashMap<>();
        this.library = library;
    }

    public boolean borrowBook(LibraryRequest request) {
        Reader reader = library.getReader(request.getStudentId());

        LibraryBookId libraryBookId = request.getBookId();
        if (!library.haveTheBook(libraryBookId)) {
            return false;
        }
        if (libraryBookId.isTypeA()) {
            return false;
        } else if (libraryBookId.isTypeB()) { //B or C
            if (reader.haveOneB()) {

                addBook(libraryBookId);
                library.subBook(libraryBookId);
                return false;
            } else {
                reader.addBook(libraryBookId);
                library.subBook(libraryBookId);
                return true;
            }
        } else {
            if (reader.haveOneSpeC(libraryBookId)) {
                addBook(libraryBookId);
                library.subBook(libraryBookId);
                return false;
            } else {
                reader.addBook(libraryBookId);
                library.subBook(libraryBookId);
                return true;
            }
        }

    }

    public void addBook(LibraryBookId libraryBookId) {
        if (broBooks.containsKey(libraryBookId)) {
            int pastNum = broBooks.get(libraryBookId);
            broBooks.replace(libraryBookId, pastNum + 1);
        } else {
            broBooks.put(libraryBookId, 1);
        }
    }

    public boolean returnBook(LibraryRequest request) {
        addBook(request.getBookId());
        Reader reader = library.getReader(request.getStudentId());
        reader.subBook(request.getBookId());
        return true;
    }

    public HashMap<LibraryBookId, Integer> getBroBooks() {
        return broBooks;
    }
}
