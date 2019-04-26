package github.mri.sample.springboot.dao;

import github.mri.sample.springboot.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
