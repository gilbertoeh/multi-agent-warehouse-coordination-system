package smwarehouse2;

public class Incident {
    private String id;
    private String zoneName;
    private String reportedBy;
    private boolean resolved;

    public Incident(String id, String zoneName, String reportedBy, boolean resolved) {
        this.id = id;
        this.zoneName = zoneName;
        this.reportedBy = reportedBy;
        this.resolved = resolved;
    }

    public String getId() {
        return id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public String toString() {
        return "Incident{" +
                "id='" + id + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", reportedBy='" + reportedBy + '\'' +
                ", resolved=" + resolved +
                '}';
    }
}