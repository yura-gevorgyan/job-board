
package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyWishlist;
import am.itspace.jobboard.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CompanyWishlistService {

    Page<CompanyWishlist> findByUserId(int index, int id);

    void save(Company company, User user);

    void delete(Company company, User user);

    List<CompanyWishlist> findAllByUserId(int id);
}
