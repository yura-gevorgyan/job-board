package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {

    int getResumeCount();

    Resume findByUserIdAndIsActiveTrue(int userId);

    List<Resume> getLast6Resumes();

    int getTotalPages();

    Page<Resume> findAllResumes(int index);

    Page<Resume> findAllResumes(Specification<Resume> resumeSpecification, int index);

    void deleteById(int id);

    Page<Resume> findAllByActiveTrue(int index);

    Page<Resume> findAll(Specification<Resume> resumeSpecification, int searchIndex);

    Resume getById(int id);

    List<Resume> findTop6();

    Resume create(Resume resume, MultipartFile multipartFile);

    Resume update(Resume resume, MultipartFile multipartFile);

    Resume findById(int id);
}
