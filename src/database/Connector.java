package database;

import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import facebook.Post;
import facebook.Profile;
import facebook.Reactions;
import facebook.User;
import facebook.App;


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

	public Boolean insertPost(Post post, int idapp){

		Connection con = null;

		// the mysql insert statement
		String query = " INSERT INTO posts (idpost, message, created_on, profile_idprofile, app_idapp) values (?, ?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, post.getId());
			preparedStmt.setString (2, post.getMessage());
			preparedStmt.setDate  (3, post.getData_created_On());
			preparedStmt.setString(4, post.getProfile_id());
			preparedStmt.setInt(5, idapp);

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

	public Boolean insertProfile(Profile profile, int idapp){

		Connection con = null;
		String query = null;


		// the mysql insert statement
		query = " INSERT INTO profile (idprofile, first_name, last_name, app_idapp)"
				+ " values (?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, profile.getId());
			preparedStmt.setString (2, profile.getFirst_name());
			preparedStmt.setString (3, profile.getLast_name());
			preparedStmt.setInt(4, idapp);

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

	public Boolean insertUser(User user, int idapp){

		Connection con = null;
		String query = null;

		// the mysql insert statement
		query = " INSERT INTO users (iduser, first_name, last_name, app_idapp)"
				+ " values (?, ?, ?, ?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, user.getId());
			preparedStmt.setString (2, user.getFirst_name());
			preparedStmt.setString (3, user.getLast_name());
			preparedStmt.setInt(4, idapp);

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

	public int insertApp(App app){
	
		Connection con = null;
		String query = null;

		// the mysql insert statement
		query = " INSERT INTO app (app_name, access_token, app_secret, permissions, profiles)"
				+ " values (?, ?, ?, ?,?)";

		// create the mysql insert preparedstatement
		java.sql.PreparedStatement preparedStmt;
		try {
			con = get_connection();
			preparedStmt = con.prepareStatement(query);
			preparedStmt.setString (1, app.getApp_name());
			preparedStmt.setString(2, app.getAccess_token());
			preparedStmt.setString(3, app.getApp_secret());
			preparedStmt.setString(4, app.getPermission().toString());
			preparedStmt.setString(5, app.getProfiles().toString());

			// execute the preparedstatement
			preparedStmt.execute();
			con.close();
			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
		
	public Boolean insertReactions(Reactions reaction){
		
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
		int idapp = 0;
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
				idapp = rs.getInt("idapp");
				app_name = rs.getString("app_name");
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
	
 	public Post getPostbyId(String id){

		Connection conn = null;
		String message = null;
		Date dateCreated = null;
		String idprofile = null;

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
				dateCreated = rs.getDate("created_on");
				idprofile = rs.getString("profile_idprofile");

			}
			st.close();
			return new Post(id, message, dateCreated, idprofile);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}

	public User getUserbyId(String id){

		Connection conn = null;
		String first_name = null;
		String last_name = null;

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

				first_name = rs.getString("first_name");
				last_name = rs.getString("last_name");
			}
			st.close();
			return new User(id, first_name, last_name);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}

	}

	public Profile getProfilebyId(String id){
		
		Connection conn = null;
		String first_name = null;
		String last_name = null;

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

				first_name = rs.getString("first_name");
				last_name = rs.getString("last_name");
			}
			st.close();
			return new Profile(id, first_name, last_name);
		}
		catch (Exception e)
		{
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
			return null;
		}
	
	}
			
	public static void main(String [] args){

		Connector c = new Connector("jdbc:mysql://localhost:3306/FaceAnalytics?autoReconnect=true&useSSL=false", "root", "20060907jl");

		// create a sql date object so we can use it in our INSERT statement
		Calendar calendar = Calendar.getInstance();
		java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
		App app = new App("tese0", "tes1","test2", new String[]{"a","b","c"}, new String[]{"a","b","c"});
		c.insertApp(app);

		Reactions reaction = new Reactions("1","1","1",1, "fooooda");
		c.insertReactions(reaction);

		System.out.println("o");
	}

}