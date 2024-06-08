package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.repository.ResumeWishlistRepository;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.util.PictureUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeServiceImplTest {

    @Mock
    private ResumeRepository resumeRepository;
    @InjectMocks
    private ResumeServiceImpl resumeService;
    @Mock
    private ApplicantListRepository applicantListRepository;
    @Mock
    private SendMailService sendMailService;
    @Mock
    private ResumeWishlistRepository resumeWishlistRepository;
    @Mock
    private PictureUtil pictureUtil;
    @Mock
    private CategoryService categoryService;

    @Mock
    private UserService userService;

    private Resume resume;
    private Resume updatedResume;
    private List<Resume> resumes;
    private MultipartFile multipartFile;
    private final int PAGE_SIZE = 20;
    private PageImpl<Resume> pageResumes;

    @BeforeEach
    public void setUp() {
        resume = new Resume();
        resume.setId(1);
        resume.setUser(new User());
        resume.setCategory(new Category());
        resume.setPicName("original_pic.jpg");
        resume.setActive(true);

        updatedResume = new Resume();
        updatedResume.setId(1);
        updatedResume.setUser(new User());
        updatedResume.setPicName("updated_pic.jpg");
        updatedResume.setCreatedDate(resume.getCreatedDate());
        updatedResume.setCategory(new Category());
        updatedResume.setActive(true);

        multipartFile = mock(MultipartFile.class);

        resumes = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Resume res = new Resume();
            res.setId(i + 1);
            resumes.add(res);
        }
        pageResumes = new PageImpl<>(resumes);
    }

    @Test
    public void testGetResumeCount() {
        when(resumeRepository.countBy()).thenReturn(10);

        int result = resumeService.getResumeCount();

        assertEquals(10, result);
        verify(resumeRepository, times(1)).countBy();
    }

    @Test
    public void testFindByUserIdAndIsActiveTrueFound() {
        when(resumeRepository.findByUserIdAndIsActiveTrue(1)).thenReturn(Optional.of(resume));

        Resume result = resumeService.findByUserIdAndIsActiveTrue(1);

        assertEquals(resume, result);
        verify(resumeRepository, times(1)).findByUserIdAndIsActiveTrue(1);
    }

    @Test
    public void testFindByUserIdAndIsActiveTrueNotFound() {
        when(resumeRepository.findByUserIdAndIsActiveTrue(1)).thenReturn(Optional.empty());

        Resume result = resumeService.findByUserIdAndIsActiveTrue(1);

        assertNull(result);
        verify(resumeRepository, times(1)).findByUserIdAndIsActiveTrue(1);
    }

    @Test
    public void testGetLast6Resumes() {
        when(resumeRepository.findTop6ByOrderByCreatedDateDesc()).thenReturn(resumes);

        List<Resume> result = resumeService.getLast6Resumes();

        assertEquals(resumes.size(), result.size());
        assertEquals(resumes, result);
        verify(resumeRepository, times(1)).findTop6ByOrderByCreatedDateDesc();
    }

    @Test
    public void testGetTotalPagesWithNoResumes() {
        when(resumeRepository.count()).thenReturn(0L);

        int result = resumeService.getTotalPages();

        assertEquals(0, result);
        verify(resumeRepository, times(1)).count();
    }

    @Test
    public void testGetTotalPagesWithSomeResumes() {
        when(resumeRepository.count()).thenReturn(45L);

        int result = resumeService.getTotalPages();

        assertEquals(3, result);
        verify(resumeRepository, times(1)).count();
    }

    @Test
    public void testGetTotalPagesWithExactPageSizeMultiple() {
        when(resumeRepository.count()).thenReturn(40L);

        int result = resumeService.getTotalPages();

        assertEquals(2, result);
        verify(resumeRepository, times(1)).count();
    }

    @Test
    public void testGetTotalPagesWithLessThanPageSize() {
        when(resumeRepository.count()).thenReturn(15L);

        int result = resumeService.getTotalPages();

        assertEquals(1, result);
        verify(resumeRepository, times(1)).count();
    }

    @Test
    public void testFindAllResumes() {
        int index = 1;
        PageRequest pageRequest = PageRequest.of(index - 1, PAGE_SIZE, Sort.by("createdDate"));

        when(resumeRepository.findAll(any(PageRequest.class))).thenReturn(pageResumes);

        Page<Resume> result = resumeService.findAllResumes(index);

        assertEquals(pageResumes, result);
        verify(resumeRepository, times(1)).findAll(pageRequest);
    }

    @Test
    public void testDeleteByIdResumeExists() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.of(resume));

        resumeService.deleteById(id);

        verify(applicantListRepository, times(1)).deleteAllByResumeId(id);
        verify(resumeWishlistRepository, times(1)).deleteAllByResumeId(id);
        verify(sendMailService, times(1)).sendEmailResumeDeleted(resume.getUser());
        verify(resumeRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteByIdResumeDoesNotExist() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.empty());

        resumeService.deleteById(id);

        verify(applicantListRepository, times(0)).deleteAllByResumeId(id);
        verify(resumeWishlistRepository, times(0)).deleteAllByResumeId(id);
        verify(sendMailService, times(0)).sendEmailResumeDeleted(any(User.class));
        verify(resumeRepository, times(0)).deleteById(id);
    }

    @Test
    public void testFindAllByActiveTrue() {
        int index = 1;
        PageRequest pageRequest = PageRequest.of(index - 1, PAGE_SIZE, Sort.by("createdDate"));

        when(resumeRepository.findAllByIsActiveTrue(any(PageRequest.class))).thenReturn(pageResumes);

        Page<Resume> result = resumeService.findAllByActiveTrue(index);

        assertEquals(pageResumes, result);
        verify(resumeRepository, times(1)).findAllByIsActiveTrue(pageRequest);
    }

    @Test
    public void testFindAll_withSpecification() {
        int searchIndex = 1;
        Specification<Resume> specification = (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
        PageRequest pageRequest = PageRequest.of(searchIndex - 1, PAGE_SIZE, Sort.by("createdDate"));

        when(resumeRepository.findAll(any(Specification.class), eq(pageRequest))).thenReturn(pageResumes);

        Page<Resume> result = resumeService.findAll(specification, searchIndex);

        assertEquals(pageResumes, result);
        verify(resumeRepository, times(1)).findAll(any(Specification.class), eq(pageRequest));
    }

    @Test
    public void testGetByIdResumeExists() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.of(resume));

        Resume result = resumeService.getById(id);

        assertEquals(resume, result);
        verify(resumeRepository, times(1)).findById(id);
    }

    @Test
    public void testGetByIdResumeDoesNotExist() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.empty());

        Resume result = resumeService.getById(id);

        assertNull(result);
        verify(resumeRepository, times(1)).findById(id);
    }

    @Test
    public void testFindTop6() {
        when(resumeRepository.findRandomResumes(6)).thenReturn(resumes);

        List<Resume> result = resumeService.findTop6();

        assertEquals(resumes.size(), result.size());
        assertEquals(resumes, result);
        verify(resumeRepository, times(1)).findRandomResumes(6);
    }

    @Test
    public void testUpdateResumeNotExists() {
        when(resumeRepository.findByIdAndIsActiveTrue(resume.getId())).thenReturn(Optional.empty());

        Resume result = resumeService.update(updatedResume, multipartFile);

        assertNull(result);
        verifyNoInteractions(categoryService, userService, pictureUtil, multipartFile);
        verify(resumeRepository, times(0)).save(any(Resume.class));
    }

    @Test
    public void testFindByIdResumeExists() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.of(resume));

        Resume result = resumeService.findById(id);

        assertNotNull(result);
        assertEquals(resume, result);
        verify(resumeRepository, times(1)).findById(id);
    }

    @Test
    public void testFindByIdResumeDoesNotExist() {
        int id = 1;
        when(resumeRepository.findById(id)).thenReturn(Optional.empty());

        Resume result = resumeService.findById(id);

        assertNull(result);
        verify(resumeRepository, times(1)).findById(id);
    }
}
