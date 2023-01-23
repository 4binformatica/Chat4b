package com.chat4b;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
                + "	password text \n"
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

    public boolean newUser(String username, String password) throws SQLException{
        if(checkUsername(username)){
            System.out.println("Username already exists");
            return false;
        }
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.executeUpdate();
        return true;
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

    public void updatePassword(String username, String newPassword) throws SQLException{
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    public void addMessage(Message msg) throws SQLException{
        String sql = "INSERT INTO messages(username, message, receiver, date) VALUES(?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getUsername());
        pstmt.setString(2, msg.getData());
        pstmt.setString(3, msg.getReceiver());
        pstmt.setString(4, msg.getDate());
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

    /*public ArrayList<Message> getMessages(String receiver) throws SQLException{
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, receiver);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            messages.add(new Message("message", rs.getString("username"), receiver, rs.getString("message"), rs.getString("date")));
        }
        return messages;
    }*/

    //get send and received messages
    public ArrayList<Message> getMessages(String username) throws SQLException{
        //take all the message with the username as receiver and username
        ArrayList<Message> messages = new ArrayList<>();
        System.out.println("Getting messages for " + username);
        String sql = "SELECT * FROM messages WHERE receiver = ? OR username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, username);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            messages.add(new Message("message", rs.getString("username"), rs.getString("receiver"), rs.getString("message"), rs.getString("date")));
        }
        return messages;
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
}
