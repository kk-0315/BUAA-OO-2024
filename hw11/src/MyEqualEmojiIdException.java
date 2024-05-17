import com.oocourse.spec3.exceptions.EqualEmojiIdException;

import java.util.HashMap;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private int id;
    private static Counter counter;

    public MyEqualEmojiIdException(int id, Counter counter) {
        this.id = id;
        MyEqualEmojiIdException.counter = counter;
        counter.addTotMyEqualEmojiIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualEmojiIdException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyEqualEmojiIdException();
        HashMap<Integer, Integer> counts = counter.getCountMyEqualEmojiIdException();
        System.out.println("eei-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
