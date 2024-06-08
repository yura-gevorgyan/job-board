package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyWishlist;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyWishlistRepository extends JpaRepository<CompanyWishlist, Integer> {

    Page<CompanyWishlist> findAllByUserId(Pageable pageable, int id);

    CompanyWishlist findByCompanyAndUser(Company company, User user);

    List<CompanyWishlist> findAllByUserId(int id);

    void deleteAllByCompanyId(int id);

    List<CompanyWishlist> findAllByCompanyId(int id);
}
