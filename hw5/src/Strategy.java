import java.util.ArrayList;
import java.util.HashMap;

public class Strategy {
    private RequestTable requestTable;

    public Strategy(RequestTable requestTable) {
        this.requestTable = requestTable;
    }

    public Advice getAdvice(int nowNum, int nowFloor, int direction,
                            HashMap<Integer, ArrayList<Person>> dstMap) {
        if (canOpenForOut(nowFloor, dstMap) || canOpenForIn(nowFloor, nowNum, direction)) {
            return Advice.OPEN;
        }
        if (nowNum != 0) {
            return Advice.MOVE;
        } else {
            if (!requestTable.isEmpty()) {
                if (requestForOriginDir(nowFloor, direction)) {
                    return Advice.MOVE;
                } else {
                    return Advice.REVERSE;
                }
            } else {
                if (!requestTable.isEnd()) {
                    return Advice.WAIT;
                } else {
                    return Advice.OVER;
                }
            }
        }
    }

    public Boolean requestForOriginDir(int nowFloor, int direction) {
        HashMap<Integer, ArrayList<Person>> requestMap = requestTable.getRequestMap();
        for (Integer key : requestMap.keySet()) {
            if (((key > nowFloor && direction == 0) || (key < nowFloor && direction == 1))
                    && !requestMap.get(key).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public Boolean canOpenForOut(int nowFloor, HashMap<Integer, ArrayList<Person>> dstMap) {
        ArrayList<Person> dst = dstMap.get(nowFloor);
        if (!dst.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean canOpenForIn(int nowFloor, int nowNum, int direction) {
        HashMap<Integer, ArrayList<Person>> requestMap = requestTable.getRequestMap();
        ArrayList<Person> floorRequest = requestMap.get(nowFloor);
        if (!floorRequest.isEmpty()) {

            for (int i = 0; i < floorRequest.size(); i++) {
                if ((((floorRequest.get(i).getToFloor() > nowFloor) && direction == 0) ||
                        ((floorRequest.get(i).getToFloor() < nowFloor) && direction == 1)) &&
                                nowNum < 6) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
