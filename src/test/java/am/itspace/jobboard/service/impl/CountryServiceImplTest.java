package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Country;
import am.itspace.jobboard.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceImplTest {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryRepository countryRepository;

    @Test
    void testFindAll() {
        List<Country> expectedCountries = List.of(new Country(), new Country());
        when(countryRepository.findAll()).thenReturn(expectedCountries);

        List<Country> actualCountries = countryService.findAll();

        assertEquals(expectedCountries, actualCountries);
        verify(countryRepository).findAll();
    }

    @Test
    void testFindPhoneCodeById() {
        int countryId = 123;
        Country expectedCountry = new Country();
        when(countryRepository.findById(countryId)).thenReturn(expectedCountry);

        Country actualCountry = countryService.findPhoneCodeById(countryId);

        assertEquals(expectedCountry, actualCountry);
        verify(countryRepository).findById(countryId);
    }

}