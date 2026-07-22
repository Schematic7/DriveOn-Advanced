package bg.softuni.autoservice.web.advice;

import bg.softuni.autoservice.exceptions.DuplicateResourceException;
import bg.softuni.autoservice.exceptions.EmailAlreadyExistsException;
import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException exception) {
        log.error("Resource not found: {}", exception.getMessage());
        return createErrorModelAndView(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ModelAndView handleUnauthorizedAction(UnauthorizedActionException exception) {
        log.error("Unauthorized action: {}", exception.getMessage());
        return createErrorModelAndView(exception.getMessage());
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ModelAndView handleDuplicateResource(DuplicateResourceException exception) {
        log.warn("Duplicate resource: {}", exception.getMessage());
        return createErrorModelAndView(exception.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ModelAndView handleEmailExists(EmailAlreadyExistsException exception) {
        log.warn("Email already exists: {}", exception.getMessage());
        return createErrorModelAndView(exception.getMessage());
    }

    private ModelAndView createErrorModelAndView(String message) {
        ModelAndView modelAndView = new ModelAndView("custom-error");
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }
}