import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;
import java.util.HashMap;

public class InputHandler extends Thread {
    private RequestTable mainRequestTable;
    private HashMap<Integer, RequestTable> requestTables;

    private Schedule schedule;
    private HashMap<Integer, Boolean> atFlags;

    public InputHandler(RequestTable mainRequestTable, HashMap<Integer, RequestTable>
            requestTables, HashMap<Integer, Elevator> elevators,
                        HashMap<Integer, Boolean> atFlags) {

        this.mainRequestTable = mainRequestTable;
        this.requestTables = requestTables;
        this.atFlags = atFlags;
        this.schedule = new Schedule(mainRequestTable, requestTables, elevators);
    }

    @Override
    public void run() {
        try {
            ElevatorInput elevatorInput = new ElevatorInput(System.in);
            while (true) {
                Request request = elevatorInput.nextRequest();

                // when request == null
                // it means there are no more lines in stdin
                if (request == null) {
                    //System.out.println("mainend");
                    this.mainRequestTable.setEndFlag();
                    //System.out.println("Inputend");
                    break;
                } else {
                    if (request instanceof PersonRequest) {
                        // a PersonRequest
                        // your code here
                        int personId = ((PersonRequest) request).getPersonId();
                        int fromFloor = ((PersonRequest) request).getFromFloor();
                        int toFloor = ((PersonRequest) request).getToFloor();
                        this.mainRequestTable.addRequest(new Person(personId,
                                fromFloor, toFloor));
                    } else if (request instanceof NormalResetRequest) {
                        //System.out.println("reset");
                        // an ElevatorRequest
                        // your code here
                        //System.out.println(request);
                        int elevatorId = ((NormalResetRequest) request).getElevatorId();
                        int capacity = ((NormalResetRequest) request).getCapacity();
                        double speed = ((NormalResetRequest) request).getSpeed();
                        Reset reset = new NormalReset(elevatorId, capacity, speed);
                        //System.out.println(reset.getElevatorId());
                        //mainRequestTable.addReset(reset);
                        requestTables.get(reset.getElevatorId()).addReset(reset);
                        mainRequestTable.addResetNum();
                        //elevators.get(reset.getElevatorId()).setResetFlag();
                    } else if (request instanceof DoubleCarResetRequest) {
                        int elevatorId = ((DoubleCarResetRequest) request).getElevatorId();
                        int capacity = ((DoubleCarResetRequest) request).getCapacity();
                        double speed = ((DoubleCarResetRequest) request).getSpeed();
                        int transferFloor = ((DoubleCarResetRequest) request).getTransferFloor();
                        Reset reset = new DcReset(elevatorId, capacity, speed, transferFloor);
                        requestTables.get(reset.getElevatorId()).addReset(reset);
                        mainRequestTable.addResetNum();
                        //requestTables.get(reset.getElevatorId()).setResetFlag(true);
                        this.schedule.create(elevatorId, transferFloor,
                                capacity, speed, atFlags.get(elevatorId));

                    }
                    // a new valid request
                }
            }
            elevatorInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
