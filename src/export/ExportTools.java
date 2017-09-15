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
	private Connector connector;

	public ExportTools(ProfileElement profile, Connector conn) {
		super();
		this.profile = profile;
		this.connector = conn;
	}
	public Connector getConnector() {
		return connector;
	}
	public void setConnector(Connector connector) {
		this.connector = connector;
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

		LinkedHashMap <String, PostElement > result = connector.selectPostsWhere("profile_idprofile", profile.getId());
		
		for (String key: result.keySet()){
                        
                        String line_out ="";
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

                        line_out +=  post.getMessage().replaceAll("\n", " ") + "<sep>"	;			

			// delete trailing <sep> at the end of String
			if (line_out.endsWith("<sep>")){

				line_out = line_out.substring(0, line_out.length()-5);				
			}

			line_out += "\n";
                        System.out.println("Writing post: " + post.getId());
                        
                        try {
                            writeFiles(file_location+fileName, line_out);

                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                return false;
                        }

                        
		}
                return true;
		/*try {
			writeFiles(file_location+fileName, line_out);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}*/
	}

	public Boolean writeCommentsToFile(String id_profile, String fileName, String file_location, List<String> fields){
                
            
                LinkedHashMap <String, PostElement> all_posts = connector.selectPostsWhere("profile_idprofile", id_profile);
                String line_out;
                
                                
                for (String key1: all_posts.keySet()){
                    PostElement post = all_posts.get(key1);
                     
                    LinkedHashMap <String, CommentsElement > result = connector.selectCommentsWhere("posts_idpost", post.getId());


                    for (String key: result.keySet()){
                            
                            line_out ="";
                            
                            CommentsElement comment = result.get(key);
                            line_out += "<COMMENT>";
                            if (fields.contains("idcomment")){
                                    line_out +=  comment.getId() + "<sep>";			
                            }

                            if (fields.contains("created_on")){
                                    line_out +=  comment.getData_created_On().toString() + "<sep>";			
                            }
                            if (fields.contains("users_iduser")){
                                    line_out +=  comment.getUser().getId() + "<sep>";			
                            }

                            if (fields.contains("user_name")){			

                                line_out += connector.getUserbyId(comment.getUser().getId()).getName() + "<sep>";			
                            }

                            // comment is always on
                            line_out +=  comment.getComment().replaceAll("\n", " ") + "<sep>"	;			

                            // delete trailing <sep> at the end of String
                            if (line_out.endsWith("<sep>")){

                                    line_out = line_out.substring(0, line_out.length()-5);				
                            }

                            line_out += "\n";
                            System.out.println("Writing comment: " + comment.getId());
                            
                            try {
                                    writeFiles(file_location+fileName, line_out);
                            } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                    return false;
                            }
                    }
                }
                
                return true;
           
	}
		
	private String concatenateComments(String text_building, String id_post, String fileName, String file_location, List<String> fields){

		LinkedHashMap <String, CommentsElement > result = connector.selectCommentsWhere("posts_idpost", id_post);


		for (String key: result.keySet()){

			CommentsElement comment = result.get(key);
			text_building += "<COMMENT>";
			if (fields.contains("idcomment")){
				text_building +=  comment.getId() + "<sep>";			
			}

			if (fields.contains("created_on")){
				text_building +=  comment.getData_created_On().toString() + "<sep>"	;			
			}
			if (fields.contains("iduser")){
				text_building +=  comment.getUser().getId() + "<sep>"	;			
			}

			if (fields.contains("user_name")){				

				text_building += connector.getUserbyId(comment.getUser().getId()).getName() + "<sep>"	;			
			}

                        // comment is always present
                        text_building +=  comment.getComment().replaceAll("\n", " ") + "<sep>"	;			
			

			// delete trailing <sep> at the end of String
			if (text_building.endsWith("<sep>")){

				text_building = text_building.substring(0, text_building.length()-5);				
			}

			text_building += "\n";
                        System.out.println("Writing comment: " + comment.getId());
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
		LinkedHashMap <String, PostElement> all_posts = connector.selectPostsWhere("profile_idprofile", id_profile);

		String line_post="";
		for (String key: all_posts.keySet()){
                    
			line_post="";
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
                        line_post +=  post.getMessage().replaceAll("\n", " ") + "<sep>"	;			

			// delete trailing <sep> at the end of String
			if (line_post.endsWith("<sep>")){

				line_post = line_post.substring(0, line_post.length()-5);				
			}

			line_post += "\n";
                        System.out.println("Writing comment: " + post.getId());
			line_post = concatenateComments(line_post, post.getId(), fileName, file_location, fields_comments);
			
                        try {
                                writeFiles(file_location+fileName, line_post);
                        } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                        }
		}
		return true;
	}
}


