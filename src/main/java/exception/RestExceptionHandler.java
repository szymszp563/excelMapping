package exception;


import exception.custom.JsonIncompatibleWithClassException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JsonIncompatibleWithClassException.class)
    protected ResponseEntity<Object> handleJsonIncompatibleWithClassException(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.NO_CONTENT);
        apiError.setMessage(ex.getMessage());
        log.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
