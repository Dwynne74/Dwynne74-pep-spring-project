package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.MessageRepository;

@Service
// @Transactional
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

    public Message createMessage(Message message){
        if(!message.getMessageText().isEmpty() && message.getMessageText().length() <= 255 && accountService.accountExist(message.getPostedBy())){
            Message newMessage = new Message(message.getPostedBy(), message.getMessageText(), message.getTimePostedEpoch());
            messageRepository.save(newMessage);
            return newMessage;
        } else {
            return null;
        }
    }

    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messagesFromUser = new ArrayList<Message>();
        MessageService messageService = new MessageService(messageRepository, accountService);
        for(Message message: messageService.getMessages()){
            if(message.getPostedBy() == accountId){
                messagesFromUser.add(message);
            }
        }
        return messagesFromUser;
    }

    // @Transactional
    public int deleteById(int messageId){
        if(messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        } else {
            return 0;
        }
    }

    public int updateMessage(int messageId, Message message){
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if(optionalMessage.isPresent() && message.getMessageText().length() > 0 && message.getMessageText().length() <= 255){
            Message updatedMessage = optionalMessage.get();
            updatedMessage.setMessageText(message.getMessageText());
            messageRepository.save(updatedMessage);
            return 1;
        } else {
            return 0;
        }
    }


    
}
