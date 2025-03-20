package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.ResourceNotFoundException;
import com.example.service.AccountService;
import com.example.service.MessageService;


@RestController
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService){
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        Account newAccount = new Account();
        try {
            newAccount = accountService.register(account);
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<Account>(HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException e){
            return new ResponseEntity<Account>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        Account loginAccount = accountService.login(account);
        if(loginAccount != null){
            return ResponseEntity.ok(loginAccount);
        } else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message newMessage = messageService.createMessage(message);
        if(newMessage != null){
            return ResponseEntity.ok(newMessage);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
    }

    @DeleteMapping(value = "/messages/{messageId}")
    public ResponseEntity<?> deleteById(@PathVariable("messageId") int messageId){
        int numOfRows = messageService.deleteById(messageId);
        if(numOfRows != 0){
            return ResponseEntity.ok(numOfRows);
        } else {
            return ResponseEntity.ok("");
        }
    }

    @PatchMapping(value = "/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable("messageId") int messageId, @RequestBody Message message){
        int numOfRows = messageService.updateMessage(messageId, message);
        if(numOfRows != 0){
            return ResponseEntity.ok(numOfRows);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping(value = "/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable("accountId") int accountId){
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    } 

}
