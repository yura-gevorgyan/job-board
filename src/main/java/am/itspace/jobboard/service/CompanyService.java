package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CompanyService {

    int getCompanyCount();

    int getTotalPages();

    Page<Company> getCompaniesFromNToM(int index);

    int getTotalPagesOfSearch(int categoryId, String name);

    int getCompanyCountOfCategoryName(int categoryId, String name);

    Page<Company> getCompaniesFromNToMForSearch(int index, int categoryId, String name);
    Company findCompanyByUserId(int userId);

    void deleteById(int id);

}

