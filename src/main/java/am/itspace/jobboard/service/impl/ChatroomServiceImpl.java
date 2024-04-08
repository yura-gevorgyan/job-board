package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.entity.Chatroom;
import am.itspace.jobboard.repository.ChatroomRepository;
import am.itspace.jobboard.repository.UserRepository;
import am.itspace.jobboard.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatroomRepository chatroomRepository;

    private final UserRepository userRepository;

    @Override
    public Optional<String> getChatroomId(int senderId, int recipientId, boolean createNewRoomIfNotExists) {

        return chatroomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(Chatroom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        String charId = createChatId(senderId, recipientId);
                        return Optional.of(charId);
                    }
                    return Optional.empty();
                });
    }

    @Transactional
    @Override
    public void deleteConversationForOneUser(int currentUserId, int selectedUserId) {
        chatroomRepository.deleteBySenderIdAndRecipientId(currentUserId, selectedUserId);
    }

    private String createChatId(int senderId, int recipientId) {
        String chatId = String.format("%s_%s", senderId, recipientId);

        Chatroom senderRecipient = Chatroom.builder()
                .chatId(chatId)
                .sender(userRepository.findById(senderId).get())
                .recipient(userRepository.findById(recipientId).get())
                .build();

        Chatroom recipientSender = Chatroom.builder()
                .chatId(chatId)
                .recipient(userRepository.findById(senderId).get())
                .sender(userRepository.findById(recipientId).get())
                .build();
        chatroomRepository.save(senderRecipient);
        chatroomRepository.save(recipientSender);

        return chatId;
    }
}
