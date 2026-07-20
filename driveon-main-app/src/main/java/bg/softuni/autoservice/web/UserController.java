package bg.softuni.autoservice.web;

import bg.softuni.autoservice.model.dto.loyalty.PointsResponseDto;
import bg.softuni.autoservice.model.dto.user.UserProfileDTO;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.service.UserService;
import bg.softuni.autoservice.service.loyalty.client.LoyaltyClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoyaltyClient loyaltyClient;

    @Value("${loyalty.api.key}")
    private String apiKey;

    public UserController(UserService userService, LoyaltyClient loyaltyClient) {
        this.userService = userService;
        this.loyaltyClient = loyaltyClient;
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

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();

        UserProfileDTO userProfile = userService.getUserProfile(username);
        model.addAttribute("userProfile", userProfile);

        Integer userPoints = 0;
        boolean pointsServiceAvailable = true;

        try {
            PointsResponseDto response = loyaltyClient.getUserPoints(username, apiKey);
            if (response != null && response.getTotalPoints() != null) {
                userPoints = response.getTotalPoints();
            }
        } catch (Exception e) {
            pointsServiceAvailable = false;
        }

        model.addAttribute("loyaltyPoints", userPoints);
        model.addAttribute("pointsServiceAvailable", pointsServiceAvailable);

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Principal principal, Model model) {
        if (!model.containsAttribute("userProfile")) {
            UserProfileDTO userProfile = userService.getUserProfile(principal.getName());
            model.addAttribute("userProfile", userProfile);
        }
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfileConfirm(@Valid @ModelAttribute("userProfile") UserProfileDTO userProfile,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfile", userProfile);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userProfile", bindingResult);
            return "redirect:/users/profile/edit";
        }

        userService.updateProfile(principal.getName(), userProfile);

        return "redirect:/users/profile";
    }
}
