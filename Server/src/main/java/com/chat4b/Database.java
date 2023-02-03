/**
 * It's a class that connects to a database and allows you to do CRUD operations on it
 */
package com.chat4b;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
                + " mail text,\n"
                + " profilepic text, \n"
                + " bio text, \n"
                + " admin text \n"
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
                + " id text, \n"
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
     * It creates a table called forgotpassword in the database if it doesn't already exist
     */
    public void createForgotPasswordTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS forgotpassword (\n"
                + "	mail text ,\n"
                + "	code text \n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createAdminCodeTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS admincode (\n"
                + " username text ,\n"
                + "	code text, \n"
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

    public void createMailVerificationTable(){
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS mailverification (\n"
                + " username text ,\n"
                + "	mail text, \n"
                + "	code text, \n"
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

        //create group table with groupname, members, admin
    public void createGroupTable() throws SQLException{
        String sql = "CREATE TABLE IF NOT EXISTS groups (\n"
                + " groupname text ,\n"
                + " username text, \n"
                + " role text \n"
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
     * It takes 4 strings as parameters and inserts them into the database
     * 
     * @param username String
     * @param password String
     * @param profilepic The path to the profile picture
     * @param mail String
     * @return A boolean value.
     */
    public boolean newUser(String username, String password, String profilepic, String mail) throws SQLException{
        String sql = "INSERT INTO users(username, password, profilepic, mail) VALUES(?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        pstmt.setString(3, profilepic);
        pstmt.setString(4, mail);
        pstmt.executeUpdate();
        return true;
    }

    public boolean checkUsernameExists(String username) throws SQLException{
        String sql = "SELECT username FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public void changeUsername(String username, String newUsername) throws SQLException{
        String sql = "UPDATE users SET username = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newUsername);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
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
     * It checks if the user is an administrator
     * 
     * @param username The username of the user you want to check.
     * @return A boolean value.
     */
    public boolean isAdministator(String username) throws SQLException{
        String sql = "SELECT * FROM users WHERE username = ? AND admin = 1";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public boolean verifyAdminCode(String username, String code) throws SQLException{
        String sql = "SELECT * FROM admincode WHERE username = ? AND code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, code);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            //check if the code is outdated 5 minutes
            String date = rs.getString("date");
            Instant instant = Instant.parse(date);
            Instant now = Instant.now();
            Duration duration = Duration.between(instant, now);
            if(duration.toMinutes() > 5){
                return false;
            }
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
     * It gets the mail of a user from the database
     * 
     * @param username The username of the user you want to get the mail from.
     * @return The mail of the user.
     */
    public String getMail(String username) throws SQLException{
        String sql = "SELECT mail FROM users WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("mail");
        }
        return "";
    }

    public void changeMail(String username, String mail) throws SQLException{
        String sql = "UPDATE users SET mail = ? WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        pstmt.setString(2, username);
        pstmt.executeUpdate();
    }

    public boolean checkMailExists(String mail) throws SQLException{
        String sql = "SELECT * FROM users WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
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
     * It returns an ArrayList of all the usernames in the database
     * 
     * @return An ArrayList of Strings
     */
    public ArrayList <String> getAllUsers() throws SQLException{
        ArrayList <String> users = new ArrayList <String>();
        String sql = "SELECT username FROM users";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            users.add(rs.getString("username"));
        }
        return users;
    }

    /**
     * It adds a code to the database
     * 
     * @param username String
     * @param code the code that the user will enter to become an admin
     */
    public void addAdminCode(String username, String code) throws SQLException{
        if(checkIfUserAlreadyHaveAdminCode(username)){
            removeAdminCode(username);
        }
        String sql = "INSERT INTO admincode(username, code, date) VALUES(?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, code);
        pstmt.setString(3, Instant.now().toString());
        pstmt.executeUpdate();
    }

    public void removeAdminCode(String username) throws SQLException{
        String sql = "DELETE FROM admincode WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.executeUpdate();
    }

    public boolean checkIfUserAlreadyHaveAdminCode(String username) throws SQLException{
        String sql = "SELECT * FROM admincode WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * This function deletes all the rows in the users table
     */
    public void removeAllUsers() throws SQLException{
        String sql = "DELETE FROM users";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.executeUpdate();
    }

    /**
     * This function deletes all the messages from the database
     */
    public void removeAllMessages() throws SQLException{
        String sql = "DELETE FROM messages";
        PreparedStatement pstmt = connection.prepareStatement(sql);
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
     * It updates the password of a user with the mail address mail to the new password newPassword
     * 
     * @param mail the mail of the user
     * @param newPassword the new password
     */
    public void updatePasswordWithMail(String mail, String newPassword) throws SQLException{
        String sql = "UPDATE users SET password = ? WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, mail);
        pstmt.executeUpdate();
    }

    /**
     * It returns the username of the user with the given mail
     * 
     * @param mail the mail of the user
     * @return The username of the user with the given mail.
     */
    public String getUserByMail(String mail) throws SQLException{
        String sql = "SELECT username FROM users WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("username");
        }
        return "";
    }

    /**
     * It adds a mail and a code to the forgotpassword table
     * 
     * @param mail the email address of the user
     * @param code the code that is generated by the forgotPassword method
     */
    public void addForgotCode(String mail, String code) throws SQLException{
        String sql = "INSERT INTO forgotpassword(mail, code) VALUES(?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        pstmt.setString(2, code);
        pstmt.executeUpdate();
    }

    /**
     * It checks if the code exists in the database
     * 
     * @param code The code that the user entered
     * @return A boolean value.
     */
    public boolean checkForgotCode(String code) throws SQLException{
        String sql = "SELECT * FROM forgotpassword WHERE code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, code);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public String getMailFromCode(String code) throws SQLException{
        String sql = "SELECT mail FROM forgotpassword WHERE code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, code);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getString("mail");
        }
        return "";
    }

    /**
     * It deletes the row from the database where the mail is equal to the mail that is passed in as a
     * parameter
     * 
     * @param mail the mail of the user
     */
    public void removeForgotCode(String mail) throws SQLException{
        String sql = "DELETE FROM forgotpassword WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        pstmt.executeUpdate();
    }

    /**
     * It checks if the mail exists in the database
     * 
     * @param mail the email address of the user
     * @return A boolean value.
     */
    public boolean checkIfForgotCodeAlreadyExists(String mail) throws SQLException{
        String sql = "SELECT * FROM forgotpassword WHERE mail = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It checks if the code is for the mail
     * 
     * @param mail The mail of the user
     * @param code The code that the user has entered
     * @return A boolean value.
     */
    public boolean checkIfCodeIsForMail(String mail, String code) throws SQLException{
        String sql = "SELECT * FROM forgotpassword WHERE mail = ? AND code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, mail);
        pstmt.setString(2, code);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
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
        int id = getLatestMessageId() + 1;
        
        String sql = "INSERT INTO messages(datatype, username, message, receiver, id, date) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "message");
        pstmt.setString(2, msg.getUsername());
        pstmt.setString(3, msg.getData());
        pstmt.setString(4, msg.getReceiver());
        pstmt.setString(5, Integer.toString(id));
        pstmt.setString(6, msg.getDate());
        pstmt.executeUpdate();
    }

    public int getLatestMessageId() throws SQLException{
        String sql = "SELECT id FROM messages ORDER BY id DESC LIMIT 1";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return rs.getInt("id");
        }
        return 0;
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
        if(contact == null || contact.equals("")){
            return;
        }
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

    /**
     * It adds a verification code to the database
     * 
     * @param username the username of the user
     * @param mail the email address of the user
     * @param code the verification code
     */
    public void addVerificationCode(String username, String mail, String code) throws SQLException{
        String sql = "INSERT INTO mailverification(username, mail, code) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, mail);
        pstmt.setString(3, code);
        pstmt.executeUpdate();
    }

    /**
     * It checks if a verification code exists in the database
     * 
     * @param username The username of the user
     * @param mail the mail that the user entered
     * @param code The code that the user has entered
     * @return A boolean value.
     */
    public boolean checkIfVerificationCodeExists(String username, String code) throws SQLException{
        String sql = "SELECT * FROM mailverification WHERE username = ? AND code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, code);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    /**
     * It deletes a row from the verification table where the username, mail and code are equal to the
     * parameters
     * 
     * @param username The username of the user
     * @param mail the email address of the user
     * @param code The code that was sent to the user's email
     */
    public void removeVerificationCode(String username, String code) throws SQLException{
        String sql = "DELETE FROM mailverification WHERE username = ? AND code = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, code);
        pstmt.executeUpdate();
    }

    public boolean needVerification(String username) throws SQLException{
        String sql = "SELECT * FROM mailverification WHERE username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }


    //*GROUP */




    public boolean isAdminOfGroup(String groupName, String username) throws SQLException{
        //check if user is admin of group
        String sql = "SELECT * FROM groups WHERE groupname = ? AND username = ? AND role = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        pstmt.setString(2, username);
        pstmt.setString(3, "admin");
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public boolean isInGroup(String groupName, String username) throws SQLException{
        //check if user is in group
        String sql = "SELECT * FROM groups WHERE groupname = ? AND username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        pstmt.setString(2, username);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }        
        return false;
    }

    public void addUserToGroup(String groupName, String username) throws SQLException{
        //add user to group
        if(!userExist(username) || isInGroup(groupName, username)){
            return;
        }
        String sql = "INSERT INTO groups(groupname, username, role) VALUES(?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        pstmt.setString(2, username);
        pstmt.setString(3, "member");
        pstmt.executeUpdate();
    }

    public void removeUserFromGroup(String groupName, String username) throws SQLException{
        //remove user from group
        String sql = "DELETE FROM groups WHERE groupname = ? AND username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        pstmt.setString(2, username);
        pstmt.executeUpdate();        
    }

    public void setGroupAdmin(String groupName, String username) throws SQLException{
        //set user as admin of group
        String sql = "UPDATE groups SET role = ? WHERE groupname = ? AND username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "admin");
        pstmt.setString(2, groupName);
        pstmt.setString(3, username);
        pstmt.executeUpdate();
    }

    public void revokeGroupAdmin(String groupName, String username) throws SQLException{
        //revoke admin rights from user
        String sql = "UPDATE groups SET role = ? WHERE groupname = ? AND username = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, "member");
        pstmt.setString(2, groupName);
        pstmt.setString(3, username);
        pstmt.executeUpdate();        
    }

    public boolean checkIfGroupExists(String groupName) throws SQLException{
        //check if group exists
        String sql = "SELECT * FROM groups WHERE groupname = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    public void deleteGroup(String groupName) throws SQLException{
        //delete group
        String sql = "DELETE FROM groups WHERE groupname = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, groupName);
        pstmt.executeUpdate();
    }
    





}


