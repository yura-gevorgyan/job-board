package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.service.ApplicantListService;
import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ApplicantListServiceImpl implements ApplicantListService {

    private final ApplicantListRepository applicantListRepository;

    private final int PAGE_SIZE = 20;

    @Override
    public ApplicantList findByEmployerIdAndResumeId(int employerId, int resumeId) {
        return applicantListRepository.findByToEmployerIdAndResumeIdAndIsActiveTrue(employerId, resumeId).orElse(null);
    }

    @Override
    public ApplicantList findByJobIdAndResumeId(int jobId, int resumeId) {
        return applicantListRepository.findByJobIdAndResumeIdAndIsActiveTrue(jobId, resumeId).orElse(null);
    }

    @Override
    public void save(Job job, Resume resume) {
        ApplicantList applicantList = new ApplicantList();

        applicantList.setSendDate(new Date());
        applicantList.setResume(resume);
        applicantList.setToEmployer(job.getUser());
        applicantList.setActive(true);
        applicantList.setApplicantListStatus(ApplicantListStatus.WAITING);
        applicantList.setJob(job);

        applicantListRepository.save(applicantList);
    }

    @Override
    public void deleteById(int id) {
        applicantListRepository.deleteById(id);
    }

    @Override
    public Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(int resumeId, int index) {
        return applicantListRepository.findAllByResumeIdAndIsActiveTrue(resumeId, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("sendDate")));
    }

    @Override
    public Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(Specification<ApplicantList> specification, int index, int resumeId) {
        return applicantListRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("sendDate")));
    }


    @Override
    public Page<ApplicantList> findAll(Specification<ApplicantList> specification, int index) {
        return applicantListRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("sendDate")));
    }
}
