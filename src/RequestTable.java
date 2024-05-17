import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {

    private boolean endFlag;
    private int requestNum;
    private HashMap<Integer, ArrayList<Person>> requestMap;//出发地在key层的所有人的集合

    private HashMap<Integer, Reset> resetMap;
    private int resetNum;
    private Boolean resetFlag;
    private Boolean dcFlag;
    private int crossNum;

    public RequestTable() {
        this.endFlag = false;
        this.requestNum = 0;
        this.requestMap = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            requestMap.put(i, new ArrayList<>());
        }
        this.resetMap = new HashMap<>();
        this.resetNum = 0;
        this.resetFlag = false;
        this.dcFlag = false;
        this.crossNum = 0;

    }

    public synchronized void addCrossNum() {
        this.crossNum++;
        notifyAll();
    }

    public synchronized void subCrossNum() {
        this.crossNum--;
        notifyAll();
    }

    public synchronized Boolean isCrossNumEmpty() {
        notifyAll();
        return this.crossNum == 0;
    }

    public synchronized Boolean isDC() {
        notifyAll();
        return this.dcFlag;
    }

    public synchronized int getRequestNum() {
        notifyAll();
        return this.requestNum;
    }

    public synchronized void setDcFlag(Boolean k) {
        this.dcFlag = k;
        notifyAll();
    }

    public synchronized void addReset(Reset reset) {
        this.resetFlag = true;
        this.resetMap.put(reset.getElevatorId(), reset);
        //System.out.println("addreset"+resetNum);
        notifyAll();
    }

    public synchronized void subResetNum() {
        this.resetNum--;
        notifyAll();
    }

    public synchronized Reset getReset(int id) {
        notifyAll();
        return resetMap.get(id);
    }

    public synchronized Boolean isReseting() {
        notifyAll();
        return resetFlag;
    }

    public synchronized void addRequestTable(RequestTable requestTable) {
        HashMap<Integer, ArrayList<Person>> requestTableMap = requestTable.getRequestMap();
        for (Integer key : requestTableMap.keySet()) {
            ArrayList<Person> personArrayList = requestTableMap.get(key);
            for (int i = 0; i < personArrayList.size(); i++) {
                Person person = personArrayList.get(i);
                person.setElevatorId(0);
                int fromFloor = person.getFromFloor();
                this.requestMap.get(fromFloor).add(person);
                requestNum++;
            }
        }

        notifyAll();
    }

    public synchronized void setResetFlag(Boolean k) {
        this.resetFlag = k;
        notifyAll();
    }

    public synchronized void addResetNum() {
        this.resetNum++;
        notifyAll();
    }

    public synchronized void clear(int id) {
        this.requestMap = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            requestMap.put(i, new ArrayList<>());
        }

        requestNum = 0;
        this.resetMap.remove(id);
        notifyAll();
    }

    public synchronized void addRequest(Person person) {
        int fromFloor = person.getFromFloor();
        this.requestMap.get(fromFloor).add(person);
        requestNum++;
        notifyAll();
    }

    public synchronized void delRequest(int key, int personNum) {
        this.requestMap.get(key).remove(personNum);
        requestNum--;
        notifyAll();
    }

    public synchronized Boolean isResetEmpty() {
        notifyAll();
        return this.resetNum == 0;
    }

    public synchronized void setEndFlag() {
        notifyAll();
        this.endFlag = true;
    }

    public synchronized Boolean isEnd() {
        notifyAll();
        return this.endFlag;
    }

    public synchronized Boolean isEmpty() {
        notifyAll();
        return requestNum == 0;
    }

    public synchronized Person getOneRequest() {

        if (this.isEmpty() && !this.isEnd()) {
            try {
                //System.out.println("2");
                this.waitRequest();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Person person = null;
        int flagkey = 0;
        int flagnum = 0;
        for (Integer key : requestMap.keySet()) {
            if (!requestMap.get(key).isEmpty()) {
                int len = requestMap.get(key).size();
                person = requestMap.get(key).get(len - 1);
                flagkey = key;
                flagnum = len - 1;
                break;
            }
        }
        if (person != null) {
            delRequest(flagkey, flagnum);
        }
        notifyAll();
        return person;
    }

    public synchronized HashMap<Integer, ArrayList<Person>> getRequestMap() {
        notifyAll();
        return this.requestMap;
    }

    public synchronized void waitRequest() throws InterruptedException {
        wait();
    }

}
