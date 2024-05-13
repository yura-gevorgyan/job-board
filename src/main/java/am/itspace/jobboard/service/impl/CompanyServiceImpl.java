package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.repository.CompanyWishlistRepository;
import am.itspace.jobboard.service.CompanyService;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.util.PictureUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PictureUtil pictureUtil;
    private final CompanyWishlistRepository companyWishlistRepository;

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
    public Page<Company> findAllCompanies(int index) {
        Page<Company> companies = companyRepository.findAll(PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("foundedDate")));
        for (Company company : companies) {
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
        }
        return companies;
    }

    @Override
    public Page<Company> findAllCompanies(Specification<Company> companySpecification, int index) {
        Page<Company> companies = companyRepository.findAll(companySpecification, PageRequest.of(index - 1, PAGE_SIZE).withSort(Sort.by("foundedDate")));
        for (Company company : companies) {
            company.setActiveJobs(jobService.getCountByCompanyId(company.getId()));
        }
        return companies;
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
            companyWishlistRepository.deleteAllByCompanyId(company.getId());
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
        pictureUtil.processImageUpload(company, logo, fileName);

        return companyRepository.save(company);
    }

    @Override
    @SneakyThrows
    public Company update(Company oldCompany, Company newCompany, User companyOwner, MultipartFile logo) {

        newCompany.setId(oldCompany.getId());
        newCompany.setActive(true);
        newCompany.setUser(companyOwner);

        if (!logo.isEmpty() && logo.getSize() > 1 && logo.getOriginalFilename() != null
                && !logo.getOriginalFilename().isBlank()
                && !newCompany.getLogoName().equals(logo.getOriginalFilename())) {

            PictureUtil.deletePicture(fileName, oldCompany.getLogoName());
            pictureUtil.processImageUpload(newCompany, logo, fileName);

        } else {
            newCompany.setLogoName(oldCompany.getLogoName());
        }

        if (newCompany.equals(oldCompany)) {
            return oldCompany;
        }

        List<Job> jobs = jobService.findAllByCompanyIdAndIsDeletedFalse(newCompany.getId());
        jobs.parallelStream()
                .forEach(job -> {
                    job.setLogoName(newCompany.getLogoName());
                    jobService.save(job);
                });

        return companyRepository.save(newCompany);
    }

    @Override
    public List<Company> findRandomCompanies(int limit) {
        return companyRepository.findRandomCompanies(limit);
    }
}
