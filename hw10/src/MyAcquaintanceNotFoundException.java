import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

import java.util.HashMap;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    private int id;
    private static Counter counter;

    public MyAcquaintanceNotFoundException(int id, Counter counter) {
        this.id = id;
        MyAcquaintanceNotFoundException.counter = counter;
        counter.addTotMyAcquaintanceNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyAcquaintanceNotFoundException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyAcquaintanceNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyAcquaintanceNotFoundException();
        System.out.println("anf-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
