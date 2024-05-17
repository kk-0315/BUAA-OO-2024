public class Node implements Comparable<Node> {
    private int acqId;
    private int value;

    public Node(int acqId, int value) {
        this.acqId = acqId;
        this.value = value;
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.value,o.value);
    }

    public int getAcqId() {
        return this.acqId;
    }

    public int getValue() {
        return this.value;
    }
}
