package eu.arrowhead.common.dto.internal;

public class ChoreographerStartSessionDTO {

    private long sessionId;
    private long planId;

    public ChoreographerStartSessionDTO() {
    }

    public ChoreographerStartSessionDTO(long sessionId, long planId) {
        this.sessionId = sessionId;
        this.planId = planId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }
}
