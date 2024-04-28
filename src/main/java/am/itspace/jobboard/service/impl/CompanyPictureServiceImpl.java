package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyPicture;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.service.CompanyPictureService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyPictureServiceImpl implements CompanyPictureService {

    @Value("${program.pictures.file.path}")
    private String fileName;


    private final CompanyPictureRepository companyPictureRepository;

    @Override
    public List<CompanyPicture> findAllByCompanyId(int id) {
        return companyPictureRepository.findAllByCompanyId(id);
    }

    @Override
    @SneakyThrows
    public void addPictures(Company company, MultipartFile[] multipartFiles) {
        savePictures(company, multipartFiles);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void update(Company company, MultipartFile[] newPictures, String[] deletedPictures) {
        savePictures(company, newPictures);

        for (String deletedPicture : deletedPictures) {
            companyPictureRepository.deleteByName(deletedPicture);
        }

    }

    private void savePictures(Company company, MultipartFile[] newPictures) throws IOException {
        for (MultipartFile companyPicture : newPictures) {
            if (!companyPicture.isEmpty() && companyPicture.getSize() > 1 && companyPicture.getOriginalFilename() != null && !companyPicture.getOriginalFilename().isBlank()) {
                String picture = System.currentTimeMillis() + "_" + companyPicture.getOriginalFilename();
                companyPicture.transferTo(new File(fileName, picture));
                companyPictureRepository.save(CompanyPicture.builder()
                        .name(picture)
                        .company(company)
                        .build());
            }
        }
    }

}

