package bg.softuni.autoservice.web;

import bg.softuni.autoservice.service.AppointmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AppointmentService appointmentService;

    public AdminController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
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
}