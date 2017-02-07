package util;

import java.io.Serializable;

public class FaceSettings implements Serializable{

	private String hostname;
	private String user;
	private String port;
	private String password;
	
	
	
	public FaceSettings(String hostname, String user, String port, String password) {
		super();
		this.hostname = hostname;
		this.user = user;
		this.port = port;
		this.password = password;
	}
	
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
