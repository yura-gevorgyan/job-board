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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyWishlistServiceImpl implements CompanyWishlistService {

    private final CompanyWishlistRepository companyWishlistRepository;

    @Override
    public Page<CompanyWishlist> findByUserid(int index, int id) {
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
    public List<CompanyWishlist> findAllByUserid(int id) {
        return companyWishlistRepository.findAllByUserId(id);
    }

}
