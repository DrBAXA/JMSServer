package ua.datalink.jms.server.listener;

import org.apache.log4j.Logger;
import ua.datalink.jms.server.service.ProcessingService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static ua.datalink.jms.server.util.StaticResources.PROCESSING_SERVICE_JNDI_NAME;

/**
 *
 */

public class JSONRequestMessageListener implements MessageListener {

    private static final Logger logger = Logger.getLogger(JSONRequestMessageListener.class);

    private ProcessingService processingService;

    public JSONRequestMessageListener() {
        super();
        try {
            Context jndiContext = new InitialContext();
            processingService = (ProcessingService)jndiContext.lookup(PROCESSING_SERVICE_JNDI_NAME);
        } catch (NamingException ne) {
            logger.error(ne.getMessage());
            logger.debug(ne.getMessage(), ne);
        }
    }

    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            processingService.process((TextMessage) message);
        }
    }
}
