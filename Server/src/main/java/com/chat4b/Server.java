package com.chat4b;

import java.io.File;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.sql.SQLException;
import java.time.Instant;

public class Server extends WebSocketServer {

    HashMap <String, ArrayList<WebSocket>> clients = new HashMap<String, ArrayList<WebSocket>>();
    Database database = new Database();
    

	public Server(InetSocketAddress address) throws ClassNotFoundException, SQLException {
		super(address);
        checkDatabase();
        database.databaseConnect();
        database.createUserTable();
        database.createMessageTable();
        database.createContactTable();
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
		sendTo(conn, new Message("Welcome", "Server", "Client", "Welcome to the server"));
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        for(String key : clients.keySet()){
            if(clients.get(key).contains(conn)){
                System.out.println("removed " + conn.getRemoteSocketAddress() + " from " + key);
                clients.get(key).remove(conn);
            }
        }
	}

	@Override
	public void onMessage(WebSocket conn, String message) {	
        Message msg = new Message(message, conn);
        if(!msg.getOperation().equals( "keepAlive")){
            System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        }
        try {
            manageOperation(msg);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(clients.get(msg.getUsername()) == null){
            clients.put(msg.getUsername(), new ArrayList<WebSocket>());
            clients.get(msg.getUsername()).add(conn);
        }
        else{
            clients.remove(msg.getUsername());
            clients.put(msg.getUsername(), new ArrayList<WebSocket>());
            clients.get(msg.getUsername()).add(conn);
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
        for(WebSocket conn : clients.get(name)){
            if (conn != null) {
                conn.send(message);
            }
        }
    }


    public void sendTo(String name, Message message) throws SQLException {
        message.setReceiver(name);
        sendTo(message);
    }

    public void sendTo(Message message) throws SQLException {
        System.out.println(clients.get(message.getReceiver()) + " " + message.getReceiver());
        for(WebSocket conn : clients.get(message.getReceiver())){
            if (conn != null) {
                System.out.println("Sending message to " + message.getReceiver() + " from " + message.getUsername());
                conn.send(message.toJson());
            }
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

    public void sendReoload(String username) throws SQLException{
        Message msg = new Message("reload", "Server", username, "reload");
        sendTo(username, msg);
    }

    public boolean register(String username, String password) throws SQLException{
        if(database.newUser(username, password))
            return true;
        return false;
    }

    public void manageOperation(Message msg) throws SQLException{
        
        switch(msg.getOperation()){
            case "login":
                if(login(msg.getUsername(), msg.getData())){
                    if(clients.get(msg.getUsername()) == null){
                        clients.put(msg.getUsername(), new ArrayList<WebSocket>());
                        clients.get(msg.getUsername()).add(msg.getConn());
                    }
                    else{
                        if(!clients.get(msg.getUsername()).contains(msg.getConn())){
                            clients.get(msg.getUsername()).add(msg.getConn());
                        }                        
                    }
                    System.out.println("Login successful " + msg.getUsername() + " " + msg.getData() );
                    sendTo(msg.getConn(),  new Message("login", msg.getUsername(), msg.getUsername(), "success"));
                }else{
                    sendTo(msg.getConn(), new Message("login", msg.getUsername(), msg.getUsername(), "Login failed, wrong username or password"));
                }
                break;
            case "send":
                sendTo(msg.getReceiver(), msg.getMessage());
                break;
            case "register":
                if(register(msg.getUsername(), msg.getData())){
                    sendTo(msg.getConn(), new Message("register", msg.getUsername(), msg.getUsername(), "success"));
                }else{
                    sendTo(msg.getConn(), new Message("register", msg.getUsername(), null, "Username already exists"));
                }
                break;
            case "logout":
                clients.remove(msg.getUsername());
                break;
            case "message":
                database.addMessage(msg);
                sendTo(msg);
                break;
            case "removeMessage":
                database.removeMessageByDate(msg.getReceiver(), msg.getData());
                sendReoload(msg.getReceiver());
                break;
            case "addContact":
                if(database.userExist(msg.getData())){
                    database.addContact(msg.getUsername(), msg.getData());
                    database.addContact(msg.getData(), msg.getUsername());
                    sendTo(msg.getConn(), new Message("addContact", msg.getUsername(), msg.getData(), "success"));
                }else{
                    sendTo(msg.getConn(), new Message("addContact", msg.getUsername(), msg.getData(), "User does not exist"));
                }
                break;
            case "removeContact":
                database.removeContact(msg.getUsername(), msg.getData());
                database.removeContact(msg.getData(), msg.getUsername());
                sendReoload(msg.getData());
                break;
            case "getMessages":
                ArrayList<Message> messages = database.getMessages(msg.getUsername());
                System.out.println("Sending " + messages.size() + " messages to " + msg.getUsername());
                for(Message m : messages){
                    if((m.getReceiver().equals(msg.getUsername()) && m.getUsername().equals(msg.getData()) || (m.getReceiver().equals(msg.getData()) && m.getUsername().equals(msg.getUsername())))){
                        sendTo(msg.getConn(), m);
                    }
                }
                break;
            case "getContacts":
                ArrayList<String> contacts = database.getContacts(msg.getUsername());
                for(String contact : contacts){
                    sendTo(msg.getConn(), new Message("contact", msg.getUsername(), msg.getUsername(), contact));
                }
                break;
            case "ping":
                String t1 = msg.getDate();
                String t2 = Instant.now().toString();
                long diff = Instant.parse(t2).toEpochMilli() - Instant.parse(t1).toEpochMilli();
                sendTo(msg.getConn(), new Message("pong", msg.getUsername(), msg.getUsername(), Long.toString(diff)));
                break;
            case "keepAlive":
                break;
            default:
                System.out.println("Unknown operation: " + msg.getOperation());
                break;
        }
    }


    


	
}