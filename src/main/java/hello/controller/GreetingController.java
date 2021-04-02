package hello.controller;

import hello.DO.Greeting;
import hello.DO.HelloMessage;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
@Validated
public class GreetingController {

    private final SimpMessagingTemplate simpMessageSendingOperations;

    @MessageMapping("/hello")
    public void messageHandle(@Valid HelloMessage message) throws Exception {
        simpMessageSendingOperations.convertAndSend("/topic/"+message.getRoomNum(),new Greeting(message.getName()));
    }



}
