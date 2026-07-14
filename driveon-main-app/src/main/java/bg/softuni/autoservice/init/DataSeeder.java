package bg.softuni.autoservice.init;

import bg.softuni.autoservice.model.entity.ServiceType;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.repository.ServiceTypeRepository;
import bg.softuni.autoservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ServiceTypeRepository serviceTypeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public DataSeeder(ServiceTypeRepository serviceTypeRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (serviceTypeRepository.count() == 0) {

            ServiceType diagnostics = ServiceType.builder()
                    .name("Diagnostics")
                    .description("Full computer diagnostics and system scan")
                    .price(50.00)
                    .build();

            ServiceType maintenance = ServiceType.builder()
                    .name("Regular Maintenance")
                    .description("Standard service including oil, filters, and basic fluid checks")
                    .price(100.00)
                    .build();

            ServiceType repair = ServiceType.builder()
                    .name("General Repair")
                    .description("Mechanical or electrical repairs. Final price is determined after inspection.")
                    .price(0.00)
                    .build();

            ServiceType inspection = ServiceType.builder()
                    .name("Annual Technical Inspection")
                    .description("Official vehicle safety and emissions check")
                    .price(60.00)
                    .build();

            serviceTypeRepository.saveAll(List.of(diagnostics, maintenance, repair, inspection));
        }

        if (userRepository.count() == 0) {

            User regularUser = User.builder()
                    .username("user")
                    .email("user@autoservice.com")
                    .password(passwordEncoder.encode("12345"))
                    .role(bg.softuni.autoservice.model.enums.UserRole.USER)
                    .build();

            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@autoservice.com")
                    .password(passwordEncoder.encode("12345"))

                    .role(bg.softuni.autoservice.model.enums.UserRole.ADMIN)
                    .build();

            userRepository.saveAll(List.of(regularUser, adminUser));
        }
    }
}