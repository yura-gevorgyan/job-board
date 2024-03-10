package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

    @Override
    public int getResumeCount() {
        return resumeRepository.countBy();
    }

    @Override
    public List<Resume> getLast6Resumes() {
        return resumeRepository.findTop6ByOrderByCreatedDateDesc();
    }

    @Override
    public int getTotalPages() {
        int pageSize = 20;
        long totalCount = resumeRepository.count();
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public Page<Resume> getResumesFromNToM(int index) {
        int pageSize = 20;
        return resumeRepository.findAll(PageRequest.of(index - 1, pageSize).withSort(Sort.by("createdDate")));
    }

    @Override
    public int getTotalPagesOfSearch(int categoryId, String userEmail) {
        int pageSize = 20;
        long totalCount = getResumeCountOfCategoryUserEmail(categoryId, userEmail);
        return (int) Math.ceil((double) totalCount / pageSize);
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
        int pageSize = 20;
        if (categoryId <= 0 && (userEmail == null || userEmail.trim().isEmpty())) {
            return null;
        }
        if (categoryId <= 0) {
            return resumeRepository.findAllByUserEmailContaining(PageRequest.of(index - 1, pageSize), userEmail);
        }
        if (userEmail == null || userEmail.trim().isEmpty()) {
            return resumeRepository.findAllByCategoryId(PageRequest.of(index - 1, pageSize), categoryId);
        }
        return resumeRepository.findAllByUserEmailContainingAndCategoryId(PageRequest.of(index - 1, pageSize), userEmail, categoryId);
    }


}
