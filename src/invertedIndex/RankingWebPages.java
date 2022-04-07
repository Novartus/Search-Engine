package invertedIndex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class RankingWebPages {

  public HashMap<String, HashMap<Integer, Integer>> freq;

  public RankingWebPages() {
    freq = new HashMap<String, HashMap<Integer, Integer>>();
  }

  public List<Entry<Integer, Integer>> sortingText(String singleWord) {
    List<Entry<Integer, Integer>> inpuText =
      this.sortingHashMap(this.freq.get(singleWord));
    HashMap<Integer, Integer> sorted = new HashMap<Integer, Integer>();
    for (Entry<Integer, Integer> entry : inpuText) sorted.put(
      entry.getKey(),
      entry.getValue()
    );
    this.freq.replace(singleWord, sorted);
    return inpuText;
  }

  public List<Entry<Integer, Integer>> sortingTexts(String[] multipleWords) {
    HashMap<Integer, Integer> wordRank = new HashMap<Integer, Integer>();
    for (String word : multipleWords) {
      List<Entry<Integer, Integer>> sortedLst =
        this.sortingText(word.toLowerCase());
      for (Entry<Integer, Integer> entry : sortedLst) {
        if (!wordRank.containsKey(entry.getKey())) wordRank.put(
          entry.getKey(),
          entry.getValue()
        ); else wordRank.replace(
          entry.getKey(),
          entry.getValue() + wordRank.get(entry.getKey())
        );
      }
    }
    return this.sortingHashMap(wordRank);
  }

  private List<Entry<Integer, Integer>> sortingHashMap(
    HashMap<Integer, Integer> trtm
  ) {
    List<Entry<Integer, Integer>> lst = new ArrayList<>(trtm.entrySet());
    Collections.sort(
      lst,
      new Comparator<Entry<Integer, Integer>>() {
        public int compare(
          Entry<Integer, Integer> text1,
          Entry<Integer, Integer> text2
        ) {
          return text2.getValue().compareTo(text1.getValue());
        }
      }
    );
    return lst;
  }
}
