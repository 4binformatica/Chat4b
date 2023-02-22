package com.chat4b.Message;

import java.time.Instant;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

/**
 * Message
 */

public class Message {
    private String message;

    @Expose
    private String operation;
    @Expose
    private String username;
    @Expose
    private String receiver;
    @Expose
    private String data;
    @Expose
    private String date;

    private ClientInfo clientInfo = new ClientInfo();
    private WebSocket conn;

    public Message(String message) {
        this.message = message;
        decode();
    }

    public Message(String message, WebSocket conn2) {
        this.message = message;
        this.conn = conn2;
        decode();
    }

    public Message(String operation, String username, String receiver, String data, String date) {
        this.operation = operation;
        this.username = username;
        this.receiver = receiver;
        this.data = data;
        this.date = date;
    }

    public Message(String operation, String username, String receiver, String data) {
        this.operation = operation;
        this.username = username;
        this.receiver = receiver;
        this.data = data;
        this.date = Instant.now().toString();
    }

    public void decode() {
        Gson gson = new Gson();
        Message msg = gson.fromJson(message, Message.class);
        this.operation = msg.getOperation();
        this.username = msg.getUsername();
        this.receiver = msg.getReceiver();
        this.data = msg.getData();
        this.date = msg.getDate();
    }

    public String getOperation() {
        return operation;
    }

    public String getUsername() {
        return username;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public WebSocket getConn() {
        return conn;
    }

    public String toJson() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(this);
    }

    public static String toJson(Message m) {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        return gson.toJson(m);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public void setReceiver(String name) {
        this.receiver = name;
    }

    public void setUserName(String name) {
        this.username = name;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MessageData getMessageData(){
        if(!operation.equals("message")) return null;
        Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
        return gson.fromJson(data, MessageData.class);
    }

    public void setMessageData(MessageData dat) {
        Gson gson = new GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create();
        this.data = gson.toJson(dat);
    }

}