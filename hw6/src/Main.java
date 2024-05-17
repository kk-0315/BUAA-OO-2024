import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        RequestTable mainRequestTable = new RequestTable();
        HashMap<Integer, RequestTable> eleRequests = new HashMap<>();
        ArrayList<Elevator> elevators = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            eleRequests.put(i, new RequestTable());
        }
        elevators.add(new Elevator(0, new RequestTable(), mainRequestTable));//0号空电梯

        for (int i = 1; i <= 6; i++) {
            Elevator elevator = new Elevator(i, eleRequests.get(i), mainRequestTable);
            elevators.add(elevator);
        }

        Schedule schedule = new Schedule(mainRequestTable, eleRequests, elevators);
        InputHandler inputHandler = new InputHandler(mainRequestTable,
                eleRequests, elevators);//顺序有关系吗？
        for (int i = 1; i <= 6; i++) {
            elevators.get(i).start();
        }
        schedule.start();
        inputHandler.start();


    }
}
