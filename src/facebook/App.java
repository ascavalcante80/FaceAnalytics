package facebook;

public class App {

	private String access_token;
	private String app_id;
	private String app_secret;
	private String [] permission;
	private String [] profiles;
	
	public App(String access_token, String app_id, String app_secret, String[] permission, String[] profiles) {
		super();
		this.access_token = access_token;
		this.app_id = app_id;
		this.app_secret = app_secret;
		this.permission = permission;
		this.profiles = profiles;
	}
	
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
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
