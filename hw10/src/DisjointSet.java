import java.util.HashMap;

public class DisjointSet {
    private HashMap<Integer, Integer> rep;
    private HashMap<Integer, Integer> rank;

    public DisjointSet() {
        this.rank = new HashMap<>();
        this.rep = new HashMap<>();
    }

    public void add(int id) {
        if (!rep.containsKey(id)) {
            rep.put(id, id);
            rank.put(id, 0);
        }
    }

    public void sub(MyPerson person1, MyPerson person2) {
        int id1 = person1.getId();
        int id2 = person2.getId();
        rep.replace(id1, id1);
        rep.replace(id2, id2);
        HashMap<Integer, Boolean> visited1 = new HashMap<>();
        dfs(person1, visited1, rep, id1);
        if (rep.get(id2) == id2) {
            HashMap<Integer, Boolean> visited2 = new HashMap<>();
            dfs(person2, visited2, rep, id2);
        }
    }

    public void dfs(MyPerson person, HashMap<Integer, Boolean> visited,
                    HashMap<Integer, Integer> rep, int replaceId) {
        rep.replace(person.getId(), replaceId);

        // 标记当前Person为已访问
        visited.put(person.getId(), true);

        // 遍历当前Person的所有朋友
        for (Integer key : person.getAcquaintance().keySet()) {
            if (!visited.containsKey(key) || !visited.get(key)) {
                dfs((MyPerson) person.getAcquaintance().get(key), visited, rep, replaceId);
            }
        }
    }

    //有问题
    public int find(int id) {
        int pre = id;
        while (pre != rep.get(pre)) { //不是根节点，继续往上爬
            pre = rep.get(pre);
        }

        //这时pre是代表元，是根节点
        int now = id;
        while (now != pre) {
            int father = rep.get(now);//保存下一个节点
            rep.replace(now, pre);//把所有节点的父节点设置为代表元
            now = father;//继续移动
        }
        return pre;
    }

    public int merge(int id1, int id2) {
        int pre1 = find(id1);
        int pre2 = find(id2);
        if (pre1 == pre2) {
            return -1;
        }

        int rank1 = rank.get(id1);
        int rank2 = rank.get(id2);
        if (rank1 < rank2) {
            rep.replace(pre1, pre2);
        } else if (rank1 == rank2) {
            rank.replace(pre1, rank1 + 1);
            rep.replace(pre2, pre1);
        } else {
            rep.replace(pre2, pre1);
        }
        return 0;
    }
}
