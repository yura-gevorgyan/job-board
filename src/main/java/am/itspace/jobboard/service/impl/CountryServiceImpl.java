package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Country;
import am.itspace.jobboard.repository.CountryRepository;
import am.itspace.jobboard.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public Country findPhoneCodeById(int id) {
        Country byId = countryRepository.findById(id);
        return byId;
    }
}
