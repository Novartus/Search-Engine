package invertedIndex;

import htmlDataProcessing.removeStopWords;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class invertedIndex {

  // mapping of the filekeys (0 to n) and file names
  HashMap<Integer, String> fileMap;
  
  // mapping of the (word and set of filekeys), checked to search word
  HashMap<String, HashSet<Integer>> trashMap;
  RankingWebPages rank;
  removeStopWords sameWord;
  
  // the format of println
  private String sizeForm = "%-8s%-8s%-18s%-18s\n";

  public invertedIndex() {
    fileMap = new HashMap<Integer, String>();
    trashMap = new HashMap<String, HashSet<Integer>>();
    rank = new RankingWebPages();
  }

  public void processCleanedTextFiles() throws IOException {
    int fileKey = 0;
    File[] files = null;
    BufferedReader file = null;
    try {
      // reading files having cleaned text
      File folder = new File("Cleaned-Text-Files");
      files = folder.listFiles();

      for (fileKey = 0; fileKey < files.length; fileKey++) {
        FileReader fr = new FileReader(files[fileKey]);
        file = new BufferedReader(fr);

        String fileName = files[fileKey].getName().trim();

        // setting the keys and values having key and fileName
        fileMap.put(fileKey, fileName);

        String line;
        while ((line = file.readLine()) != null) {
          String[] wordArray = line.split("\\W+");
//          System.out.println(line);
//          System.out.println(Arrays.toString(wordArray));

          for (int j = 0; j < wordArray.length; j++) {
            if (!trashMap.containsKey(wordArray[j].toLowerCase())) {
              trashMap.put(wordArray[j].toLowerCase(), new HashSet<Integer>());
            }

            trashMap.get(wordArray[j].toLowerCase()).add(fileKey);
            
            
            // to check if the word already exists in the rank HashMap and if it not then put freq value to 1 
            if (!rank.freq.containsKey(wordArray[j].toLowerCase())) {
              HashMap<Integer, Integer> freq = new HashMap<Integer, Integer>();
              freq.put(fileKey, 1);

              rank.freq.put(wordArray[j].toLowerCase(), freq);
            } 
//            if it exists then first check filekey exists in the set or not, if it is then increment by 1 
            else {
              HashMap<Integer, Integer> freq = rank.freq.get(wordArray[j].toLowerCase());

              if (!freq.containsKey(fileKey)) {
                freq.put(fileKey, 1);
              } else {
                freq.replace(fileKey, freq.get(fileKey) + 1);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println(
        "Exception in processCleanedTextFiles method: " +files[fileKey].getName() +" file not found because of " +e);
    } finally {
      file.close();
    }
  }

  // Function for searching text
  public List<String> searchText(String search) throws IOException {
    String[] words = search.split("\\W+");
    
    // if search word not exists then gives suggestion about search word
    if (!trashMap.containsKey(words[0].toLowerCase())) {
      List<String> wordSug = removeStopWords.findSuggestion(removeStopWords.stemWords(words[0].toLowerCase()));
      for (String word : wordSug) {
        if (wordSug.size() > 1) {
          System.out.println("The few suggestion related to your search: ");
          System.out.print(word + " | ");
          return null;
        } else {
          System.out.println("Search results for the " + word);
          words[0] = word;
        }
      }
      if (wordSug.size() == 0) {
        System.out.println(
          "Your search is not found, please try another search operation!"
        );
        return null;
      }
    } else {
    	System.out.println("Search results for the word \"" + search +"\"");
    }
    
    // print the result of occurences to console
    int count = 1;
    List<String> files = new ArrayList<String>();
    List<Entry<Integer, Integer>> results = rank.sortingTexts(words);
    
    System.out.println("Total Search Results: " + results.size());
    System.out.print("------------------------------------------------------------------ \n");
    System.out.format(sizeForm , "Sr.No.", "File", "Occurrences", "File Title");
    System.out.print("------------------------------------------------------------------ \n");

    HashSet<String> setFile = new HashSet<String>();

    for (Entry<Integer, Integer> entry : results) {
      if (count <= 10) {
        String fileName = fileMap.get(entry.getKey());
        final List<String> lines = Files.readAllLines(Paths.get("Converted Text Files/" + fileName),StandardCharsets.ISO_8859_1);
        if (!setFile.contains(lines.get(1))) {
          System.out.format(sizeForm, count, fileName, entry.getValue(), lines.get(1));
          files.add(lines.get(0));
          files.add(lines.get(1));
          setFile.add(lines.get(1));
        }
        count += 1;
      } else {
        break;
      }
    }
    return files;
  }

  // Function for executing the page ranking algorithm
  public static void executePageRanking() throws IOException {
    invertedIndex ivr = new invertedIndex();
    ivr.processCleanedTextFiles();

    System.out.print("Write a keyword to search? ");

    // taking input from the user to search a keyword
    InputStreamReader isr = new InputStreamReader(System.in);
    BufferedReader input = new BufferedReader(isr);
    String text = input.readLine();
    ivr.searchText(text);
  }
  
  public static void main(String[] args) {
	  try {
		  executePageRanking();
	  }catch (Exception e) {
		System.out.println("Exception in main method in invertedIndex class: "+e);
	}
  }
}
