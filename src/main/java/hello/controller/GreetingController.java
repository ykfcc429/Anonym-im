package hello.controller;

import hello.DO.Greeting;
import hello.DO.HelloMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class GreetingController {

    private final SimpMessagingTemplate simpMessageSendingOperations;

    @MessageMapping("/send")
    public void messageHandle(HelloMessage message) {
        if(message==null || message.getRoomNum()==null) {
            log.error("request message error , message is {}", message);
        }
        try {
            assert message != null;
            simpMessageSendingOperations.convertAndSend("/topic/"+message.getRoomNum(),new Greeting(message.getName()+":"+message.getMessage(),message.getName()));
        }catch (MessagingException messagingException){
            log.error("send Message failed , The Message is {}",messagingException.getFailedMessage(),messagingException);
        }
    }

}
