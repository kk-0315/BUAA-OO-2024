import java.util.HashMap;

public class Counter {
    private static int totMyEqualPersonIdException;
    private static int totMyEqualRelationException;
    private static int totMyPersonIdNotFoundException;
    private static int totMyRelationNotFoundException;
    private static int totMyAcquaintanceNotFoundException;
    private static int totMyPathNotFoundException;
    private static int totMyEqualTagIdException;
    private static int totMyTagIdNotFoundException;
    private static int totMyEmojiIdNotFoundException;
    private static int totMyEqualEmojiIdException;
    private static int totMyEqualMessageIdException;
    private static int totMyMessageIdNotFoundException;
    private static HashMap<Integer, Integer> countMyEqualPersonIdException;
    private static HashMap<Integer, Integer> countMyEqualRelationException;
    private static HashMap<Integer, Integer> countMyPersonIdNotFoundException;
    private static HashMap<Integer, Integer> countMyRelationNotFoundException;
    private static HashMap<Integer, Integer> countMyAcquaintanceNotFoundException;
    private static HashMap<Integer, Integer> countMyPathNotFoundException;
    private static HashMap<Integer, Integer> countMyEqualTagIdException;
    private static HashMap<Integer, Integer> countMyTagIdNotFoundException;
    private static HashMap<Integer, Integer> countMyEmojiIdNotFoundException;
    private static HashMap<Integer, Integer> countMyEqualEmojiIdException;
    private static HashMap<Integer, Integer> countMyEqualMessageIdException;
    private static HashMap<Integer, Integer> countMyMessageIdNotFoundException;

    public Counter() {
        totMyEqualPersonIdException = 0;
        totMyEqualRelationException = 0;
        totMyPersonIdNotFoundException = 0;
        totMyRelationNotFoundException = 0;
        totMyAcquaintanceNotFoundException = 0;
        totMyPathNotFoundException = 0;
        totMyEqualTagIdException = 0;
        totMyTagIdNotFoundException = 0;
        totMyEmojiIdNotFoundException = 0;
        totMyEqualEmojiIdException = 0;
        totMyEqualMessageIdException = 0;
        totMyMessageIdNotFoundException = 0;
        countMyEqualPersonIdException = new HashMap<>();
        countMyEqualRelationException = new HashMap<>();
        countMyPersonIdNotFoundException = new HashMap<>();
        countMyRelationNotFoundException = new HashMap<>();
        countMyAcquaintanceNotFoundException = new HashMap<>();
        countMyPathNotFoundException = new HashMap<>();
        countMyEqualTagIdException = new HashMap<>();
        countMyTagIdNotFoundException = new HashMap<>();
        countMyEmojiIdNotFoundException = new HashMap<>();
        countMyEqualEmojiIdException = new HashMap<>();
        countMyEqualMessageIdException = new HashMap<>();
        countMyMessageIdNotFoundException = new HashMap<>();
    }

    public int getTotMyAcquaintanceNotFoundException() {
        return totMyAcquaintanceNotFoundException;
    }

    public int getTotMyPathNotFoundException() {
        return totMyPathNotFoundException;
    }

    public int getTotMyEqualTagIdException() {
        return totMyEqualTagIdException;
    }

    public int getTotMyTagIdNotFoundException() {
        return totMyTagIdNotFoundException;
    }

    public void addTotMyAcquaintanceNotFoundException() {
        totMyAcquaintanceNotFoundException++;
    }

    public void addTotMyEqualTagIdException() {
        totMyEqualTagIdException++;
    }

    public void addTotMyPathNotFoundException() {
        totMyPathNotFoundException++;
    }

    public void addTotMyTagIdNotFoundException() {
        totMyTagIdNotFoundException++;
    }

    public void addTotMyEmojiIdNotFoundException() {
        totMyEmojiIdNotFoundException++;
    }

    public int getTotMyEmojiIdNotFoundException() {
        return totMyEmojiIdNotFoundException;
    }

    public int getTotMyEqualPersonIdException() {
        return totMyEqualPersonIdException;
    }

    public void addTotMyEqualPersonIdException() {
        totMyEqualPersonIdException++;
    }

    public int getTotMyEqualRelationException() {
        return totMyEqualRelationException;
    }

    public void addTotMyEqualRelationException() {
        totMyEqualRelationException++;
    }

    public int getTotMyPersonIdNotFoundException() {
        return totMyPersonIdNotFoundException;
    }

    public void addTotMyPersonIdNotFoundException() {
        totMyPersonIdNotFoundException++;
    }

    public int getTotMyRelationNotFoundException() {
        return totMyRelationNotFoundException;
    }

    public void addTotMyRelationNotFoundException() {
        totMyRelationNotFoundException++;
    }

    public void addTotMyEqualEmojiIdException() {
        totMyEqualEmojiIdException++;
    }

    public int getTotMyEqualEmojiIdException() {
        return totMyEqualEmojiIdException;
    }

    public void addTotMyEqualMessageIdException() {
        totMyEqualMessageIdException++;
    }

    public int getTotMyEqualMessageIdException() {
        return totMyEqualMessageIdException;
    }

    public void addTotMyMessageIdNotFoundException() {
        totMyMessageIdNotFoundException++;
    }

    public int getTotMyMessageIdNotFoundException() {
        return totMyMessageIdNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyEqualPersonIdException() {
        return countMyEqualPersonIdException;
    }

    public HashMap<Integer, Integer> getCountMyEqualRelationException() {
        return countMyEqualRelationException;
    }

    public HashMap<Integer, Integer> getCountMyPersonIdNotFoundException() {
        return countMyPersonIdNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyRelationNotFoundException() {
        return countMyRelationNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyAcquaintanceNotFoundException() {
        return countMyAcquaintanceNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyEmojiIdNotFoundException() {
        return countMyEmojiIdNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyPathNotFoundException() {
        return countMyPathNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyEqualTagIdException() {
        return countMyEqualTagIdException;
    }

    public HashMap<Integer, Integer> getCountMyTagIdNotFoundException() {
        return countMyTagIdNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyEqualEmojiIdException() {
        return countMyEqualEmojiIdException;
    }

    public HashMap<Integer, Integer> getCountMyEqualMessageIdException() {
        return countMyEqualMessageIdException;
    }

    public HashMap<Integer, Integer> getCountMyMessageIdNotFoundException() {
        return countMyMessageIdNotFoundException;
    }
}