package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.MessageRepository;
import am.itspace.jobboard.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

}
