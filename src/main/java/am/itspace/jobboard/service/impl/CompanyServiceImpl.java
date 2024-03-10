package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final JobService jobService;

    @Override
    public int getCompanyCount() {
        return companyRepository.countBy();
    }

    @Override
    public int getTotalPages() {
        int pageSize = 20;
        long totalCount = companyRepository.count();
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public Page<Company> getCompaniesFromNToM(int index) {
        int pageSize = 20;
        Page<Company> all = companyRepository.findAll(PageRequest.of(index - 1, pageSize));
        for (Company company : all) {
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
        }
        return companyRepository.findAll(PageRequest.of(index - 1, pageSize));
    }

    @Override
    public int getTotalPagesOfSearch(int categoryId, String name) {
        int pageSize = 20;
        long totalCount = getCompanyCountOfCategoryName(categoryId, name);
        return (int) Math.ceil((double) totalCount / pageSize);
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
        int pageSize = 20;
        if (categoryId <= 0 && (name == null || name.trim().isEmpty())) {
            return null;
        }
        if (categoryId <= 0) {
            return companyRepository.findAllByNameContaining(PageRequest.of(index - 1, pageSize), name);
        }
        if (name == null || name.trim().isEmpty()) {
            return companyRepository.findAllByCategoryId(PageRequest.of(index - 1, pageSize), categoryId);
        }
        return companyRepository.findAllByNameContainingAndCategoryId(PageRequest.of(index - 1, pageSize), name, categoryId);
    }

}
