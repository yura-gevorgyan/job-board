package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;

public interface ApplicantListService {

    ApplicantList findByEmployerIdAndResumeId(int employerId, int resumeId);


    void save(Job job, Resume resume);
}

