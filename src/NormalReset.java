
public class NormalReset implements Reset {
    private int elevatorId;
    private int capacity;
    private double speed;

    public NormalReset(int elevatorId, int capacity, double speed) {
        this.capacity = capacity;
        this.elevatorId = elevatorId;
        this.speed = speed;
    }

    public int getElevatorId() {
        return this.elevatorId;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public double getSpeed() {
        return this.speed;
    }

}
