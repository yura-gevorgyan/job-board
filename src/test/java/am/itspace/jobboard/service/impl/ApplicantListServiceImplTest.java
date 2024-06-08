package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.ApplicantList;
import am.itspace.jobboard.entity.Category;
import am.itspace.jobboard.entity.Country;
import am.itspace.jobboard.entity.Job;
import am.itspace.jobboard.entity.Resume;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.entity.enums.ApplicantListStatus;
import am.itspace.jobboard.entity.enums.Gender;
import am.itspace.jobboard.entity.enums.Role;
import am.itspace.jobboard.entity.enums.Status;
import am.itspace.jobboard.entity.enums.WorkExperience;
import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.service.ApplicantListService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ApplicantListServiceImplTest {

    @MockBean
    private ApplicantListRepository applicantListRepository;

    @Autowired
    private ApplicantListService applicantListService;

    private ApplicantList applicantList;
    private Job job;
    private Resume resume;
    private User userJobSeeker;
    private User userEmployer;

    @BeforeEach
    void setUp() {
        userJobSeeker = User.builder()
                .id(1)
                .name("Poxos")
                .surname("Poxosyan")
                .email("poxos@gmail.com")
                .password("posxos1977")
                .token("54321")
                .activated(true)
                .isDeleted(false)
                .role(Role.JOB_SEEKER)
                .registerDate(new Date())
                .build();

        userEmployer = User.builder()
                .id(2)
                .name("Petros")
                .surname("Petrosyna")
                .email("petros@gmail.com")
                .password("petros1977")
                .token("54312")
                .activated(true)
                .isDeleted(false)
                .role(Role.EMPLOYEE)
                .registerDate(new Date())
                .build();

        Country country = Country.builder()
                .id(1)
                .countryName("Armenia")
                .phoneCode("+374")
                .build();

        job = Job.builder()
                .id(1)
                .title("Developer")
                .logoName("gfrt56")
                .publishedDate(new Date())
                .workExperience(WorkExperience.FROM_3_TO_5)
                .country(country)
                .salary(500)
                .description("Hello")
                .status(Status.FULL_TIME)
                .user(userEmployer)
                .isDeleted(false)
                .build();

        resume = Resume.builder()
                .id(1)
                .birthDate(new Date())
                .country(country)
                .gender(Gender.MALE)
                .description("Experienced Developer")
                .expectedSalary(50000.0)
                .workExperience(WorkExperience.FROM_3_TO_5)
                .profession("Software Developer")
                .category(Category.builder().id(1).name("IT").build())
                .user(userJobSeeker)
                .isActive(true)
                .build();

        applicantList = ApplicantList.builder()
                .id(1)
                .sendDate(new Date())
                .toEmployer(userEmployer)
                .resume(resume)
                .isActive(true)
                .applicantListStatus(ApplicantListStatus.WAITING)
                .job(job)
                .build();
    }

    @Test
    void findByEmployerIdAndResumeId() {
        when(applicantListRepository.findByToEmployerIdAndResumeIdAndIsActiveTrue(2, 1))
                .thenReturn(Optional.of(applicantList));

        ApplicantList result = applicantListService.findByEmployerIdAndResumeId(2, 1);
        assertNotNull(result);
        assertEquals(applicantList, result);

        verify(applicantListRepository, times(1))
                .findByToEmployerIdAndResumeIdAndIsActiveTrue(2, 1);
    }

    @Test
    void findByJobIdAndResumeId() {
        when(applicantListRepository.findByJobIdAndResumeIdAndIsActiveTrue(1, 1))
                .thenReturn(Optional.of(applicantList));

        ApplicantList result = applicantListService.findByJobIdAndResumeId(1, 1);
        assertNotNull(result);
        assertEquals(applicantList, result);

        verify(applicantListRepository, times(1))
                .findByJobIdAndResumeIdAndIsActiveTrue(1, 1);
    }

    @Test
    void save() {
        when(applicantListRepository.save(any(ApplicantList.class))).thenReturn(applicantList);
        applicantListService.save(job, resume);
        verify(applicantListRepository, times(1)).save(any(ApplicantList.class));
    }

    @Test
    void deleteById() {
        applicantListService.deleteById(1);
        verify(applicantListRepository, times(1)).deleteById(1);
    }

    @Test
    void changeStatusApproved() {
        when(applicantListRepository.findById(1)).thenReturn(Optional.of(applicantList));
        applicantListService.changeStatusApproved(1);
        assertEquals(ApplicantListStatus.APPROVED, applicantList.getApplicantListStatus());
        verify(applicantListRepository, times(1)).findById(1);
    }

    @Test
    void changeStatusRejected() {
        when(applicantListRepository.findById(1)).thenReturn(Optional.of(applicantList));
        applicantListService.changeStatusRejected(1);
        assertEquals(ApplicantListStatus.REJECTED, applicantList.getApplicantListStatus());
        verify(applicantListRepository, times(1)).findById(1);
    }
}
