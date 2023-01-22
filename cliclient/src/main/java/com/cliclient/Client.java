package com.cliclient;

import java.net.URI;
import java.net.http.WebSocket;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
    }



    
    Scanner input = new Scanner(System.in);
    String ip;
    String username;
    
    void menu(){
        System.out.println("Select an operation: ");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Message");
        System.out.println("4. Custom Operation");
        System.out.println("5. Exit");
        switch (input.nextInt()) {
          case 1:
              login();
              break;
          case 2:
              register();
              break;
          case 3:
              message();
              break;
          case 4:
              customOperation();
              break;
          case 5:
              System.exit(0);
              break;
          default:
              System.out.println("Invalid input");
              break;
      }
    }



    void login(){
        System.out.println("Enter username: ");
        String username = input.next();
        System.out.println("Enter password: ");
        String password = input.next();
        Message message = new Message("login", username, ip, "server", password);

        send(message);
    }

    void register(){
        System.out.println("Enter username: ");
        String username = input.next();
        System.out.println("Enter password: ");
        String password = input.next();
        Message message = new Message("register", username, ip, "server", password);

        send(message);
    }

    void message(){
        System.out.println("Enter receiver: ");
        String receiver = input.next();
        System.out.println("Enter message: ");
        String data = input.next();
        Message message = new Message("message", username, ip, receiver, data);

        send(message);
    }

    void customOperation(){
        System.out.println("Enter operation: ");
        String operation = input.next();
        System.out.println("Enter receiver: ");
        String receiver = input.next();
        System.out.println("Enter data: ");
        String data = input.next();
        Message message = new Message(operation, username, ip, receiver, data);

        send(message);
    }

    void start(){
        System.out.println("Connected to server");
        while(true){
            menu();
        }
    }



    void send(Message message){
        send(message.toJson());
    }



    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        
    }



    @Override
    public void onMessage(String arg0) {
        Message message = new Message(arg0);
        switch(message.getOperation()){
            case "login":
                if(message.getData().equals("success")){
                    username = message.getUsername();
                    System.out.println("Login successful");
                }else{
                    System.out.println("Login failed");
                }
                break;
            case "register":
                if(message.getData().equals("success")){
                    username = message.getUsername();
                    System.out.println("Register successful");
                }else{
                    System.out.println("Register failed");
                }
                break;
            case "message":
                System.out.println(message.getUsername() + ": " + message.getData());
                break;
            case "Welcome":
                System.out.println("Welcome to the server");
                break;
            default:
                System.out.println("Invalid operation");
                break;
        }

        
    }



    @Override
    public void onOpen(ServerHandshake arg0) {
        // TODO Auto-generated method stub
        
    }

}
