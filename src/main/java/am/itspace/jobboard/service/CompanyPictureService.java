package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyPicture;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyPictureService {

    List<CompanyPicture> findAllByCompanyId(int id);

    void addPictures(Company company, MultipartFile[] multipartFiles);

    void update(Company company, MultipartFile[] newPictures, String[] deletedPictures);
}
