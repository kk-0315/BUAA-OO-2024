import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

import java.io.IOException;

public class InputHandler extends Thread {
    private RequestTable mainRequestTable;

    public InputHandler(RequestTable mainRequestTable) {
        this.mainRequestTable = mainRequestTable;
    }

    @Override
    public void run() {
        try {
            ElevatorInput elevatorInput = new ElevatorInput(System.in);
            while (true) {
                PersonRequest request = elevatorInput.nextPersonRequest();
                // when request == null
                // it means there are no more lines in stdin
                if (request == null) {
                    this.mainRequestTable.setEndFlag();
                    break;
                } else {
                    // a new valid request
                    this.mainRequestTable.addRequest(new Person(request.getPersonId(),
                            request.getFromFloor(), request.getToFloor(), request.getElevatorId()));
                }
            }
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
