import com.oocourse.spec3.exceptions.TagIdNotFoundException;

import java.util.HashMap;

public class MyTagIdNotFoundException extends TagIdNotFoundException {
    private int id;
    private static Counter counter;

    public MyTagIdNotFoundException(int id, Counter counter) {
        this.id = id;
        MyTagIdNotFoundException.counter = counter;
        counter.addTotMyTagIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyTagIdNotFoundException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }

    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyTagIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyTagIdNotFoundException();
        System.out.println("tinf-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
