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


import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */

/*
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
public class ImageCrawler extends WebCrawler {

	private static final Pattern filters = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
			+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

//	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(png))$");
	
	private static File storageFolder, foundFolder, newFoundFolder;
	private static String[] crawlDomains;
	private static ArrayList<String> foundArray = null;

	public static void configure(String[] domain, String storageFolderName) {
		ImageCrawler.crawlDomains = domain;

		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}

		// create a new found folder
		newFoundFolder = new File("/Users/joe/Desktop/googleio/new_found");
		if (!newFoundFolder.exists()) {
			newFoundFolder.mkdirs();
		}
		
		// pull a list of file names and save them in Arraylist 		
		foundArray = new ArrayList<String>();
		
		foundFolder = new File("/Users/joe/Desktop/googleio/found");
		if (foundFolder.exists()) {
		
			File[] files = foundFolder.listFiles();			
			if (files.length > 0) {
				
				for (File file : files) {
					
					if (file.getName().contains(".png") == false)
						continue;
					
					if (foundArray != null)						
						foundArray.add(file.getName());
				}
				
			}
		}
		
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (filters.matcher(href).matches()) {
			return false;
		}

		if (imgPatterns.matcher(href).matches()) {
			return true;
		}

		for (String domain : crawlDomains) {
			if (href.startsWith(domain)) {
				return true;
			}			
		}
		return false;
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();

		// We are only interested in processing images
		if (!(page.getParseData() instanceof BinaryParseData)) {
			return;
		}

		if (!imgPatterns.matcher(url).matches()) {
			return;
		}

		// Not interested in very small images
		if (page.getContentData().length < 4 * 1024) {
			return;
		}

		// get a unique name for storing this image
		String extension = url.substring(url.lastIndexOf("."));
		String hashedName = Cryptography.MD5(url) + extension;

		// check to see if hash name is already in there, if so don't save it
		if (foundArray != null) {
			
			for (String s : foundArray) {
				
				if (s.equalsIgnoreCase(hashedName)) {
					
					if (checkImageModification(page) == true) {
						// if image modification date is recent, store image in new_folder
						IO.writeBytesToFile(page.getContentData(), newFoundFolder.getAbsolutePath() + "/" + hashedName);
						System.out.println("Stored: " + url);			
					}
					return;
				}
			}
		}
				
		if (checkImageModification(page) == true) {
			// if image modification date is recent, store image
			IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() + "/" + hashedName);
			System.out.println("Stored: " + url);			
		}
		
	}

	private boolean checkImageModification(Page page) {
		
		boolean isNew = false;
		Header[] headers = page.getFetchResponseHeaders();
		
		for (Header header : headers) {
			
			if (header.getName().equals("Last-Modified")) {
				
				String val = header.getValue();
				String[] dateFragments = val.split(" ");

				if (dateFragments.length > 3) {
					String month = dateFragments[2];
					String year = dateFragments[3];
					
					if (month.equals("Apr") && year.equals("2014")) {
						isNew = true;
					} else {
						isNew = false;
					}
					
				} else {
					isNew = false;
				}
				
				break;
				
			} else {
				// get the image if there is no "Last-Modified" also
				isNew = true;
			}		
		}
		return isNew;
	}
	
}