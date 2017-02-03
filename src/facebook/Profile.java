/**
 * 
 */
package facebook;

/**
 * @author alexandre
 *
 */
public class Profile {

	private String profile_id;
	private String name;
	
	public Profile(String profile_id, String name) {
		super();
		this.profile_id = profile_id;
		this.name = name;
	}

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
