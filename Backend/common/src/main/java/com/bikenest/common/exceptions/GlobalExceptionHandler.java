package com.bikenest.common.exceptions;

import com.bikenest.common.interfaces.GeneralExceptionResponse;
import com.bikenest.common.interfaces.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;

/**
 * This Global Exception Handlers should catch ALL of the Exceptions that happen in any microservice.
 * It will differentiate between BusinessLogicExceptions, that are thrown purposefully and other Exceptions
 * that happen accidently. In the case of an BusinessException, the Exception will contain a useful message, that
 * can be displayed to the user. In the other Case, we will just return "An Unknown error occured on the server."
 *
 * It might also be interesting to introduce yet another Exception Class like HiddenBusinessLogicException, that will
 * also be caught in this Exception Handler, but in that case we also only return "An Unknown error occured."
 * That way we can prevent people with bad intents to get relevant Information from our server. Currently someone could
 * for example try to cancel a reservation with a random id and then he will get the Information that "This reservation
 * does not belong to you" or "This reservation does not exist".
 * It is not that important to prevent this, but most other companies do it similar.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This ExceptionHandler catches all BusinessLogicExceptions and transforms then into a GeneralExceptionResponse.
     * The Response Code will be 422 (Unprocessable Entity) to indicate for the frontend, that a Business Logic error has
     * occurred.
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<GeneralExceptionResponse> handleBusinessLogicExceptions(HttpServletRequest request, BusinessLogicException ex){
        logger.error("BusinessLogicException:: URL=" + request.getRequestURL());
        logger.error("Message:: " + ex.getMessage());
        logger.error("StackTrace:: " + ex.getStackTrace());
        return ResponseEntity.unprocessableEntity().body(new GeneralExceptionResponse(ex.getMessage()));
    }

    /**
     * This will catch all other exceptions that might happen inside the controllers and that we are not aware off.
     * Therefore we cannot give a meaningful message back to the frontend and just state "An unknown error has occurred on
     * the server."
     * Response Code will be 500 (Internal Server Error)
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionResponse> handleOtherExceptions(HttpServletRequest request, Exception ex){
        logger.error("Unknown error::" + ex.getMessage());
        logger.error("Stacktrace::" + ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new GeneralExceptionResponse("Ein unbekannter Fehler ist im Server aufgetreten."));
    }

    /**
     * This handles errors that will be generated, when an API Endpoint receives and invalid RequestBody.
     * Hopefully this will prevent confusion on the frontend side (for example the signup endpoint would return
     * a bad request response, when the password had less then 6 characters).
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        logger.error("Invalid request::" + ex.getMessage());
        return ResponseEntity.badRequest().body(new GeneralExceptionResponse("Fehlerhafte Anfrage!"));
    }
}
