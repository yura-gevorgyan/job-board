package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String logoName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishedDate;

    @Enumerated(EnumType.STRING)
    private WorkExperience workExperience;

    private String location;
    private double salary;
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

}
