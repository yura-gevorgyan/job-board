package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.JobAppliesRepository;
import am.itspace.jobboard.service.JobAppliesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobAppliesServiceImpl implements JobAppliesService {

    private final JobAppliesRepository jobAppliesRepository;

}
