import java.util.HashMap;

public class Counter {
    private static int totMyEqualPersonIdException;
    private static int totMyEqualRelationException;
    private static int totMyPersonIdNotFoundException;
    private static int totMyRelationNotFoundException;
    private static HashMap<Integer, Integer> countMyEqualPersonIdException;
    private static HashMap<Integer, Integer> countMyEqualRelationException;
    private static HashMap<Integer, Integer> countMyPersonIdNotFoundException;
    private static HashMap<Integer, Integer> countMyRelationNotFoundException;

    public Counter() {
        totMyEqualPersonIdException = 0;
        totMyEqualRelationException = 0;
        totMyPersonIdNotFoundException = 0;
        totMyRelationNotFoundException = 0;
        countMyEqualPersonIdException = new HashMap<>();
        countMyEqualRelationException = new HashMap<>();
        countMyPersonIdNotFoundException = new HashMap<>();
        countMyRelationNotFoundException = new HashMap<>();
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

}
