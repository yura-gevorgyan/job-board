package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Integer> {

    Optional<Chatroom> findBySenderIdAndRecipientId(int senderId, int recipientId);

    List<Chatroom> findAllBySenderId(int senderId);

    void deleteBySenderIdAndRecipientId(int senderId, int recipientId);
}
