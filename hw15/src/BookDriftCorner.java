import com.oocourse.library3.LibraryBookId;

import java.util.ArrayList;

public class BookDriftCorner {
    private ArrayList<LibraryBookId> driftBookShelf;

    public BookDriftCorner() {
        this.driftBookShelf = new ArrayList<>();

    }

    public void donate(LibraryBookId libraryBookId) {
        driftBookShelf.add(libraryBookId);
    }

    public void addBook(LibraryBookId libraryBookId) {
        driftBookShelf.add(libraryBookId);
    }

    public void subBook(LibraryBookId libraryBookId) {
        driftBookShelf.remove(libraryBookId);
    }

    public boolean haveTheBook(LibraryBookId libraryBookId) {
        return driftBookShelf.contains(libraryBookId);
    }

    public int queryBook(LibraryBookId libraryBookId) {
        if (driftBookShelf.contains(libraryBookId)) {
            return 1;
        } else {
            return 0;
        }
    }

}
