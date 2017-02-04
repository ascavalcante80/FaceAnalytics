package crawler;
import java.io.IOException;
import java.util.LinkedHashMap;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

public class FacebookSearcher {

	private Facebook facebook;

	public FacebookSearcher(String access_token, String app_id, String app_secret, String permissions) throws InterruptedException, FacebookException, IOException{


		facebook = new FacebookFactory().getInstance();
		AccessToken ac = new AccessToken(access_token);
		facebook.setOAuthAppId(app_id,app_secret);
		facebook.setOAuthAccessToken(ac);
		facebook.setOAuthPermissions(permissions);
	}

	public LinkedHashMap<String,String> searchProfileDysplayingLastPosts(String page_name, int result_limit){
		
		LinkedHashMap<String, String> last_posts= new LinkedHashMap();
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
				last_posts.put(page_id, profile_name + "<sep>" + message );
				
			}				
			return last_posts;


			//facebook.getPosts(arg0, arg1)

		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}

