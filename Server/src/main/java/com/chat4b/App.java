package com.chat4b;

import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;


public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String host = "localhost";
		int port = 8887;
		createConfigFile();
		Config config = loadConfig();
		System.out.println("Starting server on " + config.getHost() + ":" + config.getPort());
		WebSocketServer server = new Server(new InetSocketAddress(config.getHost(), config.getPort()), config);
		server.run();
	}

	/**
	 * If the config file doesn't exist, create it and write the default config to it
	 */
	public static void createConfigFile() {
		File configFile = new File("config.json");
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				Config config = new Config("localhost", 8080, "mail@example.com", "password", "smtp.example.com", 587, true, true, "imgbbApiKey");
				Gson gson = new Gson();
				String json = gson.toJson(config);
				try (FileWriter writer = new FileWriter("config.json")) {
					writer.write(json);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}		

	/**
	 * It reads the config.json file and converts it into a Config object
	 * 
	 * @return The method is returning a Config object.
	 */
	public static Config loadConfig() {
		Gson gson = new Gson();
		String json = "";
		try (BufferedReader br = new BufferedReader(new FileReader("config.json"))) {
    		json = br.readLine();	
		} catch (IOException e) {
    		e.printStackTrace();
		}
		Config config = gson.fromJson(json, Config.class);
		return config;
	}
}