package bg.softuni.autoservice.web;

import bg.softuni.autoservice.model.dto.vehicle.VehicleEditDTO;
import bg.softuni.autoservice.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    @WithMockUser(username = "test_user")
    void testAddVehicleGet_ReturnsView() throws Exception {
        mockMvc.perform(get("/vehicles/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicle-add"))
                .andExpect(model().attributeExists("vehicleAddDTO"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void testAddVehicleConfirm_ValidData_RedirectsToHome() throws Exception {
        mockMvc.perform(post("/vehicles/add")
                        // Сменяме VIN-а да бъде точно 17 символа:
                        .param("vin", "12345678901234567")
                        .param("licensePlate", "CB1234AB")
                        .param("make", "Toyota")
                        .param("model", "Corolla")
                        .param("year", "2020")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(vehicleService, times(1)).addVehicle(any(), any());
    }

    @Test
    @WithMockUser(username = "test_user")
    void testAddVehicleConfirm_InvalidData_RedirectsToAdd() throws Exception {
        mockMvc.perform(post("/vehicles/add")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicles/add"))
                .andExpect(flash().attributeExists("vehicleAddDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.vehicleAddDTO"));

        verify(vehicleService, never()).addVehicle(any(), any());
    }

    @Test
    @WithMockUser(username = "test_user")
    void testDeleteVehicle_RedirectsToHome() throws Exception {
        mockMvc.perform(post("/vehicles/123/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(vehicleService, times(1)).deleteVehicle(eq("123"), eq("test_user"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void testEditVehicleGet_ReturnsView() throws Exception {
        when(vehicleService.getVehicleForEdit(eq("123"), eq("test_user")))
                .thenReturn(new VehicleEditDTO());

        mockMvc.perform(get("/vehicles/123/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicle-edit"))
                .andExpect(model().attributeExists("vehicleEditDTO"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void testEditVehicleConfirm_ValidData_RedirectsToHome() throws Exception {
        mockMvc.perform(post("/vehicles/123/edit")
                        .param("licensePlate", "CB9999AA")
                        .param("make", "Honda")
                        // Добавяме липсващите задължителни полета:
                        .param("model", "Civic")
                        .param("year", "2019")
                        .param("vin", "98765432109876543") // Отново 17 символа
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(vehicleService, times(1)).updateVehicle(eq("123"), any(), eq("test_user"));
    }
}