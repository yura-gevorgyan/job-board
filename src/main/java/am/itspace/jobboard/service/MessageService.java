package am.itspace.jobboard.service;

import am.itspace.jobboard.entity.ChatNotification;
import am.itspace.jobboard.entity.Message;

import java.util.List;

public interface MessageService {

    Message save(ChatNotification chatNotification);

    List<Message> findMessages(int senderId, int recipientId);

}
