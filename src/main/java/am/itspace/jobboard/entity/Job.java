package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.util.UploadAble;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job")
@Data
@EqualsAndHashCode
public class Job implements UploadAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Title is required.")
    @Length(max = 30)
    private String title;

    private String logoName;

    @PastOrPresent(message = "Published date must be today or in the past.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;

    @Enumerated(EnumType.STRING)
    private WorkExperience workExperience;

    @ManyToOne
    private Country country;

    @Min(value = 0, message = "Salary must be at least 0.")
    @Max(value = 100000000, message = "Salary must be at most 10000000.")
    @Digits(integer = 10, fraction = 0, message = "Expected salary must be numeric")
    private double salary;

    @NotBlank(message = "Description is required.")
    @Length(max = 4000)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Company company;

    private boolean isDeleted;

    @Override
    public void setPicName(String picName) {
        this.logoName = picName;
    }
}
