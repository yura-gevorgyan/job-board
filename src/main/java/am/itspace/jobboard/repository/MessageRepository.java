package am.itspace.jobboard.repository;

import am.itspace.jobboard.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {


}

