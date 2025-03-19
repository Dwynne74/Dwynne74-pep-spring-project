package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Message;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private MessageService messageService;

    @Autowired
    public SocialMediaController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping(value = "/messages")
    public ResponseEntity<List<Message>> getMessageList(){
        List<Message> messages = messageService.getMessages();
        return ResponseEntity.ok(messages);
        
    }

    @GetMapping(value = "/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId){
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<Message> createMessage(@RequestParam int postedBy,
                                                 @RequestParam String messageText,
                                                 @RequestParam long timePostedEpoch){
        Message message = messageService.createMessage(postedBy, messageText, timePostedEpoch);
        if(message != null){
            return ResponseEntity.ok(message);
        } else {
            ResponseEntity.status(400);
            return null;
        }
        
    }

    @DeleteMapping(value = "/messages/{messageId}")
    public ResponseEntity<?> deleteById(@PathVariable int messageId){
        int numOfRows = messageService.deleteById(messageId);
        if(numOfRows != 0){
            return ResponseEntity.ok(numOfRows);
        } else {
            return ResponseEntity.ok("");
        }
    }

}
