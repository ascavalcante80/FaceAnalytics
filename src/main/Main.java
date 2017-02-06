package main;

import java.io.IOException;
import java.text.ParseException;
import crawler.CrawlerPageTools;
import database.Connector;
import facebook.App;
import facebook.ProfileElement;
import facebook4j.FacebookException;

public class Main {


	public static void main(String[] args) throws InterruptedException, FacebookException, IOException, ParseException {

		
				
		// TODO INCLUIR SHARES NO MONITORING DE REACTIONS!!!!!!!!!!!!!!
		String access_token = "EAAClJnYbrdYBACXz1NOldz5dkns0kaQbFpOegmUKZC5KrdPyUKc6zQXpEnQ1iD60eDhInAmX8Mki5Kmuos389vNHsTBrxCvtoIBswvxl5ZA6aZBB2rhYHMOWvTYXH74b3qZCseyS5BkxzCoXcX66ZBVCPwzwKcJ4ZD";
		String app_id = "181584608865750";
		String app_secret = "d3ee1c88b86872e512802ceabca50970";
		String [] permissions = {"read_stream","public_profile","user_friends", "email", "user_about_me", "user_actions.books", "user_actions.fitness", "user_actions.music", "user_actions.news", "user_actions.video", "user_actions:{app_namespace}", "user_birthday", "user_education_history", "user_events", "user_games_activity", "user_hometown", "user_likes", "user_location", "user_managed_groups", "user_photos", "user_posts", "user_relationships", "user_relationship_details", "user_religion_politics", "user_tagged_places", "user_videos", "user_website", "user_work_history", "read_custom_friendlists", "read_insights", "read_audience_network_insights", "read_page_mailboxes", "manage_pages", "publish_pages", "publish_actions", "rsvp_event", "pages_show_list", "pages_manage_cta", "ads_read", "ads_management"};
		String [] profiles = {"profiles","tests"};
		Connector conn = new Connector("jdbc:mysql://localhost:3306/FaceAnalytics?autoReconnect=true&useSSL=false", "root", "20060907jl");
		
		conn.insertApp(new App(app_id,"alex_4j", access_token,app_secret, permissions, profiles));
		ProfileElement profile = new ProfileElement("1535230416709539", "Emmanuel Macron");
		conn.insertProfile(profile, app_id);
		
		CrawlerPageTools cp = new CrawlerPageTools("1535230416709539", access_token, app_id, String.join(",", permissions), app_secret, conn, 1, 1, 1);
		cp.monitor_page();

		//		FacebookCrawler fb = new FacebookCrawler(access_token, app_id, app_secret, String.join(",", permissions));
//		fb.monitorPost("1535230416709539_1910920195807224", 1, 1);
		
//		

	}

}
