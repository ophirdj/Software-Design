package ac.il.technion.twc.system;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.FuntionalityTester;

/**
 * Basic simple test
 */
public class SampleTest {
	private final FuntionalityTester $ = new FuntionalityTester();

	/**
	 * @throws Exception
	 *             shouldn't happen
	 */
	@Before
	public void setup() throws Exception {
		$.cleanPersistentData();
	}

	/**
	 * remove data in the end
	 * 
	 * @throws Exception
	 *             shouldn't happen
	 * 
	 */
	@After
	public void teardown() throws Exception {
		$.cleanPersistentData();
	}

	/**
	 * Simple general test for the system
	 * 
	 * @throws Exception
	 *             shouldn't happen
	 */
	@Test
	public void sampleTest() throws Exception {
		String[] lines = new String[] { "04/04/2014 12:00:00, iddqd",
				"05/04/2014 12:00:00, idkfa, iddqd" };
		$.importData(lines);
		lines = new String[] { "{'created_at':'Sun April 06 13:00:00 +0000 2014','id':593393706,'id_str':'593393706','text':'Software design is fun! #technion #tdd #yolo','source':'\\u003ca href=\\'http:\\/\\/twittbot.net\\/\\' rel=\\'nofollow\\'\\u003etwittbot.net\\u003c\\/a\\u003e','truncated':false,'in_reply_to_status_id':null,'in_reply_to_status_id_str':null,'in_reply_to_user_id':null,'in_reply_to_user_id_str':null,'in_reply_to_screen_name':null,'user':{'id':608956240,'id_str':'608956240','name':'Harapan.....','screen_name':'Swag','location':'You\\u2665','url':null,'description':'Kamu tau arti cinta sebenarnya? Ga tau? Huftt..Ya, contohnya kecil aja, seperti Aku cinta Kamu\\u2665','protected':false,'followers_count':383,'friends_count':0,'listed_count':1,'created_at':'Fri Jun 15 09:54:28 +0000 2012','favourites_count':0,'utc_offset':25200,'time_zone':'Bangkok','geo_enabled':false,'verified':false,'statuses_count':13534,'lang':'en','contributors_enabled':false,'is_translator':false,'profile_background_color':'C0DEED','profile_background_image_url':'http:\\/\\/a0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg','profile_background_image_url_https':'https:\\/\\/si0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg','profile_background_tile':true,'profile_image_url':'http:\\/\\/a0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg','profile_image_url_https':'https:\\/\\/si0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg','profile_banner_url':'https:\\/\\/pbs.twimg.com\\/profile_banners\\/608956240\\/1358311582','profile_link_color':'0084B4','profile_sidebar_border_color':'000000','profile_sidebar_fill_color':'DDEEF6','profile_text_color':'333333','profile_use_background_image':true,'default_profile':false,'default_profile_image':false,'following':null,'follow_request_sent':null,'notifications':null},'geo':null,'coordinates':null,'place':null,'contributors':null,'retweet_count':0,'favorite_count':0,'entities':{'hashtags':[],'symbols':[],'urls':[],'user_mentions':[]},'favorited':false,'retweeted':false,'filter_level':'medium','lang':'id'}" };
		$.importDataJson(lines);
		$.setupIndex();
		assertEquals("86400000", $.getLifetimeOfTweets("iddqd"));
		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
		lines = new String[] { "{'created_at':'Sun May 11 10:08:08 +0000 2014','id':334611146097188865,'id_str':'334611146097188865','text':'RT @Swag: Software design is fun! #technion #tdd #yolo','source':'\\u003ca href=\\'http:\\/\\/jigtwi.jp\\/?p=1001\\' rel=\\'nofollow\\'\\u003ejigtwi for Android\\u003c\\/a\\u003e','truncated':false,'in_reply_to_status_id':593393706,'in_reply_to_status_id_str':'593393706','in_reply_to_user_id':608956240,'in_reply_to_user_id_str':'608956240','in_reply_to_screen_name':'Swag','user':{'id':376122962,'id_str':'376122962','name':'\\u30c9\\u30e9\\u30b4\\u30cb\\u30c3\\u30af\\u30fb\\u30aa\\u30fc\\u30d0\\u30fc\\u30dd\\u30f3\\u30bf','screen_name':'pondumX','location':'KO\\u6587\\u7f8e\\u7f8e\\u3000\\u697d\\u53cb\\u4f1a\\u3000\\u30b3\\u30ec\\u30ae\\u3000M\\u30b3\\u30fc\\u30a2\\u3000MTG\\u52e2','url':null,'description':'(\\u00b4\\u3078\\u03c9\\u3078`*)','protected':false,'followers_count':142,'friends_count':180,'listed_count':5,'created_at':'Mon Sep 19 10:31:31 +0000 2011','favourites_count':3228,'utc_offset':32400,'time_zone':'Tokyo','geo_enabled':false,'verified':false,'statuses_count':5308,'lang':'ja','contributors_enabled':false,'is_translator':false,'profile_background_color':'C0DEED','profile_background_image_url':'http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme1\\/bg.png','profile_background_image_url_https':'https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme1\\/bg.png','profile_background_tile':false,'profile_image_url':'http:\\/\\/a0.twimg.com\\/profile_images\\/2771279375\\/de397be650b6a2e8bb8e6c8b8f290f95_normal.png','profile_image_url_https':'https:\\/\\/si0.twimg.com\\/profile_images\\/2771279375\\/de397be650b6a2e8bb8e6c8b8f290f95_normal.png','profile_link_color':'0084B4','profile_sidebar_border_color':'C0DEED','profile_sidebar_fill_color':'DDEEF6','profile_text_color':'333333','profile_use_background_image':true,'default_profile':true,'default_profile_image':false,'following':null,'follow_request_sent':null,'notifications':null},'geo':null,'coordinates':null,'place':null,'contributors':null,'retweet_count':0,'favorite_count':0,'entities':{'hashtags':[],'symbols':[],'urls':[],'user_mentions':[{'screen_name':'fujisapoppo','name':'\\u3075\\u3058\\u3055','id':313314671,'id_str':'313314671','indices':[0,12]},{'screen_name':'skralbion','name':'skr.i.','id':295110595,'id_str':'295110595','indices':[13,23]}]},'favorited':false,'retweeted':false,'filter_level':'medium','lang':'ja'}" };
		$.importData(lines);
		$.setupIndex();
		assertArrayEquals(new String[] { "2,1", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getDailyHistogram());
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0",
				"1,0", "1,1" }, $.getTemporalHistogram("04/04/2014 12:00:00",
				"05/04/2014 12:00:00"));
		assertEquals("1", $.getHashtagPopularity("yolo"));
		assertEquals("0", $.getHashtagPopularity("matam"));
	}

}