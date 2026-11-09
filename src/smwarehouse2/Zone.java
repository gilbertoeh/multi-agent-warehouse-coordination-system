package smwarehouse2;

public class Zone {
    private String name;
    private String assignedRobot;
    private boolean covered;

    public Zone(String name, String assignedRobot, boolean covered) {
        this.name = name;
        this.assignedRobot = assignedRobot;
        this.covered = covered;
    }

    public String getName() {
        return name;
    }

    public String getAssignedRobot() {
        return assignedRobot;
    }

    public void setAssignedRobot(String assignedRobot) {
        this.assignedRobot = assignedRobot;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    @Override
    public String toString() {
        return "Zone{" +
                "name='" + name + '\'' +
                ", assignedRobot='" + assignedRobot + '\'' +
                ", covered=" + covered +
                '}';
    }
}