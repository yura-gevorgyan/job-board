package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

    int countBy();

    int countByNameContaining(String name);

    int countByCategoryId(int categoryId);

    int countByNameContainingAndCategoryId(String name, int categoryId);

    Page<Company> findAllByCategoryId(PageRequest pageRequest, int categoryId);

    Page<Company> findAllByNameContaining(PageRequest pageRequest, String name);

    Page<Company> findAllByNameContainingAndCategoryId(PageRequest pageRequest, String name, int categoryId);

    Optional<Company> findByUser(User user);

    Optional<Company> findCompanyByUserId(int userId);
}
