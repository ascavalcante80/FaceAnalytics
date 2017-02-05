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

public class PostElement extends FacebookElement{

	private String message;
	private Date data_created_On;
	private String profile_id;
	private int shares;
	
	public int getShares() {
		return shares;
	}

	public void setShares(int shares) {
		this.shares = shares;
	}

	public PostElement(String id, String message, Date data_created_On, String profile_id, int shares) {
		super(id);
		this.message = message;
		this.data_created_On = data_created_On;
		this.profile_id = profile_id;
		this.shares = shares;
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
