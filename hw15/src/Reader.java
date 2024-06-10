import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.annotation.SendMessage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Reader {
    private final String id;
    private ArrayList<LibraryBookId> haveBooks;
    private HashMap<LibraryBookId, LocalDate> inDays;
    private int credit;
    private ArrayList<LibraryBookId> overDueBooks;

    public Reader(String id) {
        this.id = id;
        this.haveBooks = new ArrayList<>();
        this.inDays = new HashMap<>();
        this.credit = 10;
        this.overDueBooks = new ArrayList<>();
    }

    public void checkCloseDue(LocalDate nowDate) {
        for (LibraryBookId libraryBookId : inDays.keySet()) {
            LocalDate inDay = inDays.get(libraryBookId);
            if (!overDueBooks.contains(libraryBookId)) {
                if (libraryBookId.isTypeB()) {
                    if (nowDate.minusDays(30).isEqual(inDay)) {
                        subCredit(2);
                        overDueBooks.add(libraryBookId);
                    }
                } else if (libraryBookId.isTypeC()) {
                    if (nowDate.minusDays(60).isEqual(inDay)) {
                        subCredit(2);
                        overDueBooks.add(libraryBookId);
                    }
                } else if (libraryBookId.isTypeBU()) {
                    if (nowDate.minusDays(7).isEqual(inDay)) {
                        subCredit(2);
                        overDueBooks.add(libraryBookId);
                    }
                } else if (libraryBookId.isTypeCU()) {
                    if (nowDate.minusDays(14).isEqual(inDay)) {
                        subCredit(2);
                        overDueBooks.add(libraryBookId);
                    }
                }
            }

        }
    }

    public void checkOpenDue(LocalDate nowDate) {
        for (LibraryBookId libraryBookId : inDays.keySet()) {
            if (isOverDue(libraryBookId, nowDate) && !overDueBooks.contains(libraryBookId)) {
                subCredit(2);
                overDueBooks.add(libraryBookId);
            }
        }
    }

    public int getCredit() {
        return credit;
    }

    public boolean creditIsNeg() {
        return credit < 0;
    }

    public String getId() {
        return id;
    }

    public void addCredit(int num) {
        credit += num;
        if (credit > 20) {
            credit = 20;
        }
    }

    public void subCredit(int num) {
        credit -= num;
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
        if (overDueBooks.contains(libraryBookId)) {
            overDueBooks.remove(libraryBookId);
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

    @SendMessage(from = "Library", to = "Reader")
    public void getOrderedBook() {

    }

}
