package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.JobAppliesStatus;
import am.itspace.jobboard.repository.JobAppliesRepository;
import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobAppliesServiceImpl implements JobAppliesService {

    private final JobAppliesRepository jobAppliesRepository;

    private final int PAGE_SIZE = 20;

    @Override
    public JobApplies findByJobIdAndUserIdAndIsActiveTrue(int jobId, int userId) {
        return jobAppliesRepository.findByJobIdAndToJobSeekerIdAndIsActiveTrue(jobId, userId).orElse(null);
    }

    @Override
    public void save(Job job, User user) {
        JobApplies jobApplies = new JobApplies();
        jobApplies.setJob(job);
        jobApplies.setToJobSeeker(user);
        jobApplies.setSendDate(new Date());
        jobApplies.setActive(true);
        jobApplies.setJobAppliesStatus(JobAppliesStatus.TOTAL);
        jobAppliesRepository.save(jobApplies);
    }

    @Override
    public JobApplies findById(int id) {
        return jobAppliesRepository.findById(id).orElse(null);
    }

    @Override
    public JobApplies save(JobApplies jobApplies) {
        return jobAppliesRepository.save(jobApplies);
    }

    @Override
    public void deleteById(int jobAppliesId) {
        jobAppliesRepository.deleteById(jobAppliesId);
    }

    @Override
    public void changeStatusApproved(int id) {
        Optional<JobApplies> byId = jobAppliesRepository.findById(id);
        byId.ifPresent(jobApplies -> {
            jobApplies.setJobAppliesStatus(JobAppliesStatus.APPROVED);
            save(jobApplies);
        });
    }

    @Override
    public void changeStatusRejected(int id) {
        Optional<JobApplies> byId = jobAppliesRepository.findById(id);
        byId.ifPresent(jobApplies -> {
            jobApplies.setJobAppliesStatus(JobAppliesStatus.REJECTED);
            save(jobApplies);
        });
    }

    @Override
    public Page<JobApplies> findAllByToJobSeekerIdAndIsActiveTrue(int jobSeekerId, int index) {
        return jobAppliesRepository.findAllByToJobSeekerIdAndIsActiveTrue(jobSeekerId, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("sendDate")));
    }

    @Override
    public Page<JobApplies> findAll(Specification<JobApplies> specification, int index) {
        return jobAppliesRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("sendDate")));
    }
}
