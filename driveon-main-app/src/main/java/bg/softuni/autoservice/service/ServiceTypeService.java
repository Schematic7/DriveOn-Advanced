package bg.softuni.autoservice.service;

import bg.softuni.autoservice.model.entity.ServiceType;
import bg.softuni.autoservice.repository.ServiceTypeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Cacheable("serviceTypes")
    public List<ServiceType> getAllServices() {
        return serviceTypeRepository.findAll();
    }
}