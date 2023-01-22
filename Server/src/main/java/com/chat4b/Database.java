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
                + "	password text ,\n"
                + "	ip text \n"
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
                + "	receiver text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void newUser(String username, String password, String ip) throws SQLException{
        String sql = "INSERT INTO users(username, password, ip) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, ip);
        pstmt.executeUpdate();
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

    public void updateIP(String username, String ip) throws SQLException{
        String sql = "UPDATE users SET ip = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, ip);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    public String getIP(String username) throws SQLException{
        String sql = "SELECT ip FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        return rs.getString("ip");
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

    public void addMessage(String receiver, Message msg) throws SQLException{
        String sql = "INSERT INTO messages(username, message, receiver) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getUsername());
        pstmt.setString(2, msg.getData());
        pstmt.setString(3, receiver);
        pstmt.executeUpdate();
    }

    public void removeMessage(String receiver, Message msg) throws SQLException{
        String sql = "DELETE FROM messages WHERE username = ? AND message = ? AND receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, msg.getUsername());
        pstmt.setString(2, msg.getData());
        pstmt.setString(3, receiver);
        pstmt.executeUpdate();
    }

    public ArrayList<Message> getMessages(String receiver) throws SQLException{
        ArrayList<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE receiver = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, receiver);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            messages.add(new Message(rs.getString("username"), rs.getString("message"), rs.getString("receiver")));
        }
        return messages;
    }
}
