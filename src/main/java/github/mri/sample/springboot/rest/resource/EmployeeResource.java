package github.mri.sample.springboot.rest.resource;

import lombok.Data;

@Data
public class EmployeeResource {

    private Long dbId;
    private String name;
    private String role;

}
