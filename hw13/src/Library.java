import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;
import com.oocourse.library1.LibraryRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Library {
    private BorrowAndReturnLibrarian borrowAndReturnLibrarian;
    private OrderLibrarian orderLibrarian;
    private HashMap<LibraryBookId, Integer> shelf;//书籍编号+库存数量
    private HashMap<String, Reader> readers;

    public Library() {
        this.borrowAndReturnLibrarian = new BorrowAndReturnLibrarian(this);
        this.orderLibrarian = new OrderLibrarian(this);
        this.shelf = new HashMap<>();
        this.readers = new HashMap<>();
    }

    public void init(Map<LibraryBookId, Integer> originBooks) {
        shelf.putAll(originBooks);
    }

    public List<LibraryMoveInfo> afterOpenSort(LocalDate date) {
        List<LibraryMoveInfo> libraryMoveInfos = new ArrayList<>();
        //借还处还书还给书架
        HashMap<LibraryBookId, Integer> broBooks = borrowAndReturnLibrarian.getBroBooks();
        addSomeBooks(broBooks);
        for (LibraryBookId key : broBooks.keySet()) {
            for (int i = 0; i < broBooks.get(key); i++) {
                libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bs"));
            }
        }
        broBooks.clear();


        //预约处过期的书还回书架
        HashMap<LibraryBookId, ArrayList<Book>> aoBooks = orderLibrarian.getAoBooks();

        for (LibraryBookId key : aoBooks.keySet()) {
            ArrayList<Book> books = aoBooks.get(key);
            for (int i = books.size() - 1; i >= 0; i--) {
                if (books.get(i).isEnd(date)) {
                    addBook(key);
                    libraryMoveInfos.add(new LibraryMoveInfo(key, "ao", "bs"));
                    books.remove(i);
                }
            }

        }


        //某类书空了就remove
        Iterator<Map.Entry<LibraryBookId, ArrayList<Book>>> it = aoBooks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<LibraryBookId, ArrayList<Book>> entry = it.next();
            if ((entry.getValue()).isEmpty()) {
                it.remove();
            }
        }
        //书架送书到预约处
        HashMap<LibraryBookId, ArrayList<RegisterBook>> needBooks
                = orderLibrarian.getRegisterTable();
        HashMap<LibraryBookId, ArrayList<String>> delRegister = new HashMap<>();
        for (LibraryBookId bookId : needBooks.keySet()) {
            ArrayList<RegisterBook> registerBooks = needBooks.get(bookId);
            delRegister.put(bookId, new ArrayList<>());
            for (int i = registerBooks.size() - 1; i >= 0; i--) {
                RegisterBook registerBook = registerBooks.get(i);
                String readerId = registerBook.getReaderId();
                //预约处需要的书不够怎么办
                if (shelf.containsKey(bookId)) {
                    if (shelf.get(bookId) > 0) {
                        orderLibrarian.addBook(bookId, readerId, date, true);
                        libraryMoveInfos.add(new LibraryMoveInfo(bookId, "bs", "ao", readerId));
                        subBook(bookId);
                        delRegister.get(bookId).add(readerId);
                    }
                }
            }

        }
        for (LibraryBookId key : delRegister.keySet()) {
            ArrayList<String> delReaders = delRegister.get(key);
            for (int i = 0; i < delReaders.size(); i++) {
                orderLibrarian.subRegister(key, delReaders.get(i));
            }
        }
        return libraryMoveInfos;

    }

    public void subBook(LibraryBookId libraryBookId) {
        if (shelf.containsKey(libraryBookId)) {
            if (shelf.get(libraryBookId) > 0) {
                int pastNum = shelf.get(libraryBookId);
                shelf.replace(libraryBookId, pastNum - 1);
            }
        }
    }

    public void addSomeBooks(HashMap<LibraryBookId, Integer> books) {
        for (LibraryBookId key : books.keySet()) {
            for (int i = 0; i < books.get(key); i++) {
                if (!shelf.containsKey(key)) {
                    shelf.put(key, 1);
                } else {
                    int pastNum = shelf.get(key);
                    shelf.replace(key, pastNum + 1);
                }
            }
        }
    }

    public void addBook(LibraryBookId libraryBookId) {
        if (!shelf.containsKey(libraryBookId)) {
            shelf.put(libraryBookId, 1);
        } else {
            int pastNum = shelf.get(libraryBookId);
            shelf.replace(libraryBookId, pastNum + 1);
        }
    }

    public List<LibraryMoveInfo> afterCloseSort(LocalDate date) {
        List<LibraryMoveInfo> libraryMoveInfos = new ArrayList<>();
        //借还处还书还给书架
        HashMap<LibraryBookId, Integer> broBooks = borrowAndReturnLibrarian.getBroBooks();
        addSomeBooks(broBooks);
        for (LibraryBookId key : broBooks.keySet()) {
            for (int i = 0; i < broBooks.get(key); i++) {
                libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bs"));
            }
        }
        broBooks.clear();
        //预约处过期的书还回书架
        HashMap<LibraryBookId, ArrayList<Book>> aoBooks = orderLibrarian.getAoBooks();
        for (LibraryBookId key : aoBooks.keySet()) {
            ArrayList<Book> books = aoBooks.get(key);
            for (int i = books.size() - 1; i >= 0; i--) {
                if (books.get(i).isEnd(date)) {
                    addBook(key);
                    libraryMoveInfos.add(new LibraryMoveInfo(key, "ao", "bs"));
                    books.remove(i);
                }
            }

        }
        //某类书空了就remove
        Iterator<Map.Entry<LibraryBookId, ArrayList<Book>>> it = aoBooks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<LibraryBookId, ArrayList<Book>> entry = it.next();
            if ((entry.getValue()).isEmpty()) {
                it.remove();
            }
        }
        //书架送书到预约处
        HashMap<LibraryBookId, ArrayList<RegisterBook>> needBooks
                = orderLibrarian.getRegisterTable();
        HashMap<LibraryBookId, ArrayList<String>> delRegister = new HashMap<>();
        for (LibraryBookId bookId : needBooks.keySet()) {
            ArrayList<RegisterBook> registerBooks = needBooks.get(bookId);
            delRegister.put(bookId, new ArrayList<>());
            for (int i = registerBooks.size() - 1; i >= 0; i--) {
                RegisterBook registerBook = registerBooks.get(i);
                String readerId = registerBook.getReaderId();
                //预约处需要的书不够怎么办
                if (shelf.containsKey(bookId)) {
                    if (shelf.get(bookId) > 0) {
                        orderLibrarian.addBook(bookId, readerId, date, false);
                        libraryMoveInfos.add(new LibraryMoveInfo(bookId, "bs", "ao", readerId));
                        subBook(bookId);
                        delRegister.get(bookId).add(readerId);
                    }
                }
            }
        }
        for (LibraryBookId key : delRegister.keySet()) {
            ArrayList<String> delReaders = delRegister.get(key);
            for (int i = 0; i < delReaders.size(); i++) {
                orderLibrarian.subRegister(key, delReaders.get(i));
            }
        }

        return libraryMoveInfos;
    }

    public int queryRequest(LibraryRequest request) {
        LibraryBookId libraryBookId = request.getBookId();
        return shelf.get(libraryBookId);
    }

    public boolean borrowRequest(LibraryRequest request) {
        if (!readers.containsKey(request.getStudentId())) {
            readers.put(request.getStudentId(), new Reader(request.getStudentId()));
        }
        return borrowAndReturnLibrarian.borrowBook(request);
    }

    public boolean haveTheBook(LibraryBookId libraryBookId) {
        if (shelf.containsKey(libraryBookId)) {
            if (shelf.get(libraryBookId) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean orderRequest(LibraryRequest request) {
        if (!readers.containsKey(request.getStudentId())) {
            readers.put(request.getStudentId(), new Reader(request.getStudentId()));
        }

        return orderLibrarian.orderBook(request);
    }

    public boolean returnRequest(LibraryRequest request) {

        return borrowAndReturnLibrarian.returnBook(request);
    }

    public boolean pickRequest(LibraryRequest request) {
        LibraryBookId libraryBookId = request.getBookId();
        String readerId = request.getStudentId();
        Reader reader = readers.get(readerId);
        if (orderLibrarian.haveBookForReader(libraryBookId, readerId)
                && reader.canPickBook(libraryBookId)) {
            reader.addBook(libraryBookId);
            orderLibrarian.subBook(libraryBookId, readerId);
            return true;
        }
        return false;
    }

    public Reader getReader(String id) {
        return readers.get(id);
    }
}
