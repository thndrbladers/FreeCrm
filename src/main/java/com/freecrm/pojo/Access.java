
package com.freecrm.pojo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Access {
    @JsonProperty("private")
    private boolean isPrivate;
    private Object users;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Object getUsers() {
        return users;
    }

    public void setUsers(Object users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Access{" +
                "isPrivate=" + isPrivate + ", " +
                "users=" + users +
                "}";
    }
}