package com.chat4b;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Map;

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
    MailClient mailClient;
    Config config;

    /*  
     * Mail
     * Host : smtp.yandex.com
     * Port : 465
     * SSL : true
     * Auth : true
     *  
     */
    

	public Server(InetSocketAddress address, Config config) throws ClassNotFoundException, SQLException {
		super(address);
        mailClient = new MailClient(config.getMailHost(), config.getMailPort(), config.isMailAuth(), config.isMailStarttls(), config.getMailUser(), config.getMailPassword());
        this.config = config;
        checkDatabase();
        database.databaseConnect();
        database.createUserTable();
        database.createMessageTable();
        database.createContactTable();
        database.createLoginIDTable();
        database.createDraftTable();
        database.createForgotPasswordTable();
        database.createAdminCodeTable();
	}

    /**
     * If the database exists, connect to it. If it doesn't exist, create it
     */
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

	/**
     * When a new connection is made, send a welcome message to the client
     * 
     * @param conn The connection that was just opened.
     * @param handshake The handshake object contains the headers sent by the client.
     */
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
		sendTo(conn, new Message("Welcome", "Server", "Client", "Welcome to the server"));
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
	}

	/**
     * When a connection is closed, remove it from the list of connections
     * 
     * @param conn The connection that is closing.
     * @param code The codes can be looked up [here](https://tools.ietf.org/html/rfc6455#section-7.4.1)
     * @param reason The reason for closing the connection. This must be provided as per the WebSocket
     * protocol.
     * @param remote true if the closing of the connection was initiated by the remote host.
     */
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
	// A method that is called when a message is received from the client.
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
	// A method that is called when a message is received.
    public void onMessage( WebSocket conn, ByteBuffer message ) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	/**
     * If an error occurs, print the error to the console
     * 
     * @param conn The WebSocket connection that has been closed.
     * @param ex The exception that occurred.
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
		System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
    
	
	@Override
    // A method that is called when the activity is started.
    public void onStart() {
		System.out.println("server started successfully");
	}

    /**
     * Send a message to all clients with the given name.
     * 
     * @param name The name of the user you want to send the message to.
     * @param message The message to be sent to the client.
     */
    public void sendTo(String name, String message) {
        for(WebSocket conn : clients.get(name)){
            if (conn != null) {
                conn.send(message);
            }
        }
    }

    /**
     * This function sends a message to a user with the given name
     * 
     * @param name The name of the user you want to send the message to.
     * @param message The message to be sent.
     */
    public void sendTo(String name, Message message) throws SQLException {
        message.setReceiver(name);
        sendTo(message);
    }

    /**
     * It sends a message to a user if the user is online.
     * </code>
     * 
     * @param message The message object that contains the message, sender, and receiver.
     */
    public void sendTo(Message message) throws SQLException {
        System.out.println(clients.get(message.getReceiver()) + " " + message.getReceiver());
        for(WebSocket conn : clients.get(message.getReceiver())){
            if (conn != null) {
                System.out.println("Sending message to " + message.getReceiver() + " from " + message.getUsername());
                conn.send(message.toJson());
            }
        }
    } 

    /**
     * It sends a message to a specific connection.
     * 
     * @param conn The connection to send the message to.
     * @param message The message to send.
     */
    public void sendTo(WebSocket conn, Message message) {
        if (conn != null) {
            conn.send(message.toJson());
        }
    }

    /**
     * If the database checkUser function returns true, return true. Otherwise, return false
     * 
     * @param username String
     * @param password String
     * @return A boolean value.
     */
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

    /**
     * It sends a message to a user with the message "reload" and the sender "Server"
     * 
     * @param username The username of the user you want to send the message to.
     */
    public void sendReoload(String username) throws SQLException{
        Message msg = new Message("reload", "Server", username, "reload");
        sendTo(username, msg);
    }

    /**
     * This function takes in a username and password and returns a boolean value
     * 
     * @param username The username of the user
     * @param password The password of the user
     * @return A boolean value.
     */
    public boolean register(String username, String password) throws SQLException{
        return database.newUser(username, password, "https://i.ibb.co/NsJGFh6/istockphoto-522855255-612x612-modified.png", null);
    }

    /**
     * It takes a base64 encoded image and uploads it to imgbb.com
     * 
     * @param base64Img The base64 encoded image.
     * @return A JSON object with the following structure:
     */
    public ImgbbResponse uploadImage(String base64Img) throws Exception {
        String apiKey = config.getImgbbApiKey();
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

    /**
     * It generates a random string of 10 characters based on the username, checks if the loginID is
     * already in the database, and if it is, generates a new one
     * 
     * @param username the username of the user
     * @return The loginID is being returned.
     */
    public String generateLoginID(String username) throws SQLException{
        //create a random string of 10 characters based on the username
        String loginID = "";
        for(int i = 0; i < 10; i++){
            loginID += username.charAt((int)(Math.random() * username.length()));
        }
        //another randomizer
        if(Math.random() > 0.5){
            loginID += (int)(Math.random() * 10);
        }
        //check if the loginID is already in the database
        if(database.checkLoginID(loginID)){
            //if it is, generate a new one
            generateLoginID(username);
        }
        //if it isn't, return the loginID
        return loginID;
    }

    /**
     * It takes a string, and returns a string of 10 random characters from the original string
     * 
     * @param mail The email address of the user
     * @return A string of 10 characters from the email address.
     */
    public String generateForgotCode(String mail)
    {
        String code = "";
        for(int i = 0; i < 10; i++){
            code += mail.charAt((int)(Math.random() * mail.length()));
        }
        return code;
    }

    public String generateAdminCode(String username){
        String code = "";
        for(int i = 0; i < 30; i++){
            code += username.charAt((int)(Math.random() * username.length()));
        }
        if(Math.random() > 0.5){
            code += (int)(Math.random() * 10);
        }
        return code;
    }

    /**
     * It handles all the messages that are sent to the server
     * 
     * @param msg The message object that contains all the information about the message
     */
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
            case "getProfilePic":
                String profilePic = database.getProfilePic(msg.getUsername());
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
                if(draft == null){
                    draft = "";
                }
                sendTo(msg.getConn(), new Message("draft", msg.getUsername(), msg.getReceiver(), draft));
                break;
            case "saveDraft":
                database.createDraft(msg.getUsername(), msg.getData(), msg.getReceiver());
                break;
            case "getBio":
                String bio = database.getBio(msg.getData());
                if(bio == null){
                    bio = "";
                }
                sendTo(msg.getConn(), new Message("bio", msg.getUsername(), msg.getUsername(), bio));
                break;
            case "changeBio":
                database.changeBio(msg.getUsername(), msg.getData());
                break;
            case "ping":
                String t1 = msg.getDate();
                String t2 = Instant.now().toString();
                long diff = Instant.parse(t2).toEpochMilli() - Instant.parse(t1).toEpochMilli();
                sendTo(msg.getConn(), new Message("pong", msg.getUsername(), msg.getUsername(), Long.toString(diff)));
                break;
            case "forgotPassword":
                if(database.checkMailExists(msg.getData())){
                    if(database.checkIfForgotCodeAlreadyExists(msg.getData())){
                        database.removeForgotCode(msg.getData());
                    }
                    String forgotCode = generateForgotCode(msg.getData());
                    database.addForgotCode(msg.getData(), forgotCode);
                    sendTo(msg.getConn(), new Message("forgotPassword", msg.getUsername(), msg.getUsername(), "success"));
                    mailClient.sendMail("no-reply@kapindustries.it", msg.getData(), "Forgot password", recoveryMail(forgotCode));
                }else{
                    sendTo(msg.getConn(), new Message("forgetPassword", msg.getUsername(), msg.getUsername(), "Mail does not exist"));
                }
                break;
            case "checkForgotCode":
                if(database.checkForgotCode(msg.getData())){
                    sendTo(msg.getConn(), new Message("checkForgotCode", msg.getUsername(), msg.getUsername(), "success"));

                }else{
                    sendTo(msg.getConn(), new Message("checkForgotCode", msg.getUsername(), msg.getUsername(), "Wrong code"));
                }
                break;
            case "changeForgotPassword":
                
                System.out.println("Changing password for " + msg.getUsername() + " to " + msg.getData());
                String mail = msg.getUsername();
                String code = msg.getReceiver();
                if(!database.checkIfCodeIsForMail(mail, code)){
                    sendTo(msg.getConn(), new Message("changeForgotPassword", msg.getUsername(), msg.getUsername(), "Wrong code"));
                    return;
                }
                database.updatePasswordWithMail(mail, msg.getData());
                database.removeForgotCode(msg.getUsername());
                sendTo(msg.getConn(), new Message("changeForgotPassword", msg.getUsername(), msg.getUsername(), "success"));
                break;
            case "changeMail":
                if(database.checkMailExists(msg.getData())){
                    sendTo(msg.getConn(), new Message("changeMail", msg.getUsername(), msg.getUsername(), "Mail already exists"));
                }else{
                    database.changeMail(msg.getUsername(), msg.getData());
                    sendTo(msg.getConn(), new Message("changeMail", msg.getUsername(), msg.getUsername(), "success"));
                }
                break;
            case "isAdministrator":
                if(database.isAdministator(msg.getUsername())){
                    String adminCode = generateAdminCode(msg.getUsername());
                    database.addAdminCode(msg.getUsername(), adminCode);

                    sendTo(msg.getConn(), new Message("isAdministrator", msg.getUsername(), msg.getUsername(), "true"));
                    mailClient.sendMail("no-reply@kapindustries.it", database.getMail(msg.getUsername()), "Admin Code", "Hi " + msg.getUsername() + ", your admin code is " + adminCode + ". Please do not share it with anyone." + "\n If you did not request this code, contact Administrator immediately. \n This code will expire in 5 minutes.");
                }else{
                    sendTo(msg.getConn(), new Message("isAdministrator", msg.getUsername(), msg.getUsername(), "false"));
                }
                break;
            case "verifyAdminCode":
                if(database.verifyAdminCode(msg.getUsername(), msg.getData())){
                    sendTo(msg.getConn(), new Message("verifyAdminCode", msg.getUsername(), msg.getUsername(), "true"));
                }else{
                    sendTo(msg.getConn(), new Message("verifyAdminCode", msg.getUsername(), msg.getUsername(), "false"));
                }
                break;
            case "getAllUsers":
                if(!database.isAdministator(msg.getUsername()) || !database.verifyAdminCode(msg.getUsername(), msg.getReceiver())){
                    System.out.println("User " + msg.getUsername() + " tried to get all users without admin rights");
                    return;
                }

                System.out.println("User " + msg.getUsername() + " got all users");
                ArrayList<String> users = database.getAllUsers();
                for(String user : users){
                    sendTo(msg.getConn(), new Message("getAllUsers", user, database.getMail(user), database.getProfilePic(user)));
                }
                break;
            case "deleteUser":
                if(!database.isAdministator(msg.getUsername()) || !database.verifyAdminCode(msg.getUsername(), msg.getReceiver())){
                    return;
                }
                database.removeUser(msg.getData());
                break;
            case "deleteAllUsers":
                if(!database.isAdministator(msg.getUsername()) || !database.verifyAdminCode(msg.getUsername(), msg.getReceiver())){
                    return;
                }
                database.removeAllUsers();
                break;
            case "deleteAllMessages":
                if(!database.isAdministator(msg.getUsername()) || !database.verifyAdminCode(msg.getUsername(), msg.getReceiver())){
                    return;
                }
                database.removeAllMessages();
                break;
            
            case "keepAlive":
                break;
            default:
                System.out.println("Unknown operation: " + msg.getOperation());
                break;
        }
    }


    private String recoveryMail(String code){
        return "<!DOCTYPE html> <html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\"> <head> <title></title> <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/> <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\"/>  <style> * { box-sizing: border-box; } body { margin: 0; padding: 0; } a[x-apple-data-detectors] { color: inherit !important; text-decoration: inherit !important; } #MessageViewBody a { color: inherit; text-decoration: none; } p { line-height: inherit } .desktop_hide, .desktop_hide table { mso-hide: all; display: none; max-height: 0px; overflow: hidden; } @media (max-width:620px) { .desktop_hide table.icons-inner { display: inline-block !important; } .icons-inner { text-align: center; } .icons-inner td { margin: 0 auto; } .row-content { width: 100% !important; } .mobile_hide { display: none; } .stack .column { width: 100%; display: block; } .mobile_hide { min-height: 0; max-height: 0; max-width: 0; overflow: hidden; font-size: 0px; } .desktop_hide, .desktop_hide table { display: table !important; max-height: none !important; } } </style> </head> <body style=\"margin: 0; background-color: #091548; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #091548;\" width=\"100%\"> <tbody> <tr> <td> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-1\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #091548; background-image: url('https://i.ibb.co/zxHJcZH/background-2.png'); background-position: center top; background-repeat: repeat;\" width=\"100%\"> <tbody> <tr> <td> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 600px;\" width=\"600\"> <tbody> <tr> <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-left: 10px; padding-right: 10px; vertical-align: top; padding-top: 5px; padding-bottom: 15px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"image_block block-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"width:100%;padding-right:0px;padding-left:0px;padding-top:8px;\"> <div align=\"center\" class=\"alignment\" style=\"line-height:10px\"><img alt=\"Main Image\" src=\"https://i.ibb.co/fFLRS3Y/header3.png\" style=\"display: block; height: auto; border: 0; width: 232px; max-width: 100%;\" title=\"Main Image\" width=\"232\"/></div> </td> </tr> </table> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block block-3\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"padding-bottom:15px;padding-top:10px;\"> <div style=\"font-family: sans-serif\"> <div class=\"\" style=\"font-size: 14px; mso-line-height-alt: 16.8px; color: #ffffff; line-height: 1.2; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\"> <p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 16.8px;\"><span style=\"font-size:30px;\">Reset Your Password</span></p> </div> </div> </td> </tr> </table> <table border=\"0\" cellpadding=\"5\" cellspacing=\"0\" class=\"text_block block-4\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\"> <tr> <td class=\"pad\"> <div style=\"font-family: sans-serif\"> <div class=\"\" style=\"font-size: 14px; mso-line-height-alt: 21px; color: #ffffff; line-height: 1.5; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\"> <p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 21px;\">We received a request to reset your password. Don’t worry,</p> <p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 21px;\">we are here to help you.</p> </div> </div> </td> </tr> </table> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"button_block block-5\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"padding-bottom:20px;padding-left:15px;padding-right:15px;padding-top:20px;text-align:center;\"> <div align=\"center\" class=\"alignment\"> <a style=\"text-decoration:none;display:inline-block;color:#091548;background-color:#ffffff;border-radius:24px;width:auto;border-top:0px solid transparent;font-weight:undefined;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:5px;padding-bottom:5px;font-family:Varela Round, Trebuchet MS, Helvetica, sans-serif;font-size:15px;text-align:center;mso-border-alt:none;word-break:keep-all;\" target=\"_blank\"><span style=\"padding-left:25px;padding-right:25px;font-size:15px;display:inline-block;letter-spacing:normal;\"><span dir=\"ltr\" style=\"word-break: break-word;\"><span data-mce-style=\"\" dir=\"ltr\" style=\"line-height: 30px;\"><strong>Code: " + code +"</strong></span></span></span></a>  </div> </td> </tr> </table> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_block block-6\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"padding-bottom:15px;padding-left:10px;padding-right:10px;padding-top:10px;\"> <div align=\"center\" class=\"alignment\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"60%\"> <tr> <td class=\"divider_inner\" style=\"font-size: 1px; line-height: 1px; border-top: 1px solid #5A6BA8;\"><span> </span></td> </tr> </table> </div> </td> </tr> </table> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block block-7\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"padding-bottom:40px;padding-left:25px;padding-right:25px;padding-top:10px;\"> <div style=\"font-family: sans-serif\"> <div class=\"\" style=\"font-size: 14px; mso-line-height-alt: 21px; color: #7f96ef; line-height: 1.5; font-family: Varela Round, Trebuchet MS, Helvetica, sans-serif;\"> <p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 21px;\"><strong>Didn’t request a password reset?</strong></p> <p style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 21px;\">You can safely ignore this message.</p> </div> </div> </td> </tr> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tbody> <tr> <td> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 600px;\" width=\"600\"> <tbody> <tr> <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-left: 10px; padding-right: 10px; vertical-align: top; padding-top: 15px; padding-bottom: 15px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"html_block block-1\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"pad\"> <div align=\"center\" style=\"font-family:Varela Round, Trebuchet MS, Helvetica, sans-serif;text-align:center;\"><div style=\"height-top: 20px;\"> </div></div> </td> </tr> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-3\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tbody> <tr> <td> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row-content stack\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 600px;\" width=\"600\"> <tbody> <tr> <td class=\"column column-1\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\" width=\"100%\"> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"icons_block block-1\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"pad\" style=\"vertical-align: middle; color: #9d9d9d; font-family: inherit; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;\"> <table cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\"> <tr> <td class=\"alignment\" style=\"vertical-align: middle; text-align: center;\">   <table cellpadding=\"0\" cellspacing=\"0\" class=\"icons-inner\" role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; display: inline-block; margin-right: -4px; padding-left: 0px; padding-right: 0px;\">  <tr> </tr> </table> </td> </tr> </table> </td> </tr> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </td> </tr> </tbody> </table> </body> </html>";
    }


    


	
}