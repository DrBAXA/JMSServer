package ua.datalink.jms.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.datalink.jms.server.listener.JSONRequestMessageListener;

import javax.jms.*;

/**
 *
 */
@Service
public class JMSService {

    @Value("${jms.usetransactions}")
    private boolean transacted;
    @Value("${jms.request.destination.name}")
    private String requestQueueName;
    @Value("${jms.response.destination.name}")
    private String responseQueueName;
    private static final Logger logger = Logger.getLogger(JMSService.class);

    @Autowired
    private ConnectionFactory jmsConnectionFactory;

    private Connection connection;

    public Session getSession() {
        return session;
    }

    private Session session;
    private Destination requestDestination;
    private MessageConsumer consumer;

    public MessageProducer getProducer(Destination destination) throws JMSException {
        return session.createProducer(destination);
    }

    public boolean start(){
        try {
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

            requestDestination = session.createQueue(requestQueueName);
            consumer = session.createConsumer(requestDestination);
            consumer.setMessageListener(new JSONRequestMessageListener());

            logger.info("Server started");
            return true;
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
            return false;
        }
    }

    public boolean stop(){
        try {
            consumer.close();
            session.close();
            connection.close();
            logger.info("Server stopped.");
            return true;
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
            return false;
        }
    }
}
