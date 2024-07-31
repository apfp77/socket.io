package com.ohgiraffers.socketspring.socket;

import java.util.stream.Collectors;

import com.ohgiraffers.socketspring.constants.Constants;
import com.ohgiraffers.socketspring.model.Message;
import com.ohgiraffers.socketspring.service.SocketService;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketModule {

    private final SocketIOServer server;

    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
        server.addEventListener("send_message", Message.class, this.onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(senderClient.toString());
            log.info(data.toString());
            log.info(ackSender.toString());
            socketService.saveMessage(senderClient, data);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            System.out.println(client.toString());
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            client.joinRoom(room);
            socketService.saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through",
                    client.getSessionId().toString(), room, username);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = params.get("room").stream().collect(Collectors.joining());
            String username = params.get("username").stream().collect(Collectors.joining());
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }

}