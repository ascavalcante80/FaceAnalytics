package facebook;

import java.sql.Date;

public class CommentsElement extends FacebookElement{

	private String comment;
	private String user_id;

	private String post_id;
	private Date data_created_On;
	private int likes;
	
	public CommentsElement(String id, String comment, String user_id, String post_id,
			Date data_created_On, int likes) {
		super(id);
		this.comment = comment;
		this.user_id = user_id;
		this.post_id = post_id;
		this.data_created_On = data_created_On;
		this.likes = likes;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public Date getData_created_On() {
		return data_created_On;
	}

	public void setData_created_On(Date data_created_On) {
		this.data_created_On = data_created_On;
	}
	
	
	public void setLikes(int likes){
		this.likes = likes;
	}
	
	public int getLikes(){
		return likes;
	}
}
