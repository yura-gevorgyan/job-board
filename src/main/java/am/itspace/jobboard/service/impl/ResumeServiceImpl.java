package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.ResumeService;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.PictureUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final SendMailService sendMailService;
    private final CategoryService categoryService;
    private final UserService userService;

    private final int PAGE_SIZE = 20;

    @Value("${program.pictures.file.path}")
    private String uploadDirectoryResume;

    @Override
    public int getResumeCount() {
        return resumeRepository.countBy();
    }

    @Override
    public Resume findByUserIdAndIsActiveTrue(int userId) {
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

    @SneakyThrows
    @Override
    public Resume create(Resume resume, MultipartFile multipartFile) {
        PictureUtil.processImageUploadResume(resume, multipartFile, uploadDirectoryResume);
        resume.setCreatedDate(new Date());
        resume.setActive(true);
        return resumeRepository.save(resume);
    }

    @SneakyThrows
    @Override
    public Resume update(Resume resume, MultipartFile multipartFile) {
        Optional<Resume> byId = resumeRepository.findByIdAndIsActiveTrue(resume.getId());
        if (byId.isPresent()) {

            Resume originalResume = byId.get();
            resume.setCategory(categoryService.findById(resume.getCategory().getId()));
            resume.setUser(userService.findByIdAndIsActiveTrue(resume.getUser().getId()));
            resume.setCreatedDate(originalResume.getCreatedDate());
            resume.setActive(true);

            if (!multipartFile.isEmpty() && !resume.getPicName().equals(multipartFile.getOriginalFilename())) {
                PictureUtil.deletePicture(uploadDirectoryResume, originalResume.getPicName());
                PictureUtil.processImageUploadResume(resume, multipartFile, uploadDirectoryResume);
            } else {
                resume.setPicName(originalResume.getPicName());
            }

            if (resume.equals(originalResume)){
                return originalResume;
            }
        }
        return resumeRepository.save(resume);
    }

    @Override
    public Resume findById(int id) {
        return resumeRepository.findById(id).orElse(null);
    }
}
