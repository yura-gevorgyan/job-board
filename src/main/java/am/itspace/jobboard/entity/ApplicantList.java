package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "applicant_list")
@Data
public class ApplicantList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Past(message = "Invalid date")
    @NotNull(message = "Birth date can not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;

    @NotNull(message = "User can not be null!")
    @ManyToOne
    @JoinColumn(name = "to_employer_id")
    private User toEmployer;

    @NotNull(message = "Resume can not be null!")
    @ManyToOne
    private Resume resume;

    private boolean isActive;

    @NotNull(message = "ApplicantListStatus can not be null!")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicantListStatus applicantListStatus;

    @NotNull(message = "Job can not be null!")
    @ManyToOne
    private Job job;
}
