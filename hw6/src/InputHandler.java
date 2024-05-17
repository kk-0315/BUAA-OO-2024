import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.ResetRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class InputHandler extends Thread {
    private RequestTable mainRequestTable;
    private HashMap<Integer, RequestTable> requestTables;
    private ArrayList<Elevator> elevators;

    public InputHandler(RequestTable mainRequestTable, HashMap<Integer, RequestTable>
            requestTables, ArrayList<Elevator> elevators) {

        this.mainRequestTable = mainRequestTable;
        this.requestTables = requestTables;
        this.elevators = elevators;
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
                    } else if (request instanceof ResetRequest) {
                        //System.out.println("reset");
                        // an ElevatorRequest
                        // your code here
                        //System.out.println(request);
                        int elevatorId = ((ResetRequest) request).getElevatorId();
                        int capacity = ((ResetRequest) request).getCapacity();
                        double speed = ((ResetRequest) request).getSpeed();
                        Reset reset = new Reset(elevatorId, capacity, speed);
                        //System.out.println(reset.getElevatorId());
                        //mainRequestTable.addReset(reset);
                        requestTables.get(reset.getElevatorId()).addReset(reset);
                        mainRequestTable.addResetNum();
                        //elevators.get(reset.getElevatorId()).setResetFlag();
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
