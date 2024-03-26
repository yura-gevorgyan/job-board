package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ApplicantListServiceImpl implements ApplicantListService {

    private final ApplicantListRepository applicantListRepository;

    @Override
    public ApplicantList findByEmployerIdAndResumeId(int employerId, int resumeId) {
        return applicantListRepository.findByToEmployerIdAndResumeIdAndIsActiveTrue(employerId, resumeId).orElse(null);
    }

    @Override
    public void save(Job job, Resume resume) {
        ApplicantList applicantList = new ApplicantList();
        applicantList.setSendDate(new Date());
        applicantList.setResume(resume);
        applicantList.setToEmployer(job.getUser());
        applicantList.setActive(true);
        applicantListRepository.save(applicantList);
    }
}

