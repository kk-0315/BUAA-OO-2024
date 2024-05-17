import com.oocourse.spec2.exceptions.EqualTagIdException;

import java.util.HashMap;

public class MyEqualTagIdException extends EqualTagIdException {
    private int id;
    private static Counter counter;

    public MyEqualTagIdException(int id, Counter counter) {
        this.id = id;
        MyEqualTagIdException.counter = counter;
        counter.addTotMyEqualTagIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualTagIdException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyEqualTagIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualTagIdException();
        System.out.println("eti-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
