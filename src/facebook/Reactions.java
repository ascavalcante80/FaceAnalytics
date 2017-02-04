package facebook;

public class Reactions {
	
	private String idpost;
	private String iduser;
	private String idprofile;
	int idapp;
	String reaction;
	
	public Reactions(String idpost, String iduser, String idprofile, int idapp, String reaction) {
		super();
		this.idpost = idpost;
		this.iduser = iduser;
		this.idprofile = idprofile;
		this.idapp = idapp;
		this.reaction = reaction;
	}
	
	public String getIdpost() {
		return idpost;
	}
	public void setIdpost(String idpost) {
		this.idpost = idpost;
	}
	public String getIduser() {
		return iduser;
	}
	public void setIduser(String iduser) {
		this.iduser = iduser;
	}
	public String getIdprofile() {
		return idprofile;
	}
	public void setIdprofile(String idprofile) {
		this.idprofile = idprofile;
	}
	public int getIdapp() {
		return idapp;
	}
	public void setIdapp(int idapp) {
		this.idapp = idapp;
	}
	public String getReaction() {
		return reaction;
	}
	public void setReaction(String reaction) {
		this.reaction = reaction;
	}
	
}
