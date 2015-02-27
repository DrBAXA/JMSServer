package ua.datalink.jms.server.message;

import ua.datalink.jms.server.entity.User;

import java.util.List;

/**
 *
 */
public class ListResponseEntity extends ResponseEntity {
    private List<User> data;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
