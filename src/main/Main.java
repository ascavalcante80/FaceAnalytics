package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.json.JSONException;

import crawler.FacebookCrawler;
import crawler.FacebookSearch;
import facebook.CommentsElement;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;

public class Main {


	public static void main(String[] args) throws InterruptedException, FacebookException, IOException, ParseException {

		// TODO Auto-generated method stub
		String access_token = "EAAClJnYbrdYBACXz1NOldz5dkns0kaQbFpOegmUKZC5KrdPyUKc6zQXpEnQ1iD60eDhInAmX8Mki5Kmuos389vNHsTBrxCvtoIBswvxl5ZA6aZBB2rhYHMOWvTYXH74b3qZCseyS5BkxzCoXcX66ZBVCPwzwKcJ4ZD";
		String app_id = "181584608865750";
		String app_secret = "d3ee1c88b86872e512802ceabca50970";

		String permissions = "read_stream,public_profile, user_friends, email, user_about_me, user_actions.books, user_actions.fitness, user_actions.music, user_actions.news, user_actions.video, user_actions:{app_namespace}, user_birthday, user_education_history, user_events, user_games_activity, user_hometown, user_likes, user_location, user_managed_groups, user_photos, user_posts, user_relationships, user_relationship_details, user_religion_politics, user_tagged_places, user_videos, user_website, user_work_history, read_custom_friendlists, read_insights, read_audience_network_insights, read_page_mailboxes, manage_pages, publish_pages, publish_actions, rsvp_event, pages_show_list, pages_manage_cta, ads_read, ads_management";

//		FacebookSearch fb_crawler = new FacebookSearch(access_token, app_id, app_secret, permissions);
//
//		LinkedHashMap<String,String> rp = fb_crawler.searchProfileDysplayingLastPosts("Macron",10);
//		for (String key : rp.keySet()) {
//			String [] items = rp.get(key).split("<sep>");
//			System.out.println(key + " - " + items[0] + " " + items[1] );
//
//		}

		FacebookCrawler fb = new FacebookCrawler(access_token, app_id, app_secret, permissions);
//
//
//		fb.monitorPost("1535230416709539_1908691896030054", 10, 1);
		try {
			LinkedHashMap <String, CommentsElement> list_comms = fb.getAllComments("1535230416709539_1908691896030054", 200, 10);
			int count =0;
			for (String key : list_comms.keySet()) {
				count+= 1;
				
				System.out.println(count+"- "+key+"- "+ list_comms.get(key).getComment());
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//
//	LinkedHashMap<String, String> posts_list = fb.getAllPosts("1535230416709539", 10, 100);
//
//		for (String key : posts_list.keySet()) {
//
//			try {
//				Path path = Paths.get("posts_macron.txt");
//				if(Files.exists(path)){
//					Files.write(path, (posts_list.get(key) + "\n").getBytes("UTF-8"), StandardOpenOption.APPEND);
//				} else {
//					Files.write(path, (posts_list.get(key) + "\n").getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);				
//				}
//
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

}
