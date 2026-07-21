package bg.softuni.autoservice.web;

import bg.softuni.autoservice.service.AppointmentService;
import bg.softuni.autoservice.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public AdminController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    @GetMapping("/appointments")
    public String viewAllAppointments(Model model) {
        model.addAttribute("allAppointments", appointmentService.getAllAppointmentsForAdmin());
        return "admin-appointments";
    }

    @PostMapping("/appointments/approve/{id}")
    public String approveAppointment(@PathVariable java.util.UUID id) {
        appointmentService.approveAppointment(id);
        return "redirect:/admin/appointments";
    }

    @PostMapping("/appointments/complete/{id}")
    public String completeAppointment(@PathVariable java.util.UUID id) {
        appointmentService.completeAppointment(id);
        return "redirect:/admin/appointments";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {

        model.addAttribute("users", userService.getAllUsersForManagement());

        return "admin-users";
    }

    @PostMapping("/users/change-role/{id}")
    public String changeUserRole(@PathVariable("id") java.util.UUID id,
                                 @RequestParam("newRole") String newRole) {

        userService.changeUserRole(id, newRole);

        return "redirect:/admin/users";
    }
}