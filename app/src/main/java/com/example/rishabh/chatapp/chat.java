package com.example.rishabh.chatapp;

public class chat
{
   private String sender;
    private String receiver;
    private String messages;

    public chat()
    {

    }

    public chat(String sender, String receiver, String messages) {
        this.sender = sender;
        this.receiver = receiver;
        this.messages = messages;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
