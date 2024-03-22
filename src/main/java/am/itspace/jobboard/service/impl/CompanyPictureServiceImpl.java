package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.CompanyPicture;
import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.service.CompanyPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyPictureServiceImpl implements CompanyPictureService {

    private final CompanyPictureRepository companyPictureRepository;

    @Override
    public List<CompanyPicture> findAllByCompanyId(int id) {
        return companyPictureRepository.findAllByCompanyId(id);
    }

}

