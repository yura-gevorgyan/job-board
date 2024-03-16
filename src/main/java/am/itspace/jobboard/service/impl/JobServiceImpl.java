package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.repository.JobRepository;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    private final SendMailService sendMailService;

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
        int pageSize = 20;
        return jobRepository.findAll(PageRequest.of(index - 1, pageSize).withSort(Sort.by("publishedDate")));
    }

    @Override
    public int getTotalPages() {
        int pageSize = 20;
        long totalCount = jobRepository.count();
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public int getTotalPagesOfSearch(String title) {
        int pageSize = 20;
        long totalCount = jobRepository.countByTitleContaining(title);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public int getJobCountOfTitle(String title) {
        return jobRepository.countByTitleContaining(title);
    }

    @Override
    public Page<Job> getJobsFromNToMForSearch(int index, String title) {
        int pageSize = 20;
        return jobRepository.findAllByTitleContaining(PageRequest.of(index - 1, pageSize).withSort(Sort.by("publishedDate")), title);
    }

    @Override
    public int getCountByCompanyId(int id) {
        return jobRepository.countByCompanyId(id);
    }

    @Override
    @Async
    public void blockById(int id) {
        Optional<Job> byId = jobRepository.findById(id);
        if (byId.isPresent()) {
            Job job = byId.get();
            job.setDeleted(true);
            jobRepository.save(job);
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
}
