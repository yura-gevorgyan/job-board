package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyService {

    int getCompanyCount();

    int getTotalPages();

    Page<Company> getCompaniesFromNToM(int index);

    int getTotalPagesOfSearch(int categoryId, String name);

    int getCompanyCountOfCategoryName(int categoryId, String name);

    Page<Company> getCompaniesFromNToMForSearch(int index, int categoryId, String name);

    Company findCompanyByUserIdAndIsActiveTrue(int userId);

    void deleteById(int id);

    Page<Company> findAllByIsActiveTrue(int index);

    Page<Company> findAll(Specification<Company> specification, int index);

    Company findById(int id);

    Company create(Company company, User user, MultipartFile logo);

    Company update(Company oldCompany, Company newCompany, User companyOwner, MultipartFile logo);

}

