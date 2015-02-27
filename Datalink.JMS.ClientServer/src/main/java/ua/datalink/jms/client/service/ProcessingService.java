package ua.datalink.jms.client.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.datalink.jms.client.entity.User;
import ua.datalink.jms.client.message.EntityNotFoundException;
import ua.datalink.jms.client.message.ProcessingErrorException;
import ua.datalink.jms.client.message.RequestEntity;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ProcessingService {
    @Autowired
    private JMSService jmsService;
    @Autowired
    private ResponseProcessor responseProcessor;

    private ObjectMapper objectMapper = new ObjectMapper();

    public List<User> getAll() throws IOException, JMSException, ProcessingErrorException, EntityNotFoundException {
        RequestEntity request = RequestEntity.getGET_ALLRequest();
        String response = jmsService.requestResponse(objectMapper.writeValueAsString(request));
        JsonNode dataNode = responseProcessor.processResponse(response);
        List<User> resultList = new ArrayList<>();
        Iterator<JsonNode> iterator = dataNode.getElements();
        for(int i = 0; i < dataNode.size(); i++){
            resultList.add(objectMapper.readValue(iterator.next(), User.class));
        }
        return resultList;
    }

    public User get(int id) throws IOException, JMSException, ProcessingErrorException, EntityNotFoundException {
        RequestEntity request = RequestEntity.getGETRequest(id);
        String response = jmsService.requestResponse(objectMapper.writeValueAsString(request));
        return objectMapper.readValue(responseProcessor.processResponse(response), User.class);
    }

    public String put(User user) throws IOException, JMSException, ProcessingErrorException, EntityNotFoundException {
        RequestEntity request = RequestEntity.getPUTRequest(user);
        String response = jmsService.requestResponse(objectMapper.writeValueAsString(request));
        responseProcessor.processResponse(response);
        return "User was added to database.";
    }

    public String delete(int id) throws IOException, JMSException, ProcessingErrorException, EntityNotFoundException {
        RequestEntity request = RequestEntity.getDELETERequest(id);
        String response = jmsService.requestResponse(objectMapper.writeValueAsString(request));
        responseProcessor.processResponse(response);
        return "User with id " + id + " was deleted from database.";
    }
}
