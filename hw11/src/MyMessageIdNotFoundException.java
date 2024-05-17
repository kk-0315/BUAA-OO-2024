import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static Counter counter;

    public MyMessageIdNotFoundException(int id, Counter counter) {
        this.id = id;
        MyMessageIdNotFoundException.counter = counter;
        counter.addTotMyMessageIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyMessageIdNotFoundException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyMessageIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyMessageIdNotFoundException();
        System.out.println("minf-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
