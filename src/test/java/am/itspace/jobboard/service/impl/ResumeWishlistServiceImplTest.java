package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.ResumeWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.ResumeWishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResumeWishlistServiceImplTest {

    @Mock
    private ResumeWishlistRepository resumeWishlistRepository;

    @InjectMocks
    private ResumeWishlistServiceImpl resumeWishlistService;

    private List<ResumeWishlist> wishlistItems;

    @BeforeEach
    public void setUp() {
        wishlistItems = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ResumeWishlist wishlistItem = new ResumeWishlist();
            wishlistItem.setId(i + 1);
            wishlistItems.add(wishlistItem);
        }
    }

    @Test
    public void testFindByUserId() {
        int index = 1;
        int userId = 123;
        PageRequest pageRequest = PageRequest.of(index - 1, 20);

        Page<ResumeWishlist> pageResult = new PageImpl<>(wishlistItems, pageRequest, wishlistItems.size());

        when(resumeWishlistRepository.findAllByUserId(pageRequest, userId)).thenReturn(pageResult);

        Page<ResumeWishlist> result = resumeWishlistService.findByUserId(index, userId);

        assertNotNull(result);
        assertEquals(wishlistItems.size(), result.getContent().size());
        assertEquals(pageResult.getTotalElements(), result.getTotalElements());
        assertEquals(pageResult.getTotalPages(), result.getTotalPages());
        verify(resumeWishlistRepository, times(1)).findAllByUserId(pageRequest, userId);
    }

    @Test
    public void testSaveWishlistItemDoesNotExist() {
        Resume resume = new Resume();
        resume.setId(1);
        User user = new User();
        user.setId(1);

        when(resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId())).thenReturn(null);

        resumeWishlistService.save(resume, user);

        verify(resumeWishlistRepository, times(1)).findByResumeIdAndUserId(resume.getId(), user.getId());
        verify(resumeWishlistRepository, times(1)).save(any(ResumeWishlist.class));
    }

    @Test
    public void testSaveWishlistItemExists() {
        Resume resume = new Resume();
        resume.setId(1);
        User user = new User();
        user.setId(1);

        ResumeWishlist existingWishlistItem = new ResumeWishlist();
        existingWishlistItem.setId(1);

        when(resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId())).thenReturn(existingWishlistItem);

        resumeWishlistService.save(resume, user);

        verify(resumeWishlistRepository, times(1)).findByResumeIdAndUserId(resume.getId(), user.getId());
        verify(resumeWishlistRepository, times(0)).save(any(ResumeWishlist.class));
    }

    @Test
    public void testDeleteWishlistItemExists() {
        Resume resume = new Resume();
        resume.setId(1);
        User user = new User();
        user.setId(1);

        ResumeWishlist existingWishlistItem = new ResumeWishlist();
        existingWishlistItem.setId(1);

        when(resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId())).thenReturn(existingWishlistItem);

        resumeWishlistService.delete(resume, user);

        verify(resumeWishlistRepository, times(1)).findByResumeIdAndUserId(resume.getId(), user.getId());
        verify(resumeWishlistRepository, times(1)).delete(existingWishlistItem);
    }

    @Test
    public void testDeleteWishlistItemDoesNotExist() {
        Resume resume = new Resume();
        resume.setId(1);
        User user = new User();
        user.setId(1);

        when(resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId())).thenReturn(null);

        resumeWishlistService.delete(resume, user);

        verify(resumeWishlistRepository, times(1)).findByResumeIdAndUserId(resume.getId(), user.getId());
        verify(resumeWishlistRepository, times(0)).delete(any(ResumeWishlist.class));
    }

    @Test
    public void testFindAllByUserId() {
        int userId = 123;

        when(resumeWishlistRepository.findByUserId(userId)).thenReturn(wishlistItems);

        List<ResumeWishlist> result = resumeWishlistService.findAllByUserId(userId);

        assertNotNull(result);
        assertEquals(wishlistItems.size(), result.size());
        assertEquals(wishlistItems, result);
        verify(resumeWishlistRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testDeleteByResume() {
        int resumeId = 123;

        when(resumeWishlistRepository.findAllByResumeId(resumeId)).thenReturn(wishlistItems);

        Resume resume = new Resume();
        resume.setId(resumeId);

        resumeWishlistService.deleteByResume(resume);

        verify(resumeWishlistRepository, times(1)).findAllByResumeId(resumeId);
        verify(resumeWishlistRepository, times(1)).deleteAll(wishlistItems);
    }

    @Test
    public void testFindAllByResumeId() {
        int resumeId = 122;

        when(resumeWishlistRepository.findAllByResumeId(resumeId)).thenReturn(wishlistItems);

        List<ResumeWishlist> result = resumeWishlistRepository.findAllByResumeId(resumeId);

        assertNotNull(result);
        assertEquals(wishlistItems.size(), result.size());
        assertEquals(wishlistItems, result);
        verify(resumeWishlistRepository, times(1)).findAllByResumeId(resumeId);
    }
}