package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.security.SpringUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    List<Job> findAllByCompanyIdAndIsDeletedFalse(int companyId);

    void create(Job job, User user, Company company);

    Page<Job> findAll(int index);

    Job findById(int id);

    Job findByIdAndIsDeletedFalse(int id);

    Job updateForEmployee(Job job, Job oldJob, int categoryId, String statusStr, String experienceStr, MultipartFile multipartFile);

    Job updateForCompanyOwner(Job job,  int categoryId, String statusStr, String experienceStr);

    void deleteById(Job job);

    void recoverJobById(Job job);

    void createJobForCompanyOwner(Company company, Job job, User user, String categoryIdStr, String statusStr, String experienceStr) throws Exception;

    void createJobForEmployee(Job job, User user, String categoryIdStr, String statusStr, String experienceStr, MultipartFile multipartFile);

    Page<Job> findAllByUserId(int index,int userId);
}
