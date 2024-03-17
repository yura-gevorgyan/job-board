package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.WorkExperience;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "resume")
@Data
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String phone;
    private String location;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String picName;
    private String description;
    private double expectedSalary;

    @Enumerated(value = EnumType.STRING)
    private WorkExperience workExperience;

    private String profession;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdDate;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

    private boolean isActive;
}
