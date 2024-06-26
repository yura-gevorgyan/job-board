package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.CompanyPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyPictureRepository extends JpaRepository<CompanyPicture, Integer> {

    void deleteAllByCompanyId(int id);

    List<CompanyPicture> findAllByCompanyId(int id);

    void deleteByName(String name);

}
