package bg.softuni.autoservice.web;

import bg.softuni.autoservice.service.AppointmentService;
import bg.softuni.autoservice.service.ServiceTypeService;
import bg.softuni.autoservice.service.VehicleService;
import bg.softuni.autoservice.service.loyalty.LoyaltyIntegrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ServiceTypeService serviceTypeService;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private LoyaltyIntegrationService loyaltyIntegrationService;

    @Test
    @WithMockUser(username = "test_user")
    void testAddAppointmentGet_ReturnsView() throws Exception {
        when(vehicleService.getVehiclesForUser("test_user")).thenReturn(List.of());
        when(serviceTypeService.getAllServices()).thenReturn(List.of());
        when(loyaltyIntegrationService.getAvailablePoints("test_user")).thenReturn(150);

        mockMvc.perform(get("/appointments/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointment-add"))
                .andExpect(model().attributeExists("userVehicles"))
                .andExpect(model().attributeExists("allServices"))
                .andExpect(model().attributeExists("availablePoints"))
                .andExpect(model().attributeExists("appointmentAddDTO"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void testAddAppointmentConfirm_ValidData_RedirectsToHome() throws Exception {
        mockMvc.perform(post("/appointments/add")
                        .param("vehicleId", UUID.randomUUID().toString())
                        .param("serviceTypeId", UUID.randomUUID().toString())
                        .param("appointmentDate", "2027-01-01T10:00")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(appointmentService, times(1)).createAppointment(any(), eq("test_user"));
    }

    @Test
    @WithMockUser(username = "test_user")
    void testAddAppointmentConfirm_InvalidData_RedirectsToAdd() throws Exception {
        mockMvc.perform(post("/appointments/add")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments/add"))
                .andExpect(flash().attributeExists("appointmentAddDTO"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.appointmentAddDTO"));

        verify(appointmentService, never()).createAppointment(any(), any());
    }

    @Test
    @WithMockUser(username = "test_user")
    void testCancelAppointment_RedirectsToHome() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        mockMvc.perform(post("/appointments/cancel/" + appointmentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(appointmentService, times(1)).cancelAppointment(eq(appointmentId), eq("test_user"));
    }
}