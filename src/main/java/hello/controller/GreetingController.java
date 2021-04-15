package hello.controller;

import hello.DO.Greeting;
import hello.DO.HelloMessage;
import hello.DO.Result;
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
    public Result<?> messageHandle(HelloMessage message) {
        if(message==null || message.getRoomNum()==null) {
            log.error("request message error , message is {}", message);
            return Result.error("error",null);
        }
        try {
            simpMessageSendingOperations.convertAndSend("/topic/"+message.getRoomNum(),new Greeting(message.getMessage(),message.getName()));
        }catch (MessagingException messagingException){
            log.error("send Message failed , failedMessage is {}",messagingException.getFailedMessage(),messagingException);
            return Result.error("send failed",null);
        }
        return Result.success("ok",null);
    }

}
