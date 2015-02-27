package ua.datalink.jms.server.message;

import ua.datalink.jms.server.entity.User;

/**
 *
 */
public class RequestEntity {
    private RequestAction action;
    private User data;

    public RequestAction getAction() {
        return action;
    }

    public void setAction(RequestAction action) {
        this.action = action;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}

