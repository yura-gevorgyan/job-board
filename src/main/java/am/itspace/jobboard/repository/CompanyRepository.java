package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {


}
