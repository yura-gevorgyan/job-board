package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Integer> {

    Country findById(int id);

}
