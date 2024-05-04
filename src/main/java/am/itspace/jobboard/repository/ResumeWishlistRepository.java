package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.ResumeWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeWishlistRepository extends JpaRepository<ResumeWishlist, Integer> {

    Page<ResumeWishlist> findAllByUserId(Pageable pageable, int id);

    ResumeWishlist findByResumeIdAndUserId(int resumeId, int userId);

    List<ResumeWishlist> findByUserId(int id);

}
