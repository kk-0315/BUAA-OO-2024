import com.oocourse.spec2.exceptions.PathNotFoundException;

import java.util.HashMap;

public class MyPathNotFoundException extends PathNotFoundException {
    private int id1;
    private int id2;
    private static Counter counter;

    public MyPathNotFoundException(int id1, int id2, Counter counter) {
        this.id1 = id1;
        this.id2 = id2;
        MyPathNotFoundException.counter = counter;
        HashMap<Integer, Integer> counts = counter.getCountMyPathNotFoundException();
        counter.addTotMyPathNotFoundException();
        if (id1 != id2) {
            if (counts.containsKey(id1)) {
                int num = counts.get(id1);
                counts.replace(id1, num + 1);
            } else {
                counts.put(id1, 1);
            }
            if (counts.containsKey(id2)) {
                int num = counts.get(id2);
                counts.replace(id2, num + 1);
            } else {
                counts.put(id2, 1);
            }
        } else {
            if (counts.containsKey(id1)) {
                int num = counts.get(id1);
                counts.replace(id1, num + 1);
            } else {
                counts.put(id1, 1);
            }
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyPathNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyPathNotFoundException();
        if (id1 != id2) {
            int min = Math.min(id1, id2);
            int max = Math.max(id1, id2);
            System.out.println("pnf-" + totalCount + ", " + min + "-" +
                    counts.get(min) + ", " + max + "-" + counts.get(max));
        } else {
            System.out.println("pnf-" + totalCount + ", " + id1 + "-" +
                    counts.get(id1) + ", " + id1 + "-" + counts.get(id1));
        }
    }
}
