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
import java.util.Date;
public class Database {

    String url = "jdbc:sqlite:database.db";
    Connection connection;
    
    
    public void databaseConnect() throws SQLException{
        
        connection = DriverManager.getConnection(url);
    }

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

    public void createUserTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	username text PRIMARY KEY,\n"
                + "	password text,\n"
                + " profilepic text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    public String changeProfilePic(String username, String profilepic) throws SQLException{
        String sql = "UPDATE users SET profilepic = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, profilepic);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
        return profilepic;
    }

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

    public void removeUser(String username) throws SQLException{
        String sql = "DELETE FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

    public void updateUsername(String username, String newUsername) throws SQLException{
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newUsername);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

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

    public void updatePassword(String username, String newPassword) throws SQLException{
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

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

    public void removeMessage(String receiver, Message msg) throws SQLException{
        String sql = "DELETE FROM messages WHERE username = ? AND message = ? AND receiver = ? AND date = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getUsername());
        pstmt.setString(2, msg.getData());
        pstmt.setString(3, receiver);
        pstmt.setString(4, msg.getDate());
        pstmt.executeUpdate();
    }

    //get send and received messages
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

    public void addLoginID(String username, String loginID) throws SQLException{
        String sql = "INSERT INTO loginid(username, loginid, date) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, loginID);
        pstmt.setString(3, Instant.now().toString());
        pstmt.executeUpdate();
    }

    public void removeLoginID(String username) throws SQLException{
        String sql = "DELETE FROM loginid WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

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

    public void removeDraft(String username, String draft, String receiver) throws SQLException{
        String sql = "DELETE FROM drafts WHERE username = ? AND message = ? AND receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, draft);
        pstmt.setString(3, receiver);
        pstmt.executeUpdate();
    }

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

    public void addContact(String username, String contact) throws SQLException{
        String sql = "INSERT INTO contacts(username, contact) VALUES(?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, contact);
        pstmt.executeUpdate();
    }

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
