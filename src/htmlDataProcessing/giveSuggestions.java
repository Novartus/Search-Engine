package htmlDataProcessing;

import algorithmDesign.Sequences;
import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class giveSuggestions {

	// Read the words from dictionary file while suggestions
	public static ArrayList<String> getWordsFromDictionary(String fileData) throws Exception {
		Scanner scanObj = new Scanner(new File(fileData));
		ArrayList<String> words = new ArrayList<String>();
		while (scanObj.hasNextLine()) {
			String lineData = scanObj.nextLine();
			words.add(lineData);
		}
		return words;
	}

	// generate the final suggestions if possible and store them in the list
	public static ArrayList<String> generateSuggestions(String input) throws Exception {
		ArrayList<String> finalWords = getWordsFromDictionary("Dictionary.txt");
		ArrayList<String> suggestedWordsList = new ArrayList<String>();
		for (String wordsInList : finalWords) {
			if (Sequences.editDistance(input.toLowerCase(), wordsInList) == 1) {
				suggestedWordsList.add(wordsInList);
			}
		}
		return suggestedWordsList;
	}

	/*
	 * The input word which was entered will be first go through the stemming
	 * function and thereby suggestion will be generated
	 */
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

	/*
	 * The data from all generated text files will be read and the whole data will
	 * be preprocessed and stored into the cleaned text files
	 */
	public static void createDictionaryOfWords() throws Exception {
		File textDirectory = new File("Converted Text Files");
		File[] textFiles = textDirectory.listFiles();

		ArrayList<String> wordList = new ArrayList<String>();
		String[] textFileNames = new String[textFiles.length];

		for (int iterator = 0; iterator < textFiles.length; iterator++) {
			textFileNames[iterator] = textFiles[iterator].getName();
		}

		int i = 1;

		for (String fileName : textFileNames) {
			Scanner scanObj = new Scanner(new File("./Cleaned-Text-Files/" + fileName));
			// System.out.println("Iteration: "+ i+" "+fileName);
			String text = "";
			while (scanObj.hasNextLine()) {
				String data = scanObj.nextLine().toLowerCase();
				text = data.toLowerCase();
			}
			String[] content = text.split(" ");
			Collections.addAll(wordList, content);
			i++;
		}
		System.out.println("Total files scanned: " + i);
		HashSet<String> uniqueWords = new HashSet<String>(wordList);
		writeDictionaryToFile("Dictionary.txt", uniqueWords);
	}

	// Write the words to file called dictionary.text
	public static void writeDictionaryToFile(String filename, HashSet<String> uniqueWords) {
		FileWriter writeInFile;
		try {
			writeInFile = new FileWriter(filename);
			uniqueWords.forEach(data -> {
				try {
					writeInFile.write(data + "\n");
				} catch (Exception e) {
					System.out.println(e);
				}
			});
			writeInFile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Write all the data to file
	public static void writeDataToFile(String fileName, List<String> listOfWords) {
		try {
			FileWriter writeInFile = new FileWriter(fileName);
			for (String words : listOfWords) {
				writeInFile.write(words.trim() + System.lineSeparator());
			}
			writeInFile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function will handle the execute-Suggestions
	public static void executeSuggestions() throws Exception {

		/*
		 * Dictionary of Words will be created so that entered word can be searched
		 * inside it
		 */
		createDictionaryOfWords();
		System.out.println("Enter the term");

		// User needs to enter the input for the words which needs to be searched
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		String check = input.next();

		/*
		 * Stem-word of entered input is generated and thereby passed to generate
		 * suggestions function
		 */
		List<String> suggestionWords = generateSuggestions(stemWords(check));
		for (String wordList : suggestionWords) {
			if (suggestionWords.size() == 0) {
				System.out.println("Sorry! No suggestions are available");
			} else if (suggestionWords.size() > 1) {
				System.out.print(wordList + " | ");
			} else {
				System.out.print(wordList);
			}
		}

	}
}
