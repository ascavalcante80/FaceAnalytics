package facebook;

public class App {
	private int idapp;
	private String app_name;
	private String access_token;
	private String app_secret;
	private String [] permission;
	private String [] profiles;
	
	public App(String app_name, String access_token, String app_secret, String [] permission, String [] profiles) {
		
		this.app_name = app_name;
		this.access_token = access_token;		
		this.app_secret = app_secret;
		this.permission = permission;
		this.profiles = profiles;
	}

	public App(int idapp, String app_name, String access_token, String app_secret, String [] permission, String [] profiles) {
		
		this.idapp = idapp;
		this.app_name = app_name;
		this.access_token = access_token;		
		this.app_secret = app_secret;
		this.permission = permission;
		this.profiles = profiles;
	}
		
	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getAccess_token() {
		return access_token;
	}
	
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
		
	public String getApp_secret() {
		return app_secret;
	}
	
	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}
	
	public String[] getPermission() {
		return permission;
	}
	
	public void setPermission(String[] permission) {
		this.permission = permission;
	}
	
	public String[] getProfiles() {
		return profiles;
	}
	
	public void setProfiles(String[] profiles) {
		this.profiles = profiles;
	}	
		
}
