package database;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import facebook.PostElement;
import facebook.ProfileElement;
import facebook.ReactionsElement;
import facebook.UserElement;
import facebook.App;
import facebook.CommentsElement;


public class Connector {

	private String url;
	private String user;
	private String password;

	public Connector(String url, String user, String password) {

		this.url = url;
		this.user = user;
		this.password = password;

	}


	private Connection get_connection(){

		Connection con = null;

		try{
			con = DriverManager.getConnection(this.url, this.user, this.password);
			return con;

		} catch (SQLException ex) {

			Logger lgr = Logger.getLogger(Connector.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
			return null;
		}    	
	}


	public Boolean insertPost(PostElement post, String idapp){

		Connection con = null;

		// the mysql insert statement
		String query = " INSERT INTO posts (idpost, message, created_on, profile_idprofile, app_idapp, shares) values (?, ?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, post.getId());
			preparedStmt.setString (2, post.getMessage());
			preparedStmt.setTimestamp(3, post.getData_created_On());			
			preparedStmt.setString(4, post.getProfile_id());
			preparedStmt.setString(5, idapp);
			preparedStmt.setInt(6, post.getShares());

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {

			return false;
		}

	}


	public Boolean insertComment(CommentsElement comment, int idapp){

		Connection con = null;

		// the mysql insert statement
		String query = " INSERT INTO comments (idpost, comment, created_on, posts_idpost, posts_add_idapp, users_iduser, likes) values (?, ?, ?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, comment.getId());
			preparedStmt.setString (2, comment.getComment());
			preparedStmt.setTimestamp  (3, comment.getData_created_On());
			preparedStmt.setString(4, comment.getPost_id());
			preparedStmt.setInt(5, idapp);
			preparedStmt.setString(6, comment.getUser().getId());
			preparedStmt.setInt(7, comment.getLikes());

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


	public Boolean insertProfile(ProfileElement profile, String idapp){

		Connection con = null;
		String query = null;


		// the mysql insert statement
		query = " INSERT INTO profile (idprofile, name, app_idapp)"
				+ " values (?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, profile.getId());
			preparedStmt.setString (2, profile.getName());
			preparedStmt.setString(3, idapp);

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


	public Boolean insertUser(UserElement user, int idapp){

		Connection con = null;
		String query = null;

		// the mysql insert statement
		query = " INSERT INTO users (iduser, name, app_idapp)"
				+ " values (?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, user.getId());
			preparedStmt.setString (2, user.getName());
			preparedStmt.setInt(3, idapp);

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}


	public Boolean insertApp(App app){

		Connection con = null;
		String query = null;

		// the mysql insert statement
		query = " INSERT INTO app (idapp, name, access_token, app_secret, permissions, profiles)"
				+ " values (?, ?, ?, ?, ?,?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);

			preparedStmt.setString (1, app.getIdapp());
			preparedStmt.setString (2, app.getApp_name());
			preparedStmt.setString(3, app.getAccess_token());
			preparedStmt.setString(4, app.getApp_secret());
			preparedStmt.setString(5, String.join(",", app.getPermission()));
			preparedStmt.setString(6, String.join(",", app.getProfiles()));

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


	public Boolean insertReactions(ReactionsElement reaction){

		Connection con = null;
		String query = null;

		// the mysql insert statement
		query = " INSERT INTO reactions (posts_idpost, posts_profile_idprofile, users_iduser, reaction, app_idapp)"
				+ " values (?, ?, ?, ?,?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, reaction.getIdpost());
			preparedStmt.setString(2, reaction.getIdprofile());
			preparedStmt.setString(3, reaction.getIduser());
			preparedStmt.setString(4, reaction.getReaction());
			preparedStmt.setInt(5, reaction.getIdapp());

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


	public App getAppbyId(int id){

		App app = null;

		Connection conn = null;
		String idapp = null;
		String app_name = null;
		String access_token = null;
		String app_secret = null;
		String permissions = null;
		String profiles = null;

		try
		{
			// create our mysql database connection
			conn = get_connection();

			// our SQL SELECT query. 
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM app where idpost="+ id +";";

			// create the java statement
			Statement st = conn.createStatement();


			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next())
			{
				idapp = rs.getString("idapp");
				app_name = rs.getString("name");
				access_token = rs.getString("access_token");
				app_secret = rs.getString("app_secret");
				permissions = rs.getString("permissions");
				profiles = rs.getString("profiles");

			}
			st.close();
			return new App(idapp, app_name, access_token, app_secret, permissions.split(","), profiles.split(","));
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}


	public PostElement getPostbyId(String id){

		Connection conn = null;
		String message = null;
		Timestamp dateCreated = null;
		String idprofile = null;
		int shares = 0;

		try
		{
			// create our mysql database connection
			conn = get_connection();

			// our SQL SELECT query. 
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM posts where idpost='"+ id +"'";

			// create the java statement
			Statement st = conn.createStatement();


			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next())
			{

				message = rs.getString("message");
				dateCreated = rs.getTimestamp("created_on");
				idprofile = rs.getString("profile_idprofile");
				shares = rs.getInt("shares");

			}
			st.close();
			return new PostElement(id, message, dateCreated, idprofile,shares);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}
	}


	public CommentsElement getCommentbyId(String id){

		Connection conn = null;
		String comment = null;
		Timestamp data_created_On = null;
		String post_id = null;
		int app_id ;
		String id_user = null;
		String user_name = null;
		int likes = 0;

		try
		{
			// create our mysql database connection
			conn = get_connection();

			// our SQL SELECT query. 
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM FaceAnalytics.comments inner join FaceAnalytics.users on FaceAnalytics.users.iduser=comments.users_iduser where idcomment='"+ id +"'";

			// create the java statement
			Statement st = conn.createStatement();


			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next())
			{

				comment = rs.getString("comment");
				data_created_On = rs.getTimestamp("created_on");
				post_id = rs.getString("posts_idpost");
				app_id = rs.getInt("posts_app_idapp");
				id_user = rs.getString("users_iduser");
				user_name = rs.getString("name");				
				likes = rs.getInt("likes");

			}
			st.close();

			return new CommentsElement(id_user, comment, new UserElement(id_user, user_name), post_id, data_created_On, likes);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}


	public UserElement getUserbyId(String id){

		Connection conn = null;
		String name = null;

		try
		{
			// create our mysql database connection
			conn = get_connection();

			// our SQL SELECT query. 
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM users where iduser='"+ id +"'";

			// create the java statement
			Statement st = conn.createStatement();


			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next())
			{

				name = rs.getString("name");

			}
			st.close();
			return new UserElement(id, name);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}


	public ProfileElement getProfilebyId(String id){

		Connection conn = null;
		String name = null;

		try
		{
			// create our mysql database connection
			conn = get_connection();

			// our SQL SELECT query. 
			// if you only need a few columns, specify them by name instead of using "*"
			String query = "SELECT * FROM profile where idprofile='"+ id +"'";

			// create the java statement
			Statement st = conn.createStatement();


			// execute the query, and get a java resultset
			ResultSet rs = st.executeQuery(query);

			// iterate through the java resultset
			while (rs.next())
			{

				name = rs.getString("name");

			}
			st.close();
			return new ProfileElement(id, name);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}

}