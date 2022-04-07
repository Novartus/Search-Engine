package htmlDataProcessing;

import java.io.*;
import java.util.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.tokenattributes.*;

import algorithmDesign.Sequences;

public class removeStopWords {

	// Generate stem words here
	public static String stemWords(String data) throws IOException {
		StringBuffer finalAnswer = new StringBuffer();
		if (data != null && data.trim().length() > 0) {
			StringReader stringReaderObject = new StringReader(data);
			@SuppressWarnings("resource")
			Analyzer analyzerObject = new StopAnalyzer();
			TokenStream streamTokenObject = analyzerObject.tokenStream("contents", stringReaderObject);

			CharTermAttribute charTermObj = streamTokenObject.addAttribute(CharTermAttribute.class);
			streamTokenObject.reset();

			try {
				while (streamTokenObject.incrementToken()) {
					finalAnswer.append(charTermObj.toString());
					finalAnswer.append(" ");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		if (finalAnswer.length() == 0) {
			finalAnswer.append(data);
		}
		return finalAnswer.toString().trim();
	}

	// Get the name of all the files and store them in the list
	public static String[] getNameOfFiles(File textFilesDirectory) {
		File[] nameOfFilesArray = textFilesDirectory.listFiles();
		String[] finalFileName = new String[nameOfFilesArray.length];
		for (int i = 0; i < nameOfFilesArray.length; i++) {
			finalFileName[i] = nameOfFilesArray[i].getName();
		}
		return finalFileName;
	}

	// Append final data to the file
	public static void appendDataToFile(String filename, List<String> finalData) {
		try {
			FileWriter writerObj = new FileWriter(filename);
			for (String iterator : finalData) {
				writerObj.write(iterator.trim() + System.lineSeparator());
			}
			writerObj.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Performing suggestion operation
	public static List<String> findSuggestion(String input) throws FileNotFoundException {

		List<String> dictWords = readingF("Dictionary.txt");
		List<String> giveSug = new ArrayList<String>();

		for (String word : dictWords) {
			if (Sequences.editDistance(input.toLowerCase(), word) == 1) {
				giveSug.add(word);
			}
		}
		return giveSug;
	}

	// Reading the data from the file
	public static List<String> readingF(String file) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(file));
		List<String> words = new ArrayList<String>();
		while (scan.hasNextLine()) {
			String st = scan.nextLine();
			words.add(st);
		}
		return words;
	}

	// All thee text files will be cleaned here
	public void executeCleaningProcess() throws Exception {
		File textFilesDirectory = new File("./Converted Text Files");
		String[] textFileNames = getNameOfFiles(textFilesDirectory);

		for (String fileName : textFileNames) {

			try {
				@SuppressWarnings("resource")
				BufferedReader fileObj = new BufferedReader(new FileReader("Converted Text Files/" + fileName));
				String wholeContent = "";
				String line;

				while ((line = fileObj.readLine()) != null) {
					line = " " + line;
					wholeContent += line;
				}

				wholeContent = utils.filterStopWords(wholeContent);
				wholeContent = stemWords(wholeContent);

				File location = new File("Cleaned-Text-Files");
				if (location.exists()) {
					ArrayList<String> list = new ArrayList<String>();
					list.add(wholeContent);
					appendDataToFile(location.getAbsolutePath() + "\\" + fileName, list);
					System.out.println("Successfully Saved " + location.getAbsolutePath() + "\\" + fileName);
				} else {
					location.mkdir();
					ArrayList<String> list = new ArrayList<String>();
					list.add(wholeContent);
					appendDataToFile(location.getAbsolutePath() + "\\" + fileName, list);
					System.out.println("Successfully Saved " + location.getAbsolutePath() + "\\" + fileName);
				}
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}
}
