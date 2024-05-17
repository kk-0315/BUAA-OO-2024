import com.oocourse.elevator2.TimableOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class Elevator extends Thread {
    private int id;
    private int nowFloor;
    private int nowNum;
    private int direction;//0-up,1-down
    private HashMap<Integer, ArrayList<Person>> dstMap;//电梯内目的地在key层所有人的集合
    private RequestTable requestTable;//本电梯的请求
    private Strategy strategy;

    private double speed;
    private int capacity;
    private RequestTable mainRequestTable;

    public Elevator(int i, RequestTable requestTable, RequestTable mainRequestTable) {
        this.id = i;
        this.nowFloor = 1;
        this.nowNum = 0;
        this.direction = 0;
        this.requestTable = requestTable;
        this.dstMap = new HashMap<>();
        for (int j = 1; j <= 11; j++) {
            this.dstMap.put(j, new ArrayList<>());
        }
        this.strategy = new Strategy(requestTable);

        this.speed = 0.4;
        this.capacity = 6;
        this.mainRequestTable = mainRequestTable;

    }

    public void reset() throws InterruptedException {

        if (this.nowNum != 0) {
            TimableOutput.println("OPEN-" + nowFloor + "-" + id);
            Thread.sleep(200);
            for (Integer key : dstMap.keySet()) {
                ArrayList<Person> floorRequest = dstMap.get(key);
                for (int i = floorRequest.size() - 1; i >= 0; i--) { //逆序删
                    Person person0 = floorRequest.get(i);
                    if (person0.getToFloor() == nowFloor) {
                        nowNum--;
                        floorRequest.remove(i);
                        TimableOutput.println("OUT-" + person0.getId() +
                                "-" + nowFloor + "-" + id);
                        continue;
                    }
                    Person person = new Person(person0.getId(), nowFloor, person0.getToFloor());
                    requestTable.addRequest(person);//起始楼层跟出发楼层不同会有问题吗？
                    floorRequest.remove(i);
                    nowNum--;
                    TimableOutput.println("OUT-" + person0.getId() +
                            "-" + nowFloor + "-" + id);
                }
            }
            Thread.sleep(200);
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
        }
        this.requestTable.setResetFlag(true);
        Thread.sleep(1);
        TimableOutput.println("RESET_BEGIN-" + id);
        Reset reset = requestTable.getReset(id);
        this.capacity = reset.getCapacity();
        this.speed = reset.getSpeed();
        this.mainRequestTable.addRequestTable(this.requestTable);
        this.requestTable.clear(id);
        this.mainRequestTable.subResetNum();
        //System.out.println("addtomain");
        //this.mainRequestTable.clearReset(id);
        //System.out.println("clearreset");
        //System.out.println(requestTable.getReset(id)==null);
        Thread.sleep(1200);
        TimableOutput.println("RESET_END-" + id);
        this.requestTable.setResetFlag(false);


    }

    @Override
    public void run() {
        try {
            while (true) {
                //System.out.println("2");
                Advice advice = strategy.getAdvice(id, nowNum, nowFloor, direction,
                        dstMap, capacity);

                if (advice == Advice.OVER) {
                    //System.out.println("ele"+id+"end");
                    return;
                } else if (advice == Advice.MOVE) {
                    if (direction == 0) {
                        this.nowFloor++;
                    } else {
                        this.nowFloor--;
                    }
                    Thread.sleep((long) (speed * 1000));
                    TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
                } else if (advice == Advice.REVERSE) {
                    if (this.direction == 1) {
                        direction = 0;
                    } else {
                        direction = 1;
                    }
                } else if (advice == Advice.OPEN) {
                    open();
                } else if (advice == Advice.WAIT) {
                    //System.out.println("elevator"+id+"WAIT");
                    requestTable.waitRequest();

                } else {
                    reset();
                }

            }
        } catch (InterruptedException e) {
            throw new RuntimeException();//...
        }
    }

    public void open() throws InterruptedException {
        TimableOutput.println("OPEN-" + nowFloor + "-" + id);
        Thread.sleep(200);
        while (strategy.canOpenForIn(nowFloor, nowNum, direction, capacity)) {
            HashMap<Integer, ArrayList<Person>> requestMap =
                    requestTable.getRequestMap();
            ArrayList<Person> nowRequest = requestMap.get(nowFloor);
            int flag = 0;
            for (int i = 0; i < nowRequest.size(); i++) {
                Person person = nowRequest.get(i);
                if ((((person.getToFloor() > nowFloor) && direction == 0) ||
                        ((person.getToFloor() < nowFloor) && direction == 1)) &&
                        nowNum < capacity) {
                    nowNum++;
                    dstMap.get(person.getToFloor()).add(person);
                    flag = i;
                    TimableOutput.println("IN-" + person.getId() +
                            "-" + nowFloor + "-" + id);
                    break;
                }
            }

            requestTable.delRequest(nowFloor, flag);
        }
        if (strategy.canOpenForOut(nowFloor, dstMap)) {
            ArrayList<Person> dst = dstMap.get(nowFloor);
            for (int i = 0; i < dst.size(); i++) {
                Person person = dst.get(i);
                TimableOutput.println("OUT-" + person.getId() +
                        "-" + nowFloor + "-" + id);
            }
            nowNum -= dst.size();
            dst.clear();
        }
        Thread.sleep(200);
        TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
    }
}

