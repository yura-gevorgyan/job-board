package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Company;
import am.itspace.jobboard.entity.CompanyPicture;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.service.CompanyPictureService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyPictureServiceImpl implements CompanyPictureService {

    @Value("${program.pictures.file.path}")
    private String fileName;

    private final CompanyPictureRepository companyPictureRepository;
    private final TaskExecutor taskExecutor;

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

    @Override
    public void deleteByCompanyId(int id) {
        companyPictureRepository.deleteAllByCompanyId(id);
    }

    public void savePictures(Company company, MultipartFile[] newPictures) {
        Arrays.stream(newPictures).parallel().forEach(companyPicture -> {
            if (!companyPicture.isEmpty() && companyPicture.getSize() > 1
                    && companyPicture.getOriginalFilename() != null
                    && !companyPicture.getOriginalFilename().isBlank()) {
                try {
                    String picture = System.currentTimeMillis() + "_" + companyPicture.getOriginalFilename();
                    File file = new File(fileName, picture);
                    companyPicture.transferTo(file);

                    if (companyPicture.getSize() > 2097152) {
                        reduceImageQualityAsync(file);
                    }

                    companyPictureRepository.save(CompanyPicture.builder()
                            .name(picture)
                            .company(company)
                            .build());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Async
    public void reduceImageQualityAsync(File file) {
        taskExecutor.execute(() -> {
            try {
                BufferedImage originalImage = ImageIO.read(file);
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                BufferedImage resizedImage = new BufferedImage(originalImage.getWidth() / 2, originalImage.getHeight() / 2, type);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(originalImage, 0, 0, originalImage.getWidth() / 2, originalImage.getHeight() / 2, null);
                g.dispose();

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", os);

                ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
                BufferedImage compressedImage = ImageIO.read(is);

                ImageIO.write(compressedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}