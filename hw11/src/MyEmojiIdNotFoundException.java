import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private int id;
    private static Counter counter;

    public MyEmojiIdNotFoundException(int id, Counter counter) {
        this.id = id;
        MyEmojiIdNotFoundException.counter = counter;
        counter.addTotMyEmojiIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyEmojiIdNotFoundException();
        if (counts.containsKey(id)) {
            int num = counts.get(id);
            counts.replace(id, num + 1);
        } else {
            counts.put(id, 1);
        }
    }

    @Override
    public void print() {
        int totalCount = counter.getTotMyEmojiIdNotFoundException();
        HashMap<Integer, Integer> counts = counter.getCountMyEmojiIdNotFoundException();
        System.out.println("einf-" + totalCount + ", " + id + "-" + counts.get(id));
    }
}
