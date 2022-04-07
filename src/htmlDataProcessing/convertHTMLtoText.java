package htmlDataProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class convertHTMLtoText {

	public static void htmlTotext(String url, int count, File folder) {
		try {
			// Creating Document class object and using connector class of jsoup for web
			// connection
			Document doc = Jsoup.connect(url).get();

			// Fetching the text of the html file
			String fileContent = url;
			fileContent += "\n" + doc.title();
			fileContent += "\n" + doc.text();

			PrintWriter pr = new PrintWriter(folder.getAbsolutePath() + "\\" + count + ".txt");
			System.out.println(folder.getAbsolutePath() + "\\" + count + ".txt");

			// writing into the text file
			pr.println(fileContent);
			pr.close();
		} catch (Exception e) {
			System.out.println("Exception in htmlTotext class: " + e);
		}
	}

	public static void executeHTMLTotext() {
		try {

			// reading the link in file
			FileReader fileReader = new FileReader("URLs/urls.txt");
			BufferedReader bR = new BufferedReader(fileReader);

			// array list for the urls
			ArrayList<String> lstUrl = new ArrayList<String>();

			// to read single line having specific url
			String line = bR.readLine();
			while (line != null) {
				lstUrl.add(line);
				line = bR.readLine();
			}
			bR.close();
			fileReader.close();

			// for getting not duplicate links
			Set<String> urls = new HashSet<>(lstUrl);

			// specifying the path of the folder to store .txt files
			File folder = new File("Converted Text Files/");

			// to check if folder exists or not
			if (!folder.exists()) {
				folder.mkdir();
			}

			int count = 1;
			for (String url : urls) {
				if (count < 200) {
					htmlTotext(url, count, folder);
					count += 1;
				} else {
					break;
				}

			}

			removeStopWords cleaningObj = new removeStopWords();
			cleaningObj.executeCleaningProcess();

		}

		catch (Exception e) {
			System.out.println("Exception in convertHTMLtoText class: " + e);
		}
	}
}
