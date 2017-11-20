package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.Connector;
import facebook.App;
import facebook.CommentsElement;
import facebook.PostElement;
import facebook.UserElement;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import java.io.File;
import util.Converter;

public class FacebookCrawler implements Runnable {

	private Facebook facebook;
	Thread threadAnalyserPost;
	private String post_id;
	private int check_time;
	private int duration_moritonig;
	private static String access_token;
        private App app;
        
	public FacebookCrawler(App app) throws InterruptedException, FacebookException, IOException{

        this.app = app;
		this.access_token = app.getAccess_token();
		facebook = new FacebookFactory().getInstance();
		AccessToken ac = new AccessToken(access_token);
		facebook.setOAuthAppId(app.getIdapp(), app.getApp_secret());
		facebook.setOAuthAccessToken(ac);
		facebook.setOAuthPermissions(String.join(",",app.getPermission()));
	}


	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}


	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}


	public String getSharesCount(String postId)  {
		JSONObject json;
		try {

			json = readJsonFromUrl("https://graph.facebook.com/"+ postId +"?fields=shares&access_token=" + access_token);
			JSONObject jsonShares = json.getJSONObject("shares");
			return jsonShares.get("count").toString();
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			System.out.println(postId + ": Impossible to retrive shares counts. It usually happens when the profile being crawled shares a post from another user.");
			return "0";
		}
	}

        public LinkedHashMap<String,String> searchProfileDysplayingLastPosts(String page_name, int result_limit){
		
		LinkedHashMap<String, String> last_posts= new LinkedHashMap<String, String>();
		try {
			ResponseList<Page> list_pages = facebook.searchPages(page_name,new Reading().limit(result_limit));
			String message;
			
			for (Page page : list_pages) {
				String page_id = page.getId();
				ResponseList<Post> feed = facebook.getFeed(page.getId());

				if (feed.size()> 0){
					Post post = feed.get(0);
					// Get (string) message.
					message = post.getMessage();					
										
				} else {
					message = "- this page is not public";
				}
				
				String profile_name = page.getName();
				last_posts.put(page_id, page_id + "<sep>" +profile_name + "<sep>" + message );
				
			}				
			return last_posts;

		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	
        
        
        public String searchProfileByIdDysplayingLastPosts(String id_profile, int result_limit){
		
		LinkedHashMap<String, String> last_posts= new LinkedHashMap();


                String message, result = null;
                Page page;
                ResponseList<Post> feed = null;
                
                try {
                    
                    page = facebook.getPage(id_profile);
                    feed = facebook.getFeed(id_profile);
                } catch (FacebookException ex) {
                    Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    return "";
                }

                if (feed.size()> 0){
                        Post post = feed.get(0);
                        // Get (string) message.
                        message = post.getMessage();					

                } else {
                        message = "- this page is not public";
                }

                String profile_name = page.getName();
                result = id_profile + "<sep>" +profile_name + "<sep>" + message;
                return result;


	}

	
	public LinkedHashMap <String, CommentsElement> getAllComments(String post_id, int result_limit, int time_sleep) {


            try {
                String query = "https://graph.facebook.com/v2.8/"+post_id+"/comments?fields=like_count,created_time,from,message,id&limit="+result_limit+"&access_token="+access_token;
                JSONObject json = readJsonFromUrl(query);
                JSONArray json_comments = json.getJSONArray("data");
                String next_url = null;
                LinkedHashMap <String, CommentsElement> list_comments = new LinkedHashMap<String, CommentsElement>();
                
                do{
                    
                    for (int i = 0; i < json_comments.length(); i++) {
                        JSONObject json_temp_comment= json_comments.getJSONObject(i);
                        String comment= json_temp_comment.getString("message");
                        String id = json_temp_comment.getString("id");
                        
                        Timestamp date_created_On = Converter.convertDate(json_temp_comment.getString("created_time"));
                        int likes = json_temp_comment.getInt("like_count");
                        String user_id = json_temp_comment.getJSONObject("from").getString("id");
                        String name_user = json_temp_comment.getJSONObject("from").getString("name");
                        list_comments.put(id, new CommentsElement(id, comment, new UserElement(user_id, name_user), post_id, date_created_On, 0));
                    }
                    try{
                        Thread.sleep(time_sleep*1000);
                        next_url = json.getJSONObject("paging").getString("next");
                        json = readJsonFromUrl(next_url);
                        json_comments = json.getJSONArray("data");
                        
                    } catch (Exception e ){
                        e.printStackTrace();
                        next_url = null;
                    }
                    
                } while(next_url != null);
                
                return list_comments;
            } catch (JSONException ex) {
                Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
	}


        public void getAndSaveAllComments(String post_id, int result_limit, int time_sleep, Connector conn) {

 
            try {
                String query = "https://graph.facebook.com/v2.8/"+post_id+"/comments?fields=like_count,created_time,from,message,id&limit="+result_limit+"&access_token="+access_token;
                JSONObject json = readJsonFromUrl(query);
                JSONArray json_comments = json.getJSONArray("data");
                String next_url = null;
                LinkedHashMap <String, CommentsElement> list_comments = new LinkedHashMap<String, CommentsElement>();
                
                do{
                    
                    for (int i = 0; i < json_comments.length(); i++) {
                        JSONObject json_temp_comment= json_comments.getJSONObject(i);
                        String comment= json_temp_comment.getString("message");
                        String id = json_temp_comment.getString("id");
                        
                        Timestamp date_created_On = Converter.convertDate(json_temp_comment.getString("created_time"));
                        int likes = json_temp_comment.getInt("like_count");
                        String user_id = json_temp_comment.getJSONObject("from").getString("id");
                        String name_user = json_temp_comment.getJSONObject("from").getString("name");
                        
                        // the User must be inserted before the comment for the DB integrity
                        conn.insertUser(new UserElement(user_id, name_user), app.getIdapp());
                        
                        // insert comment in the DB
                        conn.insertComment(new CommentsElement(id, comment, new UserElement(user_id, name_user), post_id, date_created_On, likes), app.getIdapp());
                        System.out.println("Comment: "+ id +" saved in the DB");
                    }
                    try{
                        Thread.sleep(time_sleep*1000);
                        System.out.println("Taking a nap");
                        next_url = json.getJSONObject("paging").getString("next");
                        json = readJsonFromUrl(next_url);
                        json_comments = json.getJSONArray("data");
                        
                    } catch (Exception e ){
                        e.printStackTrace();
                        next_url = null;
                    }
                    
                } while(next_url != null);
                

            } catch (JSONException ex) {
                Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FacebookCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Crawling comments done");
	}
        
	
	public LinkedHashMap <String, Integer> getAllReactions(String post_id) throws IOException, JSONException{

		LinkedHashMap <String, Integer> reactions = new LinkedHashMap<String, Integer>();

		JSONObject json = readJsonFromUrl("https://graph.facebook.com/v2.8/?ids=" + post_id + "&fields=reactions.type(LOVE).limit(0).summary(total_count).as(reactions_love)%2Creactions.type(WOW).limit(0).summary(total_count).as(reactions_wow)%2Creactions.type(HAHA).limit(0).summary(total_count).as(reactions_haha)%2C%20reactions.type(LIKE).limit(0).summary(total_count).as(reactions_like)%2Creactions.type(SAD).limit(0).summary(total_count).as(reactions_sad)%2Creactions.type(ANGRY).limit(0).summary(total_count).as(reactions_angry)&access_token=" + access_token);
		int loves  = json.getJSONObject(post_id).getJSONObject("reactions_love").getJSONObject("summary").getInt("total_count");
		int wow  = json.getJSONObject(post_id).getJSONObject("reactions_wow").getJSONObject("summary").getInt("total_count");
		int haha  = json.getJSONObject(post_id).getJSONObject("reactions_haha").getJSONObject("summary").getInt("total_count");
		int likes  = json.getJSONObject(post_id).getJSONObject("reactions_like").getJSONObject("summary").getInt("total_count");
		int sad  = json.getJSONObject(post_id).getJSONObject("reactions_sad").getJSONObject("summary").getInt("total_count");
		int angry  = json.getJSONObject(post_id).getJSONObject("reactions_angry").getJSONObject("summary").getInt("total_count");

		reactions.put("loves", new Integer(loves));
		reactions.put("wow", new Integer(wow));
		reactions.put("haha", new Integer(haha));
		reactions.put("likes", new Integer(likes));
		reactions.put("sad", new Integer(sad));
		reactions.put("angry", new Integer(angry));

		return reactions;

	}


	public void monitorPost(String post_id, int check_time, int duration_monitoring)throws FacebookException, InterruptedException, IOException {

		this.post_id = post_id;
		this.check_time = check_time; // in minutes
		this.duration_moritonig = duration_monitoring; // in hours

		String threadname = "Thread Monitor Post Reactions";
		System.out.println("Starting ");
		if (threadAnalyserPost == null)
		{
			threadAnalyserPost = new Thread (this, threadname);
			threadAnalyserPost.start ();
		}			
	}

                
	public LinkedHashMap <String, String> getAllPosts(String profile_id, int wait_time, int result_limit){

		// create list to keep posts crawled
		LinkedHashMap<String, String> posts_list = new LinkedHashMap<>();

		// convert time to milliseconds
		wait_time = wait_time * 1000;

		try {

			System.out.println("Crawling posts page: " + profile_id);

			ResponseList<Post> posts = null;
			String dateLimit = null;

			// stay in the loop until get a posts list empty
			Boolean empty = false;
			do {

				if (dateLimit == null) {
					posts = this.facebook.getFeed(profile_id, new Reading().limit(result_limit));
					dateLimit = posts.get(posts.size() - 1).getCreatedTime().toString();

				} else {
					posts = this.facebook.getPosts(profile_id, new Reading().limit(result_limit).until(dateLimit));
					if (!posts.isEmpty()) {
						posts.remove(0);
					}
				}

				// if posts is empty, quite the loop
				if (!posts.isEmpty()) {

					for (Post post : posts) {

						System.out.println("Posts id:" + post.getId() + " created on " + post.getCreatedTime().toString() + " - done");

						// save posts in list
						if (!posts_list.containsKey(post.getId())) {
							posts_list.put(post.getId(), post.getMessage()+ "<sep>" + post.getCreatedTime().toString());
						} else {
							System.out.println("Crawling done!");
							empty = true;
						}
					}
					System.out.println("Going to sleep for " + wait_time / 1000 + " seconds - " + new Date().toString());
					Thread.sleep(wait_time);
				} else {

					empty = true;
				}

				// change date limit
				if (posts.size() > 1) {
					dateLimit = posts.get(posts.size() - 1).getCreatedTime().toString();
				} else if (!posts.isEmpty()) {
					dateLimit = posts.get(0).getCreatedTime().toString();
				}

			} while (!empty);

		} catch (FacebookException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}

		return posts_list;
	}

        
        public void getAndSaveAllPosts(String profile_id, int wait_time, int result_limit, Connector conn){

		// create list to keep posts crawled
		LinkedHashMap<String, String> posts_list = new LinkedHashMap<>();
                
		// convert time to milliseconds
		wait_time = wait_time * 1000;

		try {

			System.out.println("Crawling posts page: " + profile_id);

			ResponseList<Post> posts = null;
			String dateLimit = null;

			// stay in the loop until get a posts list empty
			Boolean empty = false;
			do {

				if (dateLimit == null) {
					posts = this.facebook.getFeed(profile_id, new Reading().limit(result_limit));
					dateLimit = posts.get(posts.size() - 1).getCreatedTime().toString();

				} else {
					posts = this.facebook.getPosts(profile_id, new Reading().limit(result_limit).until(dateLimit));
					if (!posts.isEmpty()) {
						posts.remove(0);
					}
				}

				// if posts is empty, quite the loop
				if (!posts.isEmpty()) {

					for (Post post : posts) {

						System.out.println("Posts id:" + post.getId() + " created on " + post.getCreatedTime().toString() + " - done");

						// save posts in list
						if (!posts_list.containsKey(post.getId())) {
							posts_list.put(post.getId(), post.getMessage()+ "<sep>" + post.getCreatedTime().toString());
                                                                                                                                                                        
                                                        int shares = Integer.parseInt(getSharesCount(post.getId()));
                                                        Timestamp created_on = new Timestamp(post.getCreatedTime().getTime());
                                                        conn.insertPost(new PostElement(post.getId(),post.getMessage(), created_on, profile_id,shares), app.getIdapp());
                                                        System.out.println("Post: " + post.getId() + " saved in the DB");
                                                        
						} else {
							System.out.println("Crawling done!");
							empty = true;
						}
					}
					System.out.println("Going to sleep for " + wait_time / 1000 + " seconds - " + new Date().toString());
					Thread.sleep(wait_time);
				} else {

					empty = true;
				}

				// change date limit
				if (posts.size() > 1) {
					dateLimit = posts.get(posts.size() - 1).getCreatedTime().toString();
				} else if (!posts.isEmpty()) {
					dateLimit = posts.get(0).getCreatedTime().toString();
				}

			} while (!empty);

		} catch (FacebookException | InterruptedException e) {
			e.printStackTrace();
		}
            System.out.println("--Crawling done! - " + new Date().toString());
	}
               

	@Override
	public void run() {
		
		check_time = check_time * 1000 * 60;
		System.out.println("Crawling posts page: " + post_id);
		long start_time = new Date().getTime();
                long end_time;
                if(duration_moritonig== 0){
                    end_time = 999999999999999999L;
                } else {
                    end_time = start_time + TimeUnit.HOURS.toMillis(duration_moritonig);
                }
                long time_now =0;
                
                File dirOut = new File("../reaction_monitoring/");
                
                if (!dirOut.exists()) {
                    dirOut.mkdir();
                } 
                
		Path path = Paths.get("../reaction_monitoring/ "+ post_id + "_reactions_monitoring.txt");

		try {
			Files.write(path, ("time,loves,wow,haha,likes,sad,angry,shares" + "\n").getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		do{
			System.out.println(new Timestamp(System.currentTimeMillis()).toString() + " - Monitoring Reactions from post " + post_id);

			try {

				LinkedHashMap <String, Integer> reactions = getAllReactions(post_id);
								
				//build string to write the file
				String line = new Timestamp(System.currentTimeMillis()).toString() + ",";
				for (String key: reactions.keySet()) {
					line += reactions.get(key)+ ",";
				}
				
				String shares = getSharesCount(post_id);
				
				Files.write(path, (line + shares + "\n").getBytes("UTF-8"), StandardOpenOption.APPEND);			


			} catch (IOException | JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(check_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// get time
			time_now = new Date().getTime();

		}while(time_now < end_time);

	}

}
