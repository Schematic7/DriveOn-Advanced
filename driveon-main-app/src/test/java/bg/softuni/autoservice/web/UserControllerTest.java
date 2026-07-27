package bg.softuni.autoservice.web;

import bg.softuni.autoservice.model.dto.loyalty.PointsResponseDto;
import bg.softuni.autoservice.model.dto.user.UserProfileDTO;
import bg.softuni.autoservice.model.dto.user.UserRegisterDTO;
import bg.softuni.autoservice.service.UserService;
import bg.softuni.autoservice.service.loyalty.client.LoyaltyClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@TestPropertySource(properties = "loyalty.api.key=test-api-key")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private LoyaltyClient loyaltyClient;

    @Test
    @WithMockUser
    void testRegister_ReturnsView() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerDTO"));
    }

    @Test
    @WithMockUser
    void testLogin_ReturnsView() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser
    void testRegisterConfirm_ValidData_RedirectsToLogin() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "testuser")
                        .param("email", "test@test.com")
                        .param("phoneNumber", "0888123456")
                        .param("firstName", "Test")
                        .param("lastName", "User")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userService, times(1)).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    @WithMockUser
    void testRegisterConfirm_PasswordsMismatch_RedirectsToRegister() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "testuser")
                        .param("email", "test@test.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("registerDTO"))
                .andExpect(flash().attributeExists("passwordsMismatch"));

        verify(userService, never()).registerUser(any());
    }

    @Test
    @WithMockUser
    void testRegisterConfirm_HasErrors_RedirectsToRegister() throws Exception {
        mockMvc.perform(post("/users/register")
                        .param("username", "")
                        .param("password", "pass")
                        .param("confirmPassword", "pass")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.registerDTO"));

        verify(userService, never()).registerUser(any());
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProfile_Success() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        when(userService.getUserProfile("testuser")).thenReturn(profileDTO);

        PointsResponseDto pointsDto = new PointsResponseDto();
        pointsDto.setTotalPoints(150);
        when(loyaltyClient.getUserPoints(eq("testuser"), anyString())).thenReturn(pointsDto);

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("userProfile"))
                .andExpect(model().attribute("loyaltyPoints", 150))
                .andExpect(model().attribute("pointsServiceAvailable", true));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testProfile_LoyaltyServiceUnavailable() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        when(userService.getUserProfile("testuser")).thenReturn(profileDTO);

        when(loyaltyClient.getUserPoints(anyString(), anyString())).thenThrow(new RuntimeException("Service Down"));

        mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("loyaltyPoints", 0))
                .andExpect(model().attribute("pointsServiceAvailable", false));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEditProfile_ReturnsView() throws Exception {
        UserProfileDTO profileDTO = new UserProfileDTO();
        when(userService.getUserProfile("testuser")).thenReturn(profileDTO);

        mockMvc.perform(get("/users/profile/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile-edit"))
                .andExpect(model().attributeExists("userProfile"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEditProfileConfirm_ValidData_RedirectsToProfile() throws Exception {
        mockMvc.perform(post("/users/profile/edit")
                        .param("firstName", "Ivan")
                        .param("lastName", "Ivanov")
                        .param("phoneNumber", "0888123456")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile"));

        verify(userService, times(1)).updateProfile(eq("testuser"), any(UserProfileDTO.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testEditProfileConfirm_InvalidData_RedirectsToEdit() throws Exception {
        mockMvc.perform(post("/users/profile/edit")
                        .param("firstName", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/profile/edit"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.userProfile"));

        verify(userService, never()).updateProfile(anyString(), any());
    }
}