package com.chat4b;

/**
 * ImgbbResponse
 */
public class ImgbbResponse {
    Data data;
    //private String success;
    //private String status;

    public String getUrl() {
        if (data != null) {
            return data.getUrl();
        }
        return null;
    }

    //getters and setters
    class Data{
        private String url;
        public String getUrl() {
            return url;
        }
    }
}