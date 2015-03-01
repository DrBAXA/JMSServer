package ua.datalink.jms.server.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ua.datalink.jms.server.service.JMSService;
import ua.datalink.jms.server.service.ProcessingService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JSONRequestMessageListener implements MessageListener {

    private static final Logger logger = Logger.getLogger(JSONRequestMessageListener.class);

    @Autowired
    private Environment env;
    @Autowired
    private JMSService jmsService;
    @Autowired
    private ProcessingService processingService;
    private ExecutorService executorService;

    public JSONRequestMessageListener() {
    }

    @PostConstruct
    private void init(){
            executorService = Executors.newFixedThreadPool(Integer.parseInt(env.getProperty("server.threadpool.size")));
    }

    @PreDestroy
    private void destroy(){
        executorService.shutdown();
    }
    @Override
    public void onMessage(Message message) {
        executorService.submit(new ListenerThread(message));
    }

    class ListenerThread implements Runnable {

        Message message;

        public ListenerThread(Message message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                if (message instanceof TextMessage) {
                    String responseText = processingService.process(((TextMessage) message).getText());
                    TextMessage responseMessage = jmsService.getSession().createTextMessage();
                    responseMessage.setText(responseText);
                    responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
                    jmsService.getProducer().send(message.getJMSReplyTo(),responseMessage);
                }
            } catch (JMSException jmse) {
                logger.error(jmse.getMessage());
                logger.debug(jmse.getMessage(), jmse);
            }
        }
    }
}
