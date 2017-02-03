/**
 * 
 */
package facebook;

import java.util.Date;

/**
 * @author alexandre
 *
 */
public class Post {

	private String post_id;
	private String message;
	private Date data_created_On;
	
	public Post(String post_id, String message, Date data_created_On) {
		super();
		this.post_id = post_id;
		this.message = message;
		this.data_created_On = data_created_On;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
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
