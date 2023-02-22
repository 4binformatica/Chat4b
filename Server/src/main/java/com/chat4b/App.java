package com.chat4b;

import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;


public class App {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, KeyManagementException {
		createConfigFile();
		Config config = loadConfig();
		System.out.println("Starting server on " + config.getHost() + ":" + config.getPort());
		WebSocketServer server = new Server(new InetSocketAddress(config.getHost(), config.getPort()), config);
		// load up the key store
		String STORETYPE = "JKS";
		String KEYSTORE = Paths.get("KeyStore.jks")
			.toString();
		String STOREPASSWORD = "Jisgay69";
		String KEYPASSWORD = "Jisgay69";
	
		KeyStore ks = KeyStore.getInstance(STORETYPE);
		File kf = new File(KEYSTORE);
		ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());
	
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, KEYPASSWORD.toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ks);
	
		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
	
		server.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
	
		server.start();
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