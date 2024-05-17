import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Schedule extends Thread {
    private RequestTable mainRequestTable;
    private HashMap<Integer, RequestTable> requestTables;//管理各个电梯请求表的容器
    private ArrayList<Elevator> elevators;//管理电梯的容器
    private HashMap<Integer, ArrayList<Person>> wait;

    private static int instructionNum = 0;

    public Schedule(RequestTable mainRequestTable,
                    HashMap<Integer, RequestTable> requestTables, ArrayList<Elevator> elevators) {
        this.mainRequestTable = mainRequestTable;
        this.requestTables = requestTables;
        this.elevators = elevators;
        this.wait = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            this.wait.put(i, new ArrayList<>());
        }

    }

    public Boolean isWaitEmpty() {
        for (int i = 1; i <= 6; i++) {
            if (!wait.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        while (true) {
            //System.out.println("1");
            if (mainRequestTable.isEmpty() && mainRequestTable.isEnd()
                    && mainRequestTable.isResetEmpty() && isWaitEmpty()) {
                //System.out.println("end");
                for (int i = 1; i <= 6; i++) {
                    requestTables.get(i).setEndFlag();
                }
                //System.out.println("schend");
                return;
            }
            for (int i = 1; i <= 6; i++) {

                if (!requestTables.get(i).isReseting() && !wait.get(i).isEmpty()) {
                    for (int j = 0; j < wait.get(i).size(); j++) {
                        TimableOutput.println("RECEIVE-" + wait.get(i).get(j).getId() + "-" + i);
                        requestTables.get(i).addRequest(wait.get(i).get(j));
                    }
                    wait.replace(i, new ArrayList<>());
                }
            }

            Person person = mainRequestTable.getOneRequest();//...从mainRequestTable中得到一个request

            if (person != null) {
                int num = Choose(person);
                if (num == 0) {
                    continue;
                }
                //将person需求加入合适的电梯表
                requestTables.get(num).addRequest(person);
            }


        }
    }

    public int Choose(Person person) { //调度策略:?
        instructionNum++;
        //(int) (Math.random() * 6) + 1风险太高
        //instructionNum%6+1
        if (requestTables.get(instructionNum % 6 + 1).isReseting()) {
            wait.get(instructionNum % 6 + 1).add(person);
            return 0;
        } else {
            person.setElevatorId(instructionNum % 6 + 1);
            TimableOutput.println("RECEIVE-" + person.getId() + "-" + (instructionNum % 6 + 1));
            //System.out.println(elevators.get(1).isEmpty());
            //System.out.println(person.getFromFloor()+" "+person.getToFloor());
            return person.getElevatorId();
        }
    }
}
