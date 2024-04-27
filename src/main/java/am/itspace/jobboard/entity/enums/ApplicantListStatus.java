package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum ApplicantListStatus {

    WAITING("Waiting"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String name;

    ApplicantListStatus(String name) {
        this.name = name;
    }
}
