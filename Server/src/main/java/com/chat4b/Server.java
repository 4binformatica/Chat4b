package com.chat4b;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.time.Instant;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;







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
        database.createLoginIDTable();
        database.createDraftTable();
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
        } catch (Exception e) {
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
        return database.newUser(username, password, "https://i.ibb.co/NsJGFh6/istockphoto-522855255-612x612-modified.png");
    }

    public ImgbbResponse uploadImage(String base64Img) throws Exception {
        String apiKey = "dcade2ebf0fb763a669491b8e637524c";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost("https://api.imgbb.com/1/upload");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("key", apiKey, ContentType.TEXT_PLAIN);
        builder.addTextBody("image", base64Img, ContentType.TEXT_PLAIN);
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            ImgbbResponse imgbbResponse = gson.fromJson(responseString, ImgbbResponse.class);
            return imgbbResponse;
        } finally {
            response.close();
        }
    }

    public String generateLoginID(String username) throws SQLException{
        //create a random string of 10 characters based on the username
        String loginID = "";
        for(int i = 0; i < 10; i++){
            loginID += username.charAt((int)(Math.random() * username.length()));
        }
        //check if the loginID is already in the database
        if(database.checkLoginID(loginID)){
            //if it is, generate a new one
            generateLoginID(username);
        }
        //if it isn't, return the loginID
        return loginID;
    }


    public void manageOperation(Message msg) throws Exception{
        
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
                    if(database.getLoginID(msg.getUsername()) != null){
                        database.removeLoginID(msg.getUsername());
                    }
                    database.addLoginID(msg.getUsername(), generateLoginID(msg.getUsername()));
                    sendTo(msg.getConn(), new Message("loginID", msg.getUsername(), msg.getUsername(), database.getLoginID(msg.getUsername())));
                    sendTo(msg.getConn(), new Message("login", msg.getUsername(), msg.getUsername(), "success"));
                }else{
                    sendTo(msg.getConn(), new Message("login", msg.getUsername(), msg.getUsername(), "Login failed, wrong username or password"));
                }
                break;
            case "checkLoginID":
                //check if tje loginID is outdated not generate new one
                if(database.checkLoginIDOutdated(msg.getUsername())
                && !database.getLoginID(msg.getUsername()).equals(msg.getData())){
                    sendTo(msg.getConn(), new Message("checkLoginID", msg.getUsername(), msg.getUsername(), "false"));
                }
                if(database.checkLoginID(msg.getData())){
                    sendTo(msg.getConn(), new Message("checkLoginID", msg.getUsername(), msg.getUsername(), "true"));
                }else{
                    sendTo(msg.getConn(), new Message("checkLoginID", msg.getUsername(), msg.getUsername(), "false"));
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
                database.removeLoginID(msg.getUsername());
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
                    if(msg.getData().equals(msg.getUsername())){
                        return;
                    }
                    database.addContact(msg.getUsername(), msg.getData());
                    database.addContact(msg.getData(), msg.getUsername());
                    sendTo(msg.getConn(), new Message("addContact", msg.getUsername(), msg.getData(), "success"));
                    sendReoload(msg.getData());
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
                    if(m.getReceiver() == null){
                        System.out.println("Receiver is null");
                    }
                    else{
                        if((m.getReceiver().equals(msg.getUsername()) && m.getUsername().equals(msg.getData()) || (m.getReceiver().equals(msg.getData()) && m.getUsername().equals(msg.getUsername())))){
                            sendTo(msg.getConn(), m);
                        }
                    }
                    
                }
                break;
            case "getProfilepic":
                String profilePic = database.getProfilePic(msg.getData());
                sendTo(msg.getConn(), new Message("profilePic", msg.getData(), msg.getUsername(), profilePic));
                break;
            case "getContacts":
                ArrayList<String> contacts = database.getContacts(msg.getUsername());
                for(String contact : contacts){
                    sendTo(msg.getConn(), new Message("contact", contact, msg.getUsername(), database.getProfilePic(contact)));                    
                }
                break;
            case "image":
                ImgbbResponse imgbbResponse = uploadImage(msg.getData());
                Message m = new Message("image", msg.getUsername(), msg.getReceiver(), imgbbResponse.getUrl());
                database.addImage(m);
                sendTo(msg.getConn(), m);
                sendTo(msg.getReceiver(), m);
                break;
            case "changeProfilePic":
                ImgbbResponse imgbbResponse2 = uploadImage(msg.getData());
                database.changeProfilePic(msg.getUsername(), imgbbResponse2.getUrl());
                break;
            case "getDraft":
                String draft = database.getDraft(msg.getUsername(), msg.getReceiver());
                sendTo(msg.getConn(), new Message("draft", msg.getUsername(), msg.getReceiver(), draft));
                break;
            case "saveDraft":
                database.createDraft(msg.getUsername(), msg.getData(), msg.getReceiver());
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