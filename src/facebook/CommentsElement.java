package facebook;

import java.sql.Date;
import java.sql.Timestamp;

public class CommentsElement extends FacebookElement{

	private String comment;
	private UserElement user;
	private String post_id;
	private Timestamp data_created_On;
	private int likes;

	public CommentsElement(String id, String comment, UserElement user, String post_id, Timestamp data_created_On,
			int likes) {
		super(id);
		this.comment = comment;
		this.user = user;
		this.post_id = post_id;
		this.data_created_On = data_created_On;
		this.likes = likes;
	}

	public UserElement getUser() {
		return user;
	}
	
	public void setUser(UserElement user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public Timestamp getData_created_On() {
		return data_created_On;
	}

	public void setData_created_On(Timestamp data_created_On) {
		this.data_created_On = data_created_On;
	}
	
	
	public void setLikes(int likes){
		this.likes = likes;
	}
	
	public int getLikes(){
		return likes;
	}
}
