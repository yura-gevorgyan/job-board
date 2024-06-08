package am.itspace.jobboard.service.impl;

import am.itspace.jobboard.repository.ChatroomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChatroomServiceImplTest {

    @MockBean
    private ChatroomRepository chatroomRepository;

    @Autowired
    private ChatroomServiceImpl chatroomService;

    @Test
    void deleteConversationForOneUser() {
        int currentUserId = 1;
        int selectedUserId = 2;

        doNothing().when(chatroomRepository).deleteBySenderIdAndRecipientId(currentUserId, selectedUserId);
        chatroomService.deleteConversationForOneUser(currentUserId, selectedUserId);

        verify(chatroomRepository, times(1)).deleteBySenderIdAndRecipientId(currentUserId, selectedUserId);
    }
}
