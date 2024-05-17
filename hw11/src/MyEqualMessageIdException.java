import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private int id;
    private static Counter counter;

    public MyEqualMessageIdException(int id, Counter counter) {
        this.id = id;
        MyEqualMessageIdException.counter = counter;
        counter.addTotMyEqualMessageIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualMessageIdException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyEqualMessageIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualMessageIdException();
        System.out.println("emi-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
