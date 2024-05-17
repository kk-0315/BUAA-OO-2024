import java.util.ArrayList;
import java.util.HashMap;

public class Strategy {
    private RequestTable requestTable;

    public Strategy(RequestTable requestTable) {
        this.requestTable = requestTable;
    }

    public Boolean canReset(int id) {

        Reset reset = requestTable.getReset(id);
        //System.out.println(id);
        //System.out.println(reset==null);
        if (reset == null) {
            return false;
        }
        return true;
    }

    public Advice getAdvice(int id, int nowNum, int nowFloor, int direction,
                            HashMap<Integer, ArrayList<Person>> dstMap, int capacity) {
        //System.out.println("1");
        if (canReset(id)) {
            //System.out.println("get advice reset");
            return Advice.RESET;
        } else {

            if (canOpenForOut(nowFloor, dstMap) || canOpenForIn(nowFloor, nowNum,
                    direction, capacity)) {
                return Advice.OPEN;
            }
            if (nowNum != 0) {
                //System.out.println("MOVE");
                return Advice.MOVE;
            } else {
                if (!requestTable.isEmpty()) {
                    if (requestForOriginDir(nowFloor, direction)) {
                        //System.out.println("OriMOve");
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
    }

    public Boolean requestForOriginDir(int nowFloor, int direction) {
        HashMap<Integer, ArrayList<Person>> requestMap = requestTable.getRequestMap();
        for (Integer key : requestMap.keySet()) {
            if (((key > nowFloor && direction == 0) || (key < nowFloor && direction == 1))
                    && !requestMap.get(key).isEmpty()) {
                //System.out.println(key+" "+nowFloor);
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

    public Boolean canOpenForIn(int nowFloor, int nowNum, int direction, int capacity) {
        HashMap<Integer, ArrayList<Person>> requestMap = requestTable.getRequestMap();
        ArrayList<Person> floorRequest = requestMap.get(nowFloor);
        if (!floorRequest.isEmpty()) {

            for (int i = 0; i < floorRequest.size(); i++) {
                if ((((floorRequest.get(i).getToFloor() > nowFloor) && direction == 0) ||
                        ((floorRequest.get(i).getToFloor() < nowFloor) && direction == 1)) &&
                        nowNum < capacity) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

}
