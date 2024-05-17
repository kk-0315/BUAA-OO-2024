public class DcReset implements Reset {
    private int id;
    private int capacity;
    private double speed;
    private int transferFloor;

    public DcReset(int id, int capacity, double speed, int transferFloor) {
        this.id = id;
        this.capacity = capacity;
        this.speed = speed;
        this.transferFloor = transferFloor;
    }

    @Override
    public int getElevatorId() {
        return this.id;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public double getSpeed() {
        return this.speed;
    }

    public int getTransferFloor() {
        return this.transferFloor;
    }
}
