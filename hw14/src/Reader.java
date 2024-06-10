import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
    private final String id;
    private ArrayList<LibraryBookId> haveBooks;
    private HashMap<LibraryBookId, LocalDate> inDays;

    public Reader(String id) {
        this.id = id;
        this.haveBooks = new ArrayList<>();
        this.inDays = new HashMap<>();
    }

    public boolean haveOneB() {
        for (int i = 0; i < haveBooks.size(); i++) {
            if (haveBooks.get(i).isTypeB()) {
                return true;
            }
        }
        return false;
    }

    public boolean haveOneBU() {
        for (int i = 0; i < haveBooks.size(); i++) {
            if (haveBooks.get(i).isTypeBU()) {
                return true;
            }
        }
        return false;
    }

    public void addBook(LibraryBookId libraryBookId, LocalDate localDate) {
        haveBooks.add(libraryBookId);
        inDays.put(libraryBookId, localDate);

    }

    public void subBook(LibraryBookId libraryBookId) {
        if (haveBooks.contains(libraryBookId)) {
            haveBooks.remove(libraryBookId);
        }
        if (inDays.containsKey(libraryBookId)) {
            inDays.remove(libraryBookId);
        }
    }

    public boolean isOverDue(LibraryBookId libraryBookId, LocalDate nowDate) {
        LocalDate date = inDays.get(libraryBookId);
        if (libraryBookId.isTypeB()) {
            return nowDate.minusDays(30).isAfter(date);
        } else if (libraryBookId.isTypeC()) {
            return nowDate.minusDays(60).isAfter(date);
        } else if (libraryBookId.isTypeBU()) {
            return nowDate.minusDays(7).isAfter(date);
        } else {
            return nowDate.minusDays(14).isAfter(date);
        }
    }

    public boolean tryRenew(LibraryBookId libraryBookId, LocalDate localDate,
                            Boolean isBookBeingOrderedAndNoBookOnShelf) {
        LocalDate inDay = inDays.get(libraryBookId);
        if (libraryBookId.isTypeB()) {
            if (!(inDay.plusDays(25).isBefore(localDate) &&
                    inDay.plusDays(31).isAfter(localDate))) {
                return false;
            }
        } else if (libraryBookId.isTypeC()) {
            if (!(inDay.plusDays(55).isBefore(localDate) &&
                    inDay.plusDays(61).isAfter(localDate))) {
                return false;
            }
        } else if (libraryBookId.isTypeBU()) {
            if (!(inDay.plusDays(2).isBefore(localDate) && inDay.plusDays(8).isAfter(localDate))) {
                return false;
            }
        } else {
            if (!(inDay.plusDays(9).isBefore(localDate) && inDay.plusDays(15).isAfter(localDate))) {
                return false;
            }
        }
        if (isBookBeingOrderedAndNoBookOnShelf) {
            return false;
        }
        inDays.replace(libraryBookId, inDay.plusDays(30));
        return true;
    }

    public boolean haveOneSpeC(LibraryBookId libraryBookId) {
        for (int i = 0; i < haveBooks.size(); i++) {
            if (haveBooks.get(i).equals(libraryBookId)) {
                return true;
            }
        }
        return false;
    }

    public boolean haveOneSpeCU(LibraryBookId libraryBookId) {
        for (int i = 0; i < haveBooks.size(); i++) {
            if (haveBooks.get(i).equals(libraryBookId)) {
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
