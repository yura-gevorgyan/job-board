package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final CompanyPictureRepository companyPictureRepository;

    private final JobService jobService;

    private final SendMailService sendMailService;

    private static final int PAGE_SIZE = 20;

    @Override
    public int getCompanyCount() {
        return companyRepository.countBy();
    }

    @Override
    public int getTotalPages() {
        long totalCount = companyRepository.count();
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public Page<Company> getCompaniesFromNToM(int index) {
        Page<Company> all = companyRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE));
        for (Company company : all) {
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
        }
        return all;
    }

    @Override
    public int getTotalPagesOfSearch(int categoryId, String name) {
        long totalCount = getCompanyCountOfCategoryName(categoryId, name);
        return (int) Math.ceil((double) totalCount / PAGE_SIZE);
    }

    @Override
    public int getCompanyCountOfCategoryName(int categoryId, String name) {
        if (categoryId <= 0 && (name == null || name.trim().isEmpty())) {
            return 0;
        }
        if (categoryId <= 0) {
            return companyRepository.countByNameContaining(name);
        }
        if (name == null || name.trim().isEmpty()) {
            return companyRepository.countByCategoryId(categoryId);
        }
        return companyRepository.countByNameContainingAndCategoryId(name, categoryId);
    }

    @Override
    public Page<Company> getCompaniesFromNToMForSearch(int index, int categoryId, String name) {
        if (categoryId <= 0 && (name == null || name.trim().isEmpty())) {
            return null;
        }
        if (categoryId <= 0) {
            Page<Company> all = companyRepository.findAllByNameContaining(PageRequest.of(index - 1, PAGE_SIZE), name);
            for (Company company : all) {
                company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
            }
            return all;
        }
        if (name == null || name.trim().isEmpty()) {
            Page<Company> all = companyRepository.findAllByCategoryId(PageRequest.of(index - 1, PAGE_SIZE), categoryId);
            for (Company company : all) {
                company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
            }
            return all;
        }
        Page<Company> all = companyRepository.findAllByNameContainingAndCategoryId(PageRequest.of(index - 1, PAGE_SIZE), name, categoryId);
        for (Company company : all) {
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
        }
        return all;
    }

    @Override
    public Company findCompanyByUserIdAndIsActiveTrue(int userId) {
        return companyRepository.findCompanyByUserIdAndIsActiveTrue(userId).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isPresent()) {
            Company company = byId.get();
            List<Job> allByCompanyId = jobService.getAllByCompanyId(company.getId());
            for (Job job : allByCompanyId) {
                job.setCompany(null);
                jobService.save(job);
            }
            companyPictureRepository.deleteAllByCompanyId(company.getId());
            sendMailService.sendEmailCompanyDeleted(company.getUser());
            companyRepository.delete(company);
        }
    }

    @Override
    public Page<Company> findAllByIsActiveTrue(int index) {
        return companyRepository.findAllByIsActiveTrue(PageRequest.of(index - 1, PAGE_SIZE));
    }

    @Override
    public Page<Company> findAll(Specification<Company> specification, int index) {
        return companyRepository.findAll(specification, PageRequest.of(index - 1, PAGE_SIZE));
    }

    @Override
    public Company findById(int id) {
        return companyRepository.findById(id).orElse(null);
    }


}
