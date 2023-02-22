package com.chat4b.Message;

public class ClientInfo {
    private String ip;
    private String version;
    private String os;
    private String browser;

    public ClientInfo(String ip, String version, String os, String browser) {
        this.ip = ip;
        this.version = version;
        this.os = os;
        this.browser = browser;
    }

    public ClientInfo() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }


}
