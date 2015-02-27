package ua.datalink.jms.client.message;

/**
 * Thrown when data server didn't find some entity in db.
 */
public class EntityNotFoundException extends ProcessingException {
    public EntityNotFoundException() {
        super("Entity with given id not found.");
    }
}
