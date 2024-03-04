package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.JobRepository;
import am.itspace.jobboard.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

}
