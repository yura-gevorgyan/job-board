package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.repository.JobAppliesRepository;
import am.itspace.jobboard.repository.JobRepository;
import am.itspace.jobboard.repository.JobWishlistRepository;
import am.itspace.jobboard.service.CategoryService;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.util.PictureUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobServiceImplTest {

    @InjectMocks
    private JobServiceImpl jobService;

    @Mock
    private SendMailService sendMailService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private PictureUtil pictureUtil;
    @Mock
    private ApplicantListRepository applicantListRepository;
    @Mock
    private JobAppliesRepository jobAppliesRepository;
    @Mock
    private JobWishlistRepository jobWishlistRepository;
    @Mock
    private JobRepository jobRepository;

    @Test
    void testGetJobCount() {
        int expectedCount = 10;
        when(jobRepository.countBy()).thenReturn(expectedCount);

        int actualCount = jobService.getJobCount();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void testGetLast4Jobs() {
        List<Job> expectedLast4Jobs = new ArrayList<>();
        when(jobRepository.findTop4ByOrderByPublishedDateDesc()).thenReturn(expectedLast4Jobs);

        List<Job> actualLast4Jobs = jobService.getLast4Jobs();

        assertEquals(expectedLast4Jobs, actualLast4Jobs);
        verify(jobRepository).findTop4ByOrderByPublishedDateDesc();
    }

    @Test
    void testFindAllJobs() {
        int index = 1;
        Pageable pageable = PageRequest.of(0, 20, Sort.by("publishedDate").ascending());
        List<Job> jobList = new ArrayList<>();
        Page<Job> expectedPage = new PageImpl<>(jobList, pageable, 0);

        when(jobRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Job> actualPage = jobService.findAllJobs(index);

        assertEquals(expectedPage, actualPage);
        verify(jobRepository).findAll(pageable);
    }

    @Test
    void testGetCountByCompanyId() {
        int companyId = 123;
        int expectedCount = 5;
        when(jobRepository.countByCompanyIdAndIsDeletedFalse(companyId)).thenReturn(expectedCount);

        int actualCount = jobService.getCountByCompanyId(companyId);

        assertEquals(expectedCount, actualCount);
        verify(jobRepository).countByCompanyIdAndIsDeletedFalse(companyId);
    }

    @Test
    void testBlockById_ExistingJob() {
        int jobId = 123;
        Job job = new Job();
        job.setId(jobId);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        jobService.blockById(jobId);

        verify(jobAppliesRepository).deleteAllByJobId(jobId);
        verify(jobWishlistRepository).deleteAllByJobId(jobId);
        verify(applicantListRepository).deleteAllByJobId(jobId);
        verify(jobRepository).delete(job);
        verify(sendMailService).sendEmailJobDeleted(job.getUser());
    }

    @Test
    void testUnlockById_ExistingJob() {
        int jobId = 123;
        Job job = new Job();
        job.setId(jobId);
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        jobService.unlockById(jobId);

        assertFalse(job.isDeleted());
        verify(jobRepository).save(job);
        verify(sendMailService).sendEmailJobRecovered(job.getUser());
    }

    @Test
    void testGetAllByCompanyId() {
        int companyId = 123;
        List<Job> expectedJobs = new ArrayList<>();
        when(jobRepository.getAllByCompanyId(companyId)).thenReturn(expectedJobs);

        List<Job> actualJobs = jobService.getAllByCompanyId(companyId);

        assertEquals(expectedJobs, actualJobs);
        verify(jobRepository).getAllByCompanyId(companyId);
    }

    @Test
    void testSave() {
        Job job = new Job();
        when(jobRepository.save(job)).thenReturn(job);

        jobService.save(job);

        verify(jobRepository).save(job);
    }

    @Test
    void testFindAllByIsDeletedFalse() {
        int index = 1;
        Pageable pageable = PageRequest.of(0, 20, Sort.by("publishedDate").ascending());
        List<Job> jobList = new ArrayList<>();
        Page<Job> expectedPage = new PageImpl<>(jobList, pageable, 0);

        when(jobRepository.findByIsDeletedFalse(pageable)).thenReturn(expectedPage);

        Page<Job> actualPage = jobService.findAllByIsDeletedFalse(index);

        assertEquals(expectedPage, actualPage);
        verify(jobRepository).findByIsDeletedFalse(pageable);
    }

    @Test
    void testFindByUserId() {
        int userId = 123;
        List<Job> expectedJobs = new ArrayList<>();
        when(jobRepository.findByUserId(userId)).thenReturn(expectedJobs);

        List<Job> actualJobs = jobService.findByUserId(userId);

        assertEquals(expectedJobs, actualJobs);
        verify(jobRepository).findByUserId(userId);
    }

    @Test
    void testFindTop8ByCompanyId() {
        int companyId = 123;
        List<Job> expectedJobs = new ArrayList<>();
        when(jobRepository.findTop8ByCompanyIdAndIsDeletedFalse(companyId)).thenReturn(expectedJobs);

        List<Job> actualJobs = jobService.findTop8ByCompanyId(companyId);

        assertEquals(expectedJobs, actualJobs);
        verify(jobRepository).findTop8ByCompanyIdAndIsDeletedFalse(companyId);
    }

    @Test
    void testGetJobById_ExistingJob() {
        int jobId = 123;
        Job expectedJob = new Job();
        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(expectedJob));

        Job actualJob = jobService.getJobById(jobId);

        assertEquals(expectedJob, actualJob);
        verify(jobRepository).findByIdAndIsDeletedFalse(jobId);
    }

    @Test
    void testGetJobById_NonExistingJob() {
        int jobId = 456;
        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.empty());

        Job actualJob = jobService.getJobById(jobId);

        assertNull(actualJob);
        verify(jobRepository).findByIdAndIsDeletedFalse(jobId);
    }

    @Test
    void testGetJobById_ExistingJobIsDeletedFalse() {
        int jobId = 456;
        Job job = Job.builder().isDeleted(false).build();
        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(job));

        Job actualJob = jobService.getJobById(jobId);

        assertFalse(actualJob.isDeleted());
        verify(jobRepository).findByIdAndIsDeletedFalse(jobId);
    }

    @Test
    void testCreate() {
        Job job = new Job();
        User user = new User();
        Company company = new Company();

        when(jobRepository.save(job)).thenReturn(job);

        jobService.create(job, user, company);

        assertEquals(user, job.getUser());
        assertNotNull(job.getPublishedDate());
        assertFalse(job.isDeleted());
        assertEquals(company, job.getCompany());
        assertEquals(company.getLogoName(), job.getLogoName());
        verify(jobRepository).save(job);
    }

    @Test
    void testFindAll() {
        int index = 1;
        Pageable pageable = PageRequest.of(0, 20, Sort.by("publishedDate").descending());
        List<Job> jobList = new ArrayList<>();
        Page<Job> expectedPage = new PageImpl<>(jobList, pageable, 0);

        when(jobRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Job> actualPage = jobService.findAll(index);

        assertEquals(expectedPage, actualPage);
        verify(jobRepository).findAll(pageable);
    }

    @Test
    void testFindById_ExistingJob() {
        int jobId = 123;
        Job expectedJob = new Job();
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(expectedJob));

        Job actualJob = jobService.findById(jobId);

        assertEquals(expectedJob, actualJob);
        verify(jobRepository).findById(jobId);
    }

    @Test
    void testFindById_NonExistingJob() {
        int jobId = 456;
        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        Job actualJob = jobService.findById(jobId);

        assertNull(actualJob);
        verify(jobRepository).findById(jobId);
    }

    @Test
    void testFindByIdAndIsDeletedFalse_ExistingJob() {
        int jobId = 123;
        Job expectedJob = Job.builder().isDeleted(true).build();
        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.of(expectedJob));

        Job actualJob = jobService.findByIdAndIsDeletedFalse(jobId);

        assertTrue(actualJob.isDeleted());
        assertEquals(expectedJob, actualJob);
        verify(jobRepository).findByIdAndIsDeletedFalse(jobId);
    }

    @Test
    void testFindByIdAndIsDeletedFalse_NonExistingJob() {
        int jobId = 456;
        when(jobRepository.findByIdAndIsDeletedFalse(jobId)).thenReturn(Optional.empty());

        Job actualJob = jobService.findByIdAndIsDeletedFalse(jobId);

        assertNull(actualJob);
        verify(jobRepository).findByIdAndIsDeletedFalse(jobId);
    }

    @Test
    void testDeleteByid() {
        Job job = new Job();
        jobService.deleteById(job);

        assertTrue(job.isDeleted());
        verify(jobRepository).save(job);
    }

    @Test
    void testRecoverJobById() {
        Job job = new Job();
        job.setDeleted(true);
        jobService.recoverJobById(job);

        assertFalse(job.isDeleted());
        verify(jobRepository).save(job);
    }

    @Test
    void testFindAllByUserId() {
        int index = 1;
        int userId = 123;
        List<Job> jobs = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 20, Sort.by("publishedDate").descending());
        Page<Job> expectedPage = new PageImpl<>(jobs, pageable, 0);

        when(jobRepository.findByUserId(pageable, userId)).thenReturn(expectedPage);

        Page<Job> actualPage = jobService.findAllByUserId(index, userId);

        verify(jobRepository).findByUserId(pageable, userId);

        assertEquals(expectedPage, actualPage);
    }

}