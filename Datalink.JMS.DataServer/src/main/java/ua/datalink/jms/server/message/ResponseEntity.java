package ua.datalink.jms.server.message;

import static ua.datalink.jms.server.message.RequestResult.NOT_FOUND;
import static ua.datalink.jms.server.message.RequestResult.OK;
import static ua.datalink.jms.server.message.RequestResult.PROCESSING_ERROR;

/**
 *
 */
public class ResponseEntity {
    private RequestResult result;
    private Object data;

    public static ResponseEntity getOKResponse(){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResult(OK);
        return responseEntity;
    }

    public static ResponseEntity getERRORResponse(String errMessage){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResult(PROCESSING_ERROR);
        MessageData data = new MessageData();
        data.setDescription(errMessage);
        responseEntity.setData(data);
        return responseEntity;
    }

    public static ResponseEntity getNOTFOUNDResponse(int id){
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setResult(NOT_FOUND);
        MessageData data = new MessageData();
        data.setDescription("Entity with id " + id + " not found.");
        responseEntity.setData(data);
        return responseEntity;
    }

    public RequestResult getResult() {
        return result;
    }

    public void setResult(RequestResult result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

