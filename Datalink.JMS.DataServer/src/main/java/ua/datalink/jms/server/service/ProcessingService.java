package ua.datalink.jms.server.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.datalink.jms.server.dao.UserDAO;
import ua.datalink.jms.server.entity.User;
import ua.datalink.jms.server.message.RequestEntity;
import ua.datalink.jms.server.message.ResponseEntity;

import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProcessingService {

    private  AtomicInteger getRequests = new AtomicInteger(0);
    private  AtomicInteger getAllRequests = new AtomicInteger(0);
    private  AtomicInteger putRequests = new AtomicInteger(0);
    private  AtomicInteger deleteRequests = new AtomicInteger(0);

    public int getGetRequests() {
        return getRequests.get();
    }

    public int getGetAllRequests() {
        return getAllRequests.get();
    }

    public int getPutRequests() {
        return putRequests.get();
    }

    public int getDeleteRequests() {
        return deleteRequests.get();
    }

    @Autowired
    private UserDAO userDAO;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String process(String message) {
        String responseText = null;
        try {
            RequestEntity requestEntity = objectMapper.readValue(message, RequestEntity.class);
            responseText = processObjectRequest(requestEntity);
        } catch (IOException jpe) {
            responseText = "Unsupported request format. " + jpe.getMessage();
        }
        return responseText;
    }

    private String processObjectRequest(RequestEntity requestEntity) throws IOException {
        switch (requestEntity.getAction()) {
            case PUT:
                return put(requestEntity.getData());
            case GET:
                return get(requestEntity.getData().getId());
            case GET_ALL:
                return getAll();
            case DELETE:
                return delete(requestEntity.getData().getId());
            default:
                return null;
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
        }finally {
            getAllRequests.incrementAndGet();
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
        }finally {
            getRequests.incrementAndGet();
        }
    }

    private String put(User user) throws IOException {
        try {
            userDAO.save(user);
            return objectMapper.writeValueAsString(ResponseEntity.getOKResponse());
        } catch (PersistenceException pe) {
            return objectMapper.writeValueAsString(ResponseEntity.getERRORResponse(pe.getMessage()));
        }finally {
            putRequests.incrementAndGet();
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
        }finally {
            deleteRequests.incrementAndGet();
        }
    }
}
