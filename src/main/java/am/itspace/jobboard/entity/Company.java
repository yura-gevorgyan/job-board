package am.itspace.jobboard.entity;

import am.itspace.jobboard.util.UploadAble;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "company")
@Data
@EqualsAndHashCode
public class Company implements UploadAble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name is required.")
    @Length(max = 25)
    private String name;

    private String logoName;

    @Length(max = 4000)
    private String description;

    @ManyToOne
    private Country country;

    @Past(message = "Founded Date must contain a past date.")
    @NotNull(message = "Founded date can not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundedDate;

    @Pattern(regexp = "^[\\d\\-+.]*$", message = "Phone can contain only digits, dashes, plus signs, and dots.")
    @Length(max = 40)
    private String phone;

    @Length(max = 100)
    private String website;

    @Email(message = "Email must have right format.")
    @Length(max = 30)
    @NotBlank(message = "Email is required.")
    private String email;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category category;

    @Transient
    private int activeJobs;

    private boolean isActive;

    @Override
    public void setPicName(String picName) {
        this.logoName = picName;
    }
}
