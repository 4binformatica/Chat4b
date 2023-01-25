/**
 * It's a class that connects to a database and allows you to do CRUD operations on it
 */
package com.chat4b;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
public class Database {

    String url = "jdbc:sqlite:database.db";
    Connection connection;
    
    
    /**
     * This function connects to the database
     */
    public void databaseConnect() throws SQLException{
        
        connection = DriverManager.getConnection(url);
    }

    /**
     * This function creates a new database
     */
    public void createNewDatabase() throws ClassNotFoundException{
        //import driver
        Class.forName("org.sqlite.JDBC");
        //create a database
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function creates a table called users in the database if it doesn't already exist
     */
    public void createUserTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	username text PRIMARY KEY,\n"
                + "	password text,\n"
                + " profilepic text, \n"
                + " bio text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * It creates a table called messages if it doesn't already exist
     */
    public void createMessageTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + " datatype text ,\n"
                + "	username text ,\n"
                + "	message text ,\n"
                + "	receiver text, \n"
                + " date text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * It creates a table called loginID if it doesn't already exist.
     */
    public void createLoginIDTable()
    {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS loginID (\n"
                + "	username text ,\n"
                + "	loginID text, \n"
                + " date text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * It creates a table called drafts in the database if it doesn't already exist
     */
    public void createDraftTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS drafts (\n"
                + "	username text ,\n"
                + "	message text, \n"
                + " receiver text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * It creates a table called contacts in the database if it doesn't already exist
     */
    public void createContactTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS contacts (\n"
                + "	username text ,\n"
                + "	contact text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * It takes in a username, password, and profile picture, and if the username doesn't already
     * exist, it adds the user to the database
     * 
     * @param username String
     * @param password String
     * @param profilepic String
     * @return A boolean value.
     */
    public boolean newUser(String username, String password, String profilepic) throws SQLException{
        if(checkUsername(username)){
            System.out.println("Username already exists");
            return false;
        }
        String sql = "INSERT INTO users(username, password, profilepic) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, profilepic);
        pstmt.executeUpdate();
        return true;
    }

    /**
     * It takes a username and a profile picture and updates the profile picture of the user with the
     * given username
     * 
     * @param username the username of the user
     * @param profilepic /images/profilepics/profilepic.jpg
     * @return The profilepic is being returned.
     */
    public String changeProfilePic(String username, String profilepic) throws SQLException{
        String sql = "UPDATE users SET profilepic = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, profilepic);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
        return profilepic;
    }

    /**
     * It takes a username and password as input, and returns true if the username and password match a
     * record in the database, and false otherwise
     * 
     * @param username the username of the user
     * @param password "password"
     * @return A boolean value.
     */
    public boolean checkUser(String username, String password) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It takes a username as a parameter, and returns true if the username exists in the database, and
     * false if it doesn't
     * 
     * @param username the username of the user
     * @return A boolean value.
     */
    public boolean checkUsername(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It updates the bio of the user with the username passed in
     * 
     * @param username The username of the user who's bio you want to change.
     * @param bio the new bio
     */
    public void changeBio(String username, String bio) throws SQLException{
        String sql = "UPDATE users SET bio = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, bio);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    /**
     * It takes a username as a parameter, and returns the bio of the user with that username
     * 
     * @param username The username of the user you want to get the bio of.
     * @return The bio of the user.
     */
    public String getBio(String username) throws SQLException{
        String sql = "SELECT bio FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("bio");
        }
        return "";
    }

    /**
     * The function takes a username as a parameter and deletes the user from the database
     * 
     * @param username the username of the user to be deleted
     */
    public void removeUser(String username) throws SQLException{
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

    /**
     * It updates the username of a user in the database
     * 
     * @param username The username of the user you want to update.
     * @param newUsername The new username that the user wants to change to.
     */
    public void updateUsername(String username, String newUsername) throws SQLException{
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newUsername);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    /**
     * It checks if a user exists in the database
     * 
     * @param username the username of the user
     * @return A boolean value.
     */
    public boolean userExist(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It takes a username and a new password, and updates the password for the user with the given
     * username
     * 
     * @param username the username of the user whose password is to be updated
     * @param newPassword the new password that the user wants to change to
     */
    public void updatePassword(String username, String newPassword) throws SQLException{
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    /**
     * It adds a message to the database
     * 
     * @param msg Message object
     */
    public void addMessage(Message msg) throws SQLException{
        if(msg.getReceiver() == null){
            System.out.println("Receiver is null");
            return;
        }
        String sql = "INSERT INTO messages(datatype, username, message, receiver, date) VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "message");
        pstmt.setString(2, msg.getUsername());
        pstmt.setString(3, msg.getData());
        pstmt.setString(4, msg.getReceiver());
        pstmt.setString(5, msg.getDate());
        pstmt.executeUpdate();
    }

    /**
     * It takes a message object, and adds it to the database
     * 
     * @param msg Message object
     */
    public void addImage(Message msg) throws SQLException{
        if(msg.getReceiver() == null){
            System.out.println("Receiver is null");
            return;
        }
        String sql = "INSERT INTO messages(datatype, username, message, receiver, date) VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "image");
        pstmt.setString(2, msg.getUsername());
        pstmt.setString(3, msg.getData());
        pstmt.setString(4, msg.getReceiver());
        pstmt.setString(5, msg.getDate());
        pstmt.executeUpdate();
    }

    /**
     * It deletes a message from the database
     * 
     * @param receiver the receiver of the message
     * @param msg Message object
     */
    public void removeMessage(String receiver, Message msg) throws SQLException{
        String sql = "DELETE FROM messages WHERE username = ? AND message = ? AND receiver = ? AND date = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getUsername());
        pstmt.setString(2, msg.getData());
        pstmt.setString(3, receiver);
        pstmt.setString(4, msg.getDate());
        pstmt.executeUpdate();
    }

    /**
     * It takes all the messages from the database where the receiver is the username or the username
     * is the sender
     * 
     * @param username the username of the user
     * @return An ArrayList of Message objects.
     */
    public ArrayList<Message> getMessages(String username) throws SQLException{
        //take all the message with the username as receiver and username
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver = ? OR username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, username);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            messages.add(new Message(rs.getString("datatype"), rs.getString("username"), rs.getString("receiver"), rs.getString("message"), rs.getString("date")));
        }
        return messages;
    }

    

    /**
     * It takes a username and a loginID and inserts them into a table called loginid
     * 
     * @param username The username of the user
     * @param loginID The login ID of the user
     */
    public void addLoginID(String username, String loginID) throws SQLException{
        String sql = "INSERT INTO loginid(username, loginid, date) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, loginID);
        pstmt.setString(3, Instant.now().toString());
        pstmt.executeUpdate();
    }

    /**
     * This function deletes a row from the loginid table where the username is equal to the username
     * passed in as a parameter
     * 
     * @param username the username of the user to be deleted
     */
    public void removeLoginID(String username) throws SQLException{
        String sql = "DELETE FROM loginid WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

    /**
     * It takes a username as a parameter, and returns the loginid associated with that username
     * 
     * @param username the username of the user
     * @return The loginID is being returned.
     */
    public String getLoginID(String username) throws SQLException{
        String sql = "SELECT loginid FROM loginid WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("loginid");
        }
        return null;
    }

    /**
     * It checks if the loginID exists in the database
     * 
     * @param loginID the loginID that the user entered
     * @return A boolean value.
     */
    public boolean checkLoginID(String loginID) throws SQLException{
        String sql = "SELECT * FROM loginid WHERE loginid = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, loginID);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It checks if the loginid is outdated
     * 
     * @param username The username of the user
     * @return A boolean value.
     */
    public boolean checkLoginIDOutdated(String username) throws SQLException{
        String sql = "SELECT * FROM loginid WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            String date = rs.getString("date");
            Instant instant = Instant.parse(date);
            Instant now = Instant.now();
            Duration duration = Duration.between(instant, now);
            if(duration.getSeconds() > 3600){
                return true;
            }
        }
        return false;
    }

    /**
     * It deletes the current draft between the user and the receiver if there is one already and
     * replaces it with the new one
     * 
     * @param username the username of the user who is sending the message
     * @param draft the message that the user is trying to send
     * @param receiver the person who the user is sending the message to
     */
    public void createDraft(String username, String draft, String receiver) throws SQLException{
        //delete current draft between the user and the receiver if there is one already delete it and replace it with the new one
        String sql = "DELETE FROM drafts WHERE username = ? AND receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, receiver);
        pstmt.executeUpdate();
        //add the new draft
        sql = "INSERT INTO drafts(username, message, receiver) VALUES(?,?,?)";
        pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, draft);
        pstmt.setString(3, receiver);
        pstmt.executeUpdate();
        
    }

    /**
     * It deletes a draft from the database
     * 
     * @param username the username of the user who sent the draft
     * @param draft the message
     * @param receiver the person who the message is being sent to
     */
    public void removeDraft(String username, String draft, String receiver) throws SQLException{
        String sql = "DELETE FROM drafts WHERE username = ? AND message = ? AND receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, draft);
        pstmt.setString(3, receiver);
        pstmt.executeUpdate();
    }

    /**
     * It returns the draft message of a user to a specific receiver
     * 
     * @param username the username of the person who is sending the message
     * @param receiver the person who the message is being sent to
     * @return The message that is saved in the drafts table.
     */
    public String getDraft(String username, String receiver) throws SQLException{
        String sql = "SELECT message FROM drafts WHERE username = ? AND receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, receiver);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("message");
        }
        return null;
    }

    /**
     * It returns the profile picture of the user with the given username
     * 
     * @param username The username of the user whose profile picture you want to get.
     * @return The profile picture of the user.
     */
    public String getProfilePic(String username) throws SQLException{
        String sql = "SELECT profilepic FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("profilepic");
        }
        return null;
    }

    /**
     * It takes a username and a contact and adds the contact to the contacts table
     * 
     * @param username the username of the user who is adding the contact
     * @param contact the contact to be added
     */
    public void addContact(String username, String contact) throws SQLException{
        String sql = "INSERT INTO contacts(username, contact) VALUES(?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, contact);
        pstmt.executeUpdate();
    }

    /**
     * It removes a contact from the contacts table and removes all messages between the two users from
     * the messages table
     * 
     * @param username the username of the user who is removing the contact
     * @param contact the contact to be removed
     */
    public void removeContact(String username, String contact) throws SQLException{
        String sql = "DELETE FROM contacts WHERE username = ? AND contact = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, contact);
        pstmt.executeUpdate();

        //remove all messages between the two users
        String sql2 = "DELETE FROM messages WHERE (username = ? AND receiver = ?) OR (username = ? AND receiver = ?)";
        PreparedStatement pstmt2 = connection.prepareStatement(sql2);
        pstmt2.setString(1, username);
        pstmt2.setString(2, contact);
        pstmt2.setString(3, contact);
        pstmt2.setString(4, username);
        pstmt2.executeUpdate();
    }

    /**
     * It returns an ArrayList of Strings that contains all the contacts of a user
     * 
     * @param username the username of the user who's contacts you want to get
     * @return An ArrayList of Strings.
     */
    public ArrayList<String> getContacts(String username) throws SQLException{
        ArrayList<String> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            contacts.add(rs.getString("contact"));
        }
        return contacts;
    }

    /**
     * It deletes a message from the database
     * 
     * @param receiver " + receiver + " date: " + date
     * @param date "2016-03-01"
     * @return A boolean value.
     */
    public boolean removeMessageByDate(String receiver, String date) throws SQLException{
        System.out.println("receiver: " + receiver + " date: " + date);
        String sql = "DELETE FROM messages WHERE receiver = ? AND date = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, receiver);
        pstmt.setString(2, date);
        pstmt.executeUpdate();
        return true;
    }
}
