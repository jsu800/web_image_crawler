/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */

/*
 * IMPORTANT: Make sure that you update crawler4j.properties file and set
 * crawler.include_images to true
 */

public class ImageCrawlController {

	public static void main(String[] args) throws Exception {
//		if (args.length < 3) {
//			System.out.println("Needed parameters: ");
//			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
//			System.out.println("\t numberOfCralwers (number of concurrent threads)");
//			System.out.println("\t storageFolder (a folder for storing downloaded images)");
//			return;
//		}
//		String rootFolder = args[0];
//		int numberOfCrawlers = Integer.parseInt(args[1]);
//		String storageFolder = args[2];

		String rootFolder = "/Users/joe/Desktop/googleio/root";
		int numberOfCrawlers = 5;
		String storageFolder = "/Users/joe/Desktop/googleio/images";
		
		
		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(rootFolder);
		config.setIncludeHttpsPages(true);
		config.setResumableCrawling(true);
//		config.setMaxOutgoingLinksToFollow(10000);
		//config.setPolitenessDelay(200);
		
		/*
		 * Since images are binary content, we need to set this parameter to
		 * true to make sure they are included in the crawl.
		 */
		config.setIncludeBinaryContentInCrawling(true);

//		String[] crawlDomains = new String[] { "https://developers.google.com/glass" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/analytics/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/+/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/maps/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/cloud/" };		
//		String[] crawlDomains = new String[] { "https://developers.google.com/prediction/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/translate/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/chrome/" };
//		String[] crawlDomains = new String[] { "https://play.google.com/store" };
		String[] crawlDomains = new String[] { "https://developers.google.com/" };		
//		String[] crawlDomains = new String[] { "https://developers.google.com/games/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/google-apps/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/wallet/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/tv/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/youtube/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/open-source/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/speed/" };
//		String[] crawlDomains = new String[] { "https://developers.google.com/appengine/" };
//		String[] crawlDomains = new String[] { "https://play.google.com/", "https://developer.chrome.com/"};
//		String[] crawlDomains = new String[] { "http://www.youtube.com/yt/dev/api-resources.html", "https://developers.google.com/youtube/" };

//		String[] crawlDomains = new String[] { "https://play.google.com/", "https://developer.android.com/", "https://developer.chrome.com/", "http://www.youtube.com/", "https://developers.google.com/" };
//		String[] crawlDomains = new String[] { "https://play.google.com/", "https://developer.android.com/", "https://developer.chrome.com/", "http://www.youtube.com/" };
		
//		String[] crawlDomains = new String[] { "https://developer.chrome.com/apps/about_apps" };
		
		
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		for (String domain : crawlDomains) {
			controller.addSeed(domain);
		}

		ImageCrawler.configure(crawlDomains, storageFolder);

		controller.start(ImageCrawler.class, numberOfCrawlers);
	}

}