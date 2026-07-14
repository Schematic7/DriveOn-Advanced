package bg.softuni.autoservice.web.advice;

import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleVehicleNotFound(ResourceNotFoundException exception) {

        ModelAndView modelAndView = new ModelAndView("error-not-found");

        modelAndView.addObject("errorMessage", exception.getMessage());

        return modelAndView;
    }
}