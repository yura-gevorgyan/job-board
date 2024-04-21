package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum JobAppliesStatus {

    TOTAL("Total"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String name;

    JobAppliesStatus(String name) {
        this.name = name;
    }
}
