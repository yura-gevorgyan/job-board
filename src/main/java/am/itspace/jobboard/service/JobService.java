package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface JobService {

    int getJobCount();

    List<Job> getLast4Jobs();

    Page<Job> getJobsFromNToM(int index);

    int getTotalPages();

    int getTotalPagesOfSearch(String title);

    int getJobCountOfTitle(String title);

    Page<Job> getJobsFromNToMForSearch(int index, String title);

    int getCountByCompanyId(int id);

    void blockById(int id);

    void unlockById(int id);

    List<Job> getAllByCompanyId(int id);

    void save(Job job);

    List<Job> findTop6();

    Page<Job> findAllByIsDeletedFalse(int index);

    Page<Job> findAll(Specification<Job> specification, int index,int page);

    List<Job> findByUserId(int id);

    List<Job> findTop8ByCompanyId(int id);

    Job getJobById(int id);

    List<Job> findByUserIdAndIsDeletedFalse(int id);

    void create(Job job, User user, Company company);

    Page<Job> findAll(int index);

    Job findById(int id);

    void update(Job job, Job oldJob);

    void deleteById(Job job);

    void recoverJobById(Job job);

    Page<Job> findAllByUserId(int index,int userId);
}

