import com.oocourse.library1.LibraryBookId;

import java.util.HashMap;

public class Reader {
    private final String id;
    private HashMap<LibraryBookId, Integer> haveBooks;

    public Reader(String id) {
        this.id = id;
        this.haveBooks = new HashMap<>();
    }

    public boolean haveOneB() {
        for (LibraryBookId key : haveBooks.keySet()) {
            if (key.isTypeB() && haveBooks.get(key) > 0) {
                return true;
            }
        }
        return false;
    }

    public void addBook(LibraryBookId libraryBookId) {
        if (haveBooks.containsKey(libraryBookId)) {
            int pastNum = haveBooks.get(libraryBookId);
            haveBooks.replace(libraryBookId, pastNum + 1);
        } else {
            haveBooks.put(libraryBookId, 1);
        }
    }

    public void subBook(LibraryBookId libraryBookId) {
        if (haveBooks.containsKey(libraryBookId)) {
            if (haveBooks.get(libraryBookId) > 0) {
                int pastNum = haveBooks.get(libraryBookId);
                haveBooks.replace(libraryBookId, pastNum - 1);
                if (haveBooks.get(libraryBookId) == 0) {
                    haveBooks.remove(libraryBookId);
                }
            }
        }
    }

    public boolean haveOneSpeC(LibraryBookId libraryBookId) {
        for (LibraryBookId key : haveBooks.keySet()) {
            if (key.isTypeC() && key.equals(libraryBookId) && haveBooks.get(key) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean canOrderBook(LibraryBookId libraryBookId) {
        if (libraryBookId.isTypeA()) {
            return false;
        } else if (libraryBookId.isTypeB()) {
            if (haveOneB()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (haveOneSpeC(libraryBookId)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean canPickBook(LibraryBookId libraryBookId) {
        if (libraryBookId.isTypeA()) {
            return false;
        } else if (libraryBookId.isTypeB()) {
            if (haveOneB()) {
                return false;
            } else {
                return true;
            }
        } else {
            if (haveOneSpeC(libraryBookId)) {
                return false;
            } else {
                return true;
            }
        }
    }

}
