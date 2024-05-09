package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobWishlistService {

    Page<JobWishlist> findByUserId(int index, int id);

    List<JobWishlist> findAllByUserId(int id);

    void save(Job job, User user);

    void delete(Job job, User user);

}
