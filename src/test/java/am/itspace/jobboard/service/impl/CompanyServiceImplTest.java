package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.repository.CompanyRepository;
import am.itspace.jobboard.repository.CompanyWishlistRepository;
import am.itspace.jobboard.service.JobService;
import am.itspace.jobboard.service.SendMailService;
import am.itspace.jobboard.util.PictureUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private JobService jobService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Test
    void getCompanyCount() {
        when(companyRepository.countBy()).thenReturn(5);

        int count = companyService.getCompanyCount();

        assertEquals(5, count);
    }

    @Test
    void getTotalPages() {
        when(companyRepository.count()).thenReturn(25L);

        int totalPages = companyService.getTotalPages();

        assertEquals(2, totalPages);
    }

    @Test
    void findAllCompanies() {
        List<Company> companies = Collections.singletonList(new Company());
        Page<Company> page = new PageImpl<>(companies);

        when(companyRepository.findAll((Pageable) any())).thenReturn(page);
        when(jobService.getCountByCompanyId(anyInt())).thenReturn(5);

        Page<Company> result = companyService.findAllCompanies(1);

        assertEquals(companies, result.getContent());
    }

    @Test
    void findCompanyByUserIdAndIsActiveTrue() {
        Company company = new Company();
        when(companyRepository.findCompanyByUserIdAndIsActiveTrue(anyInt())).thenReturn(Optional.of(company));

        Company result = companyService.findCompanyByUserIdAndIsActiveTrue(1);

        assertEquals(company, result);
    }

    @Test
    void findAllByIsActiveTrue() {
        List<Company> companies = Collections.singletonList(new Company());
        Page<Company> page = new PageImpl<>(companies);

        when(companyRepository.findAllByIsActiveTrue(PageRequest.of(0, 20))).thenReturn(page);

        Page<Company> result = companyService.findAllByIsActiveTrue(1);

        assertEquals(companies, result.getContent());
    }

    @Test
    void findById() {
        Company company = new Company();
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(company));

        Company result = companyService.findById(1);

        assertEquals(company, result);
    }

    @Test
    void create() {
        Company company = new Company();
        User user = new User();
        MockMultipartFile logo = new MockMultipartFile("logo", "logo.png", "image/png", new byte[0]);

        when(companyRepository.save(any())).thenReturn(company);

        Company result = companyService.create(new Company(), user, logo);

        assertEquals(company, result);
    }

    @Test
    void update() {
        Company oldCompany = new Company();
        Company newCompany = new Company();
        User companyOwner = new User();
        MockMultipartFile logo = new MockMultipartFile("logo", "logo.png", "image/png", new byte[0]);

        when(jobService.findAllByCompanyIdAndIsDeletedFalse(anyInt())).thenReturn(Collections.singletonList(new Job()));
        when(companyRepository.save(any())).thenReturn(newCompany);

        Company result = companyService.update(oldCompany, newCompany, companyOwner, logo);

        assertEquals(newCompany, result);
    }

    @Test
    void findRandomCompanies() {
        List<Company> companies = Collections.singletonList(new Company());
        when(companyRepository.findRandomCompanies(anyInt())).thenReturn(companies);

        List<Company> result = companyService.findRandomCompanies(5);

        assertEquals(companies, result);
    }
}
