package com.fangzhou.manatee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
import java.util.Date;
import java.util.List;
import java.text.*;
 
@Controller
@RequestMapping("/")
public class WebSocketController {
 
    @RequestMapping("/mock")
    public String chat() {
        return "mock";
    }
 
    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public String sendMessage(String message) {
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return message;
        }
}
