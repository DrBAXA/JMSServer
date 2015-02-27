package ua.datalink.jms.client.service;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.datalink.jms.client.entity.User;
import ua.datalink.jms.client.message.*;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
@Service
public class ProcessingService {

    private static final Logger logger = Logger.getLogger(ProcessingService.class);

    @Autowired
    private JMSService jmsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAll() throws IOException, JMSException, ProcessingErrorException, EntityNotFounException {
        RequestEntity request = RequestEntity.getGET_ALLRequest();
        String response = jmsService.sendReceive(objectMapper.writeValueAsString(request));
        JsonNode dataNode = processResponse(response);
        List<User> resultList = new ArrayList<>();
        Iterator<JsonNode> iterator = dataNode.getElements();
        for(int i = 0; i < dataNode.size(); i++){
            resultList.add(objectMapper.readValue(iterator.next(), User.class));
        }
        return resultList;
    }

    public User get(int id) throws IOException, JMSException, ProcessingErrorException, EntityNotFounException {
        RequestEntity request = RequestEntity.getGETRequest(id);
        String response = jmsService.sendReceive(objectMapper.writeValueAsString(request));
        return objectMapper.readValue(processResponse(response), User.class);
    }

    public String put(User user) throws IOException, JMSException, ProcessingErrorException, EntityNotFounException {
        RequestEntity request = RequestEntity.getPUTRequest(user);
        String response = jmsService.sendReceive(objectMapper.writeValueAsString(request));
        processResponse(response);
        return "User was added to database.";
    }

    public String delete(int id) throws IOException, JMSException, ProcessingErrorException, EntityNotFounException {
        RequestEntity request = RequestEntity.getDELETERequest(id);
        String response = jmsService.sendReceive(objectMapper.writeValueAsString(request));
        processResponse(response);
        return "User with id " + id + " was deleted from database.";
    }

    private JsonNode processResponse(String response) throws IOException, EntityNotFounException, ProcessingErrorException {
        JsonNode responseObject = objectMapper.readTree(response);
        RequestResult result = objectMapper.readValue(responseObject.get("result"), RequestResult.class);
        switch (result){
            case OK : {
                return responseObject.get("data");
            }
            case NOT_FOUND : {
                throw new EntityNotFounException();
            }
            case PROCESSING_ERROR : {
                String errorMessage = responseObject.get("data").get("description").asText();
                throw new ProcessingErrorException(errorMessage);
            }
            default: throw new IOException("Unsupported server response.");
        }
    }
}
