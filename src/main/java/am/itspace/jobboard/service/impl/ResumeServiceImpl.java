package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.ResumeRepository;
import am.itspace.jobboard.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;

}
