package export;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import database.Connector;
import facebook.App;
import facebook.CommentsElement;
import facebook.PostElement;
import facebook.ProfileElement;

public class ExportTools {

	private ProfileElement profile;
	private Connector conn;

	public ExportTools(ProfileElement profile, Connector conn) {
		super();
		this.profile = profile;
		this.conn = conn;
	}
	public Connector getConn() {
		return conn;
	}
	public void setConn(Connector conn) {
		this.conn = conn;
	}

	public ProfileElement getProfile() {
		return profile;
	}
	public void setProfile(ProfileElement profile) {
		this.profile = profile;
	}

	private void writeFiles(String file_location, String text) throws UnsupportedEncodingException, IOException{
		Path path = Paths.get(file_location);
                Files.write(path, text.getBytes("UTF-8"), StandardOpenOption.APPEND);


	}

	public Boolean writePostsToFile(String fileName, String file_location, List<String> fields){

		LinkedHashMap <String, PostElement > result = conn.selectPostsWhere("profile_idprofile", profile.getId());
		String line_out ="";
		for (String key: result.keySet()){

			PostElement post = result.get(key);
			line_out += "<POST>";
			if (fields.contains("idpost")){
				line_out +=  post.getId() + "<sep>"	;			
			}

			if (fields.contains("idprofile")){
				line_out +=  post.getProfile_id() + "<sep>"	;			
			}
			if (fields.contains("created_on")){
				line_out +=  post.getData_created_On().toString() + "<sep>"	;			
			}

			if (fields.contains("shares")){
				line_out += "shares: " + post.getShares() + "<sep>"	;			
			}

                        line_out +=  post.getMessage() + "<sep>"	;			

			// delete trailing <sep> at the end of String
			if (line_out.endsWith("<sep>")){

				line_out = line_out.substring(0, line_out.length()-5);				
			}

			line_out += "\n";
		}
		try {
			writeFiles(file_location+fileName, line_out);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Boolean writeCommentsToFile(String id_profile, String fileName, String file_location, List<String> fields){
                
            
                LinkedHashMap <String, PostElement> all_posts = conn.selectPostsWhere("profile_idprofile", id_profile);
                String line_out ="";
                for (String key1: all_posts.keySet()){
                    PostElement post = all_posts.get(key1);
                     
                    LinkedHashMap <String, CommentsElement > result = conn.selectCommentsWhere("posts_idpost", post.getId());


                    for (String key: result.keySet()){

                            CommentsElement comment = result.get(key);
                            line_out += "<COMMENT>";
                            if (fields.contains("idcomment")){
                                    line_out +=  comment.getId() + "<sep>"	;			
                            }

                            if (fields.contains("created_on")){
                                    line_out +=  comment.getData_created_On().toString() + "<sep>"	;			
                            }
                            if (fields.contains("iduser")){
                                    line_out +=  comment.getUser().getId() + "<sep>"	;			
                            }

                            if (fields.contains("user_name")){				

                                line_out += conn.getUserbyId(comment.getUser().getId()).getName() + "<sep>"	;			
                            }

                            // comment is always on
                            line_out +=  comment.getComment() + "<sep>"	;			

                            // delete trailing <sep> at the end of String
                            if (line_out.endsWith("<sep>")){

                                    line_out = line_out.substring(0, line_out.length()-5);				
                            }

                            line_out += "\n";
                    }
		
                    
                }
                try {
			writeFiles(file_location+fileName, line_out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
                return true;
           
	}
		
	private String concatenateComments(String text_building, String id_post, String fileName, String file_location, List<String> fields){

		LinkedHashMap <String, CommentsElement > result = conn.selectCommentsWhere("posts_idpost", id_post);


		for (String key: result.keySet()){

			CommentsElement comment = result.get(key);
			text_building += "<COMMENT>";
			if (fields.contains("idcomment")){
				text_building +=  comment.getId() + "<sep>"	;			
			}

			if (fields.contains("created_on")){
				text_building +=  comment.getData_created_On().toString() + "<sep>"	;			
			}
			if (fields.contains("iduser")){
				text_building +=  comment.getUser().getId() + "<sep>"	;			
			}

			if (fields.contains("user_name")){				

				text_building += conn.getUserbyId(comment.getUser().getId()).getName() + "<sep>"	;			
			}

			if (fields.contains("comment")){
				text_building +=  comment.getComment() + "<sep>"	;			
			}

			// delete trailing <sep> at the end of String
			if (text_building.endsWith("<sep>")){

				text_building = text_building.substring(0, text_building.length()-5);				
			}

			text_building += "\n";
		}
		return text_building;
	}

	public Boolean writeCommentsAndPostToFile(String id_profile, String fileName, String file_location, List <String> fields_post, List <String> fields_comments ){

		// create file here to append after
		try {
			writeFiles(file_location+fileName, "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LinkedHashMap <String, CommentsElement > result = new LinkedHashMap();
		LinkedHashMap <String, PostElement> all_posts = conn.selectPostsWhere("profile_idprofile", id_profile);

		String line_post="";
		for (String key: all_posts.keySet()){
//			line_post="\n";
			PostElement post = all_posts.get(key);
			line_post += "<POST>";
			if (fields_post.contains("idprofile")){
				line_post +=  post.getProfile_id() + "<sep>";			
			}
			if (fields_post.contains("idpost")){
				line_post +=  post.getId() + "<sep>"	;			
			}

			if (fields_post.contains("created_on")){
				line_post +=  post.getData_created_On().toString() + "<sep>"	;			
			}

			if (fields_post.contains("shares")){
				line_post += "shares: " + post.getShares() + "<sep>"	;			
			}
                        // post is always on
                        line_post +=  post.getMessage() + "<sep>"	;			

			// delete trailing <sep> at the end of String
			if (line_post.endsWith("<sep>")){

				line_post = line_post.substring(0, line_post.length()-5);				
			}

			line_post += "\n";
//
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			line_post = concatenateComments(line_post, post.getId(), fileName, file_location, fields_comments);
			System.out.println("writing comments...");

		}
		try {
			writeFiles(file_location+fileName, line_post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;


	}

//	public static void main(String [] args){
//
//
//		String access_token = "EAAClJnYbrdYBACXz1NOldz5dkns0kaQbFpOegmUKZC5KrdPyUKc6zQXpEnQ1iD60eDhInAmX8Mki5Kmuos389vNHsTBrxCvtoIBswvxl5ZA6aZBB2rhYHMOWvTYXH74b3qZCseyS5BkxzCoXcX66ZBVCPwzwKcJ4ZD";
//		String app_id = "181584608865750";
//		String app_secret = "d3ee1c88b86872e512802ceabca50970";
//		String [] permissions = {"read_stream","public_profile","user_friends", "email", "user_about_me", "user_actions.books", "user_actions.fitness", "user_actions.music", "user_actions.news", "user_actions.video", "user_actions:{app_namespace}", "user_birthday", "user_education_history", "user_events", "user_games_activity", "user_hometown", "user_likes", "user_location", "user_managed_groups", "user_photos", "user_posts", "user_relationships", "user_relationship_details", "user_religion_politics", "user_tagged_places", "user_videos", "user_website", "user_work_history", "read_custom_friendlists", "read_insights", "read_audience_network_insights", "read_page_mailboxes", "manage_pages", "publish_pages", "publish_actions", "rsvp_event", "pages_show_list", "pages_manage_cta", "ads_read", "ads_management"};
//		String [] profiles = {"profiles","tests"};
//		Connector conn = new Connector("jdbc:mysql://localhost:3306/FaceAnalytics?autoReconnect=true&useSSL=false", "root", "20060907jl");
//
//		ProfileElement profile = new ProfileElement("1535230416709539", "Emmanuel Macron");
//		//		conn.insertProfile(profile, app_id);
//
//		ExportTools et = new ExportTools( profile, conn);
//		conn.setIdapp(app_id);
//
//		//		et.writePostsToFile("TESTS.txt", "", new ArrayList<String>(Arrays.asList("idpost", "message")));
////		et.writeCommentsToFile(profile.get,"CommenESTS.txt", "", new ArrayList<String>(Arrays.asList("idcomment", "comment")));
//		et.writeCommentsAndPostToFile(profile.getId(), "CO333MM22NENE.txt", "", new ArrayList<String>(Arrays.asList("idpost", "message")), new ArrayList<String>(Arrays.asList("idcomment", "comment")));
//
//	}
}


