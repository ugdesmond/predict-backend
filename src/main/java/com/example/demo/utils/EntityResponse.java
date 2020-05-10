package com.example.demo.utils;


import com.example.demo.controller.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Component
public class EntityResponse {


    public <T> ResponseEntity<MessageResponse<T>> getErrorResponse(T t, Exception exception) {
        if (exception instanceof HttpClientErrorException.UnprocessableEntity) {
            return getInvalidFormError(t, exception.getMessage());
        } else if (exception instanceof HttpServerErrorException.ServiceUnavailable) {
            return getUnavailableServiceError(t, exception.getMessage());
        } else if (exception instanceof NullPointerException) {
            return getInternalServerError(t, "NullPointerException");
        }
        return getInternalServerError(t, exception.getMessage());
    }


    private <T> ResponseEntity<MessageResponse<T>> getInternalServerError(T t, String message) {
        MessageResponse<T> messageResponse = new MessageResponse<>();
        messageResponse.setMessage(message);
        messageResponse.setData(t);
        messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private <T> ResponseEntity<MessageResponse<T>> getInvalidFormError(T t, String message) {

        MessageResponse<T> messageResponse = new MessageResponse<>();
        messageResponse.setData(t);
        messageResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        messageResponse.setMessage(message);
        return new ResponseEntity<>(messageResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    public <T> ResponseEntity<MessageResponse<T>> getInvalidFormError(T t) {

        MessageResponse<T> messageResponse = new MessageResponse<>();
        messageResponse.setData(t);
        messageResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        messageResponse.setMessage("Invalid form parameters");
        return new ResponseEntity<>(messageResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private <T> ResponseEntity<MessageResponse<T>> getUnavailableServiceError(T t, String message) {

        MessageResponse<T> messageResponse = new MessageResponse<>();
        messageResponse.setData(t);
        messageResponse.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        messageResponse.setMessage(message);
        return new ResponseEntity<>(messageResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }


}
