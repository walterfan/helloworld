package com.github.walterfan.hellospring;

import lombok.Data;

/**
 * Created by yafan on 12/5/2018.
 */
@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}