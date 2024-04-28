package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyPicture;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    @Value("${program.pictures.file.path}")
    private String fileName;

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

    @Override
    @SneakyThrows
    public Company create(Company company, User user, MultipartFile logo) {
        company.setUser(user);
        company.setActive(true);
        String logoName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
        logo.transferTo(new File(fileName, logoName));
        company.setLogoName(logoName);

        return companyRepository.save(company);
    }

    @Override
    @SneakyThrows
    public Company update(Company oldCompany, Company newCompany, User companyOwner, MultipartFile logo) {
        if (!logo.isEmpty() && logo.getSize() > 1 && logo.getOriginalFilename() != null && !logo.getOriginalFilename().isBlank()) {
            String logoName = System.currentTimeMillis() + "_" + logo.getOriginalFilename();
            logo.transferTo(new File(fileName, logoName));
            newCompany.setLogoName(logoName);
        } else {
            newCompany.setLogoName(oldCompany.getLogoName());
        }

        newCompany.setId(oldCompany.getId());
        newCompany.setActive(true);
        newCompany.setUser(companyOwner);

        return companyRepository.save(newCompany);
    }


}
