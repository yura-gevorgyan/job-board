package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"),
    JOB_SEEKER("Job Seeker"),
    EMPLOYEE("Employee"),
    COMPANY_OWNER("Company Owner");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
