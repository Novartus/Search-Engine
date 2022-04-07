package webCrawler;

import java.util.*;
import java.io.*;

public class runCrawler {
	// This function will execute the web crawler
	public static void executeCrawler() {

		try {
			ArrayList<String> finalURLList = new ArrayList<String>();

			// Enter the URL in proper format so that crawling can be performed
			System.out.println("Please enter the URL you want to crawl");
			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			String inputUrl = input.next();

			// The input URL should come in the format same as Regex-URL
			String regexURL = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%.\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%\\+.~#?&//=]*)";

			// If input URL matches the regex-URL crawling will be done
			if (inputUrl.matches(regexURL)) {
				System.out.println("Fetching URLs from the browser...");
				finalURLList = dataFetcher.getURLs(inputUrl);

				// Creating the Unique-URL Set
				HashSet<String> finalURLSet = new HashSet<String>(finalURLList);
				File parentDirectory = new File("URLs");
				parentDirectory.mkdir();

				// All fetched URLs will be stored in URLS.text file
				FileWriter writerObj = new FileWriter("URLs/urls.txt");

				System.out.println("URLS Extracted");

				// URLS will be written in the text file after they are validated
				System.out.println("Writing URLS into urls.txt file");
				for (String value : finalURLSet) {
					if (dataFetcher.validateURL(value))
						writerObj.write(value + System.lineSeparator());
				}
				writerObj.close();

				// Function for creating HTML files from the URL
				System.out.println("\nCreating HTML files...");
				dataFetcher.createHtmlFilesFromURL(finalURLSet);

				System.out.println("\n\nThe crawling operation is successfully implemented.\n");
			} else {
				System.out.println("Please enter URL in the format: http://www.amazon.com");
				executeCrawler();
			}
		} catch (Exception e) {
			System.out.println("Please enter URL in the format: http://www.amazon.com");
		}

	}
}
