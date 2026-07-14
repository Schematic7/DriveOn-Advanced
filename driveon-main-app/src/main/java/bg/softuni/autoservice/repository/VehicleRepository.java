package bg.softuni.autoservice.repository;

import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVin(String vin);

    List<Vehicle> findAllByOwner(User owner);
}
