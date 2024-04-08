package am.itspace.jobboard.service;

import java.util.Optional;

public interface ChatroomService {

    Optional<String> getChatroomId(int senderId, int recipientId, boolean createNewRoomIfNotExists);

    void deleteConversationForOneUser(int currentUserId, int selectedUserId);

}
