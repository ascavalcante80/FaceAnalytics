package util;

public class FaceSettings {

	private String url;
	private String user;
	private String port;
	private String password;
	
	
	
	public FaceSettings(String url, String user, String port, String password) {
		super();
		this.url = url;
		this.user = user;
		this.port = port;
		this.password = password;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
