package ua.datalink.jms.client.message;

/**
 *
 */
public class EntityNotFounException extends ProcessingException {
    public EntityNotFounException() {
        super("Entity with given id not found.");
    }
}
