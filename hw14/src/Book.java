import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;

public class Book {
    private LibraryBookId libraryBookId;
    private String readerId;
    private LocalDate inDay;
    private boolean isAfterOpen;

    public Book(LibraryBookId libraryBookId, String readerId,
                LocalDate inDay, Boolean isAfterOpen) {
        this.libraryBookId = libraryBookId;
        this.readerId = readerId;
        this.inDay = inDay;
        this.isAfterOpen = isAfterOpen;
    }

    public String getReaderId() {
        return readerId;
    }

    public boolean isEnd(LocalDate nowDay,Boolean isAfterSort) {
        if (isAfterOpen) {
            return nowDay.minusDays(4).isAfter(inDay)||(nowDay.minusDays(4).isEqual(inDay)&&isAfterSort);
        } else {
            return nowDay.minusDays(5).isAfter(inDay)||(nowDay.minusDays(5).isEqual(inDay)&&isAfterSort);
        }
    }
}
