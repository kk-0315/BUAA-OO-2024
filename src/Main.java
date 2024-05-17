import com.oocourse.elevator3.TimableOutput;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        RequestTable mainRequestTable = new RequestTable();
        HashMap<Integer, RequestTable> eleRequests = new HashMap<>();
        HashMap<Integer, Elevator> elevators = new HashMap<>();
        HashMap<Integer, Boolean> atFlags = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            atFlags.put(i, false);
        }
        for (int i = 1; i <= 6; i++) {
            eleRequests.put(i, new RequestTable());
        }


        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, eleRequests.get(i),
                    mainRequestTable, atFlags.get(i));
            elevators.put(i, elevator);
        }

        Schedule schedule = new Schedule(mainRequestTable, eleRequests, elevators);
        InputHandler inputHandler = new InputHandler(mainRequestTable,
                eleRequests, elevators, atFlags);//顺序有关系吗？
        for (int i = 1; i <= 6; i++) {
            elevators.get(i).start();
        }
        schedule.start();
        inputHandler.start();


    }
}
