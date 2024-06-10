import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryRequest;
import com.oocourse.library2.annotation.Trigger;

import java.time.LocalDate;
import java.util.HashMap;

public class BorrowAndReturnLibrarian {
    private HashMap<LibraryBookId, Integer> broBooks;
    private Library library;
    private BookDriftCorner bookDriftCorner;
    private HashMap<LibraryBookId, Integer> driftBorrowCounts;

    public BorrowAndReturnLibrarian(Library library, BookDriftCorner bookDriftCorner) {
        this.broBooks = new HashMap<>();
        this.library = library;
        this.bookDriftCorner = bookDriftCorner;
        this.driftBorrowCounts = new HashMap<>();
    }

    @Trigger(from = "AtBdc", to = {"AtReaders","AtBro"})
    @Trigger(from = "AtBs", to = {"AtReaders","AtBro"})
    public boolean borrowBook(LibraryRequest request, LocalDate localDate) {
        Reader reader = library.getReader(request.getStudentId());
        LibraryBookId libraryBookId = request.getBookId();
        if (!library.haveTheBook(libraryBookId)) {
            if (bookDriftCorner.haveTheBook(libraryBookId)) {
                if (libraryBookId.isTypeAU()) {
                    return false;
                } else if (libraryBookId.isTypeBU()) {
                    if (reader.haveOneBU()) {
                        addBook(libraryBookId);
                        bookDriftCorner.subBook(libraryBookId);

                        return false;
                    } else {
                        reader.addBook(libraryBookId, localDate);
                        bookDriftCorner.subBook(libraryBookId);

                        return true;
                    }
                } else {
                    if (reader.haveOneSpeCU(libraryBookId)) {
                        addBook(libraryBookId);
                        bookDriftCorner.subBook(libraryBookId);
                        return false;
                    } else {
                        reader.addBook(libraryBookId, localDate);
                        bookDriftCorner.subBook(libraryBookId);
                        return true; } }
            }
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
                reader.addBook(libraryBookId, localDate);
                library.subBook(libraryBookId);
                return true; }
        } else {
            if (reader.haveOneSpeC(libraryBookId)) {
                addBook(libraryBookId);
                library.subBook(libraryBookId);
                return false;
            } else {
                reader.addBook(libraryBookId, localDate);
                library.subBook(libraryBookId);
                return true; } } }

    public void addBook(LibraryBookId libraryBookId) {
        if (broBooks.containsKey(libraryBookId)) {
            int pastNum = broBooks.get(libraryBookId);
            broBooks.replace(libraryBookId, pastNum + 1);
        } else {
            broBooks.put(libraryBookId, 1);
        }
    }

    public void addCount(LibraryBookId libraryBookId) {
        if (driftBorrowCounts.containsKey(libraryBookId)) {
            int pastCount = driftBorrowCounts.get(libraryBookId);
            driftBorrowCounts.replace(libraryBookId, pastCount + 1);
        } else {
            driftBorrowCounts.put(libraryBookId, 1);
        }
    }

    public boolean isTwice(LibraryBookId libraryBookId) {
        if (driftBorrowCounts.containsKey(libraryBookId)) {
            return driftBorrowCounts.get(libraryBookId) >= 2;
        } else {
            return false;
        }
    }

    public boolean returnBook(LibraryRequest request, LocalDate localDate) {
        LibraryBookId libraryBookId = request.getBookId();
        if (libraryBookId.isTypeBU() || libraryBookId.isTypeCU()) {
            addCount(libraryBookId);
        }

        addBook(request.getBookId());
        Reader reader = library.getReader(request.getStudentId());
        if (reader.isOverDue(libraryBookId, localDate)) {
            reader.subBook(request.getBookId());
            return false;
        } else {
            reader.subBook(request.getBookId());
            return true;
        }

    }

    public HashMap<LibraryBookId, Integer> getBroBooks() {
        return broBooks;
    }
}
