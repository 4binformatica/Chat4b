package com.chat4b;


public class Config {
    private String host;
    private int port;
    private String mailUser;
    private String mailPassword;
    private String mailHost;
    private int mailPort;
    private boolean mailStarttls;
    private boolean mailAuth;
    private String imgbbApiKey;

    // It's a constructor.
    public Config(String host, int port, String mailUser, String mailPassword, String mailHost, int mailPort, boolean mailStarttls, boolean mailAuth, String imgbbApiKey) {
        this.host = host;
        this.port = port;
        this.mailUser = mailUser;
        this.mailPassword = mailPassword;
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.mailStarttls = mailStarttls;
        this.mailAuth = mailAuth;
        this.imgbbApiKey = imgbbApiKey;
    }

    //getters

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getMailUser() {
        return mailUser;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailHost() {
        return mailHost;
    }

    public int getMailPort() {
        return mailPort;
    }

    public boolean isMailStarttls() {
        return mailStarttls;
    }

    public boolean isMailAuth() {
        return mailAuth;
    }

    public String getImgbbApiKey() {
        return imgbbApiKey;
    }




}
