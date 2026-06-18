package com.freecrm.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String id;
    private String email;
    private String name;
    @JsonProperty("notification_opt_in")
    private boolean notificationOptIn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotificationOptIn() {
        return notificationOptIn;
    }

    public void setNotificationOptIn(boolean notificationOptIn) {
        this.notificationOptIn = notificationOptIn;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name + ", notificationOptIn=" + notificationOptIn
				+ "]";
	}

  
}