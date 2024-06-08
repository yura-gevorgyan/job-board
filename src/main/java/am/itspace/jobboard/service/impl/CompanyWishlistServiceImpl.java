package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.CompanyWishlistRepository;
import am.itspace.jobboard.service.CompanyWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyWishlistServiceImpl implements CompanyWishlistService {

    private final CompanyWishlistRepository companyWishlistRepository;

    @Override
    public Page<CompanyWishlist> findByUserId(int index, int id) {
        return companyWishlistRepository.findAllByUserId(PageRequest.of(index - 1, 20), id);
    }

    @Override
    public void save(Company company, User user) {
        CompanyWishlist companyAndUser = companyWishlistRepository.findByCompanyAndUser(company, user);
        if (companyAndUser == null) {
            CompanyWishlist companyWishlist = CompanyWishlist.builder()
                    .company(company)
                    .user(user)
                    .build();
            companyWishlistRepository.save(companyWishlist);
        }
    }

    @Override
    public void delete(Company company, User user) {
        CompanyWishlist companyAndUser = companyWishlistRepository.findByCompanyAndUser(company, user);
        if (companyAndUser != null) {
            companyWishlistRepository.delete(companyAndUser);
        }
    }

    @Override
    public List<CompanyWishlist> findAllByUserId(int id) {
        return companyWishlistRepository.findAllByUserId(id);
    }

    @Override
    public void deleteByUserId(int id) {
        companyWishlistRepository.deleteAll(findAllByUserId(id));
    }

    @Override
    public void deleteByCompany(Company company) {
        companyWishlistRepository.deleteAll(findAllByCompanyId(company.getId()));
    }

    private List<CompanyWishlist> findAllByCompanyId(int id) {
        return companyWishlistRepository.findAllByCompanyId(id);
    }

}
