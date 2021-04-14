package hello.controller;

import hello.DO.Greeting;
import hello.DO.HelloMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Validated
@Slf4j
public class GreetingController {

    private final SimpMessagingTemplate simpMessageSendingOperations;

    @MessageMapping("/hello")
    public Greeting messageHandle(@Valid HelloMessage message) throws Exception {
        if(message==null || message.getRoomNum()==null || message.getRoomNum().isEmpty()) {
            log.error("request message error , message is {}", message);
            return new Greeting("房间号为空");
        }
        try {
            simpMessageSendingOperations.convertAndSend("/topic/"+message.getRoomNum(),new Greeting(message.getName()));
        }catch (MessagingException messagingException){
            log.error("send Message failed , failedMessage is {}",messagingException.getFailedMessage(),messagingException);
            return new Greeting("消息发送失败");
        }
        return new Greeting("发送成功");
    }

}
