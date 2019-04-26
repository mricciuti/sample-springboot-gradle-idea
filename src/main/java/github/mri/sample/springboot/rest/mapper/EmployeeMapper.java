package github.mri.sample.springboot.rest.mapper;

import github.mri.sample.springboot.domain.Employee;
import github.mri.sample.springboot.rest.resource.EmployeeResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmployeeMapper {

    @Mapping(target = "dbId", source = "id")
    EmployeeResource entityToResource(Employee entity);

    @Mapping(target = "id", source = "dbId")
    Employee resourceToEntity(EmployeeResource resource);

}
