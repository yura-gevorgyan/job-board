package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.CompanyPicture;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

public class CompanyPictureServiceImplTest {

    @Mock
    private CompanyPictureRepository companyPictureRepository;

    @InjectMocks
    private CompanyPictureServiceImpl companyPictureService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllByCompanyId() {
        when(companyPictureRepository.findAllByCompanyId(anyInt()))
                .thenReturn(Arrays.asList(new CompanyPicture(), new CompanyPicture()));
        List<CompanyPicture> result = companyPictureService.findAllByCompanyId(123);
        assertEquals(2, result.size());
    }
}
