package am.itspace.jobboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatroom")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String chatId;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User recipient;

}
