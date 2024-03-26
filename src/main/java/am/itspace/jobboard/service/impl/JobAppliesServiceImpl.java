package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.JobAppliesRepository;
import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JobAppliesServiceImpl implements JobAppliesService {

    private final JobAppliesRepository jobAppliesRepository;


    @Override
    public JobApplies findByJobIdAndUserIdAndIsActiveTrue(int jobId, int userId) {
        return jobAppliesRepository.findByJobIdAndToJobSeekerIdAndIsActiveTrue(jobId,userId).orElse(null);
    }

    @Override
    public void save(Job job, User user) {
        JobApplies jobApplies = new JobApplies();
        jobApplies.setJob(job);
        jobApplies.setToJobSeeker(user);
        jobApplies.setSendDate(new Date());
        jobApplies.setActive(true);
        jobAppliesRepository.save(jobApplies);
    }
}

