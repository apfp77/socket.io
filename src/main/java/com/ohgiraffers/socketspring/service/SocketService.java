package com.ohgiraffers.socketspring.service;



import com.ohgiraffers.socketspring.enums.MessageType;
import com.ohgiraffers.socketspring.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {

    private final MessageService messageService;

    public void sendSocketMessage(SocketIOClient senderClient, Message message, String room) {
        for (
                SocketIOClient client: senderClient.getNamespace().getRoomOperations(room).getClients()
        ) {
            log.info("client getSesstionId: " + client.getSessionId().toString());
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                    client.sendEvent("read_message", message);
            }
        }
    }

    public void saveMessage(SocketIOClient senderClient, Message message) {

        Message storedMessage = messageService.saveMessage(
                Message.builder()
                        .messageType(MessageType.CLIENT)
                        .message(message.getMessage())
                        .room(message.getRoom())
                        .username(message.getUsername())
                        .build()
        );

        sendSocketMessage(senderClient, storedMessage, message.getRoom());

    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, String room) {
        Message storedMessage = messageService.saveMessage(
                Message.builder()
                        .messageType(MessageType.SERVER)
                        .message(message)
                        .room(room)
                        .build()
        );

        sendSocketMessage(senderClient, storedMessage, room);
    }
}