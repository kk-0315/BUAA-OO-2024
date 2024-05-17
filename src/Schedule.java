import com.oocourse.elevator3.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Schedule extends Thread {
    private RequestTable mainRequestTable;
    private HashMap<Integer, RequestTable> requestTables;//管理各个电梯请求表的容器
    private HashMap<Integer, Elevator> elevators;//管理电梯的容器
    private HashMap<Integer, ArrayList<Person>> wait;
    private static int instructionNum = -1;

    public Schedule(RequestTable mainRequestTable,
                    HashMap<Integer, RequestTable> requestTables,
                    HashMap<Integer, Elevator> elevators) {
        this.mainRequestTable = mainRequestTable;
        this.requestTables = requestTables;
        this.elevators = elevators;
        this.wait = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            this.wait.put(i, new ArrayList<>());
        }

    }

    public void create(int elevatorId, int transferFloor, int capacity,
                       double speed, Boolean atFlag) {

        RequestTable requestTable = new RequestTable();
        requestTables.put(elevatorId + 6, requestTable);
        Elevator elevator = new Elevator(elevatorId + 6, requestTable,
                mainRequestTable, transferFloor + 1, 11,
                transferFloor, capacity, speed, atFlag);
        elevator.setSecId(2);

        elevators.put(elevatorId + 6, elevator);//有问题
        elevator.start();
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

            if (mainRequestTable.isEnd()
                    && mainRequestTable.isResetEmpty() && isWaitEmpty()
                    && mainRequestTable.isCrossNumEmpty() && mainRequestTable.isEmpty()) {
                for (Integer key : requestTables.keySet()) {
                    requestTables.get(key).setEndFlag();
                }
                //System.out.println("return");
                return;

            } else if (mainRequestTable.isEnd()
                    && mainRequestTable.isResetEmpty() && isWaitEmpty()
                    && !mainRequestTable.isCrossNumEmpty() && mainRequestTable.isEmpty()) {
                //System.out.println("end");

                //System.out.println("schend");
                try {
                    mainRequestTable.waitRequest();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            schWait();

            Person person = mainRequestTable.getOneRequest();//...从mainRequestTable中得到一个request

            if (person != null) {
                if (person.getElevatorId() != 0) {

                    if (person.getElevatorId() > 6) {
                        TimableOutput.println("RECEIVE-" + person.getId() +
                                "-" + (person.getElevatorId() - 6) + "-" + "B");
                    } else {
                        TimableOutput.println("RECEIVE-" + person.getId() +
                                "-" + (person.getElevatorId()) + "-" + "A");
                    }
                    requestTables.get(person.getElevatorId()).addRequest(person);
                    mainRequestTable.subCrossNum();
                } else {
                    int num = Choose(person);
                    if (num == 0) {
                        continue;
                    }
                    //将person需求加入合适的电梯表
                    requestTables.get(num).addRequest(person);
                }
            }

            //TimableOutput.println("************");
        }
    }

    public void schWait() {
        for (int i = 1; i <= 6; i++) {

            if (!requestTables.get(i).isReseting() && !wait.get(i).isEmpty()) {
                for (int j = 0; j < wait.get(i).size(); j++) {
                    Person person = wait.get(i).get(j);
                    if (requestTables.get(i).isDC()) {
                        if (person.getFromFloor() > elevators.get(i).getMaxFloor()) {
                            if (person.getToFloor() < elevators.get(i).getMaxFloor()) {
                                mainRequestTable.addCrossNum();
                            }
                            person.setElevatorId(i + 6);
                            TimableOutput.println("RECEIVE-" + person.getId() + "-" +
                                    (i) + "-" + "B");

                        } else if (person.getFromFloor() == elevators.get(i).getMaxFloor()) {
                            if (person.getToFloor() > person.getFromFloor()) {
                                person.setElevatorId(i + 6);
                                TimableOutput.println("RECEIVE-" + person.getId() +
                                        "-" + (i) + "-" + "B");

                            } else {
                                person.setElevatorId(i);
                                TimableOutput.println("RECEIVE-" + person.getId() +
                                        "-" + (i) + "-" + "A");

                            }
                        } else {
                            if (person.getToFloor() > elevators.get(i).getMaxFloor()) {
                                mainRequestTable.addCrossNum();
                            }
                            person.setElevatorId(i);
                            TimableOutput.println("RECEIVE-" + person.getId() +
                                    "-" + (i) + "-" + "A");

                        }
                    } else {
                        person.setElevatorId(i);
                        TimableOutput.println("RECEIVE-" + person.getId() + "-" + (i));

                    }
                    requestTables.get(person.getElevatorId()).addRequest(person);
                }
                wait.replace(i, new ArrayList<>());
            }
        }
    }

    public int getNum() {
        int num = 0;
        int min = 99;
        int flag = 0;
        for (int i = 1; i <= 6; i++) {
            num += requestTables.get(i).getRequestNum();
            num += elevators.get(i).getNowNum();
            if (requestTables.get(i).isDC()) {
                num /= 2;
            }
            if (num < min) {
                min = num;
                flag = i;
            }
        }
        return flag;
    }

    public int Choose(Person person) { //调度策略:?
        instructionNum++;
        //(int) (Math.random() * 6) + 1风险太高
        //instructionNum%6+1
        int num = instructionNum % 6 + 1;
        if (requestTables.get(num).isReseting()) {
            wait.get(num).add(person);
            return 0;
        } else {
            if (requestTables.get(num).isDC()) {
                if (person.getFromFloor() > elevators.get(num).getMaxFloor()) {
                    if (person.getToFloor() < elevators.get(num).getMaxFloor()) {
                        mainRequestTable.addCrossNum();
                    }
                    person.setElevatorId(num + 6);
                    TimableOutput.println("RECEIVE-" + person.getId() +
                            "-" + (num) + "-" + "B");
                } else if (person.getFromFloor() ==
                        elevators.get(num).getMaxFloor()) {
                    if (person.getToFloor() > person.getFromFloor()) {
                        person.setElevatorId(num + 6);
                        TimableOutput.println("RECEIVE-" + person.getId() +
                                "-" + (num) + "-" + "B");
                    } else {
                        person.setElevatorId(num);
                        TimableOutput.println("RECEIVE-" + person.getId() +
                                "-" + (num) + "-" + "A");
                    }
                } else {
                    if (person.getToFloor() > elevators.get(num).getMaxFloor()) {
                        mainRequestTable.addCrossNum();
                    }
                    person.setElevatorId(num);
                    TimableOutput.println("RECEIVE-" + person.getId() +
                            "-" + (num) + "-" + "A");
                }
            } else {
                person.setElevatorId(num);
                TimableOutput.println("RECEIVE-" + person.getId() + "-" + (num));
            }
            //System.out.println(elevators.get(1).isEmpty());
            //System.out.println(person.getFromFloor()+" "+person.getToFloor());}
            return person.getElevatorId();


        }
    }
}
