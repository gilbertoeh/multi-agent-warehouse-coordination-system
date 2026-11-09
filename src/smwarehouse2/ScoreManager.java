package smwarehouse2;

public class ScoreManager {

    public int calculateScore(int coveredZones, int detectedIncidents, int resolvedIncidents,
                              int unattendedZones, int unattendedIncidents, int lateResponses) {
        int score = 0;

        score += coveredZones * 10;
        score += detectedIncidents * 20;
        score += resolvedIncidents * 15;

        score -= unattendedZones * 10;
        score -= unattendedIncidents * 15;
        score -= lateResponses * 5;

        return score;
    }
}
