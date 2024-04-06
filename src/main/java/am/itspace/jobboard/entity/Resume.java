package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.WorkExperience;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "resume")
@Data
@EqualsAndHashCode
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Past(message = "Invalid date")
    @NotNull(message = "Birth date can not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Size(max = 50, message = "Profession length must be less than or equal to 50 characters")
    private String phone;

    @Size(max = 50, message = "Profession length must be less than or equal to 50 characters")
    private String location;

    @NotNull(message = "Gender can not be null")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String picName;

    @Size(max = 1000, message = "Description length must be less than or equal to 1000 characters")
    private String description;

    @NotNull(message = "Expected Salary can not be null")
    @Min(value = 0, message = "Expected salary must be positive")
    @Digits(integer = 10, fraction = 0, message = "Expected salary must be numeric")
    private Double expectedSalary;

    @NotNull(message = "Work Experience can not be null")
    @Enumerated(value = EnumType.STRING)
    private WorkExperience workExperience;

    @Size(max = 50, message = "Profession length must be less than or equal to 100 characters")
    private String profession;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;

    @NotNull(message = "Category can not be null")
    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private boolean isActive;
}
