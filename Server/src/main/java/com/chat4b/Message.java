package com.chat4b;

import java.time.Instant;

import org.java_websocket.WebSocket;

import com.google.gson.Gson;


/**
 * Message
 */


public class Message {
    private String message;

    private String operation;
    private String username;
    private String receiver;
    private String data;
    private String date;
    private String clientInfo;
    private WebSocket conn;



    Message(String message) {
        this.message = message;
        decode();
    }

    Message(String message, WebSocket conn2){
        this.message = message;
        this.conn = conn2;
        decode();
    }

    Message(String operation, String username, String receiver, String data, String date) {
        this.operation = operation;
        this.username = username;
        this.receiver = receiver;
        this.data = data;
        this.date = date;
    }

    Message(String operation, String username, String receiver, String data) {
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

    public String toJson(){
        return "{\"operation\":\"" + operation + "\",\"username\":\"" + username  + "\",\"receiver\":\"" + receiver + "\",\"data\":\"" + data + "\",\"date\":\"" + date + "\"}";
    }

    @Override
    public String toString(){
        return getMessage();
    }

    public void setReceiver(String name) {
        this.receiver = name;
    }

    public void setUserName(String name) {
        this.username = name;
    }


        
    


}