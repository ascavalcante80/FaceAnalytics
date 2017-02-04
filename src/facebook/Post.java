/**
 * 
 */
package facebook;

import java.util.Calendar;
import java.sql.Date;

/**
 * @author alexandre
 *
 */
public class Post extends FacebookElement{

	private String message;
	private Date data_created_On;
	private String profile_id;
	
	public Post(String id, String message, Date data_created_On, String profile_id) {
		super(id);
		this.message = message;
		this.data_created_On = data_created_On;
		this.profile_id = profile_id;
	}

	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getData_created_On() {
		return data_created_On;
	}

	public void setData_created_On(Date data_created_On) {
		this.data_created_On = data_created_On;
	}
	
	
}
