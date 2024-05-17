import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;

public class MyEqualPersonIdException extends EqualPersonIdException {

    private static Counter counter;
    private int id;

    public MyEqualPersonIdException(int id, Counter counter) {
        this.id = id;

        MyEqualPersonIdException.counter = counter;


        HashMap<Integer, Integer> counts = counter.getCountMyEqualPersonIdException();
        counter.addTotMyEqualPersonIdException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyEqualPersonIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualPersonIdException();
        System.out.println("epi-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
