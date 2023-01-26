package com.chat4b;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Message;
import java.util.Properties;

public class MailClient {
    private Session session;
    private String username;
    private String password;

   // Creating a new session with the given properties.
    public MailClient(String host, int port, boolean starttls, boolean auth, String user, String pass) {
        this.username = user;
        this.password = pass;
        Properties props = new Properties();
        //yandex.ru smtp settings
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * It creates a new MimeMessage object, sets the from, to, subject, and body fields, and then sends
     * the message
     * 
     * @param from The email address of the sender.
     * @param to The email address of the recipient.
     * @param subject The subject of the email.
     * @param body The body of the email.
     */
    public void sendMail(String from, String to, String subject, String body) throws MessagingException {
        System.out.println("Sending mail to " + to);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");
        Transport.send(message);
        System.out.println("Sent message successfully....");
    }
}