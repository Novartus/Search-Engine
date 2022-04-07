package htmlDataProcessing;

import java.util.*;
import java.io.*;

public class utils {
	public static ArrayList<String> stopWords = new ArrayList<String>();
	public static String stopWordsFileName = "Stop-Words.txt";

	//Creating the stop-words array and returning it
	public utils() throws FileNotFoundException {
		try {
			Scanner scanObj = new Scanner(new File(stopWordsFileName));
			while (scanObj.hasNextLine()) {
				String dataLine = scanObj.nextLine();
				if (dataLine.matches(".*\\p{P}")) {
					stopWords.add(dataLine.replaceAll(".*\\p{Punct}", ""));
				}
				stopWords.add(dataLine);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Stop words will be filtered here and final cleaned text will be passed
	public static String filterStopWords(String textData) {
		String[] words = textData.split(" ");
		ArrayList<String> wordList = new ArrayList<String>(Arrays.asList(words));

		wordList.removeAll(stopWords);

		String finalCleanedText = String.join(" ", wordList);
		return finalCleanedText;
	}

}
