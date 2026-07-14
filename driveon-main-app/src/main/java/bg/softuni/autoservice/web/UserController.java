package bg.softuni.autoservice.web;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("registerDTO")
    public UserRegisterDTO registerDTO() {
        return new UserRegisterDTO();
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("registerDTO") UserRegisterDTO registerDTO,
                                  BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes) {

        boolean passwordsMatch = registerDTO.getPassword().equals(registerDTO.getConfirmPassword());

        if (bindingResult.hasErrors() || !passwordsMatch) {

            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO", bindingResult);

            if (!passwordsMatch) {

                redirectAttributes.addFlashAttribute("passwordsMismatch", true);
            }

            return "redirect:/users/register";
        }

        userService.registerUser(registerDTO);

        return "redirect:/users/login";
    }
}
