import java.util.HashMap;

public class Schedule extends Thread {
    private RequestTable mainRequestTable;
    private HashMap<Integer, RequestTable> requestTables;

    public Schedule(RequestTable mainRequestTable, HashMap<Integer, RequestTable> requestTables) {
        this.mainRequestTable = mainRequestTable;
        this.requestTables = requestTables;
    }

    @Override
    public void run() {
        while (true) {
            if (mainRequestTable.isEmpty() && mainRequestTable.isEnd()) {
                for (int i = 1; i <= 6; i++) {
                    requestTables.get(i).setEndFlag();
                }
                return;
            }
            Person person = mainRequestTable.getOneRequest();//...从mainRequestTable中得到一个request
            if (person == null) {
                continue;
            }
            int num = Choose(person);
            //将person需求加入合适的电梯表
            requestTables.get(num).addRequest(person);
        }
    }

    public int Choose(Person person) { //调度策略，这次是确定了的
        return person.getElevatorId();
    }
}
