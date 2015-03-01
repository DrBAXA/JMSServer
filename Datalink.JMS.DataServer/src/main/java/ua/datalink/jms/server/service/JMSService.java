package ua.datalink.jms.server.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua.datalink.jms.server.listener.JSONRequestMessageListener;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
@Service
public class JMSService {

    private AtomicBoolean status = new AtomicBoolean(false);

    private static final Logger logger = Logger.getLogger(JMSService.class);

    @Value("${jms.request.destination.name}")
    private String requestQueueName;

    @Autowired
    private ConnectionFactory jmsConnectionFactory;
    @Autowired
    private JSONRequestMessageListener listener;

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    private MessageProducer producer;

    public Session getSession() {
        return session;
    }

    public MessageProducer getProducer() throws JMSException {
        return producer;
    }

    public boolean getStatus() {
        return status.get();
    }

    @PostConstruct
    public void start(){
        try {
            connection = jmsConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination requestDestination = session.createQueue(requestQueueName);
            consumer = session.createConsumer(requestDestination);
            consumer.setMessageListener(listener);

            producer = session.createProducer(null);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            logger.info("Connected to JMSServer");
            status.set(true);

        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
        }
    }

    @PreDestroy
    public void stop(){
        try {
            consumer.close();
            producer.close();
            session.close();
            connection.close();
            logger.info("Disconnected from JMSServer.");
            status.set(false);
        }catch (JMSException jmse){
            logger.error(jmse.getMessage());
            logger.debug(jmse.getMessage(), jmse);
        }
    }

}


