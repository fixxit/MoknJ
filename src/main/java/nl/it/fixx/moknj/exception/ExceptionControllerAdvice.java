package nl.it.fixx.moknj.exception;

import nl.it.fixx.moknj.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler({AccessException.class, BalException.class})
    public @ResponseBody
    ErrorResponse handleConflict(Exception ex, WebRequest request) {
        if (ex instanceof AccessException) {
            LOG.info("Access Error", ex);
        } else if (ex instanceof BalException) {
            LOG.info("General Error", ex);
        }

        ErrorResponse response = new ErrorResponse();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return response;
    }

}
