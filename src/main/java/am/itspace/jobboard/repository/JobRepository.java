package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {


}
