package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_applies")
@Data
public class JobApplies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;

    @ManyToOne
    private Job job;

    @ManyToOne
    @JoinColumn(name = "to_job_seeker_id")
    private User toJobSeeker;

}
