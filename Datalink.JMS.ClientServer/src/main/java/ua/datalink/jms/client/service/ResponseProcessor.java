package ua.datalink.jms.client.service;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;
import ua.datalink.jms.client.message.EntityNotFoundException;
import ua.datalink.jms.client.message.ProcessingErrorException;
import ua.datalink.jms.client.message.RequestResult;

import java.io.IOException;

@Service
public class ResponseProcessor {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     *
     * @param response serialized to JSON response object
     * @return JsonNode with response object
     * @throws IOException if response JSON has unsupported format
     * @throws EntityNotFoundException if server not found requested entity
     * @throws ProcessingErrorException if server was unable process request due to error.
     *         Error message from server is in ProcessingErrorException
     */
    public JsonNode processResponse(String response) throws IOException, EntityNotFoundException, ProcessingErrorException {
        JsonNode responseObject = objectMapper.readTree(response);
        RequestResult result = objectMapper.readValue(responseObject.get("result"), RequestResult.class);
        switch (result){
            case OK : {
                return responseObject.get("data");
            }
            case NOT_FOUND : {
                throw new EntityNotFoundException();
            }
            case PROCESSING_ERROR : {
                String errorMessage = responseObject.get("data").get("description").asText();
                throw new ProcessingErrorException(errorMessage);
            }
            default: throw new IOException("Unsupported server response.");
        }
    }
}
