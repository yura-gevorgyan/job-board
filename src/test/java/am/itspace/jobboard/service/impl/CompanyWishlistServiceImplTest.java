package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyWishlist;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.CompanyWishlistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyWishlistServiceImplTest {
    @Mock
    private CompanyWishlistRepository companyWishlistRepository;

    @InjectMocks
    private CompanyWishlistServiceImpl companyWishlistService;


    @Test
    void testFindByUserId() {
        int index = 1;
        int id = 123;
        Pageable pageable = PageRequest.of(0, 20);
        List<CompanyWishlist> wishlist = List.of(new CompanyWishlist(), new CompanyWishlist());
        Page<CompanyWishlist> expectedPage = new PageImpl<>(wishlist);

        when(companyWishlistRepository.findAllByUserId(pageable, id)).thenReturn(expectedPage);

        Page<CompanyWishlist> actualPage = companyWishlistService.findByUserId(index, id);

        assertEquals(expectedPage, actualPage);
        verify(companyWishlistRepository).findAllByUserId(pageable, id);
    }

    @Test
    void save_NewWishlistEntry() {
        Company company = new Company();
        User user = new User();
        when(companyWishlistRepository.findByCompanyAndUser(company, user)).thenReturn(null);

        companyWishlistService.save(company, user);

        verify(companyWishlistRepository).save(any(CompanyWishlist.class));
    }

    @Test
    void save_ExistingWishlistEntry() {
        Company company = new Company();
        User user = new User();
        CompanyWishlist existingWishlist = new CompanyWishlist();
        when(companyWishlistRepository.findByCompanyAndUser(company, user)).thenReturn(existingWishlist);

        companyWishlistService.save(company, user);

        verify(companyWishlistRepository, never()).save(any(CompanyWishlist.class));
    }

    @Test
    void delete_ExistingWishlistEntry() {
        Company company = new Company();
        User user = new User();
        CompanyWishlist existingWishlist = new CompanyWishlist();

        when(companyWishlistRepository.findByCompanyAndUser(company, user)).thenReturn(existingWishlist);
        companyWishlistService.delete(company, user);

        verify(companyWishlistRepository).delete(existingWishlist);
    }

    @Test
    void delete_NonExistingWishlistEntry() {
        Company company = new Company();
        User user = new User();

        when(companyWishlistRepository.findByCompanyAndUser(company, user)).thenReturn(null);
        companyWishlistService.delete(company, user);

        verify(companyWishlistRepository, never()).delete(any(CompanyWishlist.class));
    }

    @Test
    void testFindAllByUserId() {
        int userId = 123;
        List<CompanyWishlist> expectedWishlist = List.of(new CompanyWishlist(), new CompanyWishlist());

        when(companyWishlistRepository.findAllByUserId(userId)).thenReturn(expectedWishlist);
        List<CompanyWishlist> actualWishlist = companyWishlistService.findAllByUserId(userId);

        assertEquals(expectedWishlist, actualWishlist);
        verify(companyWishlistRepository).findAllByUserId(userId);
    }


}