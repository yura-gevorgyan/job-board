package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Job;
import org.springframework.data.domain.Page;

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

}
