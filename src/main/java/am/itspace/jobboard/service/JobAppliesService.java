package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface JobAppliesService {

    JobApplies findByJobIdAndUserIdAndIsActiveTrue(int jobId,int userId);

    Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(int jobSeekerId, int index);

    Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(Specification<JobApplies> specification,  int index);

    Page<JobApplies> findAll(Specification<JobApplies> specification, int index);

    JobApplies findById(int id);

    JobApplies save(JobApplies jobApplies);

    void save(Job job, User user);

    void deleteById(int jobAppliesId);

    void changeStatusApproved(int id);

    void changeStatusRejected(int id);
}
