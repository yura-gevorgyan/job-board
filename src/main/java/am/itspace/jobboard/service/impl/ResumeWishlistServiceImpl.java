package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.ResumeWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.ResumeWishlistRepository;
import am.itspace.jobboard.service.ResumeWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeWishlistServiceImpl implements ResumeWishlistService {

    private final ResumeWishlistRepository resumeWishlistRepository;

    @Override
    public Page<ResumeWishlist> findByUserId(int index, int id) {
        return resumeWishlistRepository.findAllByUserId(PageRequest.of(index - 1, 20), id);
    }

    @Override
    public void save(Resume resume, User user) {
        ResumeWishlist resumeWishlist = resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId());
        if (resumeWishlist == null) {
            ResumeWishlist build = ResumeWishlist.builder()
                    .resume(resume)
                    .user(user)
                    .build();
            resumeWishlistRepository.save(build);
        }
    }

    @Override
    public void delete(Resume resume, User user) {
        ResumeWishlist resumeWishlist = resumeWishlistRepository.findByResumeIdAndUserId(resume.getId(), user.getId());
        if (resumeWishlist != null) {
            resumeWishlistRepository.delete(resumeWishlist);
        }
    }

    @Override
    public List<ResumeWishlist> findAllByUserId(int id) {
        return resumeWishlistRepository.findByUserId(id);
    }

    @Override
    public void deleteByUserId(int id) {
        resumeWishlistRepository.deleteAll(findAllByUserId(id));
    }

    @Override
    public void deleteByResume(Resume resume) {
        resumeWishlistRepository.deleteAll(findAllByResumeId(resume.getId()));
    }

    private List<ResumeWishlist> findAllByResumeId(int id) {
        return resumeWishlistRepository.findAllByResumeId(id);
    }

}
