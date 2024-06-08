package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.JobWishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobWishlistServiceImplTest {
    @InjectMocks
    private JobWishlistServiceImpl jobWishlistService;
    @Mock
    private JobWishlistRepository jobWishlistRepository;

    @Test
    void testFindByUserId() {
        int index = 1;
        int userId = 123;
        List<JobWishlist> wishlists = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 20);
        Page<JobWishlist> expectedPage = new PageImpl<>(wishlists, pageable, 0);

        when(jobWishlistRepository.findAllByUserId(pageable, userId)).thenReturn(expectedPage);

        Page<JobWishlist> actualPage = jobWishlistService.findByUserId(index, userId);

        verify(jobWishlistRepository).findAllByUserId(pageable, userId);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testFindAllByUserId() {
        int userId = 123;
        List<JobWishlist> wishlists = new ArrayList<>();
        when(jobWishlistRepository.findByUserId(userId)).thenReturn(wishlists);

        List<JobWishlist> actualWishlists = jobWishlistService.findAllByUserId(userId);

        verify(jobWishlistRepository).findByUserId(userId);

        assertEquals(wishlists, actualWishlists);
    }

    @Test
    void testSave() {
        Job job = Job.builder().id(123).build();
        User user = User.builder().id(456).build();
        when(jobWishlistRepository.findByJobIdAndUserId(job.getId(), user.getId())).thenReturn(null);

        jobWishlistService.save(job, user);

        verify(jobWishlistRepository).findByJobIdAndUserId(job.getId(), user.getId());
        verify(jobWishlistRepository).save(any(JobWishlist.class));
    }

    @Test
    void testDelete() {
        Job job = Job.builder().id(123).build();
        User user = User.builder().id(456).build();
        JobWishlist jobWishlist = new JobWishlist();
        when(jobWishlistRepository.findByJobIdAndUserId(job.getId(), user.getId())).thenReturn(jobWishlist);

        jobWishlistService.delete(job, user);

        verify(jobWishlistRepository).findByJobIdAndUserId(job.getId(), user.getId());
        verify(jobWishlistRepository).delete(jobWishlist);
    }

    @Test
    void testDeleteByUserId() {
        int userId = 123;
        List<JobWishlist> jobWishlists = new ArrayList<>();
        when(jobWishlistService.findAllByUserId(userId)).thenReturn(jobWishlists);

        jobWishlistService.deleteByUserId(userId);

        verify(jobWishlistRepository).deleteAll(jobWishlists);
    }

    @Test
    void testDeleteByJob() {
        Job job = Job.builder().id(123).build();
        List<JobWishlist> jobWishlists = new ArrayList<>();
        when(jobWishlistRepository.findAllByJobId(job.getId())).thenReturn(jobWishlists);

        jobWishlistService.deleteByJob(job);

        verify(jobWishlistRepository).deleteAll(jobWishlists);
    }


}