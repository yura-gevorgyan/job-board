package am.itspace.jobboard.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "message")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String chatId;

    private String messageText;

    @DateTimeFormat
    private Date messageDate;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

}
