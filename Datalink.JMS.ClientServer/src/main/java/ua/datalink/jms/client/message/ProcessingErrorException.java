package ua.datalink.jms.client.message;

/**
 * Thrown when server was unable to process request.
 */
public class ProcessingErrorException extends ProcessingException{
    public ProcessingErrorException(String message) {
        super(message);
    }
}