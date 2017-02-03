package crawler;
import java.io.IOException;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;

public class FacebookCrawler {

		
		public static void main(String args[]) throws InterruptedException, FacebookException, IOException{
			
			 // Marine
			Facebook facebook = new FacebookFactory().getInstance();
			AccessToken ac = new AccessToken("EAACEdEose0cBADn6HK0ydkM5tZBGqsaxeQg8ZBptgtl9Tkwrfq1I5UqfEcyNWpT7aPgsq5P8r0Ht2YroZAJBAR1vKsLxdriUv7pSq9HF8njKud4dAxvnuoLgVJ3aR6fycspsBZCZCdFA9ZByjwGkrYTLGrO5Ko6dXERNWrcgX4vkKPqQWYtIl9LwGwhR8UAyAZD");
			facebook.setOAuthAppId("181584608865750", "d3ee1c88b86872e512802ceabca50970");
			facebook.setOAuthAccessToken(ac);
			facebook.setOAuthPermissions("read_stream,public_profile, user_friends, email, user_about_me, user_actions.books, user_actions.fitness, user_actions.music, user_actions.news, user_actions.video, user_actions:{app_namespace}, user_birthday, user_education_history, user_events, user_games_activity, user_hometown, user_likes, user_location, user_managed_groups, user_photos, user_posts, user_relationships, user_relationship_details, user_religion_politics, user_tagged_places, user_videos, user_website, user_work_history, read_custom_friendlists, read_insights, read_audience_network_insights, read_page_mailboxes, manage_pages, publish_pages, publish_actions, rsvp_event, pages_show_list, pages_manage_cta, ads_read, ads_management");
			
						
		}
	}

