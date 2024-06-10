import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryMoveInfo;
import com.oocourse.library3.LibraryRequest;
import com.oocourse.library3.annotation.SendMessage;
import com.oocourse.library3.annotation.Trigger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Library {
    private BorrowAndReturnLibrarian borrowAndReturnLibrarian;
    private OrderLibrarian orderLibrarian;
    private BookDriftCorner bookDriftCorner;
    private HashMap<LibraryBookId, Integer> shelf;//书籍编号+库存数量
    private HashMap<String, Reader> readers;
    private HashMap<String, ArrayList<LibraryBookId>> donateTable;

    public Library() {
        this.bookDriftCorner = new BookDriftCorner();
        this.borrowAndReturnLibrarian = new BorrowAndReturnLibrarian(this, this.bookDriftCorner);
        this.orderLibrarian = new OrderLibrarian(this);
        this.shelf = new HashMap<>();
        this.readers = new HashMap<>();
        this.donateTable = new HashMap<>();
    }

    @Trigger(from = "InitState", to = "AtBs")
    public void init(Map<LibraryBookId, Integer> originBooks) {
        shelf.putAll(originBooks);
    }

    @Trigger(from = "AtBro", to = {"AtBs", "AtBdc"})
    public void fromBro2BsOrBdc(List<LibraryMoveInfo> libraryMoveInfos) {
        HashMap<LibraryBookId, Integer> broBooks = borrowAndReturnLibrarian.getBroBooks();

        //addSomeBooks(broBooks);
        for (LibraryBookId key : broBooks.keySet()) {
            for (int i = 0; i < broBooks.get(key); i++) {
                if (key.isTypeB() || key.isTypeC()) { //A和AU不会移动
                    addBook(key);
                    libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bs"));
                } else {
                    if (borrowAndReturnLibrarian.isTwice(key)) {
                        //绑定捐献的书和捐献的人
                        int flag = 0;
                        for (String readerId : donateTable.keySet()) {
                            ArrayList<LibraryBookId> arr = donateTable.get(readerId);
                            for (int j = 0; j < arr.size(); j++) {
                                if (arr.get(j).equals(key)) {
                                    readers.get(readerId).addCredit(2);
                                    flag = 1;
                                    break;
                                }
                            }
                            if (flag == 1) {
                                break;
                            }
                        }
                        if (key.isTypeBU()) {
                            addBook(new LibraryBookId(LibraryBookId.Type.B, key.getUid()));
                            libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bs"));
                        } else {
                            addBook(new LibraryBookId(LibraryBookId.Type.C, key.getUid()));
                            libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bs"));
                        }
                        //不用清空次数吧
                    } else {
                        bookDriftCorner.addBook(key);
                        libraryMoveInfos.add(new LibraryMoveInfo(key, "bro", "bdc"));
                    }
                }

            }
        }
        broBooks.clear();
    }

    @Trigger(from = "AtAo", to = "AtBs")
    public void fromAo2Bs(List<LibraryMoveInfo> libraryMoveInfos,
                          LocalDate date, Boolean isAfterSort) {
        HashMap<LibraryBookId, ArrayList<Book>> aoBooks = orderLibrarian.getAoBooks();

        for (LibraryBookId key : aoBooks.keySet()) {
            ArrayList<Book> books = aoBooks.get(key);
            for (int i = books.size() - 1; i >= 0; i--) {
                if (books.get(i).isEnd(date, isAfterSort)) {
                    //逾期信用-3
                    Reader reader = readers.get(books.get(i).getReaderId());
                    reader.subCredit(3);
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
    }

    @Trigger(from = "AtBs", to = "AtAo")
    @SendMessage(from = "Library", to = "OrderLibrarian")
    public void fromBs2Ao(List<LibraryMoveInfo> libraryMoveInfos,
                          LocalDate date, Boolean isAfterOpen) {
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
                        orderLibrarian.addBook(bookId, readerId, date, isAfterOpen);
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
    }

    public List<LibraryMoveInfo> afterOpenSort(LocalDate date) {

        List<LibraryMoveInfo> libraryMoveInfos = new ArrayList<>();
        for (String s : readers.keySet()) { //先进行逾期扣分
            readers.get(s).checkOpenDue(date);
        }

        //预约处过期的书还回书架
        fromAo2Bs(libraryMoveInfos, date, false);
        //借还处还书还给书架 or 漂流处
        fromBro2BsOrBdc(libraryMoveInfos);


        //书架送书到预约处
        fromBs2Ao(libraryMoveInfos, date, true);

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
        for (String s : readers.keySet()) {
            readers.get(s).checkCloseDue(date);
        }
        //预约处过期的书还回书架
        fromAo2Bs(libraryMoveInfos, date, true);
        //借还处还书还给书架
        fromBro2BsOrBdc(libraryMoveInfos);

        //书架送书到预约处
        fromBs2Ao(libraryMoveInfos, date, false);


        return libraryMoveInfos;
    }

    public int queryRequest(LibraryRequest request) {
        LibraryBookId libraryBookId = request.getBookId();
        if (libraryBookId.isTypeA() || libraryBookId.isTypeB() || libraryBookId.isTypeC()) {
            return shelf.get(libraryBookId);
        } else {
            return bookDriftCorner.queryBook(libraryBookId);
        }
    }

    public int queryCreditRequest(String readerId) {
        if (!readers.containsKey(readerId)) {
            readers.put(readerId, new Reader(readerId));
        }
        return readers.get(readerId).getCredit();
    }

    public boolean borrowRequest(LibraryRequest request, LocalDate localDate) {
        if (!readers.containsKey(request.getStudentId())) {
            readers.put(request.getStudentId(), new Reader(request.getStudentId()));
        }

        return borrowAndReturnLibrarian.borrowBook(request, localDate);
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
        Reader reader = readers.get(request.getStudentId());
        if (reader.creditIsNeg()) {
            return false;
        }
        return orderLibrarian.orderBook(request);
    }

    @Trigger(from = "AtReaders", to = "AtBro")
    public boolean returnRequest(LibraryRequest request, LocalDate localDate) {
        boolean flag = borrowAndReturnLibrarian.returnBook(request, localDate);
        Reader reader = readers.get(request.getStudentId());
        if (flag) {
            reader.addCredit(1);
        }
        return flag;
    }

    @Trigger(from = "AtAo", to = "AtReaders")
    public boolean pickRequest(LibraryRequest request, LocalDate localDate) {
        LibraryBookId libraryBookId = request.getBookId();
        String readerId = request.getStudentId();
        Reader reader = readers.get(readerId);
        if (orderLibrarian.haveBookForReader(libraryBookId, readerId)
                && reader.canPickBook(libraryBookId)) {
            reader.addBook(libraryBookId, localDate);
            orderLibrarian.subBook(libraryBookId, readerId);
            return true;
        }
        return false;
    }

    @Trigger(from = "InitState", to = "AtBdc")
    public void donateRequest(LibraryRequest request) {
        if (!readers.containsKey(request.getStudentId())) {
            readers.put(request.getStudentId(), new Reader(request.getStudentId()));
        }
        LibraryBookId libraryBookId = request.getBookId();
        bookDriftCorner.donate(libraryBookId);
        Reader reader = readers.get(request.getStudentId());
        reader.addCredit(2);
        addDonateTable(reader.getId(), libraryBookId);

    }

    public void addDonateTable(String readerId, LibraryBookId libraryBookId) {
        if (donateTable.containsKey(readerId)) {
            donateTable.get(readerId).add(libraryBookId);
        } else {
            ArrayList<LibraryBookId> arr = new ArrayList<>();
            arr.add(libraryBookId);
            donateTable.put(readerId, arr);
        }
    }

    public boolean renewRequest(LibraryRequest request, LocalDate localDate) {

        Reader reader = readers.get(request.getStudentId());
        if (reader.creditIsNeg()) {
            return false;
        }
        LibraryBookId libraryBookId = request.getBookId();
        if (libraryBookId.isTypeBU() || libraryBookId.isTypeAU() || libraryBookId.isTypeCU()) {
            return false;
        }
        //预约成功送到ao就删除了，包括在ao处的书吗
        boolean isBookBeingOrderedAndNoBookOnShelf =
                orderLibrarian.haveRegister(libraryBookId) && shelf.get(libraryBookId) == 0;

        return reader.tryRenew(libraryBookId, localDate, isBookBeingOrderedAndNoBookOnShelf);
    }

    public Reader getReader(String id) {
        return readers.get(id);
    }

    @SendMessage(from = "Main", to = "Library")
    public void orderNewBook() {

    }
}
