package ua.datalink.jms.server.message;

import ua.datalink.jms.server.entity.User;

/**
 *
 */
public class RequestEntity {
    private RequestAction action;
    private User data;

    public static RequestEntity getGET_ALLRequest(){
        RequestEntity request = new RequestEntity();
        request.setAction(RequestAction.GET_ALL);
        return request;
    }

    public static RequestEntity getGETRequest(int id){
        RequestEntity request = new RequestEntity();
        request.setAction(RequestAction.GET);
        request.setData(new User(id));
        return request;
    }

    public static RequestEntity getPUTRequest(User user){
        RequestEntity request = new RequestEntity();
        request.setAction(RequestAction.PUT);
        request.setData(user);
        return request;
    }

    public static RequestEntity getDELETERequest(int id){
        RequestEntity request = new RequestEntity();
        request.setAction(RequestAction.DELETE);
        request.setData(new User(id));
        return request;
    }

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

