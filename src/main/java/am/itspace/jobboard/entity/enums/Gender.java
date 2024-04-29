package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum Gender {

    MALE("Male"),
    FEMALE("Female");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

}
