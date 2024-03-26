package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    private final SendMailService sendMailService;

    private final int PAGE_SIZE = 20;

    @Override
    public int getResumeCount() {
        return resumeRepository.countBy();
    }

    @Override
    public Resume findByUserId(int userId) {
        return resumeRepository.findByUserIdAndIsActiveTrue(userId).orElse(null);
    }

    @Override
    public List<Resume> getLast6Resumes() {
        return resumeRepository.findTop6ByOrderByCreatedDateDesc();
    }

    @Override
    public int getTotalPages() {
        long totalCount = resumeRepository.count();
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public Page<Resume> getResumesFromNToM(int index) {
        return resumeRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("createdDate")));
    }

    @Override
    public int getTotalPagesOfSearch(int categoryId, String userEmail) {
        long totalCount = getResumeCountOfCategoryUserEmail(categoryId, userEmail);
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public int getResumeCountOfCategoryUserEmail(int categoryId, String userEmail) {
        if (categoryId <= 0 && (userEmail == null || userEmail.trim().isEmpty())) {
            return 0;
        }
        if (categoryId <= 0) {
            return resumeRepository.countByUserEmailContaining(userEmail);
        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return resumeRepository.countByCategoryId(categoryId);
        }
        return resumeRepository.countByUserEmailContainingAndCategoryId(userEmail, categoryId);
    }

    @Override
    public Page<Resume> getResumesFromNToMForSearch(int index, int categoryId, String userEmail) {
        if (categoryId <= 0 && (userEmail == null || userEmail.trim().isEmpty())) {
            return null;
        }
        if (categoryId <= 0) {
            return resumeRepository.findAllByUserEmailContaining(PageRequest.of(index - 1, PAGE_SIZE), userEmail);
        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return resumeRepository.findAllByCategoryId(PageRequest.of(index - 1, PAGE_SIZE), categoryId);
        }
        return resumeRepository.findAllByUserEmailContainingAndCategoryId(PageRequest.of(index - 1, PAGE_SIZE), userEmail, categoryId);
    }

    @Override
    public void deleteById(int id) {
        Optional<Resume> byId = resumeRepository.findById(id);
        if (byId.isPresent()) {
            sendMailService.sendEmailResumeDeleted(byId.get().getUser());
            resumeRepository.deleteById(id);
        }
    }

    @Override
    public Page<Resume> findAllByActiveTrue(int index) {
        return resumeRepository.findAllByIsActiveTrue(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("createdDate")));
    }

    @Override
    public Page<Resume> findAll(Specification<Resume> resumeSpecification, int searchIndex) {
        return resumeRepository.findAll(resumeSpecification, PageRequest.of(searchIndex - 1, PAGE_SIZE).withSort(Sort.by("createdDate")));
    }

    @Override
    public Resume getById(int id) {
        return resumeRepository.findById(id).orElse(null);
    }

    @Override
    public List<Resume> findTop6() {
        return resumeRepository.findRandomResumes(6);
    }

}

