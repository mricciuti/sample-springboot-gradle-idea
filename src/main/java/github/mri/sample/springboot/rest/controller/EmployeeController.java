package github.mri.sample.springboot.rest.controller;

import github.mri.sample.springboot.dao.EmployeeRepository;
import github.mri.sample.springboot.domain.Employee;
import github.mri.sample.springboot.rest.exception.EmployeeNotFoundException;
import github.mri.sample.springboot.rest.mapper.EmployeeMapper;
import github.mri.sample.springboot.rest.mapper.EmployeeMapperImpl;
import github.mri.sample.springboot.rest.resource.EmployeeResource;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeMapper resourceMapper = Mappers.getMapper(EmployeeMapper.class);

    // Aggregate root

    @GetMapping("/employees")
    List<EmployeeResource> all() {
        return createResources(repository.findAll());
    }

    @PostMapping("/employees")
    EmployeeResource newEmployee(@RequestBody EmployeeResource newEmployee) {
        Employee employee = resourceMapper.resourceToEntity(newEmployee);
        return createResource(repository.save(employee));
    }

    // Single item

    @GetMapping("/employees/{id}")
    EmployeeResource one(@PathVariable Long id) {
        return createResource(repository.findById(id)
                                      .orElseThrow(() -> new EmployeeNotFoundException(id)));
    }

    @PutMapping("/employees/{id}")
    EmployeeResource replaceEmployee(@RequestBody EmployeeResource newEmployeeResource, @PathVariable Long id) {
        Employee newEmployee = resourceMapper.resourceToEntity(newEmployeeResource);
        return repository.findById(id)
                .map(employeeDb -> {
                    employeeDb.setName(newEmployee.getName());
                    employeeDb.setRole(newEmployee.getRole());
                    return createResource(repository.save(employeeDb));
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return createResource(repository.save(newEmployee));
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

    private EmployeeResource createResource(Employee employee) {
        return resourceMapper.entityToResource(employee);
    }

    private List<EmployeeResource> createResources(List<Employee> employees) {
        return employees.stream().map(resourceMapper::entityToResource).collect(Collectors.toList());
    }
}