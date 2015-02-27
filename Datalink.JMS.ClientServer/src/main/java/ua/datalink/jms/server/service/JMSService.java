package ua.datalink.jms.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    public MessageProducer getProducer() {
        return producer;
    }

    private MessageProducer producer;

    public String sendReceive(String request) throws JMSException {
        Destination tempDestination = session.createTemporaryQueue();
        MessageConsumer tempConsumer = session.createConsumer(tempDestination);

        TextMessage message = session.createTextMessage(request);
        message.setJMSCorrelationID("corId");
        message.setJMSReplyTo(tempDestination);
        producer.send(message);
        Message response = tempConsumer.receive(1000);
        if(response == null){
            return "{\"result\" : \"PROCESSING_ERROR\", \"data\" : {\"description\" : \"Server didn't respond to request.\"}}";
        }
        if(response instanceof TextMessage){
            return ((TextMessage) response).getText();
        } else {
            return "{\"result\" : \"PROCESSING_ERROR\", \"data\" : {\"description\" : \"Server sent unsupported message type\"}}";
        }
    }

    @PostConstruct
    public boolean start(){
        try {
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

            requestDestination = session.createQueue(requestQueueName);

            producer = session.createProducer(requestDestination);
            logger.info("Server started");
            return true;
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
            return false;
        }
    }

    @PreDestroy
    public boolean stop(){
        try {
            producer.close();
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
