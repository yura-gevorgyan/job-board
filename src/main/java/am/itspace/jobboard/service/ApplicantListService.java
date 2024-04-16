package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface ApplicantListService {

    ApplicantList findByEmployerIdAndResumeId(int employerId, int resumeId);

    ApplicantList findByJobIdAndResumeId(int jobId, int resumeId);

    Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(int resumeId, int index);

    Page<ApplicantList> findAllByResumeIdAndIsActiveTrue(Specification<ApplicantList> specification, int index, int resumeId);

    Page<ApplicantList> findAll(Specification<ApplicantList> specification, int index);

    void save(Job job, Resume resume);

    void deleteById(int id);
}

