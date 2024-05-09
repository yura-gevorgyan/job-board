package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.JobWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobWishlistRepository extends JpaRepository<JobWishlist, Integer> {

    Page<JobWishlist> findAllByUserId(Pageable pageable, int id);

    List<JobWishlist> findByUserId(int id);

    JobWishlist findByJobIdAndUserId(int jobId,int userId);

}
