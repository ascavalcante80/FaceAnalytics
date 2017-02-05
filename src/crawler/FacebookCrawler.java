package crawler;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.net.ssl.HandshakeCompletedListener;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import util.Converter;

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
import java.sql.SQLClientInfoException;
import java.sql.Timestamp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;

import facebook.CommentsElement;
import facebook.UserElement;

public class FacebookCrawler implements Runnable {

	private Facebook facebook;
	Thread threadAnalyserPost;
	private String post_id;
	private int time_monitoring;
	private int duration_moritonig;
	private String access_token;

	public FacebookCrawler(String access_token, String app_id, String app_secret, String permissions) throws InterruptedException, FacebookException, IOException{

		this.access_token = access_token;
		facebook = new FacebookFactory().getInstance();
		AccessToken ac = new AccessToken(access_token);
		facebook.setOAuthAppId(app_id,app_secret);
		facebook.setOAuthAccessToken(ac);
		facebook.setOAuthPermissions(permissions);
	}


	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}


	private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
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


	public String getSharesCount(String postId) throws IOException, JSONException {
		JSONObject json = readJsonFromUrl("https://graph.facebook.com/"+ postId +"?fields=shares");
		JSONObject jsonShares = json.getJSONObject("shares");
		return jsonShares.get("count").toString();
	}

	public String getAllComments(String post_id) throws IOException, JSONException {
		JSONObject json = readJsonFromUrl("https://graph.facebook.com/"+ post_id + "?fields=comments");
		JSONArray json_comments = json.getJSONObject("comments").getJSONArray("data");
		String after = null;
		LinkedHashMap <String, CommentsElement> list_comments = new LinkedHashMap();
	
		do{

			for (int i = 0; i < json_comments.length(); i++) {
				JSONObject json_temp_comment= json_comments.getJSONObject(i);
				String comment= json_temp_comment.getString("message");
				String id = json_temp_comment.getString("id");

				Timestamp date_created_On = Converter.convertDate(json_temp_comment.getString("created_time"));
				int likes = json_temp_comment.getInt("like_count");
				String user_id = json_temp_comment.getJSONObject("from").getString("id");
				String name_user = json_temp_comment.getJSONObject("from").getString("name");
				list_comments.put(id, new CommentsElement(id, comment, new UserElement(user_id, name_user), post_id, date_created_On, likes));
			}
			try{
				after = json.getJSONObject("paging").getJSONObject("cursors").getString("after");
			} catch (Exception e ){
				e.printStackTrace();
				after = null;
			}
			
		} while(after != null);

		String paging_after = json.getJSONObject("paging").getJSONObject("cursors").getString("after");

		return "";
	}

	public LinkedHashMap <String, Integer> getAllReactions(String post_id) throws IOException, JSONException{

		LinkedHashMap <String, Integer> reactions = new LinkedHashMap();

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
		reactions.put("angry", new Integer(loves));

		return reactions;

	}


	public void monitorPost(String post_id, int time_monitoring, int duration_monitoring)throws FacebookException, InterruptedException, IOException {

		this.post_id = post_id;
		this.time_monitoring = time_monitoring;
		this.duration_moritonig = duration_monitoring; // in hours

		String threadname = "Thread Monitor Page";
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


	@Override
	public void run() {

		time_monitoring = time_monitoring * 1000;
		System.out.println("Crawling posts page: " + post_id);
		long start_time = new Date().getTime();
		long end_time = start_time + (duration_moritonig * 3600);
		long time_now =0;

		Path path = Paths.get(post_id + "_reactions_monitoring.txt");

		try {
			Files.write(path, ("loves,wow,haha,likes,sad,angry" + "\n").getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		do{

			try {

				LinkedHashMap <String, Integer> reactions = getAllReactions(post_id);
				String line = "";
				for (String key: reactions.keySet()) {
					line += reactions.get(key)+ ",";
				}
				line = line.substring(0, line.length() -1); // eliminate trailing comma
				Files.write(path, (line + "\n").getBytes("UTF-8"), StandardOpenOption.APPEND);			


			} catch (IOException | JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				Thread.sleep(time_monitoring);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			time_now = new Date().getTime();

		}while(time_now < end_time);

	}

}
