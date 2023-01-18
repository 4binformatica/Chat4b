package com.chat4b;

import org.java_websocket.WebSocket;

/**
 * Message
 */


public class Message {
    private String message;

    private String operation;
    private String username;
    private String ip;
    private String receiver;
    private String data;
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

    Message(String operation, String username, String ip, String receiver, String data) {
        this.operation = operation;
        this.username = username;
        this.ip = ip;
        this.receiver = receiver;
        this.data = data;
    }

    public void decode() {
        //split the json string message into an array
        String[] parts = message.split(",");
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            //split the key and value
            String[] keyValue = part.split(":");
            //remove the quotes from the key
            String key = keyValue[0].replace("\"", "");
            //remove the quotes from the value
            String value = keyValue[1].replace("\"", "");
            //remove the curly braces from the value
            value = value.replace("{", "");
            value = value.replace("}", "");
            key = key.replace("{", "");
            key = key.replace("}", "");

            switch(key){
                case "operation":
                    operation = value;
                    break;
                case "username":
                    username = value;
                    break;
                case "ip":
                    ip = value;
                    break;
                case "receiver":
                    receiver = value;
                    break;
                case "data":
                    data = value;
                    break;
            }
        }
    }

    public String getOperation() {
        return operation;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
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

    public WebSocket getConn() {
        return conn;
    }

    public String toJson(){
        return "{\"operation\":\"" + operation + "\",\"username\":\"" + username + "\",\"ip\":\"" + ip + "\",\"receiver\":\"" + receiver + "\",\"data\":\"" + data + "\"}";
    }

    @Override
    public String toString(){
        return getMessage();
    }


        
    


}