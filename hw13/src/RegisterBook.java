import com.oocourse.library1.LibraryBookId;

public class RegisterBook {
    private LibraryBookId libraryBookId;

    private String readerId;

    public RegisterBook(LibraryBookId libraryBookId, String readerId) {
        this.libraryBookId = libraryBookId;
        this.readerId = readerId;

    }

    public String getReaderId() {
        return readerId;
    }

}
