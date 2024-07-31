package com.ohgiraffers.socketspring.model;


import java.time.LocalDateTime;
import java.util.UUID;


import com.ohgiraffers.socketspring.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Long id;

    private MessageType messageType;

    private String room;

    private String username;

    private String message;

    private LocalDateTime createdAt;

}