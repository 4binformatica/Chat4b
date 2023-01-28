package com.chat4b;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
// NOTE: If you're using NanoHTTPD >= 3.0.0 the namespace is different,
//       instead of the above import use the following:
// import org.nanohttpd.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    public WebServer(Config config) throws IOException {
        super(config.getHost(),config.getPort()+1);

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://" + this.getHostname() + ":" + this.getListeningPort() + "/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader("Server\\src\\main\\java\\com\\chat4b\\WebSocketPage\\index.html"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
        }
            in.close();
        }   catch (IOException e) {
        }
        String msg= contentBuilder.toString();
        return newFixedLengthResponse(msg);
    }
}