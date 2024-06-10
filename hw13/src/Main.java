import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryCommand;
import com.oocourse.library1.LibraryMoveInfo;
import com.oocourse.library1.LibraryRequest;
import com.oocourse.library1.LibrarySystem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Library library = new Library();
        Map<LibraryBookId, Integer> originBooks;
        originBooks = LibrarySystem.SCANNER.getInventory();
        library.init(originBooks);
        while (true) {
            LibraryCommand<?> command = LibrarySystem.SCANNER.nextCommand();
            if (command == null) {
                break;
            }
            LocalDate date = command.getDate();

            if (command.getCmd().equals("OPEN")) {
                // 在图书馆开门之前干点什么

                List<LibraryMoveInfo> lists = library.afterOpenSort(date);
                LibrarySystem.PRINTER.move(date, lists);
            } else if (command.getCmd().equals("CLOSE")) {
                // 在图书馆关门之后干点什么
                List<LibraryMoveInfo> lists1 = library.afterCloseSort(date);
                LibrarySystem.PRINTER.move(date, lists1);
            } else {
                LibraryRequest request = (LibraryRequest) command.getCmd();
                // 对 request 干点什么
                if (request.getType() == LibraryRequest.Type.QUERIED) {
                    int count = library.queryRequest(request);
                    LibrarySystem.PRINTER.info(date, request.getBookId(), count);
                } else if (request.getType() == LibraryRequest.Type.BORROWED) {
                    boolean borrowFlag = library.borrowRequest(request);
                    if (borrowFlag) {
                        LibrarySystem.PRINTER.accept(date, request);
                    } else {
                        LibrarySystem.PRINTER.reject(date, request);
                    }
                } else if (request.getType() == LibraryRequest.Type.ORDERED) {
                    boolean orderFlag = library.orderRequest(request);
                    if (orderFlag) {
                        LibrarySystem.PRINTER.accept(date, request);
                    } else {
                        LibrarySystem.PRINTER.reject(date, request);
                    }
                } else if (request.getType() == LibraryRequest.Type.PICKED) {
                    boolean pickFlag = library.pickRequest(request);
                    if (pickFlag) {
                        LibrarySystem.PRINTER.accept(date, request);
                    } else {
                        LibrarySystem.PRINTER.reject(date, request);
                    }
                } else if (request.getType() == LibraryRequest.Type.RETURNED) {
                    boolean returnFlag = library.returnRequest(request);
                    if (returnFlag) {
                        LibrarySystem.PRINTER.accept(date, request);
                    } else {
                        LibrarySystem.PRINTER.reject(date, request);
                    }
                }
            }
        }
    }

}
