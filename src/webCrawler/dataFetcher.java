package webCrawler;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class d
			finalLinkArray.addAll(storeLink);
			storageArray.addAll(storeLink);
			storeLink.clear();
			for (int j = 0; j < storageArray.size(); j++) {
				if (finalLinkArray.size() >= totalLinks) {
					return;
				}
				extractURL(storageArray.get(j));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// The above function is called here and one by one links will be passed
	public static ArrayList<String> getURLs(String link) {
		try {
			extractURL(link);
		} catch (Exception e) {
			System.out.println(e);
		}
		return finalLinkArray;
	}

	// Validate URL for the response status code
	public static boolean validateURL(String link) {
		if (link.contains("twitter")) {
			return false;
		}
		if (link.contains("facebook")) {
			return false;
		}
		if (link.contains("instagram")) {
			return false;
		}
		if (link.contains("linkedin")) {
			return false;
		}
		if (link.contains("ads")) {
			return false;
		}
		return true;
	}

	// Function for creating HTML files from the URL file
	public static void createHtmlFilesFromURL(HashSet<String> finalURLSet) {
		int count = 1;

		// Iterate through final-URL list
		for (String link : finalURLSet) {

			try {
				if (validateURL(link)) {
					Connection connObj = Jsoup.connect(link).userAgent(USER_AGENT);
					int statusCode = connObj.response().statusCode();

					// If status code is not equal to 400 then only the HTML Web pages will be
					// created
					if (statusCode != 400) {
						Document jsoupObj = connObj.get();
						String htmlFileContent = jsoupObj.html();
						File parentDirectory = new File("HTML Web Pages");
						parentDirectory.mkdir();

						FileWriter writerObj = new FileWriter("HTML Web Pages/" + count + ".html");
						writerObj.write(htmlFileContent); // writing into file
						writerObj.close();
						System.out.println("HTML file generated: " + jsoupObj.title());
						count++;
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
