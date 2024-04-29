package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum WorkExperience {

    LESS_THAN_1_YEAR("Less than 1 year"), FROM_1_TO_3("1-3 years"), FROM_3_TO_5("3-5 years"), FROM_5_TO_MORE("5 and more years");

    private final String experience;

    WorkExperience(String experience) {
        this.experience = experience;
    }

}
