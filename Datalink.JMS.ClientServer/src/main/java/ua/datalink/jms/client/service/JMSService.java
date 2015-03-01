package ua.datalink.jms.client.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;

@Service
public class JMSService {

    public static final String NO_RESPONSE_FROM_SERVER = "{\"result\" : \"PROCESSING_ERROR\", " +
            "\"data\" : {\"description\" : \"Server didn't respond to request.\"}}";

    public static final String BED_RESPONSE_FROM_SERVER = "{\"result\" : \"PROCESSING_ERROR\", " +
            "\"data\" : {\"description\" : \"Server sent unsupported message type\"}}";

    @Value("${jms.request.timeout}")
    private int requestTimeout;
    @Value("${jms.request.destination.name}")
    private String requestQueueName;

    private static final Logger logger = Logger.getLogger(JMSService.class);

    @Autowired
    private ConnectionFactory jmsConnectionFactory;
    private Connection connection;
    private Session session;
    private Destination requestDestination;
    private MessageProducer producer;

    /**
     * Send request to server and receive response synchronously
     * @param request serialized to JSON request object
     * @return serialized to JSON response object or prepared JSON if server send bed response/not respond
     * @throws JMSException
     */
    public String requestResponse(String request) throws JMSException {
        Destination tempDestination = session.createTemporaryQueue();
        MessageConsumer tempConsumer = session.createConsumer(tempDestination);

        TextMessage message = session.createTextMessage(request);
        message.setJMSCorrelationID("corId");
        message.setJMSReplyTo(tempDestination);
        producer.send(message);
        Message response = tempConsumer.receive(requestTimeout);
        if(response == null){
            return NO_RESPONSE_FROM_SERVER;
        }
        if(response instanceof TextMessage){
            return ((TextMessage) response).getText();
        } else {
            return BED_RESPONSE_FROM_SERVER;
        }
    }

    @PostConstruct
    private boolean start(){
        try {
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            requestDestination = session.createQueue(requestQueueName);
            producer = session.createProducer(requestDestination);
            logger.info("Connected to JMSServer");
            return true;
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
            return false;
        }
    }

    @PreDestroy
    private boolean stop(){
        try {
            producer.close();
            session.close();
            connection.close();
            logger.info("Disconnected from JMSServer.");
            return true;
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
            return false;
        }
    }
}
