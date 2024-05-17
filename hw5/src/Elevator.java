import java.util.ArrayList;
import java.util.HashMap;

import com.oocourse.elevator1.TimableOutput;

public class Elevator extends Thread {
    private int id;
    private int nowFloor;
    private int nowNum;
    private int direction;//0-up,1-down
    private HashMap<Integer, ArrayList<Person>> dstMap;//电梯内人的
    private RequestTable requestTable;
    private Strategy strategy;

    public Elevator(int i, RequestTable requestTable) {
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
    }

    @Override
    public void run() {
        try {
            while (true) {
                Advice advice = strategy.getAdvice(nowNum, nowFloor, direction, dstMap);
                if (advice == Advice.OVER) {
                    return;
                } else if (advice == Advice.MOVE) {
                    if (direction == 0) {
                        this.nowFloor++;
                    } else {
                        this.nowFloor--;
                    }
                    Thread.sleep(400);
                    TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
                } else if (advice == Advice.REVERSE) {
                    if (this.direction == 1) {
                        direction = 0;
                    } else {
                        direction = 1;
                    }
                } else if (advice == Advice.OPEN) {
                    open();
                } else {
                    requestTable.waitRequest();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();//...
        }
    }

    public void open() throws InterruptedException {
        TimableOutput.println("OPEN-" + nowFloor + "-" + id);
        Thread.sleep(200);
        while (strategy.canOpenForIn(nowFloor, nowNum, direction)) {
            HashMap<Integer, ArrayList<Person>> requestMap =
                    requestTable.getRequestMap();
            ArrayList<Person> nowRequest = requestMap.get(nowFloor);
            int flag = 0;
            for (int i = 0; i < nowRequest.size(); i++) {
                Person person = nowRequest.get(i);
                if ((((person.getToFloor() > nowFloor) && direction == 0) ||
                        ((person.getToFloor() < nowFloor) && direction == 1)) &&
                        nowNum < 6) {
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

