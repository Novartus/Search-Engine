package mainSearchWordEngine;

import java.util.*;

import searchEngineUI.*;
import webCrawler.runCrawler;
import htmlDataProcessing.*;
import FindURLs.*;

public class mainSearchWord {
	public static void main(String args[]) throws Exception {

		// Taking users input for executing the following features displayed
		while (true) {
			System.out.println("\n\nPlease select the number corresponding to the feature for execution");
			System.out.println("1. Crawl the website.");
			System.out.println("2. Generate and Process text files.");
			System.out.println("3. Get related suggestions for the entered word.");
			System.out.println("4. Find URLS related to the word searched.");
			System.out.println("5. Apply the page ranking algorithm.");

			@SuppressWarnings("resource")
			Scanner input = new Scanner(System.in);
			try {
				int number = input.nextInt();
				if (number > 0 && number < 6) {
					switch (number) {
						case 1:
							runCrawler.executeCrawler();
							break;
						case 2:
							convertHTMLtoText.executeHTMLTotext();
							break;
						case 3:
							try {
								giveSuggestions.executeSuggestions();
							} catch (Exception e) {
								System.out.println(e);
							}
							break;
						case 4:
							searchURL.executeURLFinder();
							break;
						case 5:
							try {
								searchEngineUI.executeUI();
							} catch (Exception e) {
								System.out.println(e);
							}
							break;
					}
				} else {
					System.out.println("Please enter a number between 1 to 5");
				}
			} catch (Exception e) {
				System.out.println(e + "\nPlease eter valid integer between 1 to 5");
			}
		}
	}
}
