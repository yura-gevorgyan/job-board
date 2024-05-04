package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.JobWishlistRepository;
import am.itspace.jobboard.service.JobWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobWishlistServiceImpl implements JobWishlistService {

    private final JobWishlistRepository jobWishlistRepository;

    @Override
    public Page<JobWishlist> findByUserid(int index, int id) {
        return jobWishlistRepository.findAllByUserId(PageRequest.of(index - 1,20),id);
    }

    @Override
    public List<JobWishlist> findAllByUserid(int id) {
        return jobWishlistRepository.findByUserId(id);
    }

    @Override
    public void save(Job job, User user) {
        JobWishlist jobWishlist = jobWishlistRepository.findByJobIdAndUserId(job.getId(), user.getId());
        if (jobWishlist == null){
            JobWishlist build = JobWishlist.builder()
                    .job(job)
                    .user(user)
                    .build();
            jobWishlistRepository.save(build);
        }
    }

    @Override
    public void delete(Job job, User user) {
        JobWishlist jobWishlist = jobWishlistRepository.findByJobIdAndUserId(job.getId(), user.getId());
        if (jobWishlist != null){
            jobWishlistRepository.delete(jobWishlist);
        }
    }
}
