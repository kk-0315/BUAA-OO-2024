import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static Counter counter;
    private int id;

    public MyPersonIdNotFoundException(int id, Counter counter) {
        this.id = id;
        MyPersonIdNotFoundException.counter = counter;
        HashMap<Integer, Integer> counts = counter.getCountMyPersonIdNotFoundException();
        counter.addTotMyPersonIdNotFoundException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyPersonIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyPersonIdNotFoundException();
        System.out.println("pinf-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
