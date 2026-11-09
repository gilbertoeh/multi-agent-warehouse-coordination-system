package smwarehouse2;

public class SimulationResult {
    private int totalScore;
    private int totalIncidents;
    private int resolvedIncidents;
    private int coveredZones;

    public SimulationResult(int totalScore, int totalIncidents, int resolvedIncidents, int coveredZones) {
        this.totalScore = totalScore;
        this.totalIncidents = totalIncidents;
        this.resolvedIncidents = resolvedIncidents;
        this.coveredZones = coveredZones;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalIncidents() {
        return totalIncidents;
    }

    public int getResolvedIncidents() {
        return resolvedIncidents;
    }

    public int getCoveredZones() {
        return coveredZones;
    }

    @Override
    public String toString() {
        return "SimulationResult{" +
                "totalScore=" + totalScore +
                ", totalIncidents=" + totalIncidents +
                ", resolvedIncidents=" + resolvedIncidents +
                ", coveredZones=" + coveredZones +
                '}';
    }
}