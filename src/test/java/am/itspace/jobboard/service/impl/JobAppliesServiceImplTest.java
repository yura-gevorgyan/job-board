package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobApplies;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.JobAppliesStatus;
import am.itspace.jobboard.repository.JobAppliesRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobAppliesServiceImplTest {

    @InjectMocks
    private JobAppliesServiceImpl jobAppliesService;

    @Mock
    private JobAppliesRepository jobAppliesRepository;

    @Test
    void testSave() {
        Job job = new Job();
        User user = new User();

        when(jobAppliesRepository.save(any(JobApplies.class))).thenReturn(new JobApplies());

        jobAppliesService.save(job, user);

        verify(jobAppliesRepository).save(any(JobApplies.class));
    }

    @Test
    void save_VerifyeJobAppliesAttributes() {
        Job job = new Job();
        User user = new User();

        when(jobAppliesRepository.save(any(JobApplies.class))).thenAnswer(a -> {
            JobApplies savedJobApplies = a.getArgument(0);
            assertNotNull(savedJobApplies);
            assertEquals(job, savedJobApplies.getJob());
            assertEquals(user, savedJobApplies.getToJobSeeker());
            assertNotNull(savedJobApplies.getSendDate());
            assertTrue(savedJobApplies.isActive());
            return savedJobApplies;
        });

        jobAppliesService.save(job, user);

        verify(jobAppliesRepository).save(any(JobApplies.class));
    }

    @Test
    void findById_ExistingJobApplis() {
        int jobAppliesId = 123;
        JobApplies expectedJobApplies = new JobApplies();
        when(jobAppliesRepository.findById(jobAppliesId)).thenReturn(Optional.of(expectedJobApplies));

        JobApplies actualJobApplies = jobAppliesService.findById(jobAppliesId);

        assertEquals(expectedJobApplies, actualJobApplies);
        verify(jobAppliesRepository).findById(jobAppliesId);
    }

    @Test
    void testSave_ForJobApply() {
        JobApplies jobApplies = new JobApplies();

        when(jobAppliesRepository.save(jobApplies)).thenReturn(jobApplies);
        JobApplies savedJobApplies = jobAppliesService.save(jobApplies);

        assertEquals(jobApplies, savedJobApplies);
        verify(jobAppliesRepository).save(jobApplies);
    }

    @Test
    void testChangeStatuseApproved_ExistingJobApplies() {
        int jobAppliesId = 123;
        JobApplies jobApplies = JobApplies.builder().jobAppliesStatus(JobAppliesStatus.REJECTED).build();
        when(jobAppliesRepository.findById(jobAppliesId)).thenReturn(Optional.of(jobApplies));

        jobAppliesService.changeStatusApproved(jobAppliesId);

        assertEquals(JobAppliesStatus.APPROVED, jobApplies.getJobAppliesStatus());
    }

    @Test
    void testChangeStatuseRejected_ExistingJobApplies() {
        int jobAppliesId = 123;
        JobApplies jobApplies = new JobApplies();
        when(jobAppliesRepository.findById(jobAppliesId)).thenReturn(Optional.of(jobApplies));

        jobAppliesService.changeStatusRejected(jobAppliesId);

        assertEquals(JobAppliesStatus.REJECTED, jobApplies.getJobAppliesStatus());
    }

    @Test
    void findAllByToJobSeekerIdAndIsActiveTrue() {
        int jobSeekerId = 123;
        int index = 1;
        Pageable pageable = PageRequest.of(0, 20, Sort.by("sendDate").ascending());
        List<JobApplies> jobAppliesList = new ArrayList<>();
        Page<JobApplies> expectedPage = new PageImpl<>(jobAppliesList, pageable, 0);

        when(jobAppliesRepository.findAllByToJobSeekerIdAndIsActiveTrue(jobSeekerId, pageable)).thenReturn(expectedPage);

        Page<JobApplies> actualPage = jobAppliesService.findAllByToJobSeekerIdAndIsActiveTrue(jobSeekerId, index);

        assertEquals(expectedPage, actualPage);
        verify(jobAppliesRepository).findAllByToJobSeekerIdAndIsActiveTrue(jobSeekerId, pageable);
    }


}