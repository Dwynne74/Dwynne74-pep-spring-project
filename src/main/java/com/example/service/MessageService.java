package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountService accountService;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountService accountService){
        this.messageRepository = messageRepository;
        this.accountService = accountService;
    }

    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public Message createMessage(int postedBy, String messageText, long timePostedEpoch){
        if(!messageText.isEmpty() && messageText.length() <= 255 && accountService.accountExist(postedBy)){
            Message newMessage = new Message(postedBy, messageText, timePostedEpoch);
            messageRepository.save(newMessage);
            return newMessage;
        } else {
            return null;
        }
    }

    public int deleteById(int messageId){
        if(messageRepository.existsById(messageId)){
            return messageRepository.deleteMessageById(messageId);
        } else {
            return 0;
        }
    }
    
}
