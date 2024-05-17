public class Person {
    private int id;
    private int fromFloor;

    private int toFloor;

    private int elevatorId;

    public Person(int id, int fromFloor, int toFloor) {
        this.id = id;

        this.fromFloor = fromFloor;

        this.toFloor = toFloor;
        this.elevatorId = 0;//未分配
    }

    public int getId() {
        return this.id;
    }

    public int getFromFloor() {
        return this.fromFloor;
    }

    public int getToFloor() {
        return this.toFloor;
    }

    public int getElevatorId() {
        return this.elevatorId;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }
}
