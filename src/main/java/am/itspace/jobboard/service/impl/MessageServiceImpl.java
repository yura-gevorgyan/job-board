package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.ChatNotification;
import am.itspace.jobboard.entity.Message;
import am.itspace.jobboard.repository.MessageRepository;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.ChatroomService;
import am.itspace.jobboard.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final ChatroomService chatroomService;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @Override
    public Message save(ChatNotification chatNotification) {
        var chatroomId = chatroomService.getChatroomId(
                chatNotification.getSenderId(),
                chatNotification.getRecipientId(),
                true
        ).orElseThrow();
        return messageRepository.save(Message.builder()
                .chatId(chatroomId)
                .messageDate(new Date())
                .toUser(userRepository.findById(chatNotification.getRecipientId()).get())
                .fromUser(userRepository.findById(chatNotification.getSenderId()).get())
                .messageText(chatNotification.getContent())
                .build());
    }

    @Override
    public List<Message> findMessages(int senderId, int recipientId) {
        var chatId = chatroomService.getChatroomId(senderId, recipientId, false);
        return chatId.map(messageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
