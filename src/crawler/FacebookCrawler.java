package crawler;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FacebookCrawler {

	private Facebook facebook;

	public FacebookCrawler(String access_token, String app_id, String app_secret, String permissions) throws InterruptedException, FacebookException, IOException{

		facebook = new FacebookFactory().getInstance();
		AccessToken ac = new AccessToken(access_token);
		facebook.setOAuthAppId(app_id,app_secret);
		facebook.setOAuthAccessToken(ac);
		facebook.setOAuthPermissions(permissions);
	}

	
	
	
	public void getPost(){
		
		try {
			ResponseList<Post> feeds = facebook.getFeed("1535230416709539");
			
			for (Post post : feeds) {
				
				System.out.println(post.getMessage() + 				post.getCreatedTime().toString());
			}
			
			
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}
