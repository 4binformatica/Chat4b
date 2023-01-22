package com.chat4b;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.Optional;

import org.java_websocket.server.WebSocketServer;


public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String host = "localhost";
		int port = 8887;

		WebSocketServer server = new Server(new InetSocketAddress(host, port));
		server.run();
	}
}