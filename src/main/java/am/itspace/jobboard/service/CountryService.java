package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Country;

import java.util.List;

public interface CountryService {

    List<Country> findAll();

    Country findPhoneCodeById(int id);

}
