package com.ohgiraffers.socketspring.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.ohgiraffers.socketspring.model.Message;
import org.springframework.stereotype.Repository;


@Repository
public class MessageRepository{
    ConcurrentMap<String, List<Message>> db;
    Long id = 1L;

    public MessageRepository(){
        this.db = new ConcurrentHashMap<>();
    }

    public List<Message> findAllByRoom(String room){
        return db.get(room);
    }

    public Message save(Message message){
        message.setId(id++);
        if (message.getRoom() == null){
            return message;
        }
        if (!db.containsKey(message.getRoom())){
            db.put(message.getRoom(), List.of(message));
            return message;
        }
        System.out.println(db.get(message.getRoom()));
        // db.get(message.getRoom()).add(message);
        return message;
    }
}