package am.itspace.jobboard.entity;

import am.itspace.jobboard.entity.enums.JobAppliesStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "job_applies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobApplies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Past(message = "Invalid date")
    @NotNull(message = "Birth date can not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;

    @NotNull(message = "Job can not be null!")
    @ManyToOne
    private Job job;

    @NotNull(message = "User can not be null!")
    @ManyToOne
    @JoinColumn(name = "to_job_seeker_id")
    private User toJobSeeker;

    private boolean isActive;

    @NotNull(message = "JobAppliesStatus can not be null!")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private JobAppliesStatus jobAppliesStatus;
}
