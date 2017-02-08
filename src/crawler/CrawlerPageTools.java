package crawler;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import database.Connector;
import facebook.App;
import facebook.PostElement;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.auth.AccessToken;
import util.Converter;
import facebook4j.Reading;
import facebook4j.ResponseList;

public class CrawlerPageTools implements Runnable {
        
        private App app;
	private String page_id;
	private Facebook facebook;
	Thread threadAnalyserPage;
	private Connector conn;
	private int sleep_time;
	private int check_time;
	private int duration_monitoring;

	public CrawlerPageTools(String page_id, App app, Connector conn, int sleep_time, int check_time, int duration_monitoring) {
		super();
		this.page_id = page_id;

		facebook = new FacebookFactory().getInstance();
                this.app = app;
		AccessToken ac = new AccessToken(app.getAccess_token());
		facebook.setOAuthAppId(app.getIdapp(),app.getApp_secret());
		facebook.setOAuthAccessToken(ac);
		facebook.setOAuthPermissions(String.join(",", app.getPermission()));
		this.conn = conn;
		this.sleep_time = sleep_time * 1000 * 60;
                this.check_time = check_time;
		this.duration_monitoring = duration_monitoring;

	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}


	public void monitor_page(){

		String threadname = "Thread Monitor Page";
		System.out.println("Starting ");
		if (threadAnalyserPage == null)
		{
			threadAnalyserPage = new Thread (this, threadname);
			threadAnalyserPage.start ();
		}	
	}



	@Override
	public void run() {


		while(true){

			try {
				// checking Feed for new posts
				Post post_temp = facebook.getFeed(page_id, new Reading().limit(1)).get(0);

				Timestamp data_created_On = new Timestamp(post_temp.getCreatedTime().getTime());
                                

				int shares = Integer.parseInt(new FacebookCrawler(app).getSharesCount(post_temp.getId()));

				PostElement last_post = new PostElement(post_temp.getId(), post_temp.getMessage(), data_created_On, page_id, shares);

				if (conn.insertPost(last_post, app.getIdapp())){
					// Post wasn't in the database, start do monitor
					FacebookCrawler fb_crawler = new FacebookCrawler(app);
					fb_crawler.monitorPost(last_post.getId(), check_time, duration_monitoring);

					System.out.println("--> Starting Monitoring " + new Timestamp(System.currentTimeMillis()).toString());
				}


			} catch (FacebookException | InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
			try {

				System.out.println("No new posts to crawl... " + new Timestamp(System.currentTimeMillis()).toString());
				Thread.sleep(sleep_time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
