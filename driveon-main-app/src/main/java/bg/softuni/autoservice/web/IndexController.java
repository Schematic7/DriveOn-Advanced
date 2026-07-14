package bg.softuni.autoservice.web;

import bg.softuni.autoservice.service.AppointmentService;
import bg.softuni.autoservice.service.VehicleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final VehicleService vehicleService;
    private final AppointmentService appointmentService;

    public IndexController(VehicleService vehicleService, AppointmentService appointmentService) {
        this.vehicleService = vehicleService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("vehicles", vehicleService.getVehiclesForUser(userDetails.getUsername()));
            model.addAttribute("appointments", appointmentService.getAppointmentsForUser(userDetails.getUsername()));
        }

        return "index";
    }
}