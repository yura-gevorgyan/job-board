package am.itspace.jobboard.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private int id;
    private int senderId;
    private int recipientId;
    private String senderName;
    private String content;
    private Date date;
}
