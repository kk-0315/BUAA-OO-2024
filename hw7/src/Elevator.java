import com.oocourse.elevator3.TimableOutput;

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
    private int maxFloor;
    private int minFloor;
    private int secId;
    private Boolean atFlag;

    public Elevator(int i, RequestTable requestTable,
                    RequestTable mainRequestTable, Boolean atFlag) {
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
        this.maxFloor = 11;
        this.minFloor = 1;
        this.secId = 0;
        this.atFlag = atFlag;


    }

    public Elevator(int i, RequestTable requestTable, RequestTable mainRequestTable,
                    int nowFloor, int maxFloor, int minFloor, int capacity,
                    double speed, Boolean atFlag) {
        this.id = i;
        this.nowFloor = nowFloor;
        this.nowNum = 0;
        this.direction = 0;
        this.requestTable = requestTable;
        this.dstMap = new HashMap<>();
        for (int j = 1; j <= 11; j++) {
            this.dstMap.put(j, new ArrayList<>());
        }
        this.strategy = new Strategy(requestTable);

        this.speed = speed;
        this.capacity = capacity;
        this.mainRequestTable = mainRequestTable;
        this.maxFloor = maxFloor;
        this.minFloor = minFloor;
        this.secId = 0;
        this.atFlag = atFlag;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public int getMaxFloor() {
        return this.maxFloor;
    }

    public synchronized void setAtFlag(Boolean k) {
        this.atFlag = k;
        notifyAll();
    }

    public synchronized Boolean getAtFlag() {
        notifyAll();
        return this.atFlag;
    }

    public int getMinFloor() {
        return this.minFloor;
    }

    public void reset() throws InterruptedException {

        if (!this.isEmpty()) {
            TimableOutput.println("OPEN-" + nowFloor + "-" + id);
            Thread.sleep(200);
            for (Integer key : dstMap.keySet()) {
                ArrayList<Person> floorRequest = dstMap.get(key);
                for (int i = floorRequest.size() - 1; i >= 0; i--) { //逆序删
                    Person person0 = floorRequest.get(i);
                    if (person0.getToFloor() == nowFloor) {
                        subNowNum(1);
                        floorRequest.remove(i);
                        TimableOutput.println("OUT-" + person0.getId() +
                                "-" + nowFloor + "-" + id);
                        continue;
                    }
                    Person person = new Person(person0.getId(), nowFloor, person0.getToFloor());
                    person.setElevatorId(0);
                    requestTable.addRequest(person);//起始楼层跟出发楼层不同会有问题吗？
                    floorRequest.remove(i);
                    subNowNum(1);
                    TimableOutput.println("OUT-" + person0.getId() +
                            "-" + nowFloor + "-" + id);
                }
            }
            Thread.sleep(200);
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
        }

        TimableOutput.println("RESET_BEGIN-" + id);
        Reset reset = requestTable.getReset(id);

        this.capacity = reset.getCapacity();
        this.speed = reset.getSpeed();
        this.mainRequestTable.addRequestTable(this.requestTable);
        if (reset instanceof DcReset) {
            this.maxFloor = ((DcReset) reset).getTransferFloor();
            requestTable.setDcFlag(true);
            this.nowFloor = ((DcReset) reset).getTransferFloor() - 1;
            this.setSecId(1);
        }
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
                Advice advice = strategy.getAdvice(id, getNowNum(), nowFloor, direction,
                        dstMap, capacity);

                if (advice == Advice.OVER) {
                    //System.out.println("ele"+id+"end");
                    return;
                } else if (advice == Advice.MOVE) {
                    if (secId == 1 && nowFloor == maxFloor - 1 && direction == 0
                            || secId == 2 && nowFloor == minFloor + 1 && direction == 1) {

                        synchronized (atFlag) {
                            //System.out.println(secId + "get");
                            //System.out.println(atFlag);
                            move();
                            this.atFlag = true;
                            outAndIn();

                        }
                    } else {
                        move();
                    }
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

                } else if (advice == Advice.RESET) {
                    reset();
                }

            }
        } catch (InterruptedException e) {
            throw new RuntimeException();//...
        }
    }

    private void move() throws InterruptedException {
        if (direction == 0) {
            this.nowFloor++;
        } else {
            this.nowFloor--;
        }
        Thread.sleep((long) (speed * 1000));
        if (secId == 0) {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
        } else if (secId == 1) {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + id + "-" + "A");
        } else {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" +
                    (id - 6) + "-" + "B");
        }
    }

    public void outAndIn() throws InterruptedException {
        if (this.direction == 1) {
            direction = 0;
        } else {
            direction = 1;
        }
        getOut();
        for (Integer key : dstMap.keySet()) {
            ArrayList<Person> dst = dstMap.get(key);
            for (int i = 0; i < dst.size(); i++) {
                Person person = dst.get(i);
                if (secId == 0) {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + id);
                } else if (secId == 1) {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + id + "-" + "A");
                } else {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + (id - 6) + "-" + "B");
                }
                Person newPerson = new Person(person.getId(), nowFloor, person.getToFloor());
                if (secId == 1) {
                    newPerson.setElevatorId(id + 6);
                } else if (secId == 2) {
                    newPerson.setElevatorId(id - 6);
                }
                mainRequestTable.addRequest(newPerson);

                //System.out.println("add");
            }
            subNowNum(dst.size());

        }
        for (int j = 1; j <= 11; j++) {
            this.dstMap.put(j, new ArrayList<>());
        }
        openForIn();

        if (direction == 0) {
            nowFloor++;

        } else {
            nowFloor--;
        }
        Thread.sleep((long) (speed * 1000));
        if (secId == 0) {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + id);
        } else if (secId == 1) {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + id + "-" + "A");
        } else {
            TimableOutput.println("ARRIVE-" + nowFloor + "-" + (id - 6) + "-" + "B");
        }
        this.atFlag = false;
        //System.out.println("leave"+atFlag);

    }

    private void openForIn() throws InterruptedException {
        while (strategy.canOpenForIn(nowFloor, getNowNum(), direction, capacity)) {
            HashMap<Integer, ArrayList<Person>> requestMap =
                    requestTable.getRequestMap();
            ArrayList<Person> nowRequest = requestMap.get(nowFloor);
            int flag = 0;
            for (int i = 0; i < nowRequest.size(); i++) {
                Person person = nowRequest.get(i);
                if ((((person.getToFloor() > nowFloor) && direction == 0) ||
                        ((person.getToFloor() < nowFloor) && direction == 1)) &&
                        getNowNum() < capacity) {
                    addNowNum(1);
                    dstMap.get(person.getToFloor()).add(person);
                    flag = i;
                    if (secId == 0) {
                        TimableOutput.println("IN-" + person.getId() +
                                "-" + nowFloor + "-" + id);
                    } else if (secId == 1) {
                        TimableOutput.println("IN-" + person.getId() +
                                "-" + nowFloor + "-" + id + "-" + "A");
                    } else {
                        TimableOutput.println("IN-" + person.getId() +
                                "-" + nowFloor + "-" + (id - 6) + "-" + "B");
                    }
                    break;
                }
            }

            requestTable.delRequest(nowFloor, flag);
        }

        Thread.sleep(200);
        if (secId == 0) {
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id);
        } else if (secId == 1) {
            TimableOutput.println("CLOSE-" + nowFloor + "-" + id + "-" + "A");
        } else {
            TimableOutput.println("CLOSE-" + nowFloor + "-" + (id - 6) + "-" + "B");
        }
    }

    private void getOut() throws InterruptedException {
        if (secId == 0) {
            TimableOutput.println("OPEN-" + nowFloor + "-" + id);
        } else if (secId == 1) {
            TimableOutput.println("OPEN-" + nowFloor + "-" + id + "-" + "A");
        } else {
            TimableOutput.println("OPEN-" + nowFloor + "-" + (id - 6) + "-" + "B");
        }
        Thread.sleep(200);
        if (strategy.canOpenForOut(nowFloor, dstMap)) {
            ArrayList<Person> dst = dstMap.get(nowFloor);
            for (int i = 0; i < dst.size(); i++) {
                Person person = dst.get(i);
                if (secId == 0) {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + id);
                } else if (secId == 1) {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + id + "-" + "A");
                } else {
                    TimableOutput.println("OUT-" + person.getId() +
                            "-" + nowFloor + "-" + (id - 6) + "-" + "B");
                }

            }
            subNowNum(dst.size());
            dst.clear();
        }
    }

    public synchronized Boolean isEmpty() {
        notifyAll();
        return this.nowNum == 0;
    }

    public synchronized void addNowNum(int i) {
        this.nowNum += i;
        notifyAll();
    }

    public synchronized void subNowNum(int i) {
        this.nowNum -= i;
        notifyAll();
    }

    public synchronized int getNowNum() {
        notifyAll();
        return this.nowNum;

    }

    public void open() throws InterruptedException {
        getOut();
        openForIn();
    }
}

