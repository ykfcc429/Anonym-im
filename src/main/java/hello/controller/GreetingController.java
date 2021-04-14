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

    @MessageMapping("/hello")
    public Greeting messageHandle(HelloMessage message) throws Exception {
        if(message==null || message.getRoomNum()==null || message.getRoomNum().isEmpty()) {
            log.error("request message error , message is {}", message);
            return new Greeting("send error roomNum is null!");
        }
        try {
            simpMessageSendingOperations.convertAndSend("/topic/"+message.getRoomNum(),new Greeting(message.getName()));
        }catch (MessagingException messagingException){
            log.error("send Message failed , failedMessage is {}",messagingException.getFailedMessage(),messagingException);
            return new Greeting("send failed");
        }
        return new Greeting("send success");
    }

}
