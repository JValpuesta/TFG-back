package com.bosonit.conecta4;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/greet")
    @SendTo("/conecta4/greetings")
    public String greeting() throws InterruptedException {
        Thread.sleep(2000);
        return "Hola, gracias";
    }

}
