package bg.softuni.autoservice.mapper.vehicle;

import bg.softuni.autoservice.model.dto.vehicle.VehicleAddDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleEditDTO;
import bg.softuni.autoservice.model.dto.vehicle.VehicleViewDTO;
import bg.softuni.autoservice.model.entity.User;
import bg.softuni.autoservice.model.entity.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {

    @Test
    void testToViewDTO_WithValidVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setMake("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setLicensePlate("CB1234AB");
        vehicle.setYear(2020);
        vehicle.setVin("12345678901234567");

        VehicleViewDTO dto = VehicleMapper.toViewDTO(vehicle);

        assertNotNull(dto);
        assertEquals(vehicle.getId().toString(), dto.getId());
        assertEquals("Toyota", dto.getMake());
        assertEquals("Corolla", dto.getModel());
        assertEquals("CB1234AB", dto.getLicensePlate());
        assertEquals(2020, dto.getYear());
        assertEquals("12345678901234567", dto.getVin());
    }

    @Test
    void testToViewDTO_WithNullVehicle() {
        assertNull(VehicleMapper.toViewDTO(null));
    }

    @Test
    void testToVehicleEntity_WithValidDTO() {
        User owner = new User();
        owner.setUsername("test_owner");

        VehicleAddDTO dto = new VehicleAddDTO();
        dto.setMake("Honda");
        dto.setModel("Civic");
        dto.setLicensePlate("cb9999xx");
        dto.setYear(2019);
        dto.setVin("vin12345678901234");

        Vehicle vehicle = VehicleMapper.toVehicleEntity(dto, owner);

        assertNotNull(vehicle);
        assertEquals("Honda", vehicle.getMake());
        assertEquals("Civic", vehicle.getModel());
        assertEquals("CB9999XX", vehicle.getLicensePlate());
        assertEquals(2019, vehicle.getYear());
        assertEquals("VIN12345678901234", vehicle.getVin());
        assertEquals(owner, vehicle.getOwner());
    }

    @Test
    void testToVehicleEntity_WithNullDTO() {
        assertNull(VehicleMapper.toVehicleEntity(null, new User()));
    }

    @Test
    void testUpdateVehicleFromDto_WithValidData() {
        Vehicle vehicle = new Vehicle();

        VehicleEditDTO dto = new VehicleEditDTO();
        dto.setMake("Ford");
        dto.setModel("Focus");
        dto.setLicensePlate("pb1111aa");
        dto.setYear(2015);
        dto.setVin("fordvin1234567890");

        VehicleMapper.updateVehicleFromDto(vehicle, dto);

        assertEquals("Ford", vehicle.getMake());
        assertEquals("Focus", vehicle.getModel());
        assertEquals("PB1111AA", vehicle.getLicensePlate());
        assertEquals(2015, vehicle.getYear());
        assertEquals("FORDVIN1234567890", vehicle.getVin());
    }

    @Test
    void testUpdateVehicleFromDto_WithNullVehicleOrDTO() {
        Vehicle vehicle = new Vehicle();
        VehicleEditDTO dto = new VehicleEditDTO();

        assertDoesNotThrow(() -> VehicleMapper.updateVehicleFromDto(null, dto));
        assertDoesNotThrow(() -> VehicleMapper.updateVehicleFromDto(vehicle, null));
    }

    @Test
    void testToEditDTO_WithValidVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(UUID.randomUUID());
        vehicle.setMake("Mazda");
        vehicle.setModel("3");
        vehicle.setLicensePlate("V1234V");
        vehicle.setYear(2022);
        vehicle.setVin("MAZDA123456789012");

        VehicleEditDTO dto = VehicleMapper.toEditDTO(vehicle);

        assertNotNull(dto);
        assertEquals(vehicle.getId().toString(), dto.getId());
        assertEquals("Mazda", dto.getMake());
        assertEquals("3", dto.getModel());
    }

    @Test
    void testToEditDTO_WithNullVehicle() {
        assertNull(VehicleMapper.toEditDTO(null));
    }
}