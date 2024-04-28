package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum Status {

    PART_TIME("Part Time"), FULL_TIME("Full Time"), FREELANCE("Freelance");

    private final String name;

    Status(String name) {
        this.name = name;
    }

}
