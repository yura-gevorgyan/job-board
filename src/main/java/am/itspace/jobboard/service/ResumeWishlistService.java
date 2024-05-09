package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.ResumeWishlist;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ResumeWishlistService {

    Page<ResumeWishlist> findByUserId(int index, int id);

    void save(Resume resume, User user);

    void delete(Resume resume, User user);

    List<ResumeWishlist> findAllByUserId(int id);
}
