package ua.datalink.jms.server.listener;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "ServerRequestQueue"),
        @ActivationConfigProperty(
                propertyName = "connectionFactoryJndiName",
                propertyValue = "ActiveMqConnectionFactory")
})
public class JSONRequestMessageListener implements MessageListener {


    @Override
    public void onMessage(Message message) {
        if(message instanceof TextMessage){
            processingService.process((TextMessage) message);
        }
    }
}
