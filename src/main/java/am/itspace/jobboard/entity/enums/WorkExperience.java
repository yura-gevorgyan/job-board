package am.itspace.jobboard.entity.enums;

import lombok.Getter;

@Getter
public enum WorkExperience {

    LESS_THAN_1_YEAR("Less Than 1 Year"), FROM_1_TO_3("From 1 to 3"), FROM_3_TO_5("From 3 to 5"), FROM_5_TO_MORE("From 5 to More");

    private final String experience;

    WorkExperience(String experience) {
        this.experience = experience;
    }

}
