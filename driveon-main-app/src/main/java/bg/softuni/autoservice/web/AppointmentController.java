package bg.softuni.autoservice.web;

import bg.softuni.autoservice.model.dto.appointment.AppointmentAddDTO;
import bg.softuni.autoservice.service.AppointmentService;
import bg.softuni.autoservice.service.ServiceTypeService;
import bg.softuni.autoservice.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final VehicleService vehicleService;
    private final ServiceTypeService serviceTypeService;
    private final AppointmentService appointmentService;

    public AppointmentController(VehicleService vehicleService,
                                 ServiceTypeService serviceTypeService,
                                 AppointmentService appointmentService) {
        this.vehicleService = vehicleService;
        this.serviceTypeService = serviceTypeService;
        this.appointmentService = appointmentService;
    }

    @ModelAttribute("appointmentAddDTO")
    public AppointmentAddDTO appointmentAddDTO() {
        return new AppointmentAddDTO();
    }

    @GetMapping("/add")
    public String addAppointment(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("userVehicles", vehicleService.getVehiclesForUser(userDetails.getUsername()));

        model.addAttribute("allServices", serviceTypeService.getAllServices());

        return "appointment-add";
    }

    @PostMapping("/add")
    public String addAppointmentConfirm(@Valid AppointmentAddDTO appointmentAddDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                        @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("appointmentAddDTO", appointmentAddDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.appointmentAddDTO", bindingResult);

            return "redirect:/appointments/add";
        }

        appointmentService.createAppointment(appointmentAddDTO, userDetails.getUsername());

        return "redirect:/";
    }

    @PostMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable UUID id,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        appointmentService.cancelAppointment(id, userDetails.getUsername());

        return "redirect:/";
    }
}