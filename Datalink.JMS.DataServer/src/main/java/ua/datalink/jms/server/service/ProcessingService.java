package ua.datalink.jms.server.service;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.datalink.jms.server.dao.UserDAO;
import ua.datalink.jms.server.entity.User;
import ua.datalink.jms.server.message.RequestEntity;
import ua.datalink.jms.server.message.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import java.io.IOException;

import static ua.datalink.jms.server.util.StaticResources.PROCESSING_SERVICE_JNDI_NAME;

/**
 *
 */
@Service
public class ProcessingService {

    private static final Logger logger = Logger.getLogger(ProcessingService.class);

    @Autowired
    private JMSService JMSService;
    @Autowired
    private UserDAO userDAO;

    private ObjectMapper objectMapper;

    public void process(TextMessage message) {
        try {
            String messageText = null;
            try {
                RequestEntity requestEntity = objectMapper.readValue(message.getText(), RequestEntity.class);
                messageText = processObjectRequest(requestEntity);
            } catch (JsonParseException jpe) {
                messageText = "Unsupported request format. " + jpe.getMessage();
            }
            TextMessage responseMessage = JMSService.getSession().createTextMessage();
            responseMessage.setText(messageText);
            responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
            JMSService.getProducer(message.getJMSReplyTo()).send(responseMessage);
        }catch (JMSException | IOException ioe) {
            logger.error(ioe.getMessage());
            logger.debug(ioe.getMessage(), ioe);
        }
    }

    private String processObjectRequest(RequestEntity requestEntity) throws IOException, JMSException {
            switch (requestEntity.getAction()) {
                case PUT:
                    return put(requestEntity.getData());
                case GET:
                    return get(requestEntity.getData().getId());
                case GET_ALL:
                    return getAll();
                case DELETE:
                    return delete(requestEntity.getData().getId());
                default: throw new JMSException("Unsupported request type.");
            }
    }

    private String getAll() throws IOException {
        try {
            Iterable<User> allData = userDAO.findAll();
            ResponseEntity responseEntity = ResponseEntity.getOKResponse();
            responseEntity.setData(allData);
            return objectMapper.writeValueAsString(responseEntity);
        } catch (PersistenceException pe) {
            return objectMapper.writeValueAsString(ResponseEntity.getERRORResponse(pe.getMessage()));
        }
    }

    private String get(int id) throws IOException {
        try {
            if (userDAO.exists(id)) {
                User user = userDAO.findOne(id);
                ResponseEntity response = ResponseEntity.getOKResponse();
                response.setData(user);
                return objectMapper.writeValueAsString(response);
            } else {
                return objectMapper.writeValueAsString(ResponseEntity.getNOTFOUNDResponse(id));
            }
        } catch (PersistenceException pe) {
            return objectMapper.writeValueAsString(ResponseEntity.getERRORResponse(pe.getMessage()));
        }
    }

    private String put(User user) throws IOException, JMSException {
        try {
            userDAO.save(user);
            return objectMapper.writeValueAsString(ResponseEntity.getOKResponse());
        } catch (PersistenceException pe) {
            return objectMapper.writeValueAsString(ResponseEntity.getERRORResponse(pe.getMessage()));
        }
    }

    private String delete(int id) throws IOException {
        try {
            if (userDAO.exists(id)) {
                userDAO.delete(id);
                return objectMapper.writeValueAsString(ResponseEntity.getOKResponse());
            } else {
                return objectMapper.writeValueAsString(ResponseEntity.getNOTFOUNDResponse(id));
            }
        } catch (PersistenceException pe) {
            return objectMapper.writeValueAsString(ResponseEntity.getERRORResponse(pe.getMessage()));
        }
    }

    @PostConstruct
    private void init() {
        objectMapper = new ObjectMapper();
        try {
            Context jndiContext = new InitialContext();
            jndiContext.bind(PROCESSING_SERVICE_JNDI_NAME, this);
        } catch (NamingException ne) {
            logger.error(ne.getMessage());
            logger.debug(ne.getMessage(), ne);
        }
    }
}
