package am.itspace.jobboard.entity;

import jakarta.persistence.*;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;

    @ManyToOne
    @JoinColumn(name = "to_employer_id")
    private User toEmployer;

    @ManyToOne
    private Resume resume;

    private boolean isActive;

}
