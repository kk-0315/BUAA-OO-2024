import com.oocourse.elevator1.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        RequestTable mainRequestTable = new RequestTable();
        HashMap<Integer, RequestTable> eleRequests = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            eleRequests.put(i, new RequestTable());
        }
        InputHandler inputHandler = new InputHandler(mainRequestTable);
        Schedule schedule = new Schedule(mainRequestTable, eleRequests);
        ArrayList<Elevator> elevators = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, eleRequests.get(i));
            elevators.add(elevator);
        }

        inputHandler.start();
        schedule.start();

        for (Elevator elevator : elevators) {
            elevator.start();
        }

    }
}
