package invertedIndex;


public class invertedIndex {

  HashMap<Integer, String> wordMap;
  HashMap<String, HashSet<Integer>> trashMap;
  RankingWebPages rank;
  removeStopWords sameWord;

  public invertedIndex() {
    wordMap = new HashMap<Integer, String>();
    trashMap = new HashMap<String, HashSet<Integer>>();
    rank = new RankingWebPages();
  }

  public void processCleanedTextFiles() throws IOException {
   
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
        wordMap.put(fileKey, fileName);

        String line;
        while ((line = file.readLine()) != null) {
          String[] wordArray = line.split("\\W+");

          for (int j = 0; j < wordArray.length; j++) {
            if (!trashMap.containsKey(wordArray[j].toLowerCase())) {
              trashMap.put(wordArray[j].toLowerCase(), new HashSet<Integer>());
            }

            trashMap.get(wordArray[j].toLowerCase()).add(fileKey);

            if (!rank.freq.containsKey(wordArray[j].toLowerCase())) {
              HashMap<Integer, Integer> freq = new HashMap<Integer, Integer>();
              freq.put(fileKey, 1);

              rank.freq.put(wordArray[j].toLowerCase(), freq);
            } else {
              HashMap<Integer, Integer> freq = rank.freq.get(
                wordArray[j].toLowerCase()
              );

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
        "Exception in processCleanedTextFiles method: " +
        files[fileKey].getName() +
        " file not found because of " +
        e
      );
    } finally {
      file.close();
    }
  }

  // Fuction for searching text
  public List<String> searchText(String search) throws IOException {
    String[] words = search.split("\\W+");

    if (!trashMap.containsKey(words[0].toLowerCase())) {
      List<String> wordSug = removeStopWords.findSuggestion(
        removeStopWords.stemWords(words[0].toLowerCase())
      );
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
    } else {}

    int count = 1;
    List<String> files = new ArrayList<String>();
    List<Entry<Integer, Integer>> results = rank.sortingTexts(words);

    HashSet<String> setFile = new HashSet<String>();

    for (Entry<Integer, Integer> entry : results) {
      if (count <= 10) {
        String fileName = wordMap.get(entry.getKey());
        final List<String> lines = Files.readAllLines(
          Paths.get("Converted Text Files/" + fileName),
          StandardCharsets.ISO_8859_1
        );
        if (!setFile.contains(lines.get(1))) {
          // System.out.format(sizeForm, count, fileName, entry.getValue(), lines.get(1));
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
}
