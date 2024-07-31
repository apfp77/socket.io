package com.ohgiraffers.socketspring.service;


import java.util.List;

import com.ohgiraffers.socketspring.model.Message;
import com.ohgiraffers.socketspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> getMessage(String room) {
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}