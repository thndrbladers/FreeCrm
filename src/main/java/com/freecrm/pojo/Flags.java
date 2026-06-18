package com.freecrm.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Flags {
    @JsonProperty("new")
    private boolean isNew;
    private boolean updated;
    @JsonProperty("email_received")
    private boolean emailReceived;
    @JsonProperty("task_assigned")
    private boolean taskAssigned;
    @JsonProperty("case_assigned")
    private boolean caseAssigned;
    @JsonProperty("event_assigned")
    private boolean eventAssigned;
    @JsonProperty("call_assigned")
    private boolean callAssigned;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean isEmailReceived() {
        return emailReceived;
    }

    public void setEmailReceived(boolean emailReceived) {
        this.emailReceived = emailReceived;
    }

    public boolean isTaskAssigned() {
        return taskAssigned;
    }

    public void setTaskAssigned(boolean taskAssigned) {
        this.taskAssigned = taskAssigned;
    }

    public boolean isCaseAssigned() {
        return caseAssigned;
    }

    public void setCaseAssigned(boolean caseAssigned) {
        this.caseAssigned = caseAssigned;
    }

    public boolean isEventAssigned() {
        return eventAssigned;
    }

    public void setEventAssigned(boolean eventAssigned) {
        this.eventAssigned = eventAssigned;
    }

    public boolean isCallAssigned() {
        return callAssigned;
    }

    public void setCallAssigned(boolean callAssigned) {
        this.callAssigned = callAssigned;
    }

    @Override
    public String toString() {
        return "Flags{" +
                "isNew=" + isNew + ", " +
                "updated=" + updated + ", " +
                "emailReceived=" + emailReceived + ", " +
                "taskAssigned=" + taskAssigned + ", " +
                "caseAssigned=" + caseAssigned + ", " +
                "eventAssigned=" + eventAssigned + ", " +
                "callAssigned=" + callAssigned +
                "}";
    }
}