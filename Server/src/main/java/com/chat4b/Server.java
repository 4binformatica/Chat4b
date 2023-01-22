package com.chat4b;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Server extends WebSocketServer {

    HashMap <String, WebSocket> clients = new HashMap<String, WebSocket>();
    Database database = new Database();
    

	public Server(InetSocketAddress address) throws ClassNotFoundException, SQLException {
		super(address);
        checkDatabase();
        database.databaseConnect();
        database.createUserTable();
        database.createMessageTable();
	}

    public void checkDatabase() throws SQLException, ClassNotFoundException{
        File f = new File("database.db");
        if(f.exists() && !f.isDirectory()) { 
            System.out.println("Database exists");
            database.databaseConnect();
        }
        else{
            System.out.println("Database not found");
            database.createNewDatabase();
        }
    }

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		sendTo(conn, new Message("Welcome", "Server", "serverip", "Client", "Welcome to the server"));
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        clients.remove(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        Message msg = new Message(message, conn);
        try {
            manageOperation(msg);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(clients.get(msg.getUsername()) == null){
            clients.put(msg.getUsername(), conn);
        }
        else{
            clients.replace(msg.getUsername(), clients.get(msg.getUsername()), conn);
        }

	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
    
	
	@Override
	public void onStart() {
		System.out.println("server started successfully");
	}

    public void sendTo(String name, String message) {
        WebSocket conn = clients.get(name);
        if (conn != null) {
            conn.send(message);
        }
    }


    public void sendTo(String name, Message message) throws SQLException {
        message.setReceiver(name);
        sendTo(message);
    }

    public void sendTo(Message message) throws SQLException {
        WebSocket conn = clients.get(message.getReceiver());
        if (conn != null) {
            database.addMessage(message.getReceiver(), message);
            try {
                conn.send(message.toJson());
            } catch (Exception e) {
                database.addMessage(message.getReceiver(), message);
            }
        } else {
            database.addMessage(message.getReceiver(), message);
        }
    } 

    public void sendTo(WebSocket conn, Message message) {
        if (conn != null) {
            conn.send(message.toJson());
        }
    }

    public boolean login(String username, String password, String ip){
        try {
            if(database.checkUser(username, password)){
                database.updateIP(username, ip);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(String username, String password){
        try {
            if(database.checkUser(username, password)){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void register(String username, String password, String ip) throws SQLException{
        database.newUser(username, password, ip);
    }

    public void manageOperation(Message msg) throws SQLException{
        switch(msg.getOperation()){
            case "login":
                if(login(msg.getUsername(), msg.getData(), msg.getIp())){
                    clients.put(msg.getUsername(), msg.getConn());
                    System.out.println("Login successful " + msg.getUsername() + " " + msg.getData() + " " + msg.getIp());
                    sendTo(msg.getConn(), new Message("login", msg.getUsername(), null, null, "success"));
                }else{
                    sendTo(msg.getConn(), new Message("login", msg.getUsername(), null, null, "failed"));
                }
                break;
            case "send":
                sendTo(msg.getReceiver(), msg.getMessage());
                break;
            case "register":
                try {
                    System.out.println("Registering " + msg.getUsername() + " " + msg.getData() + " " + msg.getIp());
                    register(msg.getUsername(), msg.getData(), msg.getIp());
                    sendTo(msg.getConn(), new Message("register", msg.getUsername(), null, null, "success"));
                } 
                catch (SQLException e) {
                    e.printStackTrace();
                    sendTo(msg.getConn(), new Message("register", msg.getUsername(), null, null, "failed"));
                }
                break;
            case "logout":
                clients.remove(msg.getUsername());
                break;
            case "message":
                sendTo(msg);
                break;
            case "getMessages":
                ArrayList<Message> messages = database.getMessages(msg.getUsername());
                for(Message m : messages){
                    sendTo(m);
                }
                break;
            case "keepAlive":
                break;
            default:
                break;
        }
    }


    


	
}