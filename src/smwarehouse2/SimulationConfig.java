package smwarehouse2;

public class SimulationConfig {
    private ScenarioType scenarioType;
    private int totalCycles;
    private int totalRobots;

    public SimulationConfig(ScenarioType scenarioType, int totalCycles, int totalRobots) {
        this.scenarioType = scenarioType;
        this.totalCycles = totalCycles;
        this.totalRobots = totalRobots;
    }

    public ScenarioType getScenarioType() {
        return scenarioType;
    }

    public int getTotalCycles() {
        return totalCycles;
    }

    public int getTotalRobots() {
        return totalRobots;
    }

    @Override
    public String toString() {
        return "SimulationConfig{" +
                "scenarioType=" + scenarioType +
                ", totalCycles=" + totalCycles +
                ", totalRobots=" + totalRobots +
                '}';
    }
}