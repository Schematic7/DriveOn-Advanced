package bg.softuni.autoservice.web;

import bg.softuni.autoservice.service.AppointmentService;
import bg.softuni.autoservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testViewAllAppointments_ReturnsViewAndModel() throws Exception {
        when(appointmentService.getAllAppointmentsForAdmin()).thenReturn(List.of());

        mockMvc.perform(get("/admin/appointments"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-appointments"))
                .andExpect(model().attributeExists("allAppointments"));

        verify(appointmentService, times(1)).getAllAppointmentsForAdmin();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testApproveAppointment_RedirectsToAppointments() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        mockMvc.perform(post("/admin/appointments/approve/" + appointmentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appointments"));

        verify(appointmentService, times(1)).approveAppointment(appointmentId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCompleteAppointment_RedirectsToAppointments() throws Exception {
        UUID appointmentId = UUID.randomUUID();

        mockMvc.perform(post("/admin/appointments/complete/" + appointmentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/appointments"));

        verify(appointmentService, times(1)).completeAppointment(appointmentId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testManageUsers_ReturnsViewAndModel() throws Exception {
        when(userService.getAllUsersForManagement()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-users"))
                .andExpect(model().attributeExists("users"));

        verify(userService, times(1)).getAllUsersForManagement();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testChangeUserRole_RedirectsToUsers() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(post("/admin/users/change-role/" + userId)
                        .param("newRole", "USER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));

        verify(userService, times(1)).changeUserRole(userId, "USER");
    }
}