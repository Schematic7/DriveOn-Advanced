package bg.softuni.autoservice.init;

import bg.softuni.autoservice.repository.ServiceTypeRepository;
import bg.softuni.autoservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Test
    void testRun_WhenDatabasesAreEmpty_ShouldSeedData() throws Exception {

        when(serviceTypeRepository.count()).thenReturn(0L);
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        dataSeeder.run();

        verify(serviceTypeRepository, times(1)).saveAll(anyList());
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testRun_WhenDatabasesAreNotEmpty_ShouldNotSeedData() throws Exception {

        when(serviceTypeRepository.count()).thenReturn(4L);
        when(userRepository.count()).thenReturn(2L);

        dataSeeder.run();


        verify(serviceTypeRepository, never()).saveAll(anyList());
        verify(userRepository, never()).saveAll(anyList());
        verify(passwordEncoder, never()).encode(anyString());
    }
}