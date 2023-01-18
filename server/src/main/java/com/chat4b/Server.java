package com.chat4b;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        Message msg = new Message(message, conn);
        manageOperation(msg);
        if(clients.containsKey(msg.getUsername())){
            clients.replace(msg.getUsername(), conn);
        }else{
            clients.put(msg.getUsername(), conn);
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

    public void sendTo(String name, Message message) {
        WebSocket conn = clients.get(name);
        if (conn != null) {
            conn.send(message.toJson());
        }
    }

    public void sendTo(WebSocket conn, Message message) {
        if (conn != null) {
            conn.send(message.toJson());
        }
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

    public void manageOperation(Message msg){
        switch(msg.getOperation()){
            case "login":
                if(login(msg.getUsername(), msg.getData())){
                    System.out.println("Login successful " + msg.getUsername() + " " + msg.getData() + " " + msg.getIp());
                    sendTo(msg.getConn(), new Message("login", null, null, null, "success"));
                }else{
                    sendTo(msg.getConn(), new Message("login", null, null, null, "failed"));
                }
                break;
            case "send":
                sendTo(msg.getReceiver(), msg.getMessage());
                break;
            case "register":
                try {
                    System.out.println("Registering " + msg.getUsername() + " " + msg.getData() + " " + msg.getIp());
                    register(msg.getUsername(), msg.getData(), msg.getIp());
                    sendTo(msg.getConn(), new Message("registerr", null, null, null, "success"));
                } catch (SQLException e) {
                    e.printStackTrace();
                    sendTo(msg.getConn(), new Message("register", null, null, null, "failed"));
                }
                break;
            default:
                break;
        }
    }


    


	
}