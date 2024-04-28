package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.JobRepository;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final SendMailService sendMailService;

    private final int PAGE_SIZE = 20;

    @Override
    public int getJobCount() {
        return jobRepository.countBy();
    }

    @Override
    public List<Job> getLast4Jobs() {
        return jobRepository.findTop4ByOrderByPublishedDateDesc();
    }


    //Page size configuration
    @Override
    public Page<Job> getJobsFromNToM(int index) {
        return jobRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("publishedDate")));
    }

    @Override
    public int getTotalPages() {
        long totalCount = jobRepository.count();
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public int getTotalPagesOfSearch(String title) {
        long totalCount = jobRepository.countByTitleContaining(title);
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public int getJobCountOfTitle(String title) {
        return jobRepository.countByTitleContaining(title);
    }

    @Override
    public Page<Job> getJobsFromNToMForSearch(int index, String title) {
        return jobRepository.findAllByTitleContaining(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("publishedDate")), title);
    }

    @Override
    public int getCountByCompanyId(int id) {
        return jobRepository.countByCompanyIdAndIsDeletedFalse(id);
    }

    @Override
    @Transactional
    public void blockById(int id) {
        Optional<Job> byId = jobRepository.findById(id);
        if (byId.isPresent()) {
            Job job = byId.get();
            jobRepository.delete(job);
            sendMailService.sendEmailJobDeleted(job.getUser());
        }
    }

    @Override
    @Async
    public void unlockById(int id) {
        Optional<Job> byId = jobRepository.findById(id);
        if (byId.isPresent()) {
            Job job = byId.get();
            job.setDeleted(false);
            jobRepository.save(job);
            sendMailService.sendEmailJobRecovered(job.getUser());
        }
    }

    @Override
    public List<Job> getAllByCompanyId(int id) {
        return jobRepository.getAllByCompanyId(id);
    }

    @Override
    public void save(Job job) {
        jobRepository.save(job);
    }

    @Override
    public List<Job> findTop6() {
        return jobRepository.findRandomJobs(6);
    }

    @Override
    public Page<Job> findAllByIsDeletedFalse(int index) {
        return jobRepository.findByIsDeletedFalse(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("publishedDate")));
    }

    @Override
    public Page<Job> findAll(Specification<Job> specification, int index) {
        return jobRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("publishedDate")));
    }

    @Override
    public List<Job> findByUserId(int id) {
        return jobRepository.findByUserId(id);
    }

    @Override
    public List<Job> findTop8ByCompanyId(int id) {
        return jobRepository.findTop8ByCompanyIdAndIsDeletedFalse(id);
    }

    @Override
    public Job getJobById(int id) {
        return jobRepository.findByIdAndIsDeletedFalse(id).orElse(null);
    }

    @Override
    public List<Job> findByUserIdAndIsDeletedFalse(int id) {
        return jobRepository.findAllByUserIdAndIsDeletedFalse(id);
    }

    @Override
    public void create(Job job, User user, Company company) {

        job.setUser(user);
        job.setPublishedDate(new Date());
        job.setDeleted(false);
        job.setCompany(company);
        job.setLogoName(company.getLogoName());

        jobRepository.save(job);
    }

    @Override
    public Page<Job> findAll(int index) {
        return jobRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("publishedDate").descending()));
    }

    @Override
    public Job findById(int id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public void update(Job job, Job oldJob) {

        job.setUser(oldJob.getUser());
        job.setPublishedDate(oldJob.getPublishedDate());
        job.setDeleted(oldJob.isDeleted());
        job.setCompany(oldJob.getCompany());
        job.setLogoName(oldJob.getLogoName());

        jobRepository.save(job);
    }

    @Override
    public void deleteById(Job job) {
        job.setDeleted(true);
        jobRepository.save(job);
    }

    @Override
    public void recoverJobById(Job job) {
        job.setDeleted(false);
        jobRepository.save(job);
    }
}

