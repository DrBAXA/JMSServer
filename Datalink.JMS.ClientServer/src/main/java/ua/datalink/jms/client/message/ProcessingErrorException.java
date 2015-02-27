package ua.datalink.jms.client.message;

/**
 *
 */
public class ProcessingErrorException extends ProcessingException{
    public ProcessingErrorException() {
        super();
    }

    public ProcessingErrorException(String message) {
        super(message);
    }
}