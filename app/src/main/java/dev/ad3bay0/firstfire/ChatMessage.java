package dev.ad3bay0.firstfire;

import java.util.Date;

/**
 * Created by Adebeslick on 3/1/2018.
 */

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;


    public ChatMessage(){


    }

    public ChatMessage(String messageText,String messageUser){

        this.messageText = messageText;
        this.messageUser = messageUser;

        //initialize current time
        messageTime = new Date().getTime();
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
