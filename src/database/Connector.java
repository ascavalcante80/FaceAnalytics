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
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Connector {
    
    private String url;
    private String local_host;
    private String user;
    private String password;
    private String idapp = null;
    
    public Connector(String url, String user, String password) {
        
        this.local_host = url + "/?autoReconnect=true&useSSL=false";
        this.url = url + "/FaceAnalytics?autoReconnect=true&useSSL=false";
        this.user = user;
        this.password = password;
       
    }
    
    
    public String getIdapp(){
        return idapp;
    }
    
    
    public void setIdapp(String idapp){
        this.idapp = idapp;
    }
    
    public Boolean build_database(){
        Connection conn = null;
        
        // the mysql insert statement
        String [] queries = {"SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;","SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;","SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';","DROP SCHEMA IF EXISTS `FaceAnalytics` ;","CREATE SCHEMA IF NOT EXISTS `FaceAnalytics` DEFAULT CHARACTER SET utf8 ;","SET SQL_MODE=@OLD_SQL_MODE;","SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;","SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;"};
                
        String []queries_db = {"USE `FaceAnalytics` ;","DROP TABLE IF EXISTS `FaceAnalytics`.`app` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`app` (  `idapp` VARCHAR(100) NOT NULL,  `name` VARCHAR(200) NULL,  `access_token` VARCHAR(300) NULL,  `app_secret` VARCHAR(100) NULL,  `permissions` VARCHAR(2000) NULL,  `profiles` VARCHAR(2000) NULL,  PRIMARY KEY (`idapp`))ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;","DROP TABLE IF EXISTS `FaceAnalytics`.`users` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`users` (  `iduser` VARCHAR(60) NOT NULL,  `name` VARCHAR(100) NOT NULL,  `app_idapp` VARCHAR(100) NOT NULL,  PRIMARY KEY (`iduser`),  INDEX `fk_users_app1_idx` (`app_idapp` ASC),  CONSTRAINT `fk_users_app1`    FOREIGN KEY (`app_idapp`)    REFERENCES `FaceAnalytics`.`app` (`idapp`)    ON DELETE NO ACTION    ON UPDATE NO ACTION)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;","DROP TABLE IF EXISTS `FaceAnalytics`.`profile` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`profile` (  `idprofile` VARCHAR(60) NOT NULL,  `name` VARCHAR(100) NOT NULL,  `app_idapp` VARCHAR(100) NOT NULL,  PRIMARY KEY (`idprofile`),  INDEX `fk_profile_app1_idx` (`app_idapp` ASC),  CONSTRAINT `fk_profile_app1`    FOREIGN KEY (`app_idapp`)    REFERENCES `FaceAnalytics`.`app` (`idapp`)    ON DELETE NO ACTION    ON UPDATE NO ACTION)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;","DROP TABLE IF EXISTS `FaceAnalytics`.`posts` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`posts` (  `idpost` VARCHAR(60) NOT NULL,  `message` VARCHAR(5000) NOT NULL,  `created_on` DATE NOT NULL,  `profile_idprofile` VARCHAR(60) NOT NULL,  `shares` INT NULL,  `app_idapp` VARCHAR(100) NOT NULL,  PRIMARY KEY (`idpost`),  INDEX `fk_posts_profile_idx` (`profile_idprofile` ASC),  INDEX `fk_posts_app1_idx` (`app_idapp` ASC),  CONSTRAINT `fk_posts_profile`    FOREIGN KEY (`profile_idprofile`)    REFERENCES `FaceAnalytics`.`profile` (`idprofile`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `fk_posts_app1`    FOREIGN KEY (`app_idapp`)    REFERENCES `FaceAnalytics`.`app` (`idapp`)    ON DELETE NO ACTION    ON UPDATE NO ACTION)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;","DROP TABLE IF EXISTS `FaceAnalytics`.`reactions` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`reactions` (  `users_iduser` VARCHAR(60) NOT NULL,  `reaction` VARCHAR(45) NOT NULL,  `posts_idpost` VARCHAR(60) NOT NULL,  `app_idapp` VARCHAR(100) NOT NULL,  PRIMARY KEY (`users_iduser`),  INDEX `fk_reactions_posts1_idx` (`posts_idpost` ASC),  INDEX `fk_reactions_app1_idx` (`app_idapp` ASC),  CONSTRAINT `fk_reactions_users1`    FOREIGN KEY (`users_iduser`)    REFERENCES `FaceAnalytics`.`users` (`iduser`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `fk_reactions_posts1`    FOREIGN KEY (`posts_idpost`)    REFERENCES `FaceAnalytics`.`posts` (`idpost`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `fk_reactions_app1`    FOREIGN KEY (`app_idapp`)    REFERENCES `FaceAnalytics`.`app` (`idapp`)    ON DELETE NO ACTION    ON UPDATE NO ACTION)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;","DROP TABLE IF EXISTS `FaceAnalytics`.`comments` ;","CREATE TABLE IF NOT EXISTS `FaceAnalytics`.`comments` (  `idcomment` VARCHAR(60) NOT NULL,  `comment` VARCHAR(5000) NOT NULL,  `created_on` DATE NOT NULL,  `posts_idpost` VARCHAR(60) NOT NULL,  `users_iduser` VARCHAR(60) NOT NULL,  `likes` INT NULL,  `app_idapp` VARCHAR(100) NOT NULL,  PRIMARY KEY (`idcomment`),  INDEX `fk_comments_posts1_idx` (`posts_idpost` ASC),  INDEX `fk_comments_users1_idx` (`users_iduser` ASC),  INDEX `fk_comments_app1_idx` (`app_idapp` ASC),  CONSTRAINT `fk_comments_posts1`    FOREIGN KEY (`posts_idpost`)    REFERENCES `FaceAnalytics`.`posts` (`idpost`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `fk_comments_users1`    FOREIGN KEY (`users_iduser`)    REFERENCES `FaceAnalytics`.`users` (`iduser`)    ON DELETE CASCADE    ON UPDATE CASCADE,  CONSTRAINT `fk_comments_app1`    FOREIGN KEY (`app_idapp`)    REFERENCES `FaceAnalytics`.`app` (`idapp`)    ON DELETE NO ACTION    ON UPDATE NO ACTION)ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;"} ;
        
        // create the mysql insert preparedstatement
        java.sql.PreparedStatement preparedStmt;
        try {
            conn = DriverManager.getConnection(this.local_host, this.user, this.password);
            Statement stmt = conn.createStatement();
            for (String query: queries){
            
                stmt.executeUpdate(query);
            }
            
            conn.close();
            
            conn= DriverManager.getConnection(this.url, this.user, this.password);
            stmt = conn.createStatement();
            for (String query: queries_db){            
                stmt.executeUpdate(query);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }        
    }
    
    public Boolean db_exists(){
        LinkedHashMap<String, App> app_list = new LinkedHashMap<>();
        Connection conn = null;
        String db = null;
        try {
            conn = DriverManager.getConnection(this.local_host, this.user, this.password);
            String b= conn.getCatalog();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {            
            String query = "show databases;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                
                if(rs.getString("Database").equals("FaceAnalytics")){
                    return true;
                }
            }
            
            st.close();
        }
        catch (Exception e)
        {
            String err = e.getMessage();
            System.err.println(err);
        }
        return false;
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
    
    
    public Boolean insertComment(CommentsElement comment, String idapp){
        
        Connection con = null;
        
        // the mysql insert statement
        String query = " INSERT INTO comments (idcomment, comment, created_on, posts_idpost, app_idapp, users_iduser, likes) values (?, ?, ?, ?, ?, ?, ?)";
        
        // create the mysql insert preparedstatement
        java.sql.PreparedStatement preparedStmt;
        try {
            con = get_connection();
            preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, comment.getId());
            preparedStmt.setString (2, comment.getComment());
            preparedStmt.setTimestamp  (3, comment.getData_created_On());
            preparedStmt.setString(4, comment.getPost_id());
            preparedStmt.setString(5, idapp);
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
    
    
    public Boolean insertUser(UserElement user, String idapp){
        
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
            preparedStmt.setString(3, idapp);
            
            // execute the preparedstatement
            preparedStmt.execute();
            con.close();
            return true;
            
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(user.getId() +   ": user already saved in the database");
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
    
    
    public LinkedHashMap<String, App> selectAllApps(){
        LinkedHashMap<String, App> app_list = new LinkedHashMap<>();
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
            String query = "SELECT * FROM app;";
            
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
                
                app_list.put(idapp, new App(idapp, app_name, access_token, app_secret, permissions.split(","), profiles.split(",")));
            }
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        return app_list;
    }
    
    
    public LinkedHashMap<String, PostElement> selectPostsWhere(String field, String value){
        LinkedHashMap <String, PostElement> result = new LinkedHashMap();
        Connection conn = null;
        
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // prepare query
            String query = "SELECT * FROM FaceAnalytics.posts WHERE posts." + field +"=\""+value+"\";";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String id_post = rs.getString("idpost");
                String message = rs.getString("message");
                Timestamp created_on = new Timestamp(rs.getDate("created_on").getTime());
                String id_profile = rs.getString("profile_idprofile");
                int shares = rs.getInt("shares");
                
                result.put(id_post, new PostElement(id_post, message, created_on, id_profile, shares));
            }
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        
        return result;
    }
    
    public LinkedHashMap <String, CommentsElement> selectCommentsWhere(String field, String value){
        
        LinkedHashMap <String, CommentsElement> result = new LinkedHashMap();
        Connection conn = null;
        
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // prepare query
            String query = "SELECT * FROM FaceAnalytics.comments WHERE comments." + field +"=\"" + value + "\" and app_idapp=\"" + idapp +"\";;";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String idcomment = rs.getString("idcomment");
                String comment = rs.getString("comment");
                Timestamp created_on = new Timestamp(rs.getDate("created_on").getTime());
                String id_post = rs.getString("posts_idpost");
                String id_user = rs.getString("users_iduser");
                int likes = rs.getInt("likes");
                
                result.put(idcomment, new CommentsElement(idcomment, comment, new UserElement(id_user,""), id_post,created_on, likes));
            }
            st.close();
            conn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        
        return result;
        
        
    }
    
    public LinkedHashMap <String, App> selectAppWhere(String field, String value){
        LinkedHashMap <String, App> result = new LinkedHashMap();
        Connection conn = null;
        
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // prepare query
            String query = "SELECT * FROM FaceAnalytics.app WHERE app." + field +"=\""+value+"\" and app.idapp=\"" + idapp +"\";";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String idapp = rs.getString("idapp");
                String name = rs.getString("name");
                String access_token = rs.getString("access_token");
                String app_secret = rs.getString("app_secret");
                String permissions = rs.getString("permissions");
                String profiles = rs.getString("profiles");
                
                result.put(idapp, new App(idapp, name, access_token, app_secret, permissions.split(","), profiles.split(",")));
                
            }
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        
        return result;
        
    }
    
    
    public LinkedHashMap<String, ProfileElement> selectProfileWhere(String field, String value){
        LinkedHashMap <String, ProfileElement> result = new LinkedHashMap();
        Connection conn = null;
        
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // prepare query
            String query = "SELECT * FROM FaceAnalytics.profile WHERE profile." + field +"=\""+value+"\" and profile.app_idapp=\"" + idapp +"\";";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String id_profile = rs.getString("idprofile");
                String name = rs.getString("name");
                
                result.put(id_profile, new ProfileElement(id_profile, name));
                
            }
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
        }
        
        return result;
    }
    
    public LinkedHashMap<String,String> getPagesPreview(String idapp){
        
        Connection conn = null;
        LinkedHashMap<String,String> result = new LinkedHashMap<>();
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "Select FaceAnalytics.profile.idprofile, FaceAnalytics.profile.name, count(FaceAnalytics.posts.message) as 'Total Posts' FROM FaceAnalytics.profile inner join FaceAnalytics.posts on FaceAnalytics.posts.profile_idprofile=FaceAnalytics.profile.idprofile where posts.app_idapp=\"" + idapp + "\"  group by FaceAnalytics.profile.idprofile";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String idprofile = rs.getString("idprofile");
                String name = rs.getString("name");
                String total_posts = (String)rs.getString("Total Posts");
                
                result.put(idprofile, name + "<sep>" + total_posts);
                
            }
            st.close();
            return result;
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
            return null;
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
    
    public LinkedHashMap<String,PostElement> getAllPosts(String idprofile){
        
        Connection conn = null;

        
        LinkedHashMap <String,PostElement> result = new LinkedHashMap<>();        
        try
        {
            // create our mysql database connection
            conn = get_connection();
            
            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM FaceAnalytics.posts where profile_idprofile ='"+ idprofile +"'";
            
            // create the java statement
            Statement st = conn.createStatement();
            
            
            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);
            
            // iterate through the java resultset
            while (rs.next())
            {
                String idpost = rs.getString("idpost");
                String message = rs.getString("message");
                Timestamp dateCreated = rs.getTimestamp("created_on");
                int shares = rs.getInt("shares");
                
                result.put(idpost, new PostElement(idpost, message, dateCreated, idprofile, shares));
            }
            st.close();
            return result;
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