package ua.datalink.jms.server.message;

/**
 *
 */
public class EntityNotFounException extends ProcessingException {
    public EntityNotFounException() {
        super("Entity with given id not found.");
    }
}
