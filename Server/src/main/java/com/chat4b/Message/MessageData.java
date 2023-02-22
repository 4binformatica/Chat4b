package com.chat4b.Message;

import com.google.gson.annotations.Expose;

public class MessageData {
    @Expose
    private String text;
    @Expose
    private String image;

    public MessageData(String text, String image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
