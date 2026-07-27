package bg.softuni.autoservice.web.advice;

import bg.softuni.autoservice.exceptions.DuplicateResourceException;
import bg.softuni.autoservice.exceptions.EmailAlreadyExistsException;
import bg.softuni.autoservice.exceptions.ResourceNotFoundException;
import bg.softuni.autoservice.exceptions.UnauthorizedActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleResourceNotFound_ShouldReturnCustomErrorView() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found test");
        ModelAndView mav = exceptionHandler.handleResourceNotFound(ex);

        assertErrorView(mav, "Not found test");
    }

    @Test
    void handleUnauthorizedAction_ShouldReturnCustomErrorView() {
        UnauthorizedActionException ex = new UnauthorizedActionException("Unauthorized test");
        ModelAndView mav = exceptionHandler.handleUnauthorizedAction(ex);

        assertErrorView(mav, "Unauthorized test");
    }

    @Test
    void handleDuplicateResource_ShouldReturnCustomErrorView() {
        DuplicateResourceException ex = new DuplicateResourceException("Duplicate test");
        ModelAndView mav = exceptionHandler.handleDuplicateResource(ex);

        assertErrorView(mav, "Duplicate test");
    }

    @Test
    void handleEmailExists_ShouldReturnCustomErrorView() {
        EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email test");
        ModelAndView mav = exceptionHandler.handleEmailExists(ex);

        assertErrorView(mav, "Email test");
    }

    @Test
    void handleIllegalArgument_ShouldReturnCustomErrorView() {
        IllegalArgumentException ex = new IllegalArgumentException("Illegal argument test");
        ModelAndView mav = exceptionHandler.handleIllegalArgument(ex);

        assertErrorView(mav, "Illegal argument test");
    }

    private void assertErrorView(ModelAndView mav, String expectedMessage) {
        assertNotNull(mav);
        assertEquals("custom-error", mav.getViewName());
        assertEquals(expectedMessage, mav.getModel().get("errorMessage"));
    }
}