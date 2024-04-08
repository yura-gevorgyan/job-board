package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByChatId(String chatId);

}

