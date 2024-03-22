package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.CompanyPictureRepository;
import am.itspace.jobboard.service.CompanyPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyPictureServiceImpl implements CompanyPictureService {

    private final CompanyPictureRepository companyPictureRepository;

}

