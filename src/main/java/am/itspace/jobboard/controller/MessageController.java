package am.itspace.jobboard.controller;

import am.itspace.jobboard.entity.ChatNotification;
import am.itspace.jobboard.entity.Message;
import am.itspace.jobboard.entity.User;
import am.itspace.jobboard.service.ChatroomService;
import am.itspace.jobboard.service.MessageService;
import am.itspace.jobboard.service.UserService;
import am.itspace.jobboard.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ChatroomService chatroomService;
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping("/profile/message")
    public String messagePage() {
        return "/profile/messages";
    }

    @GetMapping("/profile/message/{id}")
    public String messagePage(@PathVariable(value = "id", required = false) String idStr, ModelMap modelMap) {
        User user = securityService.getCurrentUser();

        if (idStr != null) {
            try {
                int toUserId = Integer.parseInt(idStr);
                User byId = userService.findById(toUserId);
                if (byId != null) {
                    chatroomService.getChatroomId(user.getId(), toUserId, true);
                    modelMap.put("fromUserId", user.getId());
                    modelMap.put("toUserId", toUserId);
                    modelMap.put("userName", byId.getName());
                }
            } catch (NumberFormatException | NullPointerException e) {
                return "redirect:/profile/message";
            }
        }
        return "/profile/messages";
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<Message>> findMessages(@PathVariable("senderId") int senderId,
                                                      @PathVariable("recipientId") int recipientId) {
        return ResponseEntity.ok(messageService.findMessages(senderId, recipientId));
    }


    @MessageMapping("/chat")
    public void processPrivateMessage(@Payload ChatNotification chatNotification) {
        //save message
        Message savedMsg = messageService.save(chatNotification);
        //send to the other person
        messagingTemplate.convertAndSendToUser(String.valueOf(chatNotification.getRecipientId()),
                "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getFromUser().getId())
                        .recipientId(savedMsg.getToUser().getId())
                        .senderName(savedMsg.getFromUser().getName())
                        .content(savedMsg.getMessageText())
                        .date(savedMsg.getMessageDate())
                        .build());
    }

    @GetMapping("/delete-conversation/{selectedUserId}")
    public String deleteConversation(@PathVariable("selectedUserId") String selectedUserIdStr) {
        User user = securityService.getCurrentUser();

        try {
            int selectedUserId = Integer.parseInt(selectedUserIdStr);
            chatroomService.deleteConversationForOneUser(user.getId(), selectedUserId);
        } catch (NumberFormatException ignored) {
        }
        return "/profile/messages";
    }
}
