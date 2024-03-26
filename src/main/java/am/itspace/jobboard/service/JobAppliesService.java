package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;

public interface JobAppliesService {

    JobApplies findByJobIdAndUserIdAndIsActiveTrue(int jobId,int userId);

    void save(Job job, User user);

}

