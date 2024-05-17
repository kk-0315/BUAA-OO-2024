import java.util.ArrayList;
import java.util.HashMap;

public class RequestTable {

    private boolean endFlag;
    private int requestNum;
    private HashMap<Integer, ArrayList<Person>> requestMap;//出发地在key层的所有人的集合

    public RequestTable() {
        this.endFlag = false;
        this.requestNum = 0;
        this.requestMap = new HashMap<>();
        for (int i = 1; i <= 11; i++) {
            requestMap.put(i, new ArrayList<>());
        }
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
                wait();
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
