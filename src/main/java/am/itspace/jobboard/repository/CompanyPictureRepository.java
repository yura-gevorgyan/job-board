package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.CompanyPicture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyPictureRepository extends JpaRepository<CompanyPicture, Integer> {

    void deleteAllByCompanyId(int id);

}
