package bg.softuni.autoservice.web;

import bg.softuni.autoservice.model.dto.vehicle.VehicleAddDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleEditDTO;
import bg.softuni.autoservice.service.VehicleService;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @ModelAttribute("vehicleAddDTO")
    public VehicleAddDTO vehicleAddDTO() {
        return new VehicleAddDTO();
    }

    @GetMapping("/add")
    public String addVehicle() {
        return "vehicle-add";
    }

    @PostMapping("/add")
    public String addVehicleConfirm(@Valid VehicleAddDTO vehicleAddDTO,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("vehicleAddDTO", vehicleAddDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.vehicleAddDTO", bindingResult);

            return "redirect:/vehicles/add";
        }

        vehicleService.addVehicle(vehicleAddDTO, userDetails);

        return "redirect:/";
    }

    @PostMapping("/{id}/delete")
    public String deleteVehicle(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {

        vehicleService.deleteVehicle(id, userDetails.getUsername());

        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editVehicle(@PathVariable String id,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {

        if (!model.containsAttribute("vehicleEditDTO")) {
            VehicleEditDTO editDTO = vehicleService.getVehicleForEdit(id, userDetails.getUsername());
            model.addAttribute("vehicleEditDTO", editDTO);
        }

        return "vehicle-edit";
    }

    @PostMapping("/{id}/edit")
    public String editVehicleConfirm(@PathVariable String id,
                                     @Valid VehicleEditDTO vehicleEditDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("vehicleEditDTO", vehicleEditDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.vehicleEditDTO", bindingResult);
            return "redirect:/vehicles/" + id + "/edit";
        }

        vehicleService.updateVehicle(id, vehicleEditDTO, userDetails.getUsername());

        return "redirect:/";
    }
}