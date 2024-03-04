package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.ApplicantListRepository;
import am.itspace.jobboard.service.ApplicantListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantListServiceImpl implements ApplicantListService {

    private final ApplicantListRepository applicantListRepository;

}
