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
    private static HashMap<Integer, Integer> countMyEqualPersonIdException;
    private static HashMap<Integer, Integer> countMyEqualRelationException;
    private static HashMap<Integer, Integer> countMyPersonIdNotFoundException;
    private static HashMap<Integer, Integer> countMyRelationNotFoundException;
    private static HashMap<Integer, Integer> countMyAcquaintanceNotFoundException;
    private static HashMap<Integer, Integer> countMyPathNotFoundException;
    private static HashMap<Integer, Integer> countMyEqualTagIdException;
    private static HashMap<Integer, Integer> countMyTagIdNotFoundException;

    public Counter() {
        totMyEqualPersonIdException = 0;
        totMyEqualRelationException = 0;
        totMyPersonIdNotFoundException = 0;
        totMyRelationNotFoundException = 0;
        totMyAcquaintanceNotFoundException = 0;
        totMyPathNotFoundException = 0;
        totMyEqualTagIdException = 0;
        totMyTagIdNotFoundException = 0;
        countMyEqualPersonIdException = new HashMap<>();
        countMyEqualRelationException = new HashMap<>();
        countMyPersonIdNotFoundException = new HashMap<>();
        countMyRelationNotFoundException = new HashMap<>();
        countMyAcquaintanceNotFoundException = new HashMap<>();
        countMyPathNotFoundException = new HashMap<>();
        countMyEqualTagIdException = new HashMap<>();
        countMyTagIdNotFoundException = new HashMap<>();
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

    public HashMap<Integer, Integer> getCountMyPathNotFoundException() {
        return countMyPathNotFoundException;
    }

    public HashMap<Integer, Integer> getCountMyEqualTagIdException() {
        return countMyEqualTagIdException;
    }

    public HashMap<Integer, Integer> getCountMyTagIdNotFoundException() {
        return countMyTagIdNotFoundException;
    }
}
