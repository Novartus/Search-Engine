package FindURLs;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class searchURL {

	// This function will give the suggested URL's
	public static void getSuggestedURLs(String allURLs, String pattern) {
		int present = 0;
		String urlRegex = "(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern p = Pattern.compile(urlRegex);
		Matcher m = p.matcher(allURLs);

		// Here the entered pattern will be compared to URL-Regex for suggestions
		while (m.find()) {
			if (m.group().contains(pattern.toLowerCase())) {
				present = 1;
				System.out.println("URL: " + m.group());
			}
		}
		// If the entered word is not there in URL then it won't be found
		if (present == 0) {
			System.out.println("Not found");
		}
	}

	// Function for executing URL finder
	public static void executeURLFinder() {
		try {
			// Enter any word or string so that URL related to that word can be found
			System.out.println("Enter the pattern you want to search in URL");
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			String pattern = sc.nextLine();

			// URL's file will be read for suggesting different URL's
			BufferedReader fileObj = new BufferedReader(new FileReader("URLs/urls.txt"));
			String allURLs = "";
			String line;
			while ((line = fileObj.readLine()) != null) {
				line = " " + line;
				allURLs += line;
			}
			getSuggestedURLs(allURLs, pattern);
			fileObj.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
